package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.IPolygon;
import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.areas.Polygon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PolygonTest {
    private LngLat[] elsie = new LngLat[] {
            new LngLat( -3.1907182931900024, 55.94519570234043),
            new LngLat(-3.1906163692474365, 55.94498241796357),
            new LngLat(-3.1900262832641597, 55.94507554227258),
            new LngLat(-3.190133571624756, 55.94529783810495),
            new LngLat(-3.1907182931900024, 55.94519570234043)
    };
    private LngLat insideElsie = new LngLat(-3.1901, 55.9452);
    private LngLat outsideElsie = new LngLat(-3.1901, 55.9452);
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

//    @Test
//    public void testPointCloseToPentagonIsOutside() {
//        IPolygon pentagon = new Polygon(pentagonVertices);
//
//        var outside = new LngLat(1.0013, 1.013);
//
//        assertFalse(pentagon.isPointInside(outside));
//    }

    @Test
    public void testPointInsideElsie() {
        IPolygon elsieArea = new Polygon(elsie);
        assertTrue(elsieArea.isPointInside(insideElsie));
    }
//    @Test
//    public void testPointOutsideElsie() {
//        IPolygon elsieArea = new Polygon(elsie);
//        assertFalse(elsieArea.isPointInside(outsideElsie));
//    }


}
