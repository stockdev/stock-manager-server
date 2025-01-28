package mycode.stockmanager.app.utilaje.services;

import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;

public interface UtilajCommandService {


    UtilajResponseDto createUtilaj(CreateUtilajRequest createUtilajRequest);

    UtilajResponseDto deleteUtilaj(Long id);
}
