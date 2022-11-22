package uk.ac.ed.inf;

public record Move(String orderNo, double fromLongitude, double fromLatitude, double angle, double toLatitude,
                   double toLongitude, int ticksSinceStartOfCalculation) {
}
