package uk.ac.ed.inf.areas;

import uk.ac.ed.inf.LngLat;

public interface IPolygon {
    boolean isPointInside(LngLat point);

}
