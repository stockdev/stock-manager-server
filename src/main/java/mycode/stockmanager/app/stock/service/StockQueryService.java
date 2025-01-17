package mycode.stockmanager.app.stock.service;

import mycode.stockmanager.app.stock.dtos.StockResponse;

public interface StockQueryService {

    StockResponse getStockById(Long id);

    StockResponse getStockByOrderNumber(int orderNumber);

}
