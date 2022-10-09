package uk.ac.ed.inf;

public record LngLat(double lng, double lat) {

    public static final double STEP_LENGTH = 0.00015;
    public boolean inCentralArea() {
        CentralArea centralArea = CentralArea.getInstance();
        LngLat farRight = new LngLat(centralArea.getMaxLongitude() + 0.01, this.lat());

        LngLat[] centralVertices = centralArea.getLngLats();

        int edgeIntersections = 0;
        for(int i = 0; i < centralVertices.length; i++) {
            var vertexOne = centralVertices[i];
            var vertexTwo = centralVertices[(i + 1) % centralVertices.length];
            if(lineSegsIntersect(farRight, this,  vertexOne, vertexTwo)) {
                edgeIntersections++;
            }
        }
        return edgeIntersections % 2 == 1;
    }

    public static int isPathClockwise(LngLat a, LngLat b, LngLat c) {
        LngLat ca = a.sub(c);
        LngLat cb = b.sub(c);
        double k = ca.cross(cb);
        if(k > 0) return -1;
        if(k < 0) return 1;
        return 0;
    }
    public static boolean lineSegsIntersect(LngLat a, LngLat b, LngLat c, LngLat d) {

        int abc = isPathClockwise(a, b, c);
        int abd = isPathClockwise(a, b, d);
        int cda = isPathClockwise(c, d, a);
        int cdb = isPathClockwise(c, d, b);

        if(abc == 0 || abd == 0 || cda == 0 || cdb == 0) return false;

        return abc != abd && cda != cdb;
    }
    public LngLat add(LngLat other) {
        return new LngLat(this.lng + other.lng, this.lat + other.lat);
    }
    public LngLat mul(double b) {
        return new LngLat(this.lng * b, this.lat * b);
    }
    public LngLat sub(LngLat other) {
        return new LngLat(this.lng - other.lng, this.lat - other.lat);
    }
    public double cross(LngLat other) {
        return (this.lng * other.lat) - (this.lat * other.lng);
    }
    public double distanceTo(LngLat other) {
        double deltaLng = this.lng - other.lng;
        double deltaLat = this.lat - other.lat;
        return Math.sqrt(deltaLng * deltaLng + deltaLat * deltaLat);
    }
    public boolean closeTo(LngLat other) {
        return this.distanceTo(other) < STEP_LENGTH;
    }
    public LngLat nextPosition(CompassDirection direction) {
        if(direction == null) return this;
        return this.add(direction.getOffset(STEP_LENGTH));
    }
}


