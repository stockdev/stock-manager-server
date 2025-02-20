package mycode.stockmanager.app.articles.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record MagazieResponseList(List<MagaziePrintResponse> list) {
}
