package mycode.stockmanager.app.exceptions_handler;

import mycode.stockmanager.app.articles.exceptions.AlreadyExistsArticle;
import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.location.exceptions.LocationAlreadyExists;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
import mycode.stockmanager.app.notification.exceptions.NotFoundNotification;
import mycode.stockmanager.app.stock.exceptions.InvalidStockTransaction;
import mycode.stockmanager.app.stock.exceptions.NoStockFound;
import mycode.stockmanager.app.users.exceptions.AccessDeniedException;
import mycode.stockmanager.app.users.exceptions.NoUserFound;
import mycode.stockmanager.app.users.exceptions.UserAlreadyExists;
import mycode.stockmanager.app.utilaje.exceptions.NoUtilajFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler({NoArticleFound.class})
    public ResponseEntity<Object> handleArticleNotFoundException(NoArticleFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoLocationFound.class})
    public ResponseEntity<Object> handleLocationNotFoundException(NoLocationFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoUserFound.class})
    public ResponseEntity<Object> handleUserNotFoundException(NoUserFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({UserAlreadyExists.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExists exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoStockFound.class})
    public ResponseEntity<Object> handleStockNotFoundException(NoStockFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({InvalidStockTransaction.class})
    public ResponseEntity<Object> handleInvalidStockTransactionException(InvalidStockTransaction exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({LocationAlreadyExists.class})
    public ResponseEntity<Object> handleLocationAlreadyExistsException(LocationAlreadyExists exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NoUtilajFound.class})
    public ResponseEntity<Object> handleUtilajNotFoundException(NoUtilajFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({AlreadyExistsArticle.class})
    public ResponseEntity<Object> handleAlreadyExistsArticleException(AlreadyExistsArticle exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccesDeniedException(AccessDeniedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }

    @ExceptionHandler({NotFoundNotification.class})
    public ResponseEntity<Object> handleNotFoundNotificationException(NotFoundNotification exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
