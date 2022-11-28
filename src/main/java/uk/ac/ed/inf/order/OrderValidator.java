package uk.ac.ed.inf.order;

import uk.ac.ed.inf.restutils.RestClient;
import uk.ac.ed.inf.restutils.RestRetrievalFailedException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class to be instantiated, providing order validation functionality.
 */
public class OrderValidator {
    private static final int ORDER_MAX_PIZZAS = 4;
    private static final int EXTRA_DELIVERY_COST_IN_PENCE = 100;
    private static final LocalDate EARLIEST_SUPPORTED_DATE = LocalDate.of(2023, 1, 1);
    private static final LocalDate LATEST_SUPPORTED_DATE = LocalDate.of(2023, 5, 31);
    private LocalDate currentDay;
    private Restaurant[] restaurants;
    private Order[] orders;
    private final HashSet<String> possiblePizzas;


    /**
     * Initialises the orderValidator
     * @param currentDayString The day that we require orders for
     */
    public OrderValidator(String currentDayString) {
        try{
            this.currentDay = DateParser.parseDateString(currentDayString);
        } catch(DateTimeParseException e) {
            System.err.println("""
                    Error, inputted date is invalid! \s
                    Dates must be in the form yyyy-MM-dd \s
                    exiting...
                    """);
            System.exit(2);
        }
        if(!dateIsInSupportedRange(currentDay)) {
            System.err.println("Inputted day is not in the required range! \n" +
                    "Current supported dates are within " +
                    EARLIEST_SUPPORTED_DATE + " to " +
                    LATEST_SUPPORTED_DATE);
            System.err.println("exiting...");
            System.exit(2);
        }
        this.possiblePizzas = new HashSet<>();
        this.orders = null;
        this.restaurants = null;
    }
    private boolean dateIsInSupportedRange(LocalDate date) {
        if(date.isBefore(EARLIEST_SUPPORTED_DATE) || date.isAfter(LATEST_SUPPORTED_DATE)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the validated orders
     * @return A list of validated orders, on the date specified in the constructor call.
     */
    public List<Order> getValidatedOrders() {
        System.out.println("Retrieving data from rest server...");
        retrieveDataFromRestServer();
        System.out.println("Validating orders...");
        validateOrders();
        return new ArrayList<>(Arrays.asList(orders));
    }

    private void retrieveDataFromRestServer() {
        if(currentDay == null) {
            throw new RuntimeException();
        }
        RestClient restClient = RestClient.getInstance();

        try {
            orders = restClient.getOrdersFromRestServerOnDate(currentDay.toString());
            restaurants = restClient.getRestaurantsFromRestServer();
        } catch(RestRetrievalFailedException e) {
            e.printStackTrace();
            System.err.println("Failure in retrieving data from the rest client! exiting...");
        }
    }
    private void validateOrders() {
        findAllPossiblePizzas();
        Arrays.stream(orders).forEach(this::validateOrder);
    }
    private void validateOrder(Order order) {
        order.validateOrderDate(currentDay);
        order.validateCreditCard();

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
        if(pizzas.length == 0) {
            return 0;
        }
        int deliveryCost = 0;
        //attempt to find the first pizza in the menus of all restaurants
        var foundRestaurant = Arrays.stream(restaurants)
                .filter(currRestaurant -> currRestaurant.menuContainsPizza(pizzas[0]))
                .findAny();

        if(foundRestaurant.isEmpty()) {
            throw new InvalidPizzaCombinationException();
        }

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
