package mycode.stockmanager.app.utilaje.mapper;

import mycode.stockmanager.app.utilaje.dtos.CreateUtilajRequest;
import mycode.stockmanager.app.utilaje.dtos.UtilajResponseDto;
import mycode.stockmanager.app.utilaje.model.Utilaj;

public class UtilajMapper {

    public static UtilajResponseDto utilajToResponseDto(Utilaj utilaj){

        return UtilajResponseDto.builder().name(utilaj.getName()).code(utilaj.getCode()).build();

    }

    public static Utilaj createRequestToUtilaj(CreateUtilajRequest createUtilajRequest){
        return Utilaj.builder().code(createUtilajRequest.code()).name(createUtilajRequest.name()).build();
    }
}
