package mycode.stockmanager.app.articles.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.ArticleResponseList;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.dtos.LocationResponseList;
import mycode.stockmanager.app.location.mapper.LocationMapper;
import mycode.stockmanager.app.location.model.Location;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ArticleResponseList getAllArticles() {
        List<Article> list = articleRepository.findAll();

        List<ArticleResponse> responses = new ArrayList<>();

        if(list.isEmpty()){
            throw new NoArticleFound("No articles found");
        }
        
        list.forEach(article -> {

            responses.add(ArticleMapper.articleToResponseDto(article));
        });

        return new ArticleResponseList(responses);
    }
}
