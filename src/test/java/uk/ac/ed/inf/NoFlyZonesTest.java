package uk.ac.ed.inf;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoFlyZonesTest {
    LngLat inGeorgeSquareNoFlyZone = new LngLat(	-3.1890, 	55.9436);
    LngLat inBristoSquareNoFlyZone = new LngLat(	-3.1888, 	55.9456);

    LngLat outsideNoFlyZone1 = new LngLat(-3.1905, 	55.9445);
    LngLat outsideNoFlyZone2 = new LngLat(	-3.1876, 	55.9455);
    @Test
    public void testGeorgeSquareNoFlyZone() {
        NoFlyZones noFlyZones = NoFlyZones.getInstance();

        assertTrue(noFlyZones.pointIsInNoFlyZone(inGeorgeSquareNoFlyZone));
    }
    @Test
    public void testBristoSquareNoFlyZone() {
        NoFlyZones noFlyZones = NoFlyZones.getInstance();

        assertTrue(noFlyZones.pointIsInNoFlyZone(inBristoSquareNoFlyZone));
    }

    @Test
    public void testPointOutsideNoFlyZone1() {
        NoFlyZones noFlyZones = NoFlyZones.getInstance();

        assertFalse(noFlyZones.pointIsInNoFlyZone(outsideNoFlyZone1));

    }
    @Test
    public void testPointOutsideNoFlyZone2() {
        NoFlyZones noFlyZones = NoFlyZones.getInstance();

        assertFalse(noFlyZones.pointIsInNoFlyZone(outsideNoFlyZone2));

    }
}
