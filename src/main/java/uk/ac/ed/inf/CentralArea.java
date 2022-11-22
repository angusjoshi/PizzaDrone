package uk.ac.ed.inf;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton class for retrieving and storing the central area polygon
 * from the REST-API
 */
public class CentralArea {
    private static CentralArea instance;
    private static IPolygon centralAreaPolygon;
    private CentralArea(URL apiURL) {
        LngLat[] vertices = null;
        try {
            vertices = new ObjectMapper().readValue(apiURL, LngLat[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(vertices == null) return;

        centralAreaPolygon = new Polygon(vertices);
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

    public boolean isPointInCentralArea(LngLat point) {
        return centralAreaPolygon.isPointInside(point);
    }
}
