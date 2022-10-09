package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Corner {
    @JsonProperty("name")
    String name;

    @JsonProperty("longitude")
    double longitude;

    @JsonProperty("latitude")
    double latitude;
}
