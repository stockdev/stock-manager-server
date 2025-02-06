package mycode.stockmanager.app.articles.exceptions;

public class AlreadyExistsArticle extends RuntimeException {
    public AlreadyExistsArticle(String message) {
        super(message);
    }
}
