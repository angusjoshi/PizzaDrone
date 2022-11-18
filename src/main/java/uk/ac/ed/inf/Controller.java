package uk.ac.ed.inf;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;

public class Controller {
    private static final int ORDER_MAX_PIZZAS = 4;
    private static final int EXTRA_DELIVERY_COST_IN_PENCE = 100;
    private Restaurant[] restaurants;
    private Order[] orders;

    private LocalDate currentDay;

    private final HashSet<String> possiblePizzas;

    public Controller(String currentDayString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            currentDay = LocalDate.parse(currentDayString, formatter);
        } catch(DateTimeParseException e) {
            e.printStackTrace();
            System.exit(2);
        }

        retrieveDataFromRestServer();
        possiblePizzas = new HashSet<>();
    }

    public void processOrders() {
        findAllPossiblePizzas();
        validateOrders();
        System.out.println("Hehe!");
    }
    private void validateOrders() {
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

    private void retrieveDataFromRestServer() {
        RestClient restClient;
        try {
            restClient = new RestClient(Constants.API_BASE);
        } catch (BadTestResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        orders = restClient.getOrdersFromRestServerOnDate(currentDay.toString());
        restaurants = restClient.getRestaurantsFromRestServer();
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
