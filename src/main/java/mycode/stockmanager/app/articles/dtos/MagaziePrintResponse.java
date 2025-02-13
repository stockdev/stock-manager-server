package mycode.stockmanager.app.articles.dtos;

import lombok.Builder;

@Builder
public record MagaziePrintResponse(String articleCode, String locationCode, int stock) {
}
