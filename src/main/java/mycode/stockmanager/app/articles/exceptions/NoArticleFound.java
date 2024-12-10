package mycode.stockmanager.app.articles.exceptions;

public class NoArticleFound extends RuntimeException {
    public NoArticleFound(String message) {
        super(message);
    }
}
