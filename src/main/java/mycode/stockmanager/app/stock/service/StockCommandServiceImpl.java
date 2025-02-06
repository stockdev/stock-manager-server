package mycode.stockmanager.app.stock.service;

import lombok.AllArgsConstructor;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.articles.repository.ArticleRepository;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.location.repository.LocationRepository;
import mycode.stockmanager.app.notification.model.Notification;
import mycode.stockmanager.app.notification.notification_type.NotificationType;
import mycode.stockmanager.app.notification.repository.NotificationRepository;
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
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.model.User;
import mycode.stockmanager.app.users.repository.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class StockCommandServiceImpl implements StockCommandService{

    StockRepository stockRepository;
    ArticleRepository articleRepository;
    LocationRepository locationRepository;
    NotificationRepository notificationRepository;
    UserRepository userRepository;


    private void createAndSaveNotification(User user, String message) {
        Notification notification = Notification.builder()
                .notificationType(NotificationType.INFORMATION)
                .user(user)
                .message(message)
                .build();

        notificationRepository.saveAndFlush(notification);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoUserFound("User not found"));
    }

    @Override
    public StockResponse createStockTransaction(CreateStockRequest createStockRequest) {

        isValidTransaction(createStockRequest.stockType(), createStockRequest.subStockType());


        Stock stock = Stock.builder()
                .stockType(createStockRequest.stockType())
                .subStockType(createStockRequest.subStockType())
                .article(articleRepository.findByCode(createStockRequest.articleCode())
                        .orElseThrow(() -> new NoArticleFound("No article with this code found")))
                .location(locationRepository.findByCode(createStockRequest.locationCode())
                        .orElseThrow(() -> new NoLocationFound("No location with this code found")))
                .necessary(createStockRequest.necessary())
                .orderNumber(createStockRequest.orderNumber())
                .quantity(createStockRequest.quantity())
                .transactionDate(LocalDateTime.now())
                .build();


        stockRepository.saveAndFlush(stock);

        User user = getAuthenticatedUser();

        String message = "User: " + user.getEmail() + " created stock transaction with id: " + stock.getId();
        createAndSaveNotification(user, message);
        return StockMapper.stockToResponseDto(stock);


    }

    private void isValidTransaction(StockType stockType, SubStockType subStockType) {
        if (stockType == StockType.IN && subStockType == SubStockType.P) {
            throw new InvalidStockTransaction("Invalid stock transaction, sub-stock type P cannot be stock in");
        }
        if (stockType == StockType.OUT && (subStockType == SubStockType.RP || subStockType == SubStockType.F)) {
            throw new InvalidStockTransaction("Invalid stock transaction, sub-stock type RP or F cannot be stock out");
        }
    }

    @Override
    public StockResponse deleteStockTransaction(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoStockFound("No stock transaction with this id found"));

        StockResponse stockResponse =StockMapper.stockToResponseDto(stock);

        stockRepository.delete(stock);
        User user = getAuthenticatedUser();
        String message = "User: " + user.getEmail() + " deleted stock transaction with id: " + stock.getId();
        createAndSaveNotification(user, message);
        return stockResponse;
    }

    @Override
    public StockResponse updateStockTransaction(Long id, UpdateStockRequest updateStockRequest) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoStockFound("No stock with this id found"));

        isValidTransaction(updateStockRequest.stockType(), updateStockRequest.subStockType());

        stock.setArticle(articleRepository.findByCode(updateStockRequest.articleCode()).orElseThrow(() -> new NoArticleFound("No article with this code found")));
        stock.setLocation(locationRepository.findByCode(updateStockRequest.locationCode()).orElseThrow(() -> new NoLocationFound("No location with this code found")));
        stock.setNecessary(updateStockRequest.necessary());
        stock.setOrderNumber(updateStockRequest.orderNumber());
        stock.setQuantity(updateStockRequest.quantity());
        stock.setStockType(updateStockRequest.stockType());
        stock.setSubStockType(updateStockRequest.subStockType());


        stockRepository.save(stock);

        User user = getAuthenticatedUser();
        String message = "User: " + user.getEmail() + " updated stock transaction with id: " + stock.getId();
        createAndSaveNotification(user, message);

        return StockMapper.stockToResponseDto(stock);
    }

    @Transactional
    @Override
    public void deleteAllStocksAndResetSequence() {
        locationRepository.deleteAll();
        locationRepository.resetLocationSequence();

        User user = getAuthenticatedUser();

        String message = "User: " + user.getEmail() + " deleted all locations";
        createAndSaveNotification(user, message);
    }
}
