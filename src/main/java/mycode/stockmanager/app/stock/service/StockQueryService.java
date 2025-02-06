package mycode.stockmanager.app.stock.service;

import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.dtos.StockResponseList;

import java.util.List;

public interface StockQueryService {

    StockResponse getStockById(Long id);

    StockResponse getStockByOrderNumber(String orderNumber);

    StockResponseList getAllStocks();
}
