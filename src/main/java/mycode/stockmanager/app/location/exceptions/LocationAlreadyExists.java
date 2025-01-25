package mycode.stockmanager.app.location.exceptions;

public class LocationAlreadyExists extends RuntimeException {
    public LocationAlreadyExists(String message) {
        super(message);
    }
}
