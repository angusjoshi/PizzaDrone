package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.validator.GenericValidator;


/**
 * class to store a pizza order
 */
public class Order {


    @JsonProperty
    private String orderNo;
    @JsonProperty
    private String orderDate;
    @JsonProperty
    private String customer;
    @JsonProperty
    private String creditCardNumber;
    @JsonProperty
    private String creditCardExpiry;
    @JsonProperty
    private String cvv;
    @JsonProperty
    private int priceTotalInPence;
    @JsonProperty
    private String[] orderItems;

    private OrderOutcome orderOutcome;

    public Order(String orderNo, String orderDate, String customer, String creditCardNumber, String creditCardExpiry,
                 String cvv, int priceTotalInPence, String[] orderItems) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
    }
    private Order() {}

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }
    public OrderOutcome getOrderOutcome() {
        return orderOutcome;
    }
    public void setOrderOutcome(OrderOutcome orderOutcome) {
        this.orderOutcome = orderOutcome;
    }
    public String[] getOrderItems() {
        return orderItems;
    }
    public void validateCreditCard() {
        if(!isCvvValid()) {
            orderOutcome = OrderOutcome.InvalidCvv;
        }
        if(!isCreditCardNumberValid()) {
            orderOutcome = OrderOutcome.InvalidCardNumber;
        }
        if(!isCreditCardExpiryValid()) {
            orderOutcome = OrderOutcome.InvalidExpiryDate;
        }
    }
    private boolean isCreditCardExpiryValid() {
        if(!GenericValidator.isDate(creditCardExpiry, "MM/YY", true)) {
            return false;
        }
        return true;
    }

    private boolean isCvvValid() {
        if(cvv.length() != 3) {
            return false;
        }
        if(!stringIsAllDigits(cvv)) {
            return false;
        }
        return true;
    }

    private boolean isCreditCardNumberValid() {
        return GenericValidator.isCreditCard(creditCardNumber);
    }
    
    private static boolean stringIsAllDigits(String string) {
        return string.matches("^[0-9]*$");
    }
}