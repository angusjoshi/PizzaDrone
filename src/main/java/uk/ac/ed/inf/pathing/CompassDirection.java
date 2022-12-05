package uk.ac.ed.inf.pathing;

import uk.ac.ed.inf.areas.LngLat;

/**
 * enumeration for the directions on a 16 point compass
 */
public enum CompassDirection {
    E(0),
    ENE(1),
    NE(2),
    NNE(3),
    N(4),
    NNW(5),
    NW(6),
    WNW(7),
    W(8),
    WSW(9),
    SW(10),
    SSW(11),
    S(12),
    SSE(13),
    SE(14),
    ESE(15);

    private final LngLat unitStep;
    private final double theta;

    /**
     * constructor for each compass direction.
     * determines a vector for the unit step in the direction
     * @param nSixteenths number of divisions of the direction in an anticlockwise direction
     */
    CompassDirection(int nSixteenths){
        double theta = (Math.PI / 8) * nSixteenths;
        double x = Math.cos(theta);
        double y = Math.sin(theta);

        this.unitStep = new LngLat(x, y);
        this.theta = 22.5 * nSixteenths;
    }

    /**
     * multiplies the unit direction in the direction by whatever step length we are using
     * @param stepLength length of the step
     * @return vector with required length and direction
     */
    public LngLat getOffset(double stepLength) {
        return unitStep.scale(stepLength);
    }

    /**
     * Gets a compass direction pointing exactly opposite to this one
     * @return the opposite compass direction
     */
    public CompassDirection reverseDirection() {
        return switch (this) {
            case N -> S;
            case NNE -> SSW;
            case NE -> SW;
            case ENE -> WSW;
            case E -> W;
            case ESE -> WNW;
            case SE -> NW;
            case SSE -> NNW;
            case S -> N;
            case SSW -> NNE;
            case SW -> NE;
            case WSW -> ENE;
            case W -> E;
            case WNW -> ESE;
            case NW -> SE;
            case NNW -> SSE;
            default -> null;
        };
    }

    private double getAngleAsDegrees() {
        return theta;
    }

    /**
     * Static method to get the angle associated with a compass direction.
     * This method is static so null directions call be dealt with nicely.
     * @param direction The direction to check, can be null
     * @return the angle in degrees as a Double object (not primitive)
     */
    public static Double getAngleAsDegrees(CompassDirection direction) {
        if(direction == null) {
            return null;
        }
        return direction.getAngleAsDegrees();
    }
}
