package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.CentralArea;

import static org.junit.Assert.assertNotNull;


public class CentralAreaTest {
    @Test
    public void testCentralArea1() {
        CentralArea centralArea = CentralArea.getInstance();
        assertNotNull(centralArea);
    }
}
