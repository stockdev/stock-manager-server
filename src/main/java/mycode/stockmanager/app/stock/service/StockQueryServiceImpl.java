package mycode.stockmanager.app.stock.service;


import lombok.AllArgsConstructor;
import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.dtos.StockResponseList;
import mycode.stockmanager.app.stock.exceptions.NoStockFound;
import mycode.stockmanager.app.stock.mapper.StockMapper;
import mycode.stockmanager.app.stock.model.Stock;
import mycode.stockmanager.app.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StockQueryServiceImpl implements StockQueryService {

    StockRepository stockRepository;

    @Override
    public StockResponse getStockById(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoStockFound("No stock with this id found"));

        return StockMapper.stockToResponseDto(stock);
    }

    @Override
    public StockResponse getStockByOrderNumber(String orderNumber) {
        Stock stock = stockRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NoStockFound("No stock with this order number found"));

        return StockMapper.stockToResponseDto(stock);
    }

    @Override
    public StockResponseList getAllStocks() {
        List<Stock> list = stockRepository.findAll();
        if (list.isEmpty()) {
            throw new NoStockFound("No stock's found");
        }
        List<StockResponse> responses = StockMapper.stocksToResponseDto(list);

        return new StockResponseList(responses);

    }


}
