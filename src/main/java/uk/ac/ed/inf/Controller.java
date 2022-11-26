package uk.ac.ed.inf;

import uk.ac.ed.inf.jsonutils.GeojsonWriter;
import uk.ac.ed.inf.jsonutils.JSONWriter;
import uk.ac.ed.inf.order.Order;
import uk.ac.ed.inf.order.OrderValidator;
import uk.ac.ed.inf.pathing.CalculationTimer;
import uk.ac.ed.inf.pathing.PathFinder;
import uk.ac.ed.inf.restutils.BadTestResponseException;
import uk.ac.ed.inf.restutils.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    public static int MOVE_CAPACITY = 2000;

    private final String apiString;
    private final String currentDayString;
    public Controller(String apiString, String currentDayString) {
        this.apiString = apiString;
        this.currentDayString = currentDayString;
    }
    public void processDay() {
        try {
            RestClient.initialiseRestClient(apiString);
        } catch (IOException e) {
            System.err.println("Error with initialising the rest client! exiting...");
            System.exit(2);
        } catch (BadTestResponseException e) {
            System.err.println("Unexpected test response from rest client! exiting...");
            System.exit(2);
        }

        var orderValidator = new OrderValidator(currentDayString);
        var orders = orderValidator.getValidatedOrders();

        var ordersToDeliver = chooseOrdersToBeDelivered(orders);
        deliverOrders(ordersToDeliver);

        JSONWriter.writeOrdersToJson(orders, currentDayString);

        JSONWriter.writeDeliveryPathToJson(ordersToDeliver, currentDayString);

        GeojsonWriter.writeDeliveryPathToGeojson(ordersToDeliver, currentDayString);

    }
    public void deliverOrders(List<Order> ordersToDeliver) {
        ordersToDeliver.forEach(Order::deliver);
    }
    public List<Order> chooseOrdersToBeDelivered(List<Order> orders) {
        CalculationTimer.startCalculationTimer();
        List<Order> ordersToDeliver = new ArrayList<>();
        for(var order : orders) {
            if(order.shouldBeDelivered()) {
                order.computePath();
                ordersToDeliver.add(order);
            }
        }
        Collections.sort(ordersToDeliver);
        int movesLeft = MOVE_CAPACITY;
        List<Order> chosenOrdersToDeliver = new ArrayList<>();
        for(var order : ordersToDeliver) {
            if(order.pathLength() > movesLeft) {
                break;
            }
            movesLeft -= 2*order.pathLength() + 2;
            chosenOrdersToDeliver.add(order);
        }
        return chosenOrdersToDeliver;
    }
}
//going to have a collection of orders which take a certain number of moves to fulfill and yield a certain number
//of pizzas. need to optimize the number of pizzas delivered within 2000 moves.

//remember we need to hover once at the restaurant, and once at AT.

//there is an obvious greedy soln, where we just take the orders with the most pizzas first.

//I think the optimal soln is with dynamic programming though.

//there is a greedy soln where we just find an 'pizzas per move' number and take the highest. This might actually
//work very close to optimally as well, because we will be fulfilling a decent number of orders (around 20 I think?)
//it's going to be very biased against the restaurants far away though, but in the spec we are just trying to optimize
//number of pizzas delivered so I don't think it matters

