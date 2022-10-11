package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

/**
 * Class for storing instances of restaurants
 * intended to be directly deserialized into from the restaurants API
 */
public class Restaurant {
    /**
     * extension to base API URL for the restaurants endpoint
     */
    private final String name;
    private final LngLat lngLat;
    private final Menu[] menu;

    /**
     * get the array of menu entries associated with this restaurant
     * @return the array of menu entries
     */
    public Menu[] getMenu() {
        return menu;
    }
    private Restaurant(@JsonProperty("name") String name, @JsonProperty("longitude") double lng,
                       @JsonProperty("latitude") double lat, @JsonProperty("menu") Menu[] menu) {
        this.name = name;
        this.lngLat = new LngLat(lng, lat);
        this.menu = menu;
    }

    /**
     * make a request to the REST-API and attempt to deserialize the result into an array
     * of Restaurant instances
     * @param serverBaseAddress base address for the API
     * @return array of restaurants retrieved from the restaurants API endpoint
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        Restaurant[] restaurants;
        URL restaurantsURL;
        try {
            restaurantsURL = new URL(serverBaseAddress, Constants.RESTAURANTS_EXTENSION);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            restaurants = new ObjectMapper().readValue(restaurantsURL, Restaurant[].class);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return restaurants;
    }

    /**
     * find if the menu for this restaurant contains a certain pizza
     * @param pizza the pizza to find
     * @return true if pizza was found, false otherwise
     */
    public boolean menuContainsPizza(String pizza) {
        return Arrays.stream(menu)
                .anyMatch(entry -> entry
                        .name()
                        .equals(pizza));
    }

    /**
     * find the menu entry for a given pizza if it exists in the menu for this restaurant.
     * @param pizza name of the pizza in the menu entry to find
     * @return Optional.empty if the pizza was not found, the required entry wrapped with Optional otherwise.
     */
    public Optional<Menu> findMenuEntryByName(String pizza) {
        return Arrays.stream(menu)
                .filter(menu -> menu.name().equals(pizza))
                .findAny();
    }
}
