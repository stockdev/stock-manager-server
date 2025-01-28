package mycode.stockmanager.app.utilaje.web;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.services.UtilajCommandService;
import mycode.stockmanager.app.utilaje.services.UtilajQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-manager/api/utilaj")
@AllArgsConstructor
public class UtilajController {


    private UtilajCommandService utilajCommandService;
    private UtilajQueryService utilajQueryService;


    @GetMapping("/getUtilajById/{utilajId}")
    ResponseEntity<UtilajResponseDto> getUtilajById(@PathVariable Long utilajId){
        return new ResponseEntity<>(utilajQueryService.getUtilajById(utilajId), HttpStatus.OK);
    }

    @PostMapping("/createUtilaj")
    ResponseEntity<UtilajResponseDto> createUtilaj(@RequestBody CreateUtilajRequest createUtilajRequest){
        return new ResponseEntity<>(utilajCommandService.createUtilaj(createUtilajRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteUtilajById/{utilajId}")
    ResponseEntity<UtilajResponseDto> deleteUtilajById(@PathVariable Long utilajId){
        return new ResponseEntity<>(utilajCommandService.deleteUtilaj(utilajId), HttpStatus.OK);
    }
}
