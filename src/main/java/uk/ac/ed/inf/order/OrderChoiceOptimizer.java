package uk.ac.ed.inf.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Class containing functionality for optimising order choices.
 */
public class OrderChoiceOptimizer {
    private final int moveCapacity;
    private final HashMap<PreviousComputationKey, Integer> previouslyComputed;
    private final List<Order> orders;

    private OrderChoiceOptimizer(List<Order> orders, int moveCapacity) {
        this.orders = orders;
        this.moveCapacity = moveCapacity;
        previouslyComputed = new HashMap<>();
    }

    /**
     * Chooses the orders to deliver such that the number of pizzas delivered is optimal, under the constraint of
     * the move capacity.
     * @param orders the orders to choose from
     * @param moveCapacity the move capacity
     * @return a list of chosen orders, such that the number of pizzas delivered is optimized under the move
     * capacity constraint.
     */
    public static List<Order> getOptimizedOrderChoices(List<Order> orders, int moveCapacity) {
        var optimizer = new OrderChoiceOptimizer(orders, moveCapacity);
        return optimizer.optimizeOrderChoices(moveCapacity);
    }
    private List<Order> optimizeOrderChoices(int moveCapacity) {
        if(orders.size() == 0) {
            return Collections.emptyList();
        }

        helper(0, moveCapacity);
        return extractOrders();
    }
    private int helper(int i, int capacityLeft) {
        PreviousComputationKey previousComputationKey = new PreviousComputationKey(i, capacityLeft);
        if(i == orders.size()) {
            previouslyComputed.put(previousComputationKey, 0);
            return 0;
        }

        if(previouslyComputed.containsKey(previousComputationKey)) {
            return previouslyComputed.get(previousComputationKey);
        }

        var order = orders.get(i);
        if(order.movesRequired() > capacityLeft) {
            int res =  helper(i + 1, capacityLeft);
            previouslyComputed.put(previousComputationKey, res);
            return res;
        }

        int capRequired = order.movesRequired();
        int nPizzas = order.getNumberOfPizzas();

        int result = Math.max(
                 helper(i + 1, capacityLeft - capRequired) + nPizzas,
                 helper(i + 1, capacityLeft)
        );

        previouslyComputed.put(previousComputationKey, result);
        return result;
    }
    private List<Order> extractOrders() {
        List<Order> selectedOrders = new ArrayList<>();

        int capacityLeft = moveCapacity;

        for(int i = 0; i < orders.size(); i++) {
            var order = orders.get(i);

            int capacityRequired = order.movesRequired();
            int nPizzas = order.getNumberOfPizzas();

            PreviousComputationKey dp1 = new PreviousComputationKey(i + 1, capacityLeft);
            PreviousComputationKey dp2 = new PreviousComputationKey(i + 1, capacityLeft - capacityRequired);

            if(!previouslyComputed.containsKey(dp2)) {
                continue;
            }

            //If including the ith order was better than not including it, add it to the list
            if(!previouslyComputed.containsKey(dp1) || previouslyComputed.get(dp2) + nPizzas > previouslyComputed.get(dp1)) {
                capacityLeft -= capacityRequired;
                selectedOrders.add(order);
            }
        }
        return selectedOrders;
    }
    private record PreviousComputationKey(int i, int capacityLeft) {}
}

