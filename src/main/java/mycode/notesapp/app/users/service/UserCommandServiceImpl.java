package mycode.notesapp.app.users.service;

import lombok.AllArgsConstructor;
import mycode.notesapp.app.system.security.UserRole;
import mycode.notesapp.app.users.dtos.CreateUserRequest;
import mycode.notesapp.app.users.dtos.UpdateUserRequest;
import mycode.notesapp.app.users.dtos.UserResponse;
import mycode.notesapp.app.users.exceptions.AccessDeniedException;
import mycode.notesapp.app.users.exceptions.NoUserFound;
import mycode.notesapp.app.users.exceptions.UserAlreadyExists;
import mycode.notesapp.app.users.mapper.UserMapper;
import mycode.notesapp.app.users.model.User;
import mycode.notesapp.app.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public void createUser(CreateUserRequest createUserRequest) {


        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new UserAlreadyExists("User with this email already exists!");
        }

        User newUser = User.builder()
                .fullName(createUserRequest.fullName())
                .email(createUserRequest.email())
                .phone(createUserRequest.phone())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .userRole(UserRole.USER)
                .build();

        userRepository.saveAndFlush(newUser);

    }



    @Override
    public String deleteUser(String email) {

        User userToDelete = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFound("No user with this email found"));


        userRepository.delete(userToDelete);

        return "User with email: " + email + " has been deleted!";
    }


    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest, String email) {
        User currentUser = getAuthenticatedUser();
        User userToUpdate = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserFound("No user with this email found"));

        if (!currentUser.getEmail().equals(email) &&
                userRepository.existsByEmail(updateUserRequest.email())) {
            throw new UserAlreadyExists("User with this email already exists!");
        }

        updateUserFields(userToUpdate, updateUserRequest, currentUser);
        User savedUser = userRepository.save(userToUpdate);


        return UserMapper.userToResponseDto(savedUser);
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