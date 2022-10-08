package uk.ac.ed.inf;

import java.net.URL;

public class Restaurant {
    private String name;
    private LngLat lngLat;
    private Menu menu;
    public Menu[] getMenu() {
        //TODO implement getMenu
        return new Menu[0];
    }
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        //TODO implement getRestaurantsFromRestServer
        //Note this method acts as a factory method

        return new Restaurant[0];
    }
}
