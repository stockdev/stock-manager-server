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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class LocationCommandServiceImpl implements LocationCommandService{

    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    private void createAndSaveNotification(User user, String message) {
        Notification notification = Notification.builder()
                .notificationType(NotificationType.INFORMATION)
                .user(user)
                .message(message)
                .build();

        notificationRepository.saveAndFlush(notification);
    }

    @Override
    public LocationResponse createLocation(CreateLocationRequest createLocationRequest, long userId) {
        Location location = LocationMapper.createLocationRequestToLocation(createLocationRequest);
        List<Location> list = locationRepository.findAll();

        list.forEach(location1 -> {
            if(location.getCode().equals(location1.getCode())){
                throw new LocationAlreadyExists("Location with this code already exists");
            }
        });

        locationRepository.saveAndFlush(location);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " created location with code: " + location.getCode();
        createAndSaveNotification(user, message);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, long id, long userId) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NoLocationFound("No location with this id found"));

        location.setCode(updateLocationRequest.code());

        locationRepository.save(location);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " updated article with code: " + location.getCode();
        createAndSaveNotification(user, message);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse deleteLocationByCode(String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new NoLocationFound("No location with this code found"));

        LocationResponse locationResponse = LocationMapper.locationToResponseDto(location);

        locationRepository.delete(location);

        return locationResponse;
    }

    @Transactional
    public void deleteAllLocationsAndResetSequence(long userId) {
        locationRepository.deleteAll();
        locationRepository.resetLocationSequence();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " deleted all locations";
        createAndSaveNotification(user, message);
    }

    @Override
    public LocationResponse deleteLocationById(long id, long userId) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NoLocationFound("No location with this id found"));

        LocationResponse locationResponse = LocationMapper.locationToResponseDto(location);

        String locationCode = location.getCode();

        locationRepository.delete(location);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " deleted location with code: " + locationCode;
        createAndSaveNotification(user, message);

        return locationResponse;
    }
}
