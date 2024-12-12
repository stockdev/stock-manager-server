package mycode.stockmanager.app.exceptions_handler;

import mycode.stockmanager.app.articles.exceptions.NoArticleFound;
import mycode.stockmanager.app.location.exceptions.NoLocationFound;
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

    @ExceptionHandler({NoArticleFound.class})
    public ResponseEntity<Object> handleLocationNotFoundException(NoLocationFound exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
