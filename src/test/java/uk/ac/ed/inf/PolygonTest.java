package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.IPolygon;
import uk.ac.ed.inf.areas.Polygon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PolygonTest {
    private LngLat[] pentagonVertices = new LngLat[]{
            new LngLat(1, 1.01),
            new LngLat(1.002, 1),
            new LngLat(1.008, 1),
            new LngLat(1.01, 1.01),
            new LngLat(1.005, 1.014)
    };
    @Test
    public void testIsPointOutsideWithPentagon() {
        IPolygon pentagon = new Polygon(pentagonVertices);

        var outside = new LngLat(1.015, 1);

        assertFalse(pentagon.isPointInside(outside));
    }

    @Test
    public void testIsPointInsideWithPentagon() {
        IPolygon pentagon = new Polygon(pentagonVertices);

        var inside = new LngLat(1.005, 1.008);

        assertTrue(pentagon.isPointInside(inside));
    }

    @Test
    public void testPointCloseToPentagonIsOutside() {
        IPolygon pentagon = new Polygon(pentagonVertices);

        var outside = new LngLat(1.0013, 1.013);

        assertFalse(pentagon.isPointInside(outside));
    }
}
