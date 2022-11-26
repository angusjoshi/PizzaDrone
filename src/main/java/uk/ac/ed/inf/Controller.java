package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    public static int MOVE_CAPACITY = 2000;


    public Controller(String apiString, String currentDayString) {
        OrderValidator orderValidator = new OrderValidator(apiString, currentDayString);
        List<Order> orders = orderValidator.getValidatedOrders();
        PathFinder pathFinder = PathFinder.getInstance();
        int sum = 0;
        var path = pathFinder.findPathToRestaurant(orders.get(0).getFulfillingRestaurant());
        var reversed = Move.reverseMoveList(path);
        var orders2 = chooseOrdersToBeDelivered(orders);
        var x = orders2.get(0).getDeliveryPath();

        System.out.println("asdfasd");
    }
    public List<Order> chooseOrdersToBeDelivered(List<Order> orders) {
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

