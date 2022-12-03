package uk.ac.ed.inf.jsonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.order.Order;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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

        List<OrderForWriting> ordersToWrite = new ArrayList<>();
        orders.forEach(order -> ordersToWrite.add(order.getOrderForWriting()));

        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = "deliveries-" + currentDayString + ".json";
        try {
            objectMapper.writeValue(Paths.get(fileName).toFile(), ordersToWrite);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Write the computed delivery flightpath to a json in the current working directory.
     * The filename will be of the form "flightpath-YYYY-MM_dd.json"
     * @param ordersToDeliver The orders that have been selected to be delivered.
     * @param currentDayString The date of the orders.
     */
    public static void writeDeliveryPathToJson(List<Order> ordersToDeliver, String currentDayString) {
        List<MoveForWriting> movesForWriting = new ArrayList<>();
        ordersToDeliver.forEach(order -> movesForWriting.addAll(order.getMovesForWriting()));

        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = "flightpath-" + currentDayString + ".json";
        try {
            objectMapper.writeValue(Paths.get(fileName).toFile(), movesForWriting);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }


}
