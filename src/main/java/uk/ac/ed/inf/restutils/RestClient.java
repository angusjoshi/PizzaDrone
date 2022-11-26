package uk.ac.ed.inf.restutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.order.Restaurant;
import uk.ac.ed.inf.areas.NoFlyZone;
import uk.ac.ed.inf.order.Order;

import java.io.IOException;
import java.net.URL;

/**
 * Class to make requests to the rest server and deserialise into useful forms.
 */
public class RestClient {
    private static final String RESTAURANTS_EXTENSION = "restaurants";
    private static final String ORDERS_EXTENSION = "orders";
    private static final String TEST_EXTENSION = "test/HelloWorld";
    private static final String CENTRAL_AREA_EXTENSION = "centralArea";
    private static final String NO_FLY_ZONES_EXTENSION = "noFlyZones";
    private final String baseURLString;

    private static RestClient instance;

    /**
     * Initialises the rest client instance
     * @param baseURLString The base string for the rest client.
     * @throws IOException If the URL string is malformed, or if there is an error with making a request
     * to the rest client
     * @throws BadTestResponseException If the test response from the rest client is not as expected.
     */
    public static void initialiseRestClient(String baseURLString) throws IOException, BadTestResponseException {
        instance = new RestClient(baseURLString);
    }

    /**
     * Gets the instance of the rest client.
     * @return the instance of the rest client
     */
    public static RestClient getInstance() {
        if(instance == null) {
            System.err.println("Rest client must be initialised before accessing!");
            System.exit(2);
        }
        return instance;
    }
    private RestClient(String baseURLString) throws BadTestResponseException, IOException {
        String normalisedURLString = baseURLString.endsWith("/") ? baseURLString : baseURLString + "/";
        URL testURL = new URL(normalisedURLString + TEST_EXTENSION);
        TestResponse testResponse = new ObjectMapper().readValue(testURL, TestResponse.class);
        if(!testResponse.greetingContains("HelloWorld")) {
            throw new BadTestResponseException();
        }
        this.baseURLString = normalisedURLString;
    }

    /**
     * Get the orders from the rest server on a specific date. Returns an empty array if there is an error.
     * @param date the date to get orders for in string form. MUST be in format "yyyy-MM-dd"
     * @return Array of orders, with one element for each of the orders on the required day.
     */
    public Order[] getOrdersFromRestServerOnDate(String date) {
        Order[] orders;
        try{
            URL ordersURL = new URL(baseURLString + ORDERS_EXTENSION + "/" + date);
            orders = new ObjectMapper().readValue(ordersURL, Order[].class);
        } catch(IOException e) {
            e.printStackTrace();
            orders = new Order[0];
        }
        return orders;
    }

    /**
     * Get the orders from the rest server for all dates.
     * @return An array of Order type, with one element for each order.
     */
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

    /**
     * Get the restaurant data from the rest server.
     * @return An array of Restaurant type, with one element for each restaurant.
     */
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

    /**
     * Get the vertices of the centralArea from the rest sever. the vertices are returned in anticlockwise order,
     * @return an array of LngLat with one LngLat for each vertex, in anticlockwise order.
     */
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

    /**
     * Get an array of NoFlyZone from the rest server. Each element in the array is a no fly zone for
     * use in the pathfinding computation.
     * @return An array of NoFlyZones each representing a single NoFlyZone polygon
     */
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
