package mycode.stockmanager.app.utilaje.services;

import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;

import java.util.List;

public interface UtilajQueryService {

    UtilajResponseDto getUtilajByCode(String code);

    List<UtilajResponseDto> getAllUtilaje(int page, int size, String searchTerm);
}
