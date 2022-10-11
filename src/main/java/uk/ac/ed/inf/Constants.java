package uk.ac.ed.inf;

/**
 * Final class to contain constants to be used throughout the program
 */
public final class Constants {
    //declare private constructor to hide the default constructor.
    private Constants() {
    }
    /**
     * Longitude and latitude of Appleton Tower, as defined in the spec.
     */
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    /**
     * String holding the base address for the REST-API
     */
    public static final String API_BASE = "https://ilp-rest.azurewebsites.net/";

    /**
     * URL extension for the centralArea API endpoint
     */
    public static final String CENTRAL_AREA_EXTENSION = "centralArea";

    /**
     * URL extension for the restaurants API endpoint
     */
    public static final String RESTAURANTS_EXTENSION =  "restaurants";

}
