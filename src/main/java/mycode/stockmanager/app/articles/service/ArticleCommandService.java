package mycode.stockmanager.app.articles.service;

import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleCommandService {

    ArticleResponse createArticle(CreateArticleRequest createArticleRequest);

    ArticleResponse updateArticle(UpdateArticleRequest updateArticleRequest, String articleCode);

    String  deleteArticleByCode(String code);

    ImportResponse importArticlesFromExcel(MultipartFile file);


    void deleteAllArticlesAndResetSequence();

}
