package uk.ac.ed.inf;

public record LngLat(double lng, double lat) {

    public static final double STEP_LENGTH = 0.00015;
    public boolean inCentralArea() {
        //TODO: implement inCentralArea
        //spec suggests using singleton class pattern
        return false;
    }
    public LngLat add(LngLat other) {
        return new LngLat(this.lng + other.lng, this.lat + other.lat);
    }
    public LngLat mul(double b) {
        return new LngLat(this.lng * b, this.lat * b);
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
        return this.add(direction.getOffset(STEP_LENGTH));
    }
}


