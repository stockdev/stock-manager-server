package mycode.stockmanager.app.magazie.mapper;

import mycode.stockmanager.app.magazie.dtos.MagazieResponse;
import mycode.stockmanager.app.magazie.model.Magazie;

public class MagazieMapper {

    public static MagazieResponse magazieToResponseDto(Magazie magazie){


        return MagazieResponse.builder()
                .articleCode(magazie.getArticleCode())
                .locationCode(magazie.getLocationCode())
                .stock(magazie.getStock())
                .totalStock(magazie.getTotalStock())
                .build();
    }
}
