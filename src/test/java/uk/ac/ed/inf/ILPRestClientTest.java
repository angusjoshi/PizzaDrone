package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.order.Order;
import uk.ac.ed.inf.order.Restaurant;
import uk.ac.ed.inf.restutils.BadTestResponseException;
import uk.ac.ed.inf.restutils.RestClient;
import uk.ac.ed.inf.restutils.RestRetrievalFailedException;

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
        Order[] orders = null;
        RestClient restClient = RestClient.getInstance();
        try {
            orders = restClient.getOrdersFromRestServer();
        } catch(RestRetrievalFailedException e) {
            e.printStackTrace();
        }
        if(orders == null) assert(false);
        assert(orders.length > 0);
    }
    @Test
    public void testGetRestaurants() throws BadTestResponseException, IOException {
        RestClient.initialiseRestClient(Constants.API_BASE);
        RestClient restClient = RestClient.getInstance();
        Restaurant[] restaurants = null;
        try {
             restaurants = restClient.getRestaurantsFromRestServer();
        } catch(RestRetrievalFailedException e) {
            e.printStackTrace();
        }
        assert(restaurants != null);
         assert (restaurants.length > 0);
    }
}
