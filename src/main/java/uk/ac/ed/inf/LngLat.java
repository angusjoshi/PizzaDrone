package uk.ac.ed.inf;

public record LngLat(double lng, double lat) {
    public boolean inCentralArea() {
        //TODO: implement inCentralArea
        //spec suggests using singleton class pattern
        return false;
    }
    public double distanceTo(LngLat other) {
        double deltaLng = this.lng - other.lng;
        double deltaLat = this.lat - other.lat;
        return Math.sqrt(deltaLng * deltaLng + deltaLat * deltaLat);
    }
    public boolean closeTo(LngLat other) {
        return this.distanceTo(other) < 0.00015;
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


