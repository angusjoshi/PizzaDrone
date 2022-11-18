package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;


/**
 * class to store a pizza order
 */
public class Order {
    private String orderNo;
    private String customer;
    private String creditCardNumber;
    private String cvv;
    private int priceTotalInPence;
    private String[] orderItems;

    private LocalDate orderDate;
    private LocalDate creditCardExpiry;

    private OrderOutcome orderOutcome;

    public Order(@JsonProperty("orderNo") String orderNo, @JsonProperty("orderDate") String orderDateString,
                 @JsonProperty("customer") String customer,@JsonProperty("creditCardNumber") String creditCardNumber,
                 @JsonProperty("creditCardExpiry") String cardExpiryString, @JsonProperty("cvv") String cvv,
                 @JsonProperty("priceTotalInPence") int priceTotalInPence,
                 @JsonProperty("orderItems") String[] orderItems) {

        this.orderNo = orderNo;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;

        this.orderDate = parseDateString(orderDateString);
        this.creditCardExpiry = parseCreditCardExpiry(cardExpiryString);
    }

    private LocalDate parseCreditCardExpiry(String cardExpiryString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MM/yy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();

        try {
            return LocalDate.parse(cardExpiryString, formatter);
        } catch(DateTimeParseException e) {
            return null;
        }
    }

    private LocalDate parseDateString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(dateString, formatter);
        } catch(DateTimeParseException e) {
            return null;
        }
    }
    private Order() {}

    public void validateOrderDate(LocalDate currentDay) {
        if(orderDate == null) {
            return;
        }
        if(!orderDate.isEqual(currentDay)) {
            orderOutcome = OrderOutcome.Invalid;
        }
    }
    public void validateCreditCard() throws OrderDateNotValidatedException {

        if(orderDate == null) {
            throw new OrderDateNotValidatedException(
                    "Order date must be validated before attempting credit card validation!");
        }

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
        if(creditCardExpiry == null) {
            return false;
        }

        return creditCardExpiry.isAfter(orderDate);
    }

    private boolean isCvvValid() {
        if(cvv.length() != 3) {
            return false;
        }
        return stringIsAllDigits(cvv);
    }

    private boolean isCreditCardNumberValid() {
        return GenericValidator.isCreditCard(creditCardNumber);
    }
    
    private static boolean stringIsAllDigits(String string) {
        return string.matches("^[0-9]*$");
    }

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
}