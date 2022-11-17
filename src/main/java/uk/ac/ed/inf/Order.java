package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

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


    public void validateOrder() {

    }
    /**
     * determine the cost of an order from a list of pizzas, and the data from the participating restaurants.
     * @param restaurants array of participating Restaurant instances
     * @param pizzas an order of pizzas
     * @return cost of the order of pizzas from a single restaurant
     * @throws InvalidPizzaCombinationException if the combination of pizzas cannot be ordered from a single restaurant
     */
    public static int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) throws InvalidPizzaCombinationException {
        if(pizzas.length == 0) return 0;
        int deliveryCost = 0;
        //attempt to find the first pizza in the menus of all restaurants
        var foundRestaurant = Arrays.stream(restaurants)
                                            .filter(currRestaurant -> currRestaurant.menuContainsPizza(pizzas[0]))
                                            .findAny();

        if(foundRestaurant.isEmpty()) throw new InvalidPizzaCombinationException();

        //search can be restricted to a single restaurant if we find the first one
        var restaurant = foundRestaurant.get();
        for (String pizza : pizzas) {
            var menuEntry = restaurant.findMenuEntryByName(pizza);
            if (menuEntry.isEmpty()) {
                throw new InvalidPizzaCombinationException();
            }
            deliveryCost += menuEntry.get().priceInPence();
        }
        return deliveryCost;
    }
}