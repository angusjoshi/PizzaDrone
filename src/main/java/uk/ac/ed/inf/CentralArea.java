package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CentralArea {
    private static final String CENTRAL_AREA_EXTENSION = "centralArea";
    private static CentralArea instance;
    private CentralArea(URL baseURL) {
        // retrieve from the API and deserialize into here.


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
}
