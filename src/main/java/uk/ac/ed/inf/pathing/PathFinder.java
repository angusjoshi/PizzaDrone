package uk.ac.ed.inf.pathing;

import uk.ac.ed.inf.areas.LngLat;
import uk.ac.ed.inf.order.Restaurant;
import uk.ac.ed.inf.areas.CentralArea;
import uk.ac.ed.inf.areas.NoFlyZones;

import java.util.*;

/**
 * Singleton class storing the pathfinder mechanism
 */
public class PathFinder {
    /**
     * The location of appleton tower, defined in the spec.
     */
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private static PathFinder instance;
    private final HashSet<LngLat> seenBefore;
    private final HashMap<Restaurant, List<Move>> pathsAlreadyComputed;

    /**
     * Lazily initialises the pathfinder, and returns the instance
     * @return the pathfinder instance
     */
    public static PathFinder getInstance() {
        if(instance == null) {
            instance = new PathFinder();
        }
        return instance;
    }
    private PathFinder() {
        seenBefore = new HashSet<>();
        pathsAlreadyComputed = new HashMap<>();
    }

    /**
     * Finds a path from appleton tower to a given restaurant.
     * @param restaurant The restaurant to path to
     * @param orderNo The order number
     * @return The list of moves to a restaurant
     */
    public List<Move> findPathToRestaurant(Restaurant restaurant, String orderNo) {
        if(pathsAlreadyComputed.containsKey(restaurant)) {
            return pathsAlreadyComputed.get(restaurant);
        }

        var path = findPath(APPLETON_TOWER, restaurant.getLngLat(), orderNo);
        pathsAlreadyComputed.put(restaurant, path);
        return path;
    }

    private List<Move> findPath(LngLat source, LngLat destination, String orderNo) {
        seenBefore.clear();
        CentralArea centralArea = CentralArea.getInstance();
        NoFlyZones noFlyZones = NoFlyZones.getInstance();

        PriorityQueue<SearchNode> nodes = new PriorityQueue<>();
        nodes.add(new SearchNode(source, 0, 0, null, null, 0, orderNo));

        while(true) {
            SearchNode currentNode = nodes.remove();

            if(currentNode.isSearchTooLong()) {
                return null;
            }
            if(currentNode.getLocation().closeTo(destination)) {
                return currentNode.toMoveList();
            }
            SearchNode[] potentialNextNodes = currentNode.getNextPotentialNodes(destination);
            Arrays.sort(potentialNextNodes);
            for(var potentialNextNode : potentialNextNodes) {
                var roundedLocation = potentialNextNode.getLocation().roundToNearestStep();
                if(seenBefore.contains(roundedLocation)) {
                    continue;
                }
                var location = potentialNextNode.getLocation();
                if(!centralArea.isPointInside(location) || !noFlyZones.pointIsInNoFlyZone(location)) {
                    nodes.add(potentialNextNode);
                    seenBefore.add(roundedLocation);
                }
            }
        }
    }

}

