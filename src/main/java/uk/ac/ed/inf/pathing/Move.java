package uk.ac.ed.inf.pathing;

import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.order.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Record to store a move resultant from the pathfinding process.
 * @param orderNo The order number
 * @param from The location at the start of the move
 * @param to The location at the end of the move
 * @param direction The compass direction associated with the move.
 * @param ticksSinceStartOfCalculation Milliseconds passed since the start of the calculation
 *                                     when this move was computed.
 */
public record Move(String orderNo, LngLat from, LngLat to,
                   CompassDirection direction, int ticksSinceStartOfCalculation) {
    /**
     * Gets a hover move at a given location
     * @param orderNo The order number
     * @param at Location to hover at.
     * @return The hover move at the required location
     */
    public static Move hover(String orderNo, LngLat at) {
        return new Move(orderNo, at, at, null, CalculationTimer.getTicksSinceCalculationStarted());
    }

    /**
     * Reverses a move, flipping its direction and from/to
     * @return A new move instance opposite to this one
     */
    public Move reverseMove() {
        return new Move(orderNo, to, from, direction.getOppositeDirection(),
                CalculationTimer.getTicksSinceCalculationStarted());
    }

    /**
     * Reverses a path represented as a list of moves.
     * @param moves The list of moves to reverse
     * @return The exact opposite path
     */
    public static List<Move> reverseMoveList(List<Move> moves) {
        List<Move> reversedMoves = new ArrayList<>(moves);

        Collections.reverse(reversedMoves);
        return reversedMoves.stream().map(Move::reverseMove).toList();
    }

    /**
     * Gets the from location as an array of double coordinates, to be used in file output
     * @return The coordinates array
     */
    public double[] fromAsCoordinates() {
        double[] coordinate = new double[2];
        coordinate[0] = from.lng();
        coordinate[1] = from.lat();
        return coordinate;
    }

    /**
     * Gets the complete flightpath of a drone fulfilling an entire list of orders.
     * @param ordersToDeliver The list of orders to fulfill
     * @return The complete flightpath
     */
    public static List<Move> getFlightPath(List<Order> ordersToDeliver) {
        List<Move> flightPath = new ArrayList<>();
        ordersToDeliver.stream()
                .map(Order::getDeliveryPath)
                .forEach(flightPath::addAll);
        return flightPath;
    }
}
