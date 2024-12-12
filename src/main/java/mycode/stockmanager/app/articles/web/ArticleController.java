package mycode.stockmanager.app.articles.web;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.ArticleResponseList;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.dtos.UpdateArticleRequest;
import mycode.stockmanager.app.articles.service.ArticleCommandService;
import mycode.stockmanager.app.articles.service.ArticleQueryService;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-manager/api/article")
@AllArgsConstructor
public class ArticleController {


    private final ArticleCommandService articleCommandService;
    private final ArticleQueryService articleQueryService;


    @GetMapping("/getArticleById/{articleId}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable long articleId) {
        return new ResponseEntity<>(articleQueryService.getArticleById(articleId), HttpStatus.OK);
    }
    
    @GetMapping("/getArticleByCode/{code}")
    public ResponseEntity<ArticleResponse> getArticleByCode(@PathVariable String code) {
        return new ResponseEntity<>(articleQueryService.getArticleByCode(code), HttpStatus.OK);
    }

    @GetMapping("/getAllArticles")
    public ResponseEntity<ArticleResponseList> getAllArticles(){
        return new ResponseEntity<>(articleQueryService.getAllArticles(), HttpStatus.OK);
    }
    
    @PostMapping("/createArticle")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        return new ResponseEntity<>(articleCommandService.createArticle(createArticleRequest), HttpStatus.CREATED);
    }
    
    @PutMapping("/updateArticle/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable long articleId, @RequestBody UpdateArticleRequest updateArticleRequest) {
        return new ResponseEntity<>(articleCommandService.updateArticle(updateArticleRequest, articleId), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteArticleById/{articleId}")
    public ResponseEntity<ArticleResponse> deleteArticleById(@PathVariable long articleId) {
        return new ResponseEntity<>(articleCommandService.deleteArticleById(articleId),HttpStatus.OK);
    }

    @DeleteMapping("/deleteArticleByCode/{articleCode}")
    public ResponseEntity<ArticleResponse> deleteArticleByCode(@PathVariable String articleCode) {
        return new ResponseEntity<>(articleCommandService.deleteArticleByCode(articleCode),HttpStatus.OK);
    }
    
    
}
