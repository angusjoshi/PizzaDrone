package uk.ac.ed.inf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompassDirectionTest {

    @Test
    public void testOffset1() {
        assertEquals(0.00015,  CompassDirection.N.getOffset(0.00015).lat(), 0);
    }
    @Test
    public void testOffset2() {
        assertEquals(0.00015, CompassDirection.E.getOffset(0.00015).lng(), 0);
    }
    @Test
    public void testOffset3() {
        var a = new LngLat(0, 0);
        var b = CompassDirection.ENE.getOffset(1);
        assertEquals(1, a.distanceTo(b), 0);
    }
}
