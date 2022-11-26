package uk.ac.ed.inf;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class OrderValidator {
    private static final int ORDER_MAX_PIZZAS = 4;
    private static final int EXTRA_DELIVERY_COST_IN_PENCE = 100;
    private LocalDate currentDay;
    private String apiString;
    private Restaurant[] restaurants;
    private Order[] orders;
    private final HashSet<String> possiblePizzas;


    public OrderValidator(String apiString, String currentDayString) {
        this.apiString = apiString;
        this.currentDay = DateParser.parseDateString(currentDayString);
        this.possiblePizzas = new HashSet<>();
        this.orders = null;
        this.restaurants = null;
    }
    public List<Order> getValidatedOrders() {
        retrieveDataFromRestServer();
        validateOrders();
        return new ArrayList<>(Arrays.asList(orders));
    }
    public void retrieveDataFromRestServer() {
        if(currentDay == null) {
            //TODO: improve error handling here.
            throw new RuntimeException();
        }
        RestClient restClient;
        try {
            restClient = new RestClient(apiString);
        } catch (BadTestResponseException e) {
            //TODO: improve error handling here.
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        orders = restClient.getOrdersFromRestServerOnDate(currentDay.toString());
        restaurants = restClient.getRestaurantsFromRestServer();
    }
    public void validateOrders() {
        findAllPossiblePizzas();
        Arrays.stream(orders).forEach(this::validateOrder);
    }
    private void validateOrder(Order order) {

        order.validateOrderDate(currentDay);

        try {
            order.validateCreditCard();
        } catch(OrderDateNotValidatedException e) {
            System.err.println(e.getMessage());
            return;
        }


        int numberOfPizzasInOrder = order.getOrderItems().length;
        if(numberOfPizzasInOrder < 1 || numberOfPizzasInOrder > ORDER_MAX_PIZZAS) {
            order.setOrderOutcome(OrderOutcome.InvalidPizzaCount);
            return;
        }

        if(!allOrderedPizzasExist(order)) {
            order.setOrderOutcome(OrderOutcome.InvalidPizzaNotDefined);
            return;
        }

        try {
            int deliveryCost = getDeliveryCost(order);
            if(deliveryCost != order.getPriceTotalInPence()) {
                order.setOrderOutcome(OrderOutcome.InvalidTotal);
                return;
            }
        } catch(InvalidPizzaCombinationException e) {
            order.setOrderOutcome(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers);
            return;
        }

        if(order.getOrderOutcome() == null) {
            order.setOrderOutcome(OrderOutcome.ValidButNotDelivered);
        }
    }
    private boolean allOrderedPizzasExist(Order order) {
        return Arrays.stream(order.getOrderItems())
                .map(possiblePizzas::contains)
                .reduce(true, (a, b) -> a && b);
    }


    private void findAllPossiblePizzas() {
        for(Restaurant restaurant : restaurants) {
            Arrays.stream(restaurant.getMenu())
                    .forEach(menu -> possiblePizzas.add(menu.name()));
        }
    }

    private int getDeliveryCost(Order order) throws InvalidPizzaCombinationException {
        String[] pizzas = order.getOrderItems();
        if(pizzas.length == 0) return 0;
        int deliveryCost = 0;
        //attempt to find the first pizza in the menus of all restaurants
        var foundRestaurant = Arrays.stream(restaurants)
                .filter(currRestaurant -> currRestaurant.menuContainsPizza(pizzas[0]))
                .findAny();

        if(foundRestaurant.isEmpty()) throw new InvalidPizzaCombinationException();

        //search can be restricted to a single restaurant if we find the first one
        var restaurant = foundRestaurant.get();
        order.setFulfillingRestaurant(restaurant);

        for (String pizza : pizzas) {
            var menuEntry = restaurant.findMenuEntryByName(pizza);
            if (menuEntry.isEmpty()) {
                throw new InvalidPizzaCombinationException();
            }
            deliveryCost += menuEntry.get().priceInPence();
        }
        return deliveryCost + EXTRA_DELIVERY_COST_IN_PENCE;
    }
}
