package uk.ac.ed.inf.pathing;

import uk.ac.ed.inf.LngLat;

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

    public CompassDirection getOppositeDirection() {
        switch(this) {
            case N: return S;
            case NNE: return SSW;
            case NE: return SW;
            case ENE: return WSW;
            case E: return W;
            case ESE: return WNW;
            case SE: return NW;
            case SSE: return NNW;
            case S: return N;
            case SSW: return NNE;
            case SW: return NE;
            case WSW: return ENE;
            case W: return E;
            case WNW: return ESE;
            case NW: return SE;
            case NNW: return SSE;
            default: return null;
        }
    }

    public double getAngleAsDegrees() {
        return theta;
    }
    public static Double getAngleAsDegrees(CompassDirection direction) {
        if(direction == null) {
            return null;
        }
        return direction.getAngleAsDegrees();
    }
}
