package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;

public interface ArticleCommandService {

    ArticleResponse createArticle(CreateArticleRequest createArticleRequest);

    ArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, long id);

    String  deleteArticleByCode(String code);


    void deleteAllArticlesAndResetSequence();

}
