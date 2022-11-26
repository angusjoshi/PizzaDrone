package uk.ac.ed.inf.jsonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.pathing.Move;
import uk.ac.ed.inf.order.Order;
import uk.ac.ed.inf.order.OrderOutcome;
import uk.ac.ed.inf.pathing.CompassDirection;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class JSONWriter {
    public static void writeOrdersToJson(List<Order> orders, String currentDayString) {

        Object[] ordersToWrite = orders.stream().map(order ->
                new OrderForWriting(
                    order.getOrderNo(),
                    order.getOrderOutcome(),
                    order.getPriceTotalInPence()
                )
        ).toArray();

        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = "deliveries-" + currentDayString + ".json";
        try {
            objectMapper.writeValue(Paths.get(fileName).toFile(), ordersToWrite);
        } catch (IOException e) {
            //TODO: revisit error handling
            throw new RuntimeException(e);
        }
    }
    public static void writeDeliveryPathToJson(List<Order> ordersToDeliver, String currentDayString) {
        List<Move> flightPath = Move.getFlightPath(ordersToDeliver);
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] movesForWriting = flightPath.stream().map(
                move -> new MoveForWriting(
                        move.orderNo(),
                        move.from().lng(),
                        move.from().lat(),
                        CompassDirection.getAngleAsDegrees(move.direction()),
                        move.to().lng(),
                        move.to().lat(),
                        move.ticksSinceStartOfCalculation()
                )
        ).toArray();
        String fileName = "flightpath-" + currentDayString + ".json";
        try {
            objectMapper.writeValue(Paths.get(fileName).toFile(), movesForWriting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    record OrderForWriting(String orderNo, OrderOutcome outcome, int costInPence) {
    }
    record MoveForWriting(String orderNo, double fromLongitude, double fromLatitude,
                          Double angle, double toLongitude, double toLatitude,
                          int ticksSinceStartOfCalculation) {
    }
}
