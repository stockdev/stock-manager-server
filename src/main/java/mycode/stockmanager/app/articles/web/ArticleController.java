package mycode.stockmanager.app.articles.web;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.*;
import mycode.stockmanager.app.articles.service.ArticleCommandService;
import mycode.stockmanager.app.articles.service.ArticleQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<ArticleResponseList> getAllArticles() {
        return new ResponseEntity<>(articleQueryService.getAllArticles(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createArticle")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        return new ResponseEntity<>(articleCommandService.createArticle(createArticleRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/updateArticle/{articleId}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable long articleId, @RequestBody UpdateArticleRequest updateArticleRequest) {
        return new ResponseEntity<>(articleCommandService.updateArticle(updateArticleRequest, articleId), HttpStatus.OK);
    }



    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteArticleByCode/{articleCode}")
    public ResponseEntity<String> deleteArticleByCode(@PathVariable String articleCode) {
        return new ResponseEntity<>(articleCommandService.deleteArticleByCode(articleCode), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllArticles")
    public ResponseEntity<String> deleteAllArticles() {
        articleCommandService.deleteAllArticlesAndResetSequence();
        return ResponseEntity.ok("Deleted all articles");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/importExcel")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            ImportResponse importResponse = articleCommandService.importArticlesFromExcel(file);
            return ResponseEntity.ok(importResponse);
        } catch (Exception e) {
            List<String> errorList = List.of("Error importing: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ImportResponse(0, errorList));
        }
    }

}
