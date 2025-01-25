package mycode.stockmanager.app.location.mapper;

import mycode.stockmanager.app.articles.dtos.ArticleResponse;
import mycode.stockmanager.app.articles.dtos.CreateArticleRequest;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.location.dtos.CreateLocationRequest;
import mycode.stockmanager.app.location.dtos.LocationResponse;
import mycode.stockmanager.app.location.model.Location;

public class LocationMapper {

    public static LocationResponse locationToResponseDto(Location location){
        return LocationResponse.builder()
                .code(location.getCode())
                .id(location.getId())
                .stock(location.getStock()).build();
    }

    public static Location createLocationRequestToLocation(CreateLocationRequest createLocationRequest){
        return Location.builder()
                .code(createLocationRequest.code()).build();
    }
}
