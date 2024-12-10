package mycode.stockmanager.app.articles.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.mapper.ArticleMapper;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleCommandServiceImpl implements ArticleCommandService {

    private ArticleRepository articleRepository;


    @Override
    public ArticleResponse createArticle(CreateArticleRequest createArticleRequest) {
        Article article = ArticleMapper.createArticleRequestToArticle(createArticleRequest);

        articleRepository.saveAndFlush(article);

        return ArticleMapper.articleToResponseDto(article);
    }

    @Override
    public ArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoArticleFound("No article with this id found"));


        article.setCode(updateArticleRequest.code());
        article.setName(updateArticleRequest.name());

        articleRepository.save(article);


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
    public ArticleResponse deleteArticleById(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoArticleFound("No article with this id found"));

        ArticleResponse articleResponse = ArticleMapper.articleToResponseDto(article);

        articleRepository.delete(article);

        return articleResponse;
    }


}
