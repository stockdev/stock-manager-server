package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.*;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleQueryServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleQueryServiceImpl articleQueryService;

    private Article sampleArticle;

    @BeforeEach
    void setUp() {
        sampleArticle = new Article();
        sampleArticle.setCode("ART123");
        sampleArticle.setName("Test Article");
    }

    @Test
    void testGetArticleByCode_Success() {
        when(articleRepository.findByCode("ART123")).thenReturn(Optional.of(sampleArticle));

        ArticleResponse response = articleQueryService.getArticleByCode("ART123");

        assertNotNull(response);
        assertEquals("ART123", response.code());
        assertEquals("Test Article", response.name());
    }

    @Test
    void testGetArticleByCode_NotFound() {
        when(articleRepository.findByCode("ART123")).thenReturn(Optional.empty());

        assertThrows(NoArticleFound.class, () -> articleQueryService.getArticleByCode("ART123"));
    }

    @Test
    void testGetArticles_Success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Article> articlePage = new PageImpl<>(List.of(sampleArticle), pageable, 1);

        when(articleRepository.findAll(any(Pageable.class))).thenReturn(articlePage);

        ArticleResponseList response = articleQueryService.getArticles(0, 10, "");

        assertNotNull(response);
        assertEquals(1, response.totalElements());
        assertEquals("ART123", response.list().get(0).code());
    }

    @Test
    void testGetArticles_NoResults() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Article> emptyPage = Page.empty(pageable);

        when(articleRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThrows(NoArticleFound.class, () -> articleQueryService.getArticles(0, 10, ""));
    }

    @Test
    void testGetMagazieTotalForArticle_Success() {
        Location location = new Location();
        location.setCode("LOC1");

        Stock stockIn = new Stock();
        stockIn.setStockType(StockType.IN);
        stockIn.setQuantity(50);
        stockIn.setLocation(location);
        Stock stockOut = new Stock();
        stockOut.setStockType(StockType.OUT);
        stockOut.setQuantity(20);
        stockOut.setNecessary(5);
        stockOut.setLocation(location);

        sampleArticle.setStocks(List.of(stockIn, stockOut));

        when(articleRepository.findByCode("ART123")).thenReturn(Optional.of(sampleArticle));

        MagazieTotalResponse response = articleQueryService.getMagazieTotalForArticle("ART123");

        assertNotNull(response);
        assertEquals("ART123", response.articleCode());
        assertEquals(30, response.finalStock());
        assertEquals(50, response.stockIn());
        assertEquals(20, response.stockOut());
        assertEquals(5, response.stockProduction());
    }


    @Test
    void testGetMagazieTotalForArticle_NoStock() {
        sampleArticle.setStocks(List.of());
        when(articleRepository.findByCode("ART123")).thenReturn(Optional.of(sampleArticle));

        assertThrows(NoArticleFound.class, () -> articleQueryService.getMagazieTotalForArticle("ART123"));
    }
}