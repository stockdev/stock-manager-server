package mycode.stockmanager.app.articles.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.notification_type.NotificationType;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.system.security.UserRole;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ArticleCommandServiceImpl implements ArticleCommandService {

    private ArticleRepository articleRepository;
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
    public ArticleResponse createArticle(CreateArticleRequest createArticleRequest,long userId) {
        Article article = ArticleMapper.createArticleRequestToArticle(createArticleRequest);

        articleRepository.saveAndFlush(article);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " created article with code: " + article.getCode();
        createAndSaveNotification(user, message);


        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public ArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, long id,long userId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoArticleFound("No article with this id found"));


        article.setCode(updateArticleRequest.code());
        article.setName(updateArticleRequest.name());

        articleRepository.save(article);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " updated article with code: " + article.getCode();
        createAndSaveNotification(user, message);


        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public ArticleResponse deleteArticleByCode(String code) {
        Article article = articleRepository.findByCode(code)
                .orElseThrow(() -> new NoArticleFound("No article with this code found"));

        ArticleResponse articleResponse = ArticleMapper.articleToResponseDto(article);

        articleRepository.delete(article);

        return articleResponse;

    }


    @Override
    public ArticleResponse deleteArticleById(long id,long userId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoArticleFound("No article with this id found"));

        ArticleResponse articleResponse = ArticleMapper.articleToResponseDto(article);

        String articleCode = article.getCode();

        articleRepository.delete(article);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + " deleted article with code: " + articleCode;
        createAndSaveNotification(user, message);


        return articleResponse;
    }

    @Transactional
    public void deleteAllArticlesAndResetSequence(long userId) {
        articleRepository.deleteAll();
        articleRepository.resetArticleIdSequence();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserFound("No user with this id found"));

        String message = "User: " + user.getEmail() + "  deleted all articles";
        createAndSaveNotification(user, message);

    }




}
