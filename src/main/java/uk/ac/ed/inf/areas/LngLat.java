package uk.ac.ed.inf.areas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.pathing.CompassDirection;

/**
 * Record for storing longitude and latitude of a single point,
 * also providing functionality for operations between LngLat instances
 * @param lng the point's longitude
 * @param lat the point's latitude
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat) {

    /**
     * constant to store the length of a single step
     */
    public static final double STEP_LENGTH = 0.00015;

    /**
     * Determines whether a LngLat instance is in the central area, as defined by
     * the current instance of CentralArea. Determine strict inclusion, i.e. a point on an edge
     * is not counted to be included.
     * @return true if the point is in the central area, false otherwise
     */
    public boolean inCentralArea() {
        CentralArea centralArea = CentralArea.getInstance();
        return centralArea.isPointInside(this);
    }

    /**
     * determine whether a cycle on three vertices in euclidean space is clockwise, anticlockwise, or collinear
     * @param a start and end vertex
     * @param b second vertex
     * @param c third vertex
     * @return 1 if cycle is clockwise, -1 if cycle is anticlockwise, 0 if the points are collinear
     */
    public static int isPathClockwise(LngLat a, LngLat b, LngLat c) {
        LngLat ca = a.sub(c);
        LngLat cb = b.sub(c);
        double k = ca.cross(cb);
        if(k > 0) return -1;
        if(k < 0) return 1;
        return 0;
    }

    /**
     * determine if two line segments defined by start and end points intersect at
     * exactly one point p, where p is not equal to any of the endpoints of the line segments.
     * @param a line 1 start point
     * @param b line 1 end point
     * @param c line 2 start point
     * @param d line 2 end point
     * @return true if line segments intersect at exactly one point p, where p not in {a, b, c, d}
     */
    public static boolean lineSegmentsIntersect(LngLat a, LngLat b, LngLat c, LngLat d) {

        //consider cycles starting with one of the line segments, then including one of the vertices of the other line
        int abc = isPathClockwise(a, b, c);
        int abd = isPathClockwise(a, b, d);
        int cda = isPathClockwise(c, d, a);
        int cdb = isPathClockwise(c, d, b);

        //all of these cases correspond to a collinearity between points
        if(abc == 0 || abd == 0 || cda == 0 || cdb == 0) return false;

        //line segments intersect if the two cycles starting on each line segment are different in direction
        //for both line segments
        return abc != abd && cda != cdb;
    }

    /**
     * vector addition of two LngLat instances
     * @param other LngLat instance to add to this instance
     * @return new LngLat instance resultant from the addition
     */
    public LngLat add(LngLat other) {
        return new LngLat(this.lng + other.lng, this.lat + other.lat);
    }

    /**
     * scale this LngLat instance by a scalar quantity
     * @param b scalar quantity to scale by
     * @return new LngLat instance resultant from scaling
     */
    public LngLat scale(double b) {
        return new LngLat(this.lng * b, this.lat * b);
    }

    /**
     * vector subtraction of this LngLat instance and another LngLat instance
     * @param other LngLat instance to subtract from this
     * @return new LngLat instance resultant from the subtraction
     */
    public LngLat sub(LngLat other) {
        return new LngLat(this.lng - other.lng, this.lat - other.lat);
    }

    /**
     * vector cross product of this instance and another LngLat instance
     * LngLat is restricted to two dimensions, so the resultant cross product is always
     * orthogonal to the plane we are working on
     * @param other LngLat instance to cross with this instance
     * @return k coefficient of resultant cross product
     */
    public double cross(LngLat other) {
        return (this.lng * other.lat) - (this.lat * other.lng);
    }

    /**
     * find the distance from this instance to another LngLat instance
     * @param other LngLat instance to compare this to
     * @return distance between this and other in degrees
     */
    public double distanceTo(LngLat other) {
        double deltaLng = this.lng - other.lng;
        double deltaLat = this.lat - other.lat;
        return Math.sqrt(deltaLng * deltaLng + deltaLat * deltaLat);
    }

    /**
     * determine if another LngLat instance is within one step distance to this
     * @param other LngLat instance to compare this to
     * @return true if this is within one step to other, false otherwise
     */

    public boolean closeTo(LngLat other) {
        return this.distanceTo(other) < STEP_LENGTH;
    }

    /**
     * find the next position if we were to take a step from this in a direction on the 16 point compass
     * @param direction a direction from the 16 point compass. direction = null means hover (don't move)
     * @return resultant LngLat from taking a step in direction
     */
    public LngLat nextPosition(CompassDirection direction) {
        if(direction == null) return this;
        return this.add(direction.getOffset(STEP_LENGTH));
    }

    /**
     * Round the longitude and latitude of this to the nearest multiple of the STEP_LENGTH unit
     * @return A new LngLat instance with the rounded values.
     */
    public LngLat roundToNearestStep() {
        var newLng = this.lng() - (this.lng() % STEP_LENGTH);
        var newLat = this.lat() - (this.lat() % STEP_LENGTH);

        //add half a step so we are rounding to the centre of the grid square rather than the bottom left.
        newLng += STEP_LENGTH / 2;
        newLat += STEP_LENGTH / 2;

        return new LngLat(newLng, newLat);
    }
    public double chebyshevDistance(LngLat other) {
        double latDistance = Math.abs(this.lat() - other.lat());
        double lngDistance = Math.abs(this.lng() - other.lng());
        return Math.max(latDistance, lngDistance);
    }
    public double[] toCoordinates() {
        double[] coordinates = new double[2];
        coordinates[0] = this.lng();
        coordinates[1] = this.lat();
        return coordinates;
    }
}


