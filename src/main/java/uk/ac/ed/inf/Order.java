package uk.ac.ed.inf;

import java.util.Arrays;

public class Order {
    public static int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) throws InvalidPizzaCombinationException {
        if(pizzas.length == 0) return 0;
        int deliveryCost = 0;
        var foundRestaurant = Arrays.stream(restaurants)
                                            .filter(currRestaurant -> currRestaurant.menuContainsPizza(pizzas[0]))
                                            .findAny();
        if(foundRestaurant.isEmpty()) throw new InvalidPizzaCombinationException();
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
