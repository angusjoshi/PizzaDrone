package uk.ac.ed.inf;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton class for retrieving and storing the central area polygon
 * from the REST-API
 */
public class CentralArea {
    private static CentralArea instance;
    private LngLat[] vertices;
    private static double maxLongitude;
    private CentralArea(URL apiURL) {
        try {
            vertices = new ObjectMapper().readValue(apiURL, LngLat[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(vertices == null) return;

        maxLongitude = Arrays.stream(vertices)
                .map(LngLat::lng)
                .max(Double::compareTo)
                .orElse(null);
    }

    /**
     * get the maximum longitude of all corners of the central area
     * @return the maximum longitude across all corners of the bounding polygon
     */
    public double getMaxLongitude() {
        return maxLongitude;
    }

    /**
     * Lazily initialises the singleton instance as required
     * One can assume that a null return means fetching from the API failed.
     * @return the singleton instance of CentralArea
     */
    public static CentralArea getInstance() {
        if(instance == null) {
            try{
                instance = new CentralArea(new URL(Constants.API_BASE + Constants.CENTRAL_AREA_EXTENSION));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * get the vertices of the central area in anti-clockwise order
     * @return the vertices of the central area bounding polygon
     */
    public LngLat[] getVertices() {
        return this.vertices;
    }
}
