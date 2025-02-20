package mycode.stockmanager.app.users.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.system.security.UserRole;
import mycode.stockmanager.app.users.dtos.CreateUserRequest;
import mycode.stockmanager.app.users.dtos.UpdateUserRequest;
import mycode.stockmanager.app.users.dtos.UserResponse;
import mycode.stockmanager.app.users.exceptions.AccessDeniedException;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.exceptions.UserAlreadyExists;
import mycode.stockmanager.app.users.mapper.UserMapper;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private void createAndSaveNotification(User user, String message) {
        Notification notification = Notification.builder()
                .notificationType(NotificationType.INFORMATION)
                .user(user)
                .message(message)
                .build();
        notificationRepository.saveAndFlush(notification);
    }

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        User currentUser = getAuthenticatedUser();
        UserRole currentUserRole = currentUser.getUserRole();
        UserRole requestedRole = createUserRequest.userRole();

        validateCreateUserPermissions(currentUserRole, requestedRole);

        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new UserAlreadyExists("User with this email already exists!");
        }

        User newUser = User.builder()
                .fullName(createUserRequest.fullName())
                .email(createUserRequest.email())
                .phone(createUserRequest.phone())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .userRole(requestedRole)
                .build();

        userRepository.saveAndFlush(newUser);
        createAndSaveNotification(currentUser,
                "Created new user: " + createUserRequest.email()
        );
    }

    private void validateCreateUserPermissions(UserRole currentRole, UserRole targetRole) {
        switch (currentRole) {
            case ADMIN -> {
                if (targetRole == UserRole.ADMIN) {
                    throw new AccessDeniedException("Admin cannot create another Admin!");
                }
            }
            case MANAGER -> {
                if (targetRole != UserRole.UTILIZATOR) {
                    throw new AccessDeniedException("Manager can only create UTILIZATOR users!");
                }
            }
            case UTILIZATOR -> throw new AccessDeniedException("UTILIZATOR cannot create any users!");
            default -> throw new AccessDeniedException("Invalid role for user creation!");
        }
    }

    @Override
    public String deleteUser(String email) {
        User currentUser = getAuthenticatedUser();
        User userToDelete = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFound("No user with this email found"));

        validateDeleteUserPermissions(currentUser, userToDelete);

        createAndSaveNotification(currentUser,
                "Deleted user: " + email
        );

        userRepository.delete(userToDelete);

        return "User with email: " + email + " has been deleted!";
    }

    private void validateDeleteUserPermissions(User currentUser, User targetUser) {
        UserRole currentRole = currentUser.getUserRole();
        UserRole targetRole = targetUser.getUserRole();
        boolean isSameUser = currentUser.getEmail().equals(targetUser.getEmail());

        switch (currentRole) {
            case ADMIN -> {
                if (targetRole == UserRole.ADMIN) {
                    throw new AccessDeniedException("Admin cannot delete another Admin!");
                }
            }
            case MANAGER -> {
                if (!isSameUser && targetRole != UserRole.UTILIZATOR) {
                    throw new AccessDeniedException("Manager can only delete their own account or UTILIZATOR accounts!");
                }
            }
            case UTILIZATOR -> {
                if (!isSameUser) {
                    throw new AccessDeniedException("UTILIZATOR can only delete their own account!");
                }
            }
            default -> throw new AccessDeniedException("Invalid role for user deletion!");
        }
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest, String email) {
        User currentUser = getAuthenticatedUser();
        User userToUpdate = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFound("No user with this email found"));

        validateUpdateUserPermissions(currentUser, userToUpdate, updateUserRequest);

        if (!currentUser.getEmail().equals(email) &&
                userRepository.existsByEmail(updateUserRequest.email())) {
            throw new UserAlreadyExists("User with this email already exists!");
        }

        updateUserFields(userToUpdate, updateUserRequest, currentUser);
        User savedUser = userRepository.save(userToUpdate);

        createAndSaveNotification(currentUser,
                "Updated user: " + email
        );

        return UserMapper.userToResponseDto(savedUser);
    }

    private void validateUpdateUserPermissions(User currentUser, User targetUser, UpdateUserRequest updateRequest) {
        UserRole currentRole = currentUser.getUserRole();
        UserRole targetRole = targetUser.getUserRole();
        boolean isSameUser = currentUser.getEmail().equals(targetUser.getEmail());

        switch (currentRole) {
            case ADMIN -> {
                if (isSameUser) {
                    if (!updateRequest.userRole().equals(targetRole)) {
                        throw new AccessDeniedException("Admin cannot change their own role!");
                    }
                } else {
                    if (targetRole == UserRole.ADMIN) {
                        throw new AccessDeniedException("Admin cannot modify another Admin!");
                    }
                    if (updateRequest.userRole() == UserRole.ADMIN) {
                        throw new AccessDeniedException("Admin cannot promote users to Admin role!");
                    }
                }
            }
            case MANAGER -> {
                if (isSameUser) {
                    if (!updateRequest.userRole().equals(targetRole)) {
                        throw new AccessDeniedException("Manager cannot change their own role!");
                    }
                } else {
                    if (targetRole != UserRole.UTILIZATOR) {
                        throw new AccessDeniedException("Manager can only update UTILIZATOR accounts!");
                    }
                    if (!updateRequest.userRole().equals(targetRole)) {
                        throw new AccessDeniedException("Manager cannot change user roles!");
                    }
                }
            }
            case UTILIZATOR -> {
                if (!isSameUser) {
                    throw new AccessDeniedException("UTILIZATOR can only update their own account!");
                }
                if (!updateRequest.userRole().equals(targetRole)) {
                    throw new AccessDeniedException("UTILIZATOR cannot change their role!");
                }
            }
            default -> throw new AccessDeniedException("Invalid role for user update!");
        }
    }

    private void updateUserFields(User user, UpdateUserRequest request, User currentUser) {
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phone());

        if (request.password() != null && !request.password().trim().isEmpty()) {
            user.setPassword(request.password());
        }

        boolean isSameUser = currentUser.getEmail().equals(user.getEmail());
        if (!isSameUser) {
            user.setUserRole(request.userRole());
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoUserFound("User not found"));
    }
}