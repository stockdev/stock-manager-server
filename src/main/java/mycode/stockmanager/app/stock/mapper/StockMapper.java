package mycode.stockmanager.app.stock.mapper;

import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.model.Stock;

import java.time.LocalDateTime;
import java.util.List;

public class StockMapper {

    public static StockResponse stockToResponseDto(Stock stock){
        return StockResponse.builder()
                .stockType(stock.getStockType())
                .id(stock.getId())
                .subStockType(stock.getSubStockType())
                .article(stock.getArticle())
                .location(stock.getLocation())
                .necessary(stock.getNecessary())
                .orderNumber(stock.getOrderNumber())
                .quantity(stock.getQuantity())
                .transactionDate(LocalDateTime.now()).build();
    }

    public static List<StockResponse> stocksToResponseDto(List<Stock> list) {
        return list.stream()
                .map(StockMapper::stockToResponseDto)
                .toList();
    }
}
