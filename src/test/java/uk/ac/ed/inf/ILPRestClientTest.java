package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.order.Order;
import uk.ac.ed.inf.order.Restaurant;
import uk.ac.ed.inf.restutils.BadTestResponseException;
import uk.ac.ed.inf.restutils.RestClient;

import java.io.IOException;

public class ILPRestClientTest {
    @Test
    public void testClientInitialisation() throws BadTestResponseException, IOException {
        RestClient.initialiseRestClient(Constants.API_BASE);
        RestClient restClient = RestClient.getInstance();
    }

    @Test
    public void testGetOrders() throws BadTestResponseException, IOException {
        RestClient.initialiseRestClient(Constants.API_BASE);
        RestClient restClient = RestClient.getInstance();
        Order[] orders = restClient.getOrdersFromRestServer();
        assert(orders.length > 0);
    }
    @Test
    public void testGetRestaurants() throws BadTestResponseException, IOException {
        RestClient.initialiseRestClient(Constants.API_BASE);
        RestClient restClient = RestClient.getInstance();
         Restaurant[] restaurants = restClient.getRestaurantsFromRestServer();
         assert (restaurants.length > 0);
    }
}
