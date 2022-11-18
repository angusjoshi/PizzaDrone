package uk.ac.ed.inf;

public class OrderDateNotValidatedException extends Throwable {
    private final String message;
    public OrderDateNotValidatedException(String s) {
        message = s;
    }
    public String getMessage() {
        return message;
    }
}
