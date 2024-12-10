package mycode.stockmanager.app.articles.mapper;

import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.model.Article;

public class ArticleMapper {

    public static ArticleResponse articleToResponseDto(Article article){
        return ArticleResponse.builder()
                .code(article.getCode())
                .id(article.getId())
                .name(article.getName())
                .stock(article.getStock()).build();
    }

    public static Article createArticleRequestToArticle(CreateArticleRequest createArticleRequest){
        return Article.builder()
                .code(createArticleRequest.code())
                .name(createArticleRequest.name()).build();
    }
}
