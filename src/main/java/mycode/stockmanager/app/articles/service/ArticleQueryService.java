package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.ArticleResponseList;

public interface ArticleQueryService {

    ArticleResponse getArticleById(long id);

    ArticleResponse getArticleByCode(String code);

    ArticleResponseList getAllArticles();
}
