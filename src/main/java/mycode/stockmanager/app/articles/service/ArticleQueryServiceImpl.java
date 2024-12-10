package mycode.stockmanager.app.articles.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleQueryServiceImpl implements ArticleQueryService {

    private ArticleRepository articleRepository;

    @Override
    public ArticleResponse getArticleById(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoArticleFound("No article with this id found"));

        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public ArticleResponse getArticleByCode(String code) {
        Article article = articleRepository.findByCode(code)
                .orElseThrow(() -> new NoArticleFound("No article with this code found"));

        return ArticleMapper.articleToResponseDto(article);
    }
}
