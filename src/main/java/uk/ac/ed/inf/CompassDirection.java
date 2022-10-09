package uk.ac.ed.inf;

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
    }

    /**
     * multiplies the unit direction in the direction by whatever step length we are using
     * @param stepLength length of the step
     * @return vector with required length and direction
     */
    public LngLat getOffset(double stepLength) {
        return unitStep.scale(stepLength);
    }
}
