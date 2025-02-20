package mycode.stockmanager.app.notification.exceptions;

public class NotFoundNotification extends RuntimeException{
    public NotFoundNotification(String message) {
        super(message);
    }
}
