package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CentralArea {
    private static final String CENTRAL_AREA_EXTENSION = "centralArea";
    private static CentralArea instance;
    private final LngLat[] lngLats;
    private static double maxLongitude;
    private CentralArea(URL apiURL) {
        Corner[] corners = new Corner[0];
        try{
            corners = new ObjectMapper().readValue(apiURL, Corner[].class);
        } catch(Exception e) {
            e.printStackTrace();
        }
        lngLats = Arrays.stream(corners)
                .map(corner -> new LngLat(corner.longitude, corner.latitude))
                .toArray(LngLat[]::new);
        maxLongitude = Arrays.stream(lngLats)
                .map(LngLat::lng)
                .max(Double::compareTo)
                .orElse(null);
    }
    public double getMaxLongitude() {
        return maxLongitude;
    }
    public static CentralArea getInstance() {
        if(instance == null) {
            try {
                instance = new CentralArea(new URL(App.API_BASE + CENTRAL_AREA_EXTENSION));
            } catch(MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    public LngLat[] getLngLats() {
        return this.lngLats;
    }
}
