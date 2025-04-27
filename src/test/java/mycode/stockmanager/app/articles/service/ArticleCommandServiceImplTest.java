package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import mycode.stockmanager.app.articles.exceptions.AlreadyExistsArticle;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleCommandServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ArticleCommandServiceImpl articleCommandService;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setEmail("test@example.com");

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
    }

    @Test
    void testCreateArticle_Success() {
        CreateArticleRequest request = new CreateArticleRequest("ART123", "Test Article");

        when(articleRepository.findByCode(request.code())).thenReturn(Optional.empty());


        articleCommandService.createArticle(request);

        verify(articleRepository, times(1)).saveAndFlush(any(Article.class));
        verify(notificationRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void testCreateArticle_AlreadyExists() {
        CreateArticleRequest request = new CreateArticleRequest("ART123", "Test Article");
        when(articleRepository.findByCode(request.code())).thenReturn(Optional.of(new Article()));


        AlreadyExistsArticle exception = assertThrows(AlreadyExistsArticle.class, () ->
                articleCommandService.createArticle(request));

        assertEquals("Article with this code already exists try again with different code", exception.getMessage());
    }

    @Test
    void testUpdateArticle_Success() {
        UpdateArticleRequest request = new UpdateArticleRequest("Updated Article", "ART123");
        Article existingArticle = new Article();
        existingArticle.setCode("ART123");
        existingArticle.setName("Old Article");

        when(articleRepository.findByCode("ART123")).thenReturn(Optional.of(existingArticle));

        articleCommandService.updateArticle(request, "ART123");

        verify(articleRepository, times(1)).save(existingArticle);
        verify(notificationRepository, times(1)).saveAndFlush(any());
        assertEquals("Updated Article", existingArticle.getName());
    }

    @Test
    void testUpdateArticle_NotFound() {
        UpdateArticleRequest request = new UpdateArticleRequest("ART123", "Updated Article");
        when(articleRepository.findByCode("ART123")).thenReturn(Optional.empty());

        NoArticleFound exception = assertThrows(NoArticleFound.class, () ->
                articleCommandService.updateArticle(request, "ART123"));

        assertEquals("No article with this id found", exception.getMessage());
    }

    @Test
    void testDeleteArticle_Success() {
        Article existingArticle = new Article();
        existingArticle.setCode("ART123");

        when(articleRepository.findByCode("ART123")).thenReturn(Optional.of(existingArticle));

        articleCommandService.deleteArticleByCode("ART123");

        verify(articleRepository, times(1)).delete(existingArticle);
        verify(notificationRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void testDeleteArticle_NotFound() {
        when(articleRepository.findByCode("ART123")).thenReturn(Optional.empty());

        NoArticleFound exception = assertThrows(NoArticleFound.class, () ->
                articleCommandService.deleteArticleByCode("ART123"));

        assertEquals("No article with this code found", exception.getMessage());
    }

    @Test
    void testGetAuthenticatedUser_NotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        NoUserFound exception = assertThrows(NoUserFound.class, () ->
                articleCommandService.createArticle(new CreateArticleRequest("ART123", "Test Article")));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testDeleteAllArticlesAndResetSequence() {

        articleCommandService.deleteAllArticlesAndResetSequence();

        verify(articleRepository, times(1)).deleteAll();
        verify(articleRepository, times(1)).resetArticleIdSequence();
        verify(notificationRepository, times(1)).saveAndFlush(any());
    }

}
