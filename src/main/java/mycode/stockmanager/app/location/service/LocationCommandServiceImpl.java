package mycode.stockmanager.app.location.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.UpdateLocationRequest;
import mycode.stockmanager.app.location.exceptions.LocationAlreadyExists;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.location.repository.LocationRepository;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LocationCommandServiceImpl implements LocationCommandService {

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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoUserFound("User not found"));
    }

    private void createLocationWithoutNotification(CreateLocationRequest createLocationRequest) {
        Optional<Location> locationByCode = locationRepository.findByCode(createLocationRequest.code());

        if(locationByCode.isPresent()){
            throw new LocationAlreadyExists("Location with this code already exists, try again with a different code");
        }

        Location location = LocationMapper.createLocationRequestToLocation(createLocationRequest);
        locationRepository.saveAndFlush(location);
    }

    @Override
    public LocationResponse createLocation(CreateLocationRequest createLocationRequest) {
        Location location = LocationMapper.createLocationRequestToLocation(createLocationRequest);
        List<Location> list = locationRepository.findAll();

        list.forEach(location1 -> {
            if (location.getCode().equals(location1.getCode())) {
                throw new LocationAlreadyExists("Location with this code already exists");
            }
        });

        locationRepository.saveAndFlush(location);

        User user = getAuthenticatedUser();

        String message = "User: " + user.getEmail() + " created location with code: " + location.getCode();
        createAndSaveNotification(user, message);

        return LocationMapper.locationToResponseDto(location);
    }

    @Override
    public LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest, String code) {
        Location location = locationRepository.findByCode(code)
                .orElseThrow(() -> new NoLocationFound("No location with this code found"));

        Optional<Location> locationByCode = locationRepository.findByCode(updateLocationRequest.code());
        if (locationByCode.isPresent()) {
            throw new LocationAlreadyExists("Location with this code already exists");
        }

        location.setCode(updateLocationRequest.code());
        locationRepository.save(location);
        User user = getAuthenticatedUser();
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
    @Override
    public void deleteAllLocationsAndResetSequence() {
        locationRepository.deleteAll();
        locationRepository.resetLocationSequence();

        User user = getAuthenticatedUser();

        String message = "User: " + user.getEmail() + " deleted all locations";
        createAndSaveNotification(user, message);
    }

    @Override
    public ImportResponse importLocationsFromExcel(MultipartFile file) {
        User user = getAuthenticatedUser();
        List<String> skippedRows = new ArrayList<>();
        int importedCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int firstDataRow = 4;

            for (int rowIndex = firstDataRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                Cell codeCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (codeCell == null) {
                    skippedRows.add("Row " + rowIndex + " skipped: missing code.");
                    continue;
                }

                String codeValue;
                if (codeCell.getCellType() == CellType.NUMERIC) {
                    codeValue = String.valueOf((long) codeCell.getNumericCellValue());
                } else {
                    codeValue = codeCell.getStringCellValue().trim();
                }


                CreateLocationRequest createRequest = new CreateLocationRequest(codeValue);

                try {
                    createLocationWithoutNotification(createRequest);
                    importedCount++;
                } catch (Exception e) {
                    skippedRows.add("Row " + rowIndex + " with code " + codeValue + " skipped: " + e.getMessage());
                }
            }

            String message = "User: " + user.getEmail() + " imported " + importedCount + " locations from Excel.";
            createAndSaveNotification(user, message);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage(), e);
        }

        return new ImportResponse(importedCount, skippedRows);
    }

}