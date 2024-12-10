package mycode.stockmanager.app.articles.dtos;

import lombok.Builder;
import mycode.stockmanager.app.stock.model.Stock;

@Builder
public record ArticleResponse(String code, String name, Stock stock, Long id) {
}
