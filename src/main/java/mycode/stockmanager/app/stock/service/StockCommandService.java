package mycode.stockmanager.app.stock.service;

import mycode.stockmanager.app.stock.dtos.CreateStockRequest;
import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.dtos.UpdateStockRequest;

public interface StockCommandService {

    StockResponse createStockTransaction(CreateStockRequest createStockRequest);

    StockResponse deleteStockTransaction(Long id);

    StockResponse updateStockTransaction(Long id, UpdateStockRequest updateStockRequest);
}
