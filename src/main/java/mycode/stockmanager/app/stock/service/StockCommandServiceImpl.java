package mycode.stockmanager.app.stock.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.repository.LocationRepository;
import mycode.stockmanager.app.stock.dtos.CreateStockRequest;
import mycode.stockmanager.app.stock.dtos.StockResponse;
import mycode.stockmanager.app.stock.dtos.UpdateStockRequest;
import mycode.stockmanager.app.stock.enums.StockType;
import mycode.stockmanager.app.stock.enums.SubStockType;
import mycode.stockmanager.app.stock.exceptions.InvalidStockTransaction;
import mycode.stockmanager.app.stock.exceptions.NoStockFound;
import mycode.stockmanager.app.stock.mapper.StockMapper;
import mycode.stockmanager.app.stock.model.Stock;
import mycode.stockmanager.app.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class StockCommandServiceImpl implements StockCommandService{

    StockRepository stockRepository;
    ArticleRepository articleRepository;
    LocationRepository locationRepository;

    @Override
    public StockResponse createStockTransaction(CreateStockRequest createStockRequest) {

        if (createStockRequest.stockType() == StockType.IN && createStockRequest.subStockType() == SubStockType.P) {
            throw new InvalidStockTransaction("Invalid stock transaction, sub-stock type P cannot be stock in");
        }
        if (createStockRequest.stockType() == StockType.OUT && (createStockRequest.subStockType() == SubStockType.RP || createStockRequest.subStockType() == SubStockType.F)) {
            throw new InvalidStockTransaction("Invalid stock transaction, sub-stock type RP or F cannot be stock out");
        }


        Stock stock = Stock.builder()
                .stockType(createStockRequest.stockType())
                .subStockType(createStockRequest.subStockType())
                .article(articleRepository.findByCode(createStockRequest.articleCode())
                        .orElseThrow(() -> new NoArticleFound("No article with this code found")))
                .location(locationRepository.findByCode(createStockRequest.locationCode())
                        .orElseThrow(() -> new NoLocationFound("No location with this code found")))
                .comment(createStockRequest.comment())
                .necessary(createStockRequest.necessary())
                .orderNumber(createStockRequest.orderNumber())
                .quantity(createStockRequest.quantity())
                .transactionDate(LocalDateTime.now())
                .build();


        stockRepository.saveAndFlush(stock);
        return StockMapper.stockToResponseDto(stock);


    }

    @Override
    public StockResponse deleteStockTransaction(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoStockFound("No stock transaction with this id found"));

        StockResponse stockResponse =StockMapper.stockToResponseDto(stock);

        stockRepository.delete(stock);

        return stockResponse;
    }

    @Override
    public StockResponse updateStockTransaction(Long id, UpdateStockRequest updateStockRequest) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoStockFound("No stock with this id found"));

        if (updateStockRequest.stockType() == StockType.IN && updateStockRequest.subStockType() == SubStockType.P) {
            throw new InvalidStockTransaction("Invalid stock transaction, sub-stock type P cannot be stock in");
        }
        if (updateStockRequest.stockType() == StockType.OUT && (updateStockRequest.subStockType() == SubStockType.RP || updateStockRequest.subStockType() == SubStockType.F)) {
            throw new InvalidStockTransaction("Invalid stock transaction, sub-stock type RP or F cannot be stock out");
        }

        stock.setArticle(articleRepository.findByCode(updateStockRequest.articleCode()).orElseThrow(() -> new NoArticleFound("No article with this code found")));
        stock.setComment(updateStockRequest.comment());
        stock.setLocation(locationRepository.findByCode(updateStockRequest.locationCode()).orElseThrow(() -> new NoLocationFound("No location with this code found")));
        stock.setNecessary(updateStockRequest.necessary());
        stock.setOrderNumber(updateStockRequest.orderNumber());
        stock.setQuantity(updateStockRequest.quantity());
        stock.setStockType(updateStockRequest.stockType());
        stock.setSubStockType(updateStockRequest.subStockType());


        stockRepository.save(stock);

        return StockMapper.stockToResponseDto(stock);
    }
}
