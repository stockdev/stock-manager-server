package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.ArticleResponse;

public interface ArticleQueryService {

    ArticleResponse getArticleById(long id);

    ArticleResponse getArticleByCode(String code);
}
