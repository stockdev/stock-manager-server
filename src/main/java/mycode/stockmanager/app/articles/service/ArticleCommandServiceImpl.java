package mycode.stockmanager.app.articles.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import mycode.stockmanager.app.articles.exceptions.AlreadyExistsArticle;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.enums.NotificationType;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleCommandServiceImpl implements ArticleCommandService {

    private final ArticleRepository articleRepository;
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

    private void createArticleWithoutNotification(CreateArticleRequest createArticleRequest) {
        Optional<Article> articleByCode = articleRepository.findByCode(createArticleRequest.code());

        if (articleByCode.isPresent()) {
            throw new AlreadyExistsArticle("Article with this code already exists try again with different code");
        }
        Article article = ArticleMapper.createArticleRequestToArticle(createArticleRequest);
        articleRepository.saveAndFlush(article);
    }


    @Override
    public ArticleResponse createArticle(CreateArticleRequest createArticleRequest) {
        User user = getAuthenticatedUser();

        Optional<Article> articleByCode = articleRepository.findByCode(createArticleRequest.code());
        if (articleByCode.isPresent()) {
            throw new AlreadyExistsArticle("Article with this code already exists try again with different code");
        }
        Article article = ArticleMapper.createArticleRequestToArticle(createArticleRequest);
        articleRepository.saveAndFlush(article);

        String message = "User: " + user.getEmail() + " created article with code: " + article.getCode();
        createAndSaveNotification(user, message);

        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public ArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, long id) {
        User user = getAuthenticatedUser();

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoArticleFound("No article with this id found"));

        if (updateArticleRequest.code() != null && !updateArticleRequest.code().equals(article.getCode())) {
            Optional<Article> articleByCode = articleRepository.findByCode(updateArticleRequest.code());
            if (articleByCode.isPresent()) {
                throw new AlreadyExistsArticle("Article with this code already exists try again with different code");
            }
        }

        article.setCode(updateArticleRequest.code());
        article.setName(updateArticleRequest.name());

        articleRepository.save(article);

        String message = "User: " + user.getEmail() + " updated article with code: " + article.getCode();
        createAndSaveNotification(user, message);

        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public String deleteArticleByCode(String code) {
        User user = getAuthenticatedUser();

        Article article = articleRepository.findByCode(code)
                .orElseThrow(() -> new NoArticleFound("No article with this code found"));

        ArticleResponse articleResponse = ArticleMapper.articleToResponseDto(article);
        articleRepository.delete(article);

        String message = "User: " + user.getEmail() + " deleted article with code: " + article.getCode();
        createAndSaveNotification(user, message);

        return "Article with code  " + articleResponse.code() + " was deleted";
    }

    @Override
    public ImportResponse importArticlesFromExcel(MultipartFile file) {
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

                CreateArticleRequest createRequest = new CreateArticleRequest(codeValue, nameValue);
                try {
                    createArticleWithoutNotification(createRequest);
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

    @Transactional
    public void deleteAllArticlesAndResetSequence() {
        User user = getAuthenticatedUser();

        articleRepository.deleteAll();
        articleRepository.resetArticleIdSequence();

        String message = "User: " + user.getEmail() + " deleted all articles";
        createAndSaveNotification(user, message);
    }
}
