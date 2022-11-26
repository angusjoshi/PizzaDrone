package uk.ac.ed.inf.pathing;

import uk.ac.ed.inf.LngLat;
import uk.ac.ed.inf.order.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Move(String orderNo, LngLat from, LngLat to,
                   CompassDirection direction, int ticksSinceStartOfCalculation) {
    public static Move hover(String orderNo, LngLat from) {
        return new Move(orderNo, from, from, null, CalculationTimer.getTicksSinceCalculationStarted());
    }
    public Move reverseMove() {
        return new Move(orderNo, to, from, direction.getOppositeDirection(),
                CalculationTimer.getTicksSinceCalculationStarted());
    }
    public static List<Move> reverseMoveList(List<Move> moves) {
        List<Move> reversedMoves = new ArrayList<>(moves);

        Collections.reverse(reversedMoves);
        return reversedMoves.stream().map(Move::reverseMove).toList();
    }
    public double[] fromAsCoordinates() {
        double[] coordinate = new double[2];
        coordinate[0] = from.lng();
        coordinate[1] = from.lat();
        return coordinate;
    }
    public static List<Move> getFlightPath(List<Order> ordersToDeliver) {
        List<Move> flightPath = new ArrayList<>();
        ordersToDeliver.stream()
                .map(Order::getDeliveryPath)
                .forEach(deliveryPath -> flightPath.addAll(deliveryPath));
        return flightPath;
    }
}
