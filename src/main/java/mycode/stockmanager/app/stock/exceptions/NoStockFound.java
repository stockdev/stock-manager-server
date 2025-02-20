package mycode.stockmanager.app.stock.exceptions;

public class NoStockFound extends RuntimeException {
    public NoStockFound(String message) {
        super(message);
    }
}
