package mycode.stockmanager.app.articles.web;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.service.ArticleCommandService;
import mycode.stockmanager.app.articles.service.ArticleQueryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock-manager/api/product")
@AllArgsConstructor
public class ArticleController {


    private ArticleCommandService productCommandService;
    private ArticleQueryService productQueryService;


}
