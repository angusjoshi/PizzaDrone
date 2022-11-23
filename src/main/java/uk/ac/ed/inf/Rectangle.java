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
        boolean inVerticalRange = point.lat() >= topLeft.lat() - LngLat.STEP_LENGTH
                && point.lat() <= bottomRight.lat() + LngLat.STEP_LENGTH;
        boolean inHorizontalRange = point.lng() >= topLeft.lng() - LngLat.STEP_LENGTH
                && point.lng() <= bottomRight.lng() + LngLat.STEP_LENGTH;
        //TODO: make this cleaner
        return inVerticalRange && inHorizontalRange;
    }
}
