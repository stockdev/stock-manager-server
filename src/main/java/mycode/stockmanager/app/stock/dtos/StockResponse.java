package mycode.stockmanager.app.stock.dtos;

import lombok.Builder;
import mycode.stockmanager.app.articles.model.Article;
import mycode.stockmanager.app.location.model.Location;
import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.enums.SubStockType;

import java.time.LocalDateTime;

@Builder
public record StockResponse(Long id, Location location, Article article, StockType stockType, SubStockType subStockType, String orderNumber, int quantity, int necessary, LocalDateTime transactionDate, String comment) {
}
