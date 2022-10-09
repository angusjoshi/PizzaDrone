package uk.ac.ed.inf;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class RestaurantTest {
    @Test
    public void testGetRestaurantsFromServer() {
        Restaurant[] restaurants = null;
        try{
            restaurants = Restaurant.getRestaurantsFromRestServer(new URL(App.API_BASE));
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        assert(restaurants[0].getMenu()[0].name().equals("Margarita"));
    }
}
