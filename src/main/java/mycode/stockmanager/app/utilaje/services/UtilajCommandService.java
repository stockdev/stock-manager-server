package mycode.stockmanager.app.utilaje.services;

import mycode.stockmanager.app.articles.dtos.ImportResponse;
import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UtilajCommandService {


    UtilajResponseDto createUtilaj(CreateUtilajRequest createUtilajRequest);

    String deleteUtilajByCode(String code);

    ImportResponse importUtilajeFromExcel(MultipartFile file);
}
