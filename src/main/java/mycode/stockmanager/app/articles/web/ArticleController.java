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

    @GetMapping("/getArticleByCode/{code}")
    public ResponseEntity<ArticleResponse> getArticleByCode(@PathVariable String code) {
        return new ResponseEntity<>(articleQueryService.getArticleByCode(code), HttpStatus.OK);
    }

    @GetMapping("/getAllArticles")
    public ResponseEntity<ArticleResponseList> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String searchTerm
    ) {
        ArticleResponseList response = articleQueryService.getArticles(page, size, searchTerm);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createArticle")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        return new ResponseEntity<>(articleCommandService.createArticle(createArticleRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/updateArticle/{articleCode}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable String articleCode, @RequestBody UpdateArticleRequest updateArticleRequest) {
        return new ResponseEntity<>(articleCommandService.updateArticle(updateArticleRequest, articleCode), HttpStatus.OK);
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



    @GetMapping("/getMagazieTotalForArticle/{articleCode}")
    public ResponseEntity<MagazieTotalResponse> getMagazieTotalForArticle(@PathVariable String articleCode){
        return new ResponseEntity<>(articleQueryService.getMagazieTotalForArticle(articleCode), HttpStatus.OK);
    }
}
