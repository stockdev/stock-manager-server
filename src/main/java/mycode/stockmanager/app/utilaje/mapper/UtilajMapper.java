package mycode.stockmanager.app.utilaje.mapper;

import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.model.Utilaj;

public class UtilajMapper {

    public static UtilajResponseDto utilajToResponseDto(Utilaj utilaj){

        return UtilajResponseDto.builder().name(utilaj.getName()).id(utilaj.getId()).build();

    }
}
