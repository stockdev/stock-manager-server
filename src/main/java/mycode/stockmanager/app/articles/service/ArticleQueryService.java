package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.*;

public interface ArticleQueryService {


    ArticleResponse getArticleByCode(String code);


    ArticleResponseList getArticles(int page, int size, String searchTerm);


    MagazieTotalResponse getMagazieTotalForArticle(String articleCode);

}
