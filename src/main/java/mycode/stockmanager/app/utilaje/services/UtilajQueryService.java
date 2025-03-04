package mycode.stockmanager.app.utilaje.services;

import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.dtos.UtilajeResponseList;

import java.util.List;

public interface UtilajQueryService {

    UtilajResponseDto getUtilajByCode(String code);

    UtilajeResponseList getAllUtilaje(int page, int size, String searchTerm);
}
