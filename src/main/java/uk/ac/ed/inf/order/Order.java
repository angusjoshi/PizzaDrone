package uk.ac.ed.inf.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.validator.GenericValidator;
import uk.ac.ed.inf.pathing.Move;
import uk.ac.ed.inf.pathing.PathFinder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * class to store a pizza order
 */
public class Order implements Comparable<Order> {
    private String orderNo;
    private String customer;
    private String creditCardNumber;
    private String cvv;
    private int priceTotalInPence;
    private String[] orderItems;

    private LocalDate orderDate;
    private LocalDate creditCardExpiry;

    private OrderOutcome orderOutcome;
    private Restaurant fulfillingRestaurant;

    private List<Move> computedPath;
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
        this.computedPath = null;
    }


    public List<Move> getDeliveryPath() {
        List<Move> deliveryPath = new ArrayList<>();
        deliveryPath.addAll(computedPath);

        Move lastMove = deliveryPath.get(deliveryPath.size() - 1);
        deliveryPath.add(Move.hover(lastMove.orderNo(), lastMove.to()));

        List<Move> reversedPath = Move.reverseMoveList(computedPath);
        deliveryPath.addAll(reversedPath);

        lastMove = deliveryPath.get(deliveryPath.size() - 1);
        deliveryPath.add(Move.hover(lastMove.orderNo(), lastMove.to()));

        return deliveryPath;
    }
    public int pathLength() {
        if(computedPath == null) {
            System.err.println("Error: attempt to get path length before path computation!");
            return -1;
        }
        return computedPath.size();
    }
    @Override
    public int compareTo(Order other) {
        return (int) Math.signum(this.movesPerPizza() - other.movesPerPizza());
    }
    public void computePath() {
        if(fulfillingRestaurant == null) {
            return;
        }
        if(!this.shouldBeDelivered()) {
            return;
        }
        this.computedPath = PathFinder.getInstance().findPathToRestaurant(fulfillingRestaurant, orderNo);
    }
    private double movesPerPizza() {
        return ((double) computedPath.size()) / orderItems.length;
    }
    public boolean shouldBeDelivered() {
        return orderOutcome == OrderOutcome.ValidButNotDelivered;
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
    public void setFulfillingRestaurant(Restaurant restaurant) {
        fulfillingRestaurant = restaurant;
    }
    public Restaurant getFulfillingRestaurant() {
        return fulfillingRestaurant;
    }
    public String getOrderNo() {
        return this.orderNo;
    }

    public void deliver() {
        setOrderOutcome(OrderOutcome.Delivered);
    }
}