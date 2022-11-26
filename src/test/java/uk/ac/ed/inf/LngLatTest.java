package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.pathing.CompassDirection;

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
    @Test
    public void testNextPosition3() {
        var a = new LngLat(0, 0);
        a = a.nextPosition(null);
        assertEquals(0, a.lng(), 0);
        assertEquals(0, a.lat(), 0);
    }
    @Test
    public void testInCentralArea1() {
        var p = new LngLat(-3.189135789871216,
                55.944555845685876);
        assertTrue(p.inCentralArea());
    }
    @Test
    public void testInCentralArea2() {
        var p = new LngLat(-3.2,
                55.94460090635972);
        assertFalse(p.inCentralArea());
    }
    @Test
    public void testInCentralArea3() {
        var p = new LngLat(-3.183,
                55.94460090635972);
        assertFalse(p.inCentralArea());
    }
    @Test
    public void testPathClockwise4() {
        var bus = new LngLat(-3.1878483,
                55.9440151);
        var kfc = new LngLat(-3.1878483,
                55.9455382);
        var at = Constants.APPLETON_TOWER;
        var outside = new LngLat(-3.1875962018966675,
                55.94460090635972);
        assertEquals(1, LngLat.isPathClockwise(bus, kfc, outside));
        assertEquals(1, LngLat.isPathClockwise(bus, kfc, at));
    }
    @Test
    public void testLineSegsIntersect3() {
        var top = new LngLat(-3.184319,55.946233);
        var bot = new LngLat(-3.184319,55.942617);
        var left = new LngLat(-3.1850, 55.9445);
        var right = new LngLat(	-3.1832, 	55.9445);
        assertTrue(LngLat.lineSegmentsIntersect(right, left, top, bot));
        assertFalse(LngLat.lineSegmentsIntersect(top, right, bot, left));
    }
    @Test
    public void testPathClockwise1() {
        var p = new LngLat(0, 0);
        var q = new LngLat(1, 1);
        var r = new LngLat(1, 0);

        assertEquals(1, LngLat.isPathClockwise(p, q, r));
    }
    @Test
    public void testPathClockwise2() {
        var p = new LngLat(0, 0);
        var q = new LngLat(1, 0);
        var r = new LngLat(1, 1);
        assertEquals(-1, LngLat.isPathClockwise(p, q, r));
    }
    @Test
    public void testPathClockwise3() {
        var p = new LngLat(-3.184319, 55.9426);
        var q = new LngLat(-3.184319, 55.9462);
        var r = new LngLat(-3.187596, 55.9446);
        assertEquals(-1, LngLat.isPathClockwise(p, q, r));
    }
    @Test
    public void testLineSegsIntersect1() {
        var p = new LngLat(0, 0);
        var q = new LngLat(1, 0);
        var r = new LngLat(0.5, 1);
        var s = new LngLat(0.5, -1);
        assertTrue(LngLat.lineSegmentsIntersect(p, q, r, s));
    }

    @Test
    public void testRoundPoint() {
        var pointToRound = new LngLat(0.0001, 0.0001);
        var roundedPoint = pointToRound.roundToNearestStep();

        assertEquals(0.000075, roundedPoint.lat(), 0);
        assertEquals(0.000075, roundedPoint.lng(), 0);
    }

    @Test
    public void testRoundPoint2() {
        var pointToRound = new LngLat(0.00015, 0.00015);
        var roundedPoint = pointToRound.roundToNearestStep();

        assertEquals(0.000225, roundedPoint.lng(), 0);
        assertEquals(0.000225, roundedPoint.lat(), 0);
    }
}
