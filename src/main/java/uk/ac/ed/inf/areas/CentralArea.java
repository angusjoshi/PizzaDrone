package uk.ac.ed.inf.areas;

import uk.ac.ed.inf.restutils.RestClient;


/**
 * Singleton class for retrieving and storing the central area polygon
 * from the REST-API
 */
public class CentralArea extends Polygon {
    private static CentralArea instance;
    private CentralArea(LngLat[] vertices) {
        super(vertices);
    }
    private static CentralArea centralAreaFactory() {
        RestClient restClient = RestClient.getInstance();

        LngLat[] vertices = restClient.getCentralAreaVerticesFromRestServer();
        return new CentralArea(vertices);
    }


    /**
     * Lazily initialises the singleton instance as required
     * One can assume that a null return means fetching from the API failed.
     * @return the singleton instance of CentralArea
     */
    public static CentralArea getInstance() {
        if(instance == null) {
            instance = centralAreaFactory();
        }
        return instance;
    }
}
