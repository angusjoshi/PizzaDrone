package uk.ac.ed.inf.jsonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.pathing.Move;
import uk.ac.ed.inf.order.Order;
import uk.ac.ed.inf.order.OrderOutcome;
import uk.ac.ed.inf.pathing.CompassDirection;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class with static methods for formatting and writing orders and delivery paths to jsons as required.
 */
public class JSONWriter {
    //to hide the default constructor
    private JSONWriter(){}

    /**
     * Writes a list of orders to a json in the working directory, with order outcomes.
     * Filename will be of the format "deliveries-YYYY-MM_dd.geojson"
     * @param orders The list of orders to be written
     * @param currentDayString the date of the orders.
     */
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

    /**
     * Write the computed delivery flightpath to a json in the current working directory.
     * The filename will be of the form "flightpath-YYYY-MM_dd.json"
     * @param ordersToDeliver The orders that have been selected to be delivered.
     * @param currentDayString The date of the orders.
     */
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

    /**
     * A record for structuring the Order data for serialization
     * @param orderNo The order number
     * @param outcome The order outcome
     * @param costInPence The cost in pence (as received through the rest client, not recalculated)
     */
    record OrderForWriting(String orderNo, OrderOutcome outcome, int costInPence) {
    }

    /**
     * A record for structuring the moves in the flightpath of the drone
     * @param orderNo The order number
     * @param fromLongitude Longitude before the move
     * @param fromLatitude Latitude before the move
     * @param angle Angle of direction of the move in degrees
     * @param toLongitude Longitude after the move
     * @param toLatitude Latitude after the move
     * @param ticksSinceStartOfCalculation Number of milliseconds past since the beginning of the pathfinding
     *                                     when the move was computed.
     */
    record MoveForWriting(String orderNo, double fromLongitude, double fromLatitude,
                          Double angle, double toLongitude, double toLatitude,
                          int ticksSinceStartOfCalculation) {
    }
}
