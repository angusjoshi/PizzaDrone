package uk.ac.ed.inf.order;

public class OrderDateNotValidatedException extends Throwable {
    private final String message;
    public OrderDateNotValidatedException(String s) {
        message = s;
    }
    public String getMessage() {
        return message;
    }
}
