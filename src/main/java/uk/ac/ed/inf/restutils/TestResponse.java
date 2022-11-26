package uk.ac.ed.inf.restutils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * class to be used for desearlising the test request to the rest api
 */
public class TestResponse {
    @JsonProperty
    private String greeting;

    /**
     * Check if the received greeting contains a given substring
     * @param substring the substring to look for
     * @return true if the substring is found, false otherwise
     */
    public boolean greetingContains(String substring) {
        return greeting.contains(substring);
    }
}
