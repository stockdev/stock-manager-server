package mycode.stockmanager.app.utilaje.services;

import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;

public interface UtilajQueryService {

    UtilajResponseDto getUtilajById(Long Id);
}
