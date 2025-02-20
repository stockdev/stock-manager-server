package mycode.stockmanager.app.stock.exceptions;

public class InvalidStockTransaction extends RuntimeException {
    public InvalidStockTransaction(String message) {
        super(message);
    }
}
