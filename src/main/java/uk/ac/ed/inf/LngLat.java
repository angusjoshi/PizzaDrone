package uk.ac.ed.inf;

public record LngLat(double lng, double lat) {
    public boolean inCentralArea() {
        //TODO: implement inCentralArea
        //spec suggests using singleton class pattern
        return false;
    }
    public double distanceTo(LngLat other) {
        //TODO implement dinstanceTo as euclidean distance between two points
        return 0;
    }
    public boolean closeTo(LngLat other) {
        //TODO implement closeTo
        return true;
    }
    public LngLat nextPosition(CompassDirection direction) {
        //TODO implement nextPosition
        return new LngLat(0, 0);
    }
    private enum CompassDirection {
        //TODO fill rest of compassdirection options
        NORTH,
        SOUTH,
        EAST,
        WEST,
    }
}


