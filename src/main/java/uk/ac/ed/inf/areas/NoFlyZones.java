package uk.ac.ed.inf.areas;

import uk.ac.ed.inf.restutils.RestClient;

/**
 * Singleton class storing all of the NoFlyZones retrieved from the rest sever
 */
public class NoFlyZones {
    private static NoFlyZones instance;
    private final NoFlyZone[] noFlyZones;

    /**
     * Lazily initialises the instance of NoFlyZones, and returns it
     * @return The NoFlyZones instance
     */
    public static NoFlyZones getInstance() {
        if(instance == null) {
            instance = noFlyZonesFactory();
        }
        return instance;
    }

    private NoFlyZones(NoFlyZone[] noFlyZones) {
        this.noFlyZones = noFlyZones;
    }
    private static NoFlyZones noFlyZonesFactory() {

        RestClient restClient = RestClient.getInstance();

        NoFlyZone[] noFlyZones = restClient.getNoFlyZonesFromRestServer();

        return new NoFlyZones(noFlyZones);
    }

    /**
     * Checks if a point is included in any of the NoFlyZones
     * @param point the point to be checked
     * @return true if the point is included in any of the NoFlyZones, false otherwise.
     */
    public boolean pointIsInNoFlyZone(LngLat point) {
        for(var noFlyZone : noFlyZones) {
            if(noFlyZone.isPointInside(point)) {
                return true;
            }
        }
        return false;
    }

}
