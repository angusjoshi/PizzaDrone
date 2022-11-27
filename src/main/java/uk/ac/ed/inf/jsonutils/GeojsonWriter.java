package uk.ac.ed.inf.jsonutils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.ac.ed.inf.pathing.Move;
import uk.ac.ed.inf.order.Order;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


/**
 * Class with utilities for writing flightpaths to a geojson
 */
public class GeojsonWriter {
    //To hide the default constructor
    private GeojsonWriter(){}

    /**
     * Writes a list of orders to a geojson in the working directory in the required format.
     * The filename will be in the form "drone-YYYY_MM_dd.geojson"
     * @param ordersToDeliver The list of orders that are to be delivered. Orders must be computed prior with
     *                        the appropriate method in each order instance
     * @param currentDayString The current day date string
     */
    public static void writeDeliveryPathToGeojson(List<Order> ordersToDeliver, String currentDayString) {
        List<Move> flightPath = Move.getFlightPath(ordersToDeliver);
        var coordinates =  flightPath.stream()
                .map(Move::fromAsCoordinates).toArray();

        //Fill up the objects to be written
        LineString lineString = new LineString(coordinates);
        Feature feature = new Feature(lineString);
        Feature[] features = new Feature[1];
        features[0] = feature;
        FeatureCollection featureCollection = new FeatureCollection(features);

        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = "drone-" + currentDayString + ".geojson";
        try {
            objectMapper.writeValue(Paths.get(fileName).toFile(), featureCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * class structure for writing to the geojson
 */
class FeatureCollection {
    String type;
    private final Feature[] features;

    public FeatureCollection(Feature[] features) {
        this.features = features;
        this.type = "FeatureCollection";
    }

    public String getType() {
        return this.type;
    }
    public Feature[] getFeatures() {
        return this.features;
    }
}

/**
 * class structure for writing to the geojson
 */
class Feature {
    private final String type;
    private final LineString geometry;
    private final Object properties;

    public Object getProperties() {
        return properties;
    }
    public Feature(LineString geometry) {
        this.type = "Feature";
        this.geometry = geometry;
        this.properties = new Properties();
    }
    public String getType() {
        return this.type;
    }
    public LineString getGeometry() {
        return this.geometry;
    }
}

/**
 * class structure for witing to the geojson
 */
class LineString {
    private final Object[] coordinates;
    private final String type;
    public LineString(Object[] coordinates) {
        this.coordinates = coordinates;
        this.type = "LineString";
    }

    public Object[] getCoordinates() {
        return this.coordinates;
    }
    public String getType() {
        return this.type;
    }
}

/**
 * class structure for writing to the geojson
 */
@JsonSerialize
class Properties {
    public Properties(){}
}