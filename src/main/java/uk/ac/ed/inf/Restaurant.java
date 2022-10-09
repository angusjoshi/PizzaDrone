package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class Restaurant {
    public static final String API_ENDPOINT = "restaurants";
    private final String name;
    private final LngLat lngLat;
    private final Menu[] menu;
    public Menu[] getMenu() {
        return menu;
    }
    private Restaurant(@JsonProperty("name") String name, @JsonProperty("longitude") double lng,
                       @JsonProperty("latitude") double lat, @JsonProperty("menu") Menu[] menu) {
        this.name = name;
        this.lngLat = new LngLat(lng, lat);
        this.menu = menu;
    }
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        Restaurant[] restaurants = null;
        //TODO: improve exception handling in Restaurant.getRestaurantsFromServer
        try{
            URL restaurantsURL = new URL(serverBaseAddress, API_ENDPOINT);
            restaurants = new ObjectMapper().readValue(restaurantsURL, Restaurant[].class);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }
    public boolean menuContainsPizza(String pizza) {
        return Arrays.stream(menu)
                .anyMatch(entry -> entry
                        .name()
                        .equals(pizza));
    }
    public Optional<Menu> findMenuEntryByName(String pizza) {
        return Arrays.stream(menu)
                .filter(menu -> menu.name().equals(pizza))
                .findAny();
    }
}
