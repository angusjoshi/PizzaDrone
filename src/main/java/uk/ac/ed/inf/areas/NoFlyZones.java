package uk.ac.ed.inf.areas;

import uk.ac.ed.inf.restutils.RestClient;

public class NoFlyZones {
    private static NoFlyZones instance;
    private final NoFlyZone[] noFlyZones;

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

    public boolean pointIsInNoFlyZone(LngLat point) {
        for(var noFlyZone : noFlyZones) {
            if(noFlyZone.isPointInside(point)) {
                return true;
            }
        }
        return false;
    }

}
