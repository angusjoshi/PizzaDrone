package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResponse {
    @JsonProperty
    private String greeting;

    public boolean greetingContains(String substring) {
        return greeting.contains(substring);
    }
}
