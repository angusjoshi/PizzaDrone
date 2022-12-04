package uk.ac.ed.inf.areas;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to be instantiated, storing NoFlyZones as a polygon in the LngLat space.
 */
public class NoFlyZone extends Polygon {
    private final String name;

    /**
     * Constructor for a NoFlyZone, mainly for use in the jackson deserialization.
     * @param name Name of the NoFlyZone
     * @param coordindates coordinates of the vertices, in anticlockwise order from
     *                     the top-left. Stricly an array of length 2 arrays.
     */
    public NoFlyZone(
            @JsonProperty("name") String name,
            @JsonProperty("coordinates") double[][] coordindates
    ) {
        super(coordindates);
        this.name = name;
    }
    private NoFlyZone(String name, LngLat[] vertices) {
        super(vertices);
        this.name = name;
    }
}
