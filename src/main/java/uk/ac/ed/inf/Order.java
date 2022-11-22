package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDate;

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

        this.orderDate = DateParser.parseDateString(orderDateString);
        this.creditCardExpiry = DateParser.parseCreditCardExpiry(cardExpiryString);
    }



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