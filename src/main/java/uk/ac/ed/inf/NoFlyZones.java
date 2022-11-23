package uk.ac.ed.inf;

import java.io.IOException;

public class NoFlyZones {
    private static NoFlyZones instance;
    private final NoFlyZone[] noFlyZones;

    public static NoFlyZones getInstance() {
        if(instance == null) {
            //TODO: improve error handling here
            try {
                instance = noFlyZonesFactory();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BadTestResponseException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    private NoFlyZones(NoFlyZone[] noFlyZones) {
        this.noFlyZones = noFlyZones;
    }
    private static NoFlyZones noFlyZonesFactory() throws IOException, BadTestResponseException {

        RestClient restClient = new RestClient(Constants.API_BASE);

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
