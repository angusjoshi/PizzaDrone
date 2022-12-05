package uk.ac.ed.inf.areas;

/**
 * Class to represent a rectangle in the LngLat space. The rectangle can only be oriented
 * with it's base parallel to the longitudinal dimension.
 */
public class Rectangle implements IPolygon {
    private final LngLat topLeft;
    private final LngLat bottomRight;

    /**
     * basic constructor for a rectangle from the top left and bottom right corners.
     * @param topLeft the top left corner point as a LngLat
     * @param bottomRight the bottom right corner point as a LngLat
     */
    public Rectangle(LngLat topLeft, LngLat bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Determine if a LngLat point is inside this rectangle. The rectangle is assumed to be oriented
     * with it's base parallel to the longitudinal dimension.
     * @param point the point as a LngLat to be checked for inclusion within the rectangle.
     * @return true if the point is inside the rectangle, false otherwise.
     */
    @Override
    public boolean isPointInside(LngLat point) {
        boolean inVerticalRange = point.lat() >= topLeft.lat() && point.lat() <= bottomRight.lat();
        boolean inHorizontalRange = point.lng() >= topLeft.lng() && point.lng() <= bottomRight.lng();

        return inVerticalRange && inHorizontalRange;
    }
}
