package mycode.stockmanager.app.utilaje.web;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.services.UtilajCommandService;
import mycode.stockmanager.app.utilaje.services.UtilajQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/stock-manager/api/utilaj")
@AllArgsConstructor
public class UtilajController {


    private UtilajCommandService utilajCommandService;
    private UtilajQueryService utilajQueryService;

    @GetMapping("/getAllUtilaje")
    public ResponseEntity<List<UtilajResponseDto>> getAllUtilaje(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String searchTerm) {

        List<UtilajResponseDto> utilaje = utilajQueryService.getAllUtilaje(page, size, searchTerm);
        return new ResponseEntity<>(utilaje, HttpStatus.OK);
    }


    @GetMapping("/getUtilajByCode/{utilajCode}")
    ResponseEntity<UtilajResponseDto> getUtilajByCode(@PathVariable String utilajCode){
        return new ResponseEntity<>(utilajQueryService.getUtilajByCode(utilajCode), HttpStatus.OK);
    }

    @PostMapping("/createUtilaj")
    ResponseEntity<UtilajResponseDto> createUtilaj(@RequestBody CreateUtilajRequest createUtilajRequest){
        return new ResponseEntity<>(utilajCommandService.createUtilaj(createUtilajRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteUtilajByCode/{utilajCode}")
    ResponseEntity<String> deleteUtilajByCode(@PathVariable String utilajCode){
        return new ResponseEntity<>(utilajCommandService.deleteUtilajByCode(utilajCode), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/importExcel")
    public ResponseEntity<ImportResponse> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            ImportResponse importResponse = utilajCommandService.importUtilajeFromExcel(file);
            return ResponseEntity.ok(importResponse);
        } catch (Exception e) {
            List<String> errorList = List.of("Error importing: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ImportResponse(0, errorList));
        }
    }
}
