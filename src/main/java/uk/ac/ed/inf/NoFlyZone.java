package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoFlyZone extends Polygon {

    private String name;

    public NoFlyZone(@JsonProperty("name") String name, @JsonProperty("coordinates") double[][] coordindates) {
        super(coordindates);
        this.name = name;
    }
    private NoFlyZone(String name, LngLat[] vertices) {
        super(vertices);
        this.name = name;
    }
}
