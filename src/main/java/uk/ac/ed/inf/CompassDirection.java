package uk.ac.ed.inf;

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

    CompassDirection(int nSixteenths){
        double theta = (Math.PI / 8) * nSixteenths;
        double x = Math.cos(theta);
        double y = Math.sin(theta);
        this.unitStep = new LngLat(x, y);
    }
    public LngLat getOffset(double stepLength) {
        return unitStep.mul(stepLength);
    }
}
