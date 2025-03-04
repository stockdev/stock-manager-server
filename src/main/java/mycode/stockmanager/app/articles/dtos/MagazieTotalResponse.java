package mycode.stockmanager.app.articles.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record MagazieTotalResponse(String articleName, String articleCode, int stockIn, int stockOut, int finalStock, int stockProduction,
                                   List<MagaziePrintResponse> locations) {
}
