package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.areas.NoFlyZones;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoFlyZonesTest {
    LngLat inDrElsie = new LngLat(-3.1907, 		55.9452);
    LngLat inGeorgeSquareNoFlyZone = new LngLat(	-3.1890, 	55.9436);
    LngLat inBristoSquareNoFlyZone = new LngLat(	-3.1888, 	55.9456);
    LngLat inBristoSquareNoFlyZone2 = new LngLat(	-3.1894, 	55.9456);

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
        assertTrue(noFlyZones.pointIsInNoFlyZone(inBristoSquareNoFlyZone2));
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
//    @Test
//    public void testPointInDrElsie() {
//        NoFlyZones noFlyZones = NoFlyZones.getInstance();
//
//        assertTrue(noFlyZones.pointIsInNoFlyZone(inDrElsie));
//    }
}
