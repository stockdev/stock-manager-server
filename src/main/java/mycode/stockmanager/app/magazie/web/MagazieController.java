package mycode.stockmanager.app.magazie.web;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.magazie.dtos.MagazieResponseList;
import mycode.stockmanager.app.magazie.service.MagazieQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock-manager/api/magazie")
@AllArgsConstructor
public class MagazieController {

    private MagazieQueryService magazieQueryService;

    @GetMapping("/getAllByArticleCode/{articleCode}")
    public ResponseEntity<MagazieResponseList> getAllByArticleCode(@PathVariable String articleCode){
        return new ResponseEntity<>(magazieQueryService.getAllByArticleCode(articleCode), HttpStatus.OK);
    }
}
