package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class RestClient {
    public static final String RESTAURANTS_EXTENSION = "restaurants";
    public static final String ORDERS_EXTENSION = "orders";
    public static final String TEST_EXTENSION = "test/HelloWorld";
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
    public Order[] getOrdersFromRestServer() {
        Order[] orders;
        try{
            URL ordersURL = new URL(baseURLString + ORDERS_EXTENSION);
            orders = new ObjectMapper().readValue(ordersURL, Order[].class);
        } catch(IOException e) {
            e.printStackTrace();
            orders = null;
        }
        return orders;
    }
    public Restaurant[] getRestaurantsFromRestServer() {
        Restaurant[] restaurants;
        URL restaurantsURL;
        try{
            restaurantsURL = new URL(baseURLString + RESTAURANTS_EXTENSION);
            restaurants = new ObjectMapper().readValue(restaurantsURL, Restaurant[].class);
        } catch(IOException e) {
            e.printStackTrace();
            restaurants = null;
        }
        return restaurants;
    }
}
