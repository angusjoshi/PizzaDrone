package uk.ac.ed.inf.areas;

/**
 * Interface for polygons in the LngLat space.
 */
public interface IPolygon {
    /**
     * determine whether or not a point is inside the polygon.
     * @param point the point to check
     * @return true if the point is inside, false otherwise
     */
    boolean isPointInside(LngLat point);

}
