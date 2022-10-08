package uk.ac.ed.inf;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class LngLatTest {
    @Test
    public void testDistanceTo1() {
        var a = new LngLat(0, 0);
        var b = new LngLat(1, 0);
        assertEquals(1, a.distanceTo(b), 0);
    }
    @Test
    public void testDistanceTo2() {
        var a = new LngLat(0, 0);
        var b = new LngLat(1, 1);
        assertEquals(Math.sqrt(2), a.distanceTo(b), 0);
    }
    @Test
    public void testCloseTo1() {
        var a = new LngLat(0, 0);
        var b = new LngLat(0, 0.00014);
        assertTrue(a.closeTo(b));
    }
    @Test
    public void testCloseTo2() {
        var a = new LngLat(0, 0);
        var b = new LngLat(0, 0.00016);
        assertFalse(a.closeTo(b));
    }
    @Test
    public void testCloseTo3() {
        var a = new LngLat(1, 1);
        var b = new LngLat(1.1, 0.9);
        assertFalse(a.closeTo(b));
    }
    @Test
    public void testNextPosition1() {
        var a = new LngLat(0, 0);
        var b = a.nextPosition(CompassDirection.E);
        assertEquals(0.00015, b.lng(), 0);
        assertEquals(0, b.lat(), 0);
    }
    @Test
    public void testNextPosition2() {
        var a = new LngLat(0, 0);
        a = a.nextPosition(CompassDirection.NE);
        a = a.nextPosition(CompassDirection.SW);
        a = a.nextPosition(CompassDirection.ESE);
        a = a.nextPosition(CompassDirection.WNW);
        assertEquals(0, a.lng(), 1E-15);
        assertEquals(0, a.lat(), 1E-15);
    }

}
