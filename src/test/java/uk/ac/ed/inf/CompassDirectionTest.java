package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.pathing.CompassDirection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CompassDirectionTest {
    @Test
    public void testOppositeDirection() {
        assertEquals(CompassDirection.S, CompassDirection.N.getOppositeDirection());
        assertEquals(CompassDirection.N, CompassDirection.S.getOppositeDirection());
        assertNotEquals(CompassDirection.S, CompassDirection.S.getOppositeDirection());
        assertNotEquals(CompassDirection.N, CompassDirection.N.getOppositeDirection());
    }
    @Test
    public void testOppositeDirection2() {
        assertEquals(CompassDirection.E, CompassDirection.W.getOppositeDirection());
    }

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
