package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class RestClient {
    public static final String RESTAURANTS_EXTENSION = "restaurants";
    public static final String ORDERS_EXTENSION = "orders";
    public static final String TEST_EXTENSION = "test/HelloWorld";
    public static final String CENTRAL_AREA_EXTENSION = "centralArea";
    public static final String NO_FLY_ZONES_EXTENSION = "noFlyZones";
    protected String baseURLString;


    public RestClient(String baseURLString) throws BadTestResponseException, IOException {
        String normalisedURLString = baseURLString.endsWith("/") ? baseURLString : baseURLString + "/";
        URL testURL = new URL(normalisedURLString + TEST_EXTENSION);
        TestResponse testResponse = new ObjectMapper().readValue(testURL, TestResponse.class);
        if(!testResponse.greetingContains("HelloWorld")) {
            throw new BadTestResponseException();
        }
        this.baseURLString = normalisedURLString;
    }
    public Order[] getOrdersFromRestServerOnDate(String date) {
        Order[] orders;
        try{
            //TODO: improve slash handling for this URL creation
            //TODO: deal with duplication between this and getOrdersFromRestServer()
            URL ordersURL = new URL(baseURLString + ORDERS_EXTENSION + "/" + date);
            orders = new ObjectMapper().readValue(ordersURL, Order[].class);
        } catch(IOException e) {
            e.printStackTrace();
            orders = new Order[0];
        }
        return orders;
    }
    public Order[] getOrdersFromRestServer() {
        Order[] orders;
        try{
            URL ordersURL = new URL(baseURLString + ORDERS_EXTENSION);
            orders = new ObjectMapper().readValue(ordersURL, Order[].class);
        } catch(IOException e) {
            e.printStackTrace();
            orders = new Order[0];
        }
        return orders;
    }
    public Restaurant[] getRestaurantsFromRestServer() {
        Restaurant[] restaurants;
        try{
            URL restaurantsURL = new URL(baseURLString + RESTAURANTS_EXTENSION);
            restaurants = new ObjectMapper().readValue(restaurantsURL, Restaurant[].class);
        } catch(IOException e) {
            e.printStackTrace();
            restaurants = new Restaurant[0];
        }
        return restaurants;
    }
    public LngLat[] getCentralAreaVerticesFromRestServer() {
        LngLat[] vertices = null;
        try {
            URL centralAreaURL = new URL(baseURLString + CENTRAL_AREA_EXTENSION);
            vertices = new ObjectMapper().readValue(centralAreaURL, LngLat[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return vertices;
    }
    public NoFlyZone[] getNoFlyZonesFromRestServer() {
        NoFlyZone[] noFlyZones = null;
        try {
            URL centralAreaURL = new URL(baseURLString + NO_FLY_ZONES_EXTENSION);
            noFlyZones = new ObjectMapper().readValue(centralAreaURL, NoFlyZone[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return noFlyZones;
    }
}
