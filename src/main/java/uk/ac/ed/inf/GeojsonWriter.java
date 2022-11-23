package uk.ac.ed.inf;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;


public class GeojsonWriter {
    public static void makeGeoJsonForTesting(SearchNode finalNode) {
        double[][] coordinates = new double[finalNode.getNSteps() + 1][];
        SearchNode currentNode = finalNode;
        int stepN = finalNode.getNSteps();
        while(stepN >= 0) {
            double[] lngLatAsCoordinates = currentNode.getLocation().toCoordinates();
            coordinates[stepN] = lngLatAsCoordinates;
            currentNode = currentNode.getPrevNode();
            stepN--;
        }
        LineString lineString = new LineString(coordinates);
        Feature feature = new Feature(lineString);
        Feature[] features = new Feature[1];
        features[0] = feature;
        FeatureCollection featureCollection = new FeatureCollection(features);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(Paths.get("hehe.geojson").toFile(), featureCollection);
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
        this.properties = null;
    }
    public String getType() {
        return this.type;
    }
    public LineString getGeometry() {
        return this.geometry;
    }
}
class LineString {
    private double[][] coordinates;
    private String type;
    public LineString(double[][] coordinates) {
        this.coordinates = coordinates;
        this.type = "LineString";
    }

    public double[][] getCoordinates() {
        return this.coordinates;
    }
    public String getType() {
        return this.type;
    }
}