package uk.ac.ed.inf;

public class Rectangle implements IPolygon {
    private final LngLat topLeft;
    private final LngLat bottomRight;

    public Rectangle(LngLat topLeft, LngLat bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public boolean isPointInside(LngLat point) {
        boolean inVerticalRange = point.lat() >= topLeft.lat() && point.lat() <= bottomRight.lat();
        boolean inHorizontalRange = point.lng() >= topLeft.lng() && point.lng() <= bottomRight.lng();
        return inVerticalRange && inHorizontalRange;
    }
}
