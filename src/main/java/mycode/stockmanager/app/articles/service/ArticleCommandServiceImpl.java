package mycode.stockmanager.app.articles.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import mycode.stockmanager.app.articles.exceptions.AlreadyExistsArticle;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
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

        if(updateArticleRequest.code() != null && !updateArticleRequest.code().equals(article.getCode())) {
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

    @Transactional
    public void deleteAllArticlesAndResetSequence() {
        User user = getAuthenticatedUser();

        articleRepository.deleteAll();
        articleRepository.resetArticleIdSequence();

        String message = "User: " + user.getEmail() + " deleted all articles";
        createAndSaveNotification(user, message);
    }
}
