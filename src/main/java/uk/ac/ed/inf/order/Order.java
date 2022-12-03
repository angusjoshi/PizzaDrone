package uk.ac.ed.inf.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.validator.GenericValidator;
import uk.ac.ed.inf.jsonutils.MoveForWriting;
import uk.ac.ed.inf.pathing.CompassDirection;
import uk.ac.ed.inf.pathing.Move;
import uk.ac.ed.inf.pathing.PathFinder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * class to store a pizza order
 */
public class Order implements Comparable<Order> {
    private final String orderNo;
    private final String customer;
    private final String creditCardNumber;
    private final String cvv;
    private final int priceTotalInPence;
    private final String[] orderItems;

    private final LocalDate orderDate;
    private final LocalDate creditCardExpiry;

    private OrderOutcome orderOutcome;
    private Restaurant fulfillingRestaurant;

    private List<Move> computedPath;

    /**
     * Constructor to be used mainly for jackson deserialisation.
     * @param orderNo Order number
     * @param orderDateString Order date
     * @param customer Customer name
     * @param creditCardNumber Credit card number as a string
     * @param cardExpiryString Credit card expiry as a string
     * @param cvv Credit card CVV as a string
     * @param priceTotalInPence Claimed price total in pence.
     * @param orderItems Array of ordered pizzas as strings.
     */
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


    /**
     * Method to return the list of moves required to fulfill this delivery. Returns an empty list if the
     * path has not yet been computed
     * @return The list of moves required to fulfill this order, including returning to appleton tower
     * and hovering appropriately.
     */
    public List<Move> getDeliveryPath() {
        if(computedPath == null) {
            return new ArrayList<>();
        }

        List<Move> deliveryPath = new ArrayList<>(computedPath);

        Move lastMove = deliveryPath.get(deliveryPath.size() - 1);
        deliveryPath.add(Move.hover(lastMove.to()));

        List<Move> reversedPath = Move.reverseMoveList(computedPath);
        deliveryPath.addAll(reversedPath);

        lastMove = deliveryPath.get(deliveryPath.size() - 1);
        deliveryPath.add(Move.hover(lastMove.to()));

        return deliveryPath;
    }

    /**
     * Gets the length of the computed path in one direction from appleton tower to the restaurant
     * required for this order.
     * @return the length of the path in one direction.
     */
    public int pathLength() {
        if(computedPath == null) {
            System.err.println("Error: attempt to get path length before path computation!");
            return -1;
        }
        return computedPath.size();
    }

    /**
     * Comparator function to be used for sorting in order of number of pizzas delivered per move
     * @param other the object to be compared.
     * @return 1 if this is greater than other, 0 if they are equal, -1 otherwise.
     */
    @Override
    public int compareTo(Order other) {
        return (int) Math.signum(this.movesPerPizza() - other.movesPerPizza());
    }

    /**
     * Computes the path required to fulfill this order. Does nothing if the order status is such that
     * the order should not be delivered.
     */
    public void computePath() {
        if(!this.shouldBeDelivered()) {
            return;
        }
        this.computedPath = PathFinder.getInstance().findPathToRestaurant(fulfillingRestaurant, orderNo);
    }
    private double movesPerPizza() {
        return ((double) computedPath.size()) / orderItems.length;
    }

    /**
     * Gets whether or not the order should be delivered.
     * @return true if the order should be delivered, false otherwise.
     */
    public boolean shouldBeDelivered() {
        return orderOutcome == OrderOutcome.ValidButNotDelivered;
    }

    /**
     * validates the order date against a day that is passed in. Sets order outcome to invalid if
     * the days do not match.
     * @param currentDay The day to be compared against.
     */
    public void validateOrderDate(LocalDate currentDay) {
        if(orderDate == null) {
            return;
        }
        if(!orderDate.isEqual(currentDay)) {
            orderOutcome = OrderOutcome.Invalid;
        }
    }

    /**
     * validates the credit card associated with this order, including cvv validation
     * cc number validation, and cc expiry validation.
     */
    protected void validateCreditCard() {
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

    /**
     * validates whether a credit card expiry is valid
     * @return true if the expiry is valid, false otherwise.
     */
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

    /**
     * Getter for price total in pence
     * @return the price total in pence
     */
    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    /**
     * getter for order outcome
     * @return The order outcome
     */
    public OrderOutcome getOrderOutcome() {
        return orderOutcome;
    }

    /**
     * Setter for the orderoutcome
     * @param orderOutcome orderoutcome to set to
     */
    protected void setOrderOutcome(OrderOutcome orderOutcome) {
        this.orderOutcome = orderOutcome;
    }

    /**
     * Getter for the orderItems
     * @return the orderItems associated with this order.
     */
    public String[] getOrderItems() {
        return orderItems;
    }

    /**
     * Setter for the restaurant that will fulfill this order
     * @param restaurant The restaurant to fulfill this order
     */
    protected void setFulfillingRestaurant(Restaurant restaurant) {
        fulfillingRestaurant = restaurant;
    }

    /**
     * Getter for the order number associated with this order.
     * @return The order number
     */
    public String getOrderNo() {
        return this.orderNo;
    }

    /**
     * Mark this order as delivered
     */
    public void deliver() {
        setOrderOutcome(OrderOutcome.Delivered);
    }

    /**
     * Determine whether a path has been found to fulfill this order
     * @return true if a path has been found, false otherwise.
     */
    public boolean pathFound() {
        return computedPath != null;
    }

    /**
     * Gets the list of moves for writing to the json
     * @return the list of moves formatted correctly for json writing
     */
    public List<MoveForWriting> getMovesForWriting() {
        List<MoveForWriting> movesForWriting = computedPath.stream().map(
                move -> new MoveForWriting(
                        orderNo,
                        move.from().lng(),
                        move.from().lat(),
                        CompassDirection.getAngleAsDegrees(move.direction()),
                        move.to().lng(),
                        move.to().lat(),
                        move.ticksSinceStartOfCalculation()
                )
        ).toList();
        return movesForWriting;
    }
}