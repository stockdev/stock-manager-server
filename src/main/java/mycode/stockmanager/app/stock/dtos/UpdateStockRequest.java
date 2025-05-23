package mycode.stockmanager.app.stock.dtos;

import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.enums.SubStockType;

public record UpdateStockRequest(String locationCode, String articleCode, StockType stockType, SubStockType subStockType, String orderNumber, int quantity, int necessary) {
}
