package mycode.stockmanager.app.stock.dtos;

import lombok.Builder;
import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.enums.SubStockType;

import java.time.LocalDateTime;

@Builder
public record CreateStockRequest(String locationCode, String articleCode, StockType stockType, SubStockType subStockType, String orderNumber, int quantity, int necessary, String comment) {
}
