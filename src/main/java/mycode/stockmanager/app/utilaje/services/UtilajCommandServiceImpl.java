package mycode.stockmanager.app.utilaje.services;



import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.exceptions.LocationAlreadyExists;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.exceptions.NoUtilajFound;
import mycode.stockmanager.app.utilaje.exceptions.UtilajAlreadyExists;
import mycode.stockmanager.app.utilaje.mapper.UtilajMapper;
import mycode.stockmanager.app.utilaje.model.Utilaj;
import mycode.stockmanager.app.utilaje.repository.UtilajeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilajCommandServiceImpl implements UtilajCommandService{

    private UtilajeRepository utilajeRepository;
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

    private void createUtilajWithoutNotification(CreateUtilajRequest createUtilajRequest) {
        Optional<Utilaj> utilajByCode = utilajeRepository.findByCode(createUtilajRequest.code());

        if(utilajByCode.isPresent()){
            throw new UtilajAlreadyExists("Utilaj with this code already exists, try again with a different code");
        }

        Utilaj utilaj = UtilajMapper.createRequestToUtilaj(createUtilajRequest);
        utilajeRepository.saveAndFlush(utilaj);
    }


    @Override
    public UtilajResponseDto createUtilaj(CreateUtilajRequest createUtilajRequest) {

        User user = getAuthenticatedUser();

        Optional<Utilaj> utilajByCode = utilajeRepository.findByCode(createUtilajRequest.code());

        if(utilajByCode.isPresent()){
            throw new UtilajAlreadyExists("Utilaj with this code already exists, try again with a different code");
        }


        Utilaj utilaj = Utilaj.builder().name(createUtilajRequest.name()).code(createUtilajRequest.code()).build();
        utilajeRepository.saveAndFlush(utilaj);

        String message = "User: " + user.getEmail() + " created article with code: " + utilaj.getCode();
        createAndSaveNotification(user, message);

        return UtilajMapper.utilajToResponseDto(utilaj);
    }

    @Override
    public String deleteUtilajByCode(String code) {
        User user = getAuthenticatedUser();

        Utilaj utilaj = utilajeRepository.findByCode(code)
                .orElseThrow(() -> new NoUtilajFound("No utilaj with this code found"));

        UtilajResponseDto utilajResponseDto = UtilajMapper.utilajToResponseDto(utilaj);
        utilajeRepository.delete(utilaj);

        String message = "User: " + user.getEmail() + " deleted utilaj with code: " + utilaj.getCode();
        createAndSaveNotification(user, message);

        return "Utilaj with code  " + utilajResponseDto.code() + " was deleted";


    }

    @Override
    public void deleteAllUtilaje() {
        User user = getAuthenticatedUser();
        utilajeRepository.deleteAll();
        utilajeRepository.resetUtilajeIdSequence();

        String message = "User: " + user.getEmail() + " deleted all utilaje";
        createAndSaveNotification(user, message);
    }

    @Override
    public ImportResponse importUtilajeFromExcel(MultipartFile file) {
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
                Cell nameCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (codeCell == null || nameCell == null) {
                    skippedRows.add("Row " + rowIndex + " skipped: missing code or name.");
                    continue;
                }

                String codeValue;
                if (codeCell.getCellType() == CellType.NUMERIC) {
                    codeValue = String.valueOf((long) codeCell.getNumericCellValue());
                } else {
                    codeValue = codeCell.getStringCellValue().trim();
                }

                String nameValue = nameCell.getStringCellValue().trim();

                CreateUtilajRequest createRequest = new CreateUtilajRequest(codeValue, nameValue);
                try {
                    createUtilajWithoutNotification(createRequest);
                    importedCount++;
                } catch (Exception e) {
                    skippedRows.add("Row " + rowIndex + " with code "
                            + codeValue + " skipped: " + e.getMessage());
                }
            }

            String message = "User: " + user.getEmail() + " imported "
                    + importedCount + " articles from Excel.";
            createAndSaveNotification(user, message);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage(), e);
        }

        return new ImportResponse(importedCount, skippedRows);
    }
}
