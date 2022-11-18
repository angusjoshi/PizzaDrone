package uk.ac.ed.inf;

import org.junit.Test;

import java.io.IOException;

public class ILPRestClientTest {
    @Test
    public void testClientInitialisation() throws BadTestResponseException, IOException {
        RestClient restClient = new RestClient(Constants.API_BASE);
    }

    @Test
    public void testGetOrders() throws BadTestResponseException, IOException {
        RestClient restClient = new RestClient(Constants.API_BASE);
        Order[] orders = restClient.getOrdersFromRestServer();
        assert(orders.length > 0);
    }
    @Test
    public void testGetRestaurants() throws BadTestResponseException, IOException {
         RestClient restClient = new RestClient(Constants.API_BASE);
         Restaurant[] restaurants = restClient.getRestaurantsFromRestServer();
         assert (restaurants.length > 0);
    }
}