package uk.ac.ed.inf;

import uk.ac.ed.inf.jsonutils.GeojsonWriter;
import uk.ac.ed.inf.jsonutils.JSONWriter;
import uk.ac.ed.inf.order.Order;
import uk.ac.ed.inf.order.OrderValidator;
import uk.ac.ed.inf.pathing.CalculationTimer;
import uk.ac.ed.inf.restutils.BadTestResponseException;
import uk.ac.ed.inf.restutils.RestClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main controller for the application
 */
public class Controller {
    private static final int MOVE_CAPACITY = 2000;

    private final String apiString;
    private final String currentDayString;

    /**
     * Constructor for the controller
     * @param apiString The api string to be used
     * @param currentDayString The current day string to be used
     */
    public Controller(String apiString, String currentDayString) {
        this.apiString = apiString;
        this.currentDayString = currentDayString;
    }

    /**
     * Processes order validation, pathfinding and file output for all orders on the required day
     */
    public void processDay() {
        initialiseRestClient();
        var orderValidator = new OrderValidator(currentDayString);
        var orders = orderValidator.getValidatedOrders();

        System.out.println("Computing flightpath...");
        var ordersToDeliver = chooseOrdersToBeDelivered(orders);
        deliverOrders(ordersToDeliver);

        System.out.println("Writing output to file...");
        JSONWriter.writeOrdersToJson(orders, currentDayString);

        JSONWriter.writeDeliveryPathToJson(ordersToDeliver, currentDayString);

        GeojsonWriter.writeDeliveryPathToGeojson(ordersToDeliver, currentDayString);

    }

    private void initialiseRestClient() {
        try {
            RestClient.initialiseRestClient(apiString);

        } catch(MalformedURLException e) {
            e.printStackTrace();
            System.err.println("Error with forming the URL for the rest client!" +
                    "Please check the inputted URL. exiting...");
            System.exit(2);
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("Error with initialising the rest client! exiting...");
            System.exit(2);
        } catch (BadTestResponseException e) {
            e.printStackTrace();
            System.err.println("Unexpected test response from rest client! exiting...");
            System.exit(2);
        }
    }

    private void deliverOrders(List<Order> ordersToDeliver) {
        ordersToDeliver.forEach(Order::deliver);
    }
    private List<Order> chooseOrdersToBeDelivered(List<Order> orders) {
        CalculationTimer.startCalculationTimer();
        List<Order> ordersToDeliver = new ArrayList<>();
        for(var order : orders) {
            if(order.shouldBeDelivered()) {
                order.computePath();
                if(order.pathFound()) {
                    ordersToDeliver.add(order);
                }
            }
        }

        //Orders are sorted in order of moves per pizza delivered
        Collections.sort(ordersToDeliver);
        int movesLeft = MOVE_CAPACITY;
        List<Order> chosenOrdersToDeliver = new ArrayList<>();
        for(var order : ordersToDeliver) {
            //greedily choose the orders with the best move to pizza ratio
            if(order.pathLength() > movesLeft) {
                break;
            }
            movesLeft -= 2*order.pathLength() + 2;
            chosenOrdersToDeliver.add(order);
        }
        return chosenOrdersToDeliver;
    }
}