package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.pathing.PathFinder;
import uk.ac.ed.inf.restutils.BadTestResponseException;
import uk.ac.ed.inf.restutils.RestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PathFinderTest {
    @Test
    public void testFindPath() throws BadTestResponseException, IOException {
        PathFinder pathFinder = PathFinder.getInstance();
        LngLat at = new LngLat(	-3.1869, 	55.9445);
        LngLat civ = new LngLat(-3.1940, 	55.9439);

        RestClient.initialiseRestClient(Constants.API_BASE);
        RestClient restClient = RestClient.getInstance();

        var restaurants = restClient.getRestaurantsFromRestServer();
        var paths = Arrays.stream(restaurants).map(restaurant -> pathFinder.findPathToRestaurant(restaurant, "hehe")).collect(Collectors.toList());

        System.out.println("asdkljf;asldf");
    }

}
