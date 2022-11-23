package uk.ac.ed.inf;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton class for retrieving and storing the central area polygon
 * from the REST-API
 */
public class CentralArea extends Polygon {
    private static CentralArea instance;
    private CentralArea(LngLat[] vertices) {
        super(vertices);
    }
    private static CentralArea centralAreaFactory(URL apiURL) {
        LngLat[] vertices = null;
        try {
            vertices = new ObjectMapper().readValue(apiURL, LngLat[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(vertices == null) {
            return null;
        }
        return new CentralArea(vertices);
    }


    /**
     * Lazily initialises the singleton instance as required
     * One can assume that a null return means fetching from the API failed.
     * @return the singleton instance of CentralArea
     */
    public static CentralArea getInstance() {
        if(instance == null) {
            try{
                instance = centralAreaFactory(new URL(Constants.API_BASE + Constants.CENTRAL_AREA_EXTENSION));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
