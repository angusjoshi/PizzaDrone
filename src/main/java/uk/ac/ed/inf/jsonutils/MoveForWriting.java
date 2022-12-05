package uk.ac.ed.inf.jsonutils;

/**
 * A record for structuring the moves in the flightpath of the drone
 * @param orderNo The order number
 * @param fromLongitude Longitude before the move
 * @param fromLatitude Latitude before the move
 * @param angle Angle of direction of the move in degrees
 * @param toLongitude Longitude after the move
 * @param toLatitude Latitude after the move
 * @param ticksSinceStartOfCalculation Number of milliseconds past since the beginning of the pathfinding
 *                                     when the move was computed.
 */
public record MoveForWriting(
        String orderNo,
        double fromLongitude,
        double fromLatitude,
        Double angle,
        double toLongitude,
        double toLatitude,
        int ticksSinceStartOfCalculation) {
}
