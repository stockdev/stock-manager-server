package mycode.stockmanager.app.articles.dtos;

import lombok.Builder;

@Builder
public record MagaziePrintResponse(String locationCode, int stock) {
}
