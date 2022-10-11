package uk.ac.ed.inf;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class RestaurantTest {
    @Test
    public void testGetRestaurantsFromServer() {
        Restaurant[] restaurants = null;
        try{
            restaurants = Restaurant.getRestaurantsFromRestServer(new URL(Constants.API_BASE));
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        assertNotNull(restaurants);
        assert(restaurants.length > 0);
        assert(restaurants[0].getMenu().length > 0);
        assert(restaurants[0].getMenu()[0].name().equals("Margarita"));
    }
}
