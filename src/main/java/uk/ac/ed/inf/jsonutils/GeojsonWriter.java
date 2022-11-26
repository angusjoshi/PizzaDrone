package uk.ac.ed.inf.jsonutils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.ac.ed.inf.pathing.Move;
import uk.ac.ed.inf.order.Order;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


public class GeojsonWriter {
    public static void writeDeliveryPathToGeojson(List<Order> ordersToDeliver, String currentDayString) {
        List<Move> flightPath = Move.getFlightPath(ordersToDeliver);
        var coordinates =  flightPath.stream()
                .map(Move::fromAsCoordinates).toArray();

        LineString lineString = new LineString(coordinates);
        Feature feature = new Feature(lineString);
        Feature[] features = new Feature[1];
        features[0] = feature;
        FeatureCollection featureCollection = new FeatureCollection(features);
        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = "drone-" + currentDayString + ".json";
        try {
            objectMapper.writeValue(Paths.get(fileName).toFile(), featureCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class FeatureCollection {
    String type;
    private Feature[] features;

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
class Feature {
    private String type;
    private LineString geometry;
    private Object properties;

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
class LineString {
    private Object[] coordinates;
    private String type;
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
@JsonSerialize
class Properties {
    public Properties(){}
}