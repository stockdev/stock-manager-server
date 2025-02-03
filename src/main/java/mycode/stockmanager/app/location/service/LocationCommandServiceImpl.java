package mycode.stockmanager.app.location.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;
import mycode.stockmanager.app.location.exceptions.LocationAlreadyExists;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.location.repository.LocationRepository;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.notification_type.NotificationType;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LocationCommandServiceImpl implements LocationCommandService{

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    private void createAndSaveNotification(User user, String message) {
        Notification notification = Notification.builder()
                .notificationType(NotificationType.INFORMATION)
                .user(user)
                .message(message)
                .build();

        notificationRepository.saveAndFlush(notification);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoUserFound("User not found"));
    }

    @Override
    public LocationResponse createLocation(CreateLocationRequest createLocationRequest) {
        User user = getAuthenticatedUser();

        Optional<Location> locationByCode = locationRepository.findByCode(createLocationRequest.code());

        if(locationByCode.isPresent()){
            throw new LocationAlreadyExists("Location with this code already exists try again with a different code");
        }

        Location location = LocationMapper.createLocationRequestToLocation(createLocationRequest);
        locationRepository.saveAndFlush(location);

        String message = "User: " + user.getEmail() + " created location with code: " + location.getCode();
        createAndSaveNotification(user, message);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, long id) {
        User user = getAuthenticatedUser();

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NoLocationFound("No location with this id found"));

        if(updateLocationRequest.code() != null && !updateLocationRequest.code().equals(location.getCode())) {
            Optional<Location> locationByCode = locationRepository.findByCode(updateLocationRequest.code());
            if (locationByCode.isPresent()) {
                throw new LocationAlreadyExists("Article with this code already exists try again with different code");
            }
        }

        location.setCode(updateLocationRequest.code());

        locationRepository.save(location);

        String message = "User: " + user.getEmail() + " updated article with code: " + location.getCode();
        createAndSaveNotification(user, message);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public String deleteLocationByCode(String code) {
        User user = getAuthenticatedUser();

        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new NoLocationFound("No location with this code found"));

        LocationResponse locationResponse = LocationMapper.locationToResponseDto(location);
        String message = "User: " + user.getEmail() + " deleted location with code: " + locationResponse.code();
        createAndSaveNotification(user, message);


        locationRepository.delete(location);

        return "Article with code  " + locationResponse.code() + " was deleted";
    }

    @Transactional
    public void deleteAllLocationsAndResetSequence() {
        User user = getAuthenticatedUser();

        locationRepository.deleteAll();
        locationRepository.resetLocationSequence();

        String message = "User: " + user.getEmail() + " deleted all locations";
        createAndSaveNotification(user, message);
    }

}
