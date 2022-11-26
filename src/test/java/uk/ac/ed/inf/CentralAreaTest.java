package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.CentralArea;
import uk.ac.ed.inf.restutils.BadTestResponseException;
import uk.ac.ed.inf.restutils.RestClient;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


public class CentralAreaTest {
    @Test
    public void testCentralArea1() throws IOException, BadTestResponseException {
        RestClient.initialiseRestClient(Constants.API_BASE);
        CentralArea centralArea = CentralArea.getInstance();
        assertNotNull(centralArea);
    }
}
