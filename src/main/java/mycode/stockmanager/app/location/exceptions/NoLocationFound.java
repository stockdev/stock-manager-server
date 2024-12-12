package mycode.stockmanager.app.location.exceptions;

public class NoLocationFound extends RuntimeException {
    public NoLocationFound(String message) {
        super(message);
    }
}
