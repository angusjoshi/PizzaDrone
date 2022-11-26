package uk.ac.ed.inf;

import java.util.*;

/**
 * Class to find a path between appleton tower and a given restaurant.
 */
public class PathFinder {
    /**
     * The location of appleton tower, defined in the spec.
     */
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private static PathFinder instance;
    private HashSet<LngLat> seenBefore;
    private HashMap<Restaurant, List<Move>> pathsAlreadyComputed;

    /**
     * Basic initialiser for the pathfinder
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
     * @param restaurant
     * @return
     */
    public List<Move> findPathToRestaurant(Restaurant restaurant) {
        if(pathsAlreadyComputed.containsKey(restaurant)) {
            return pathsAlreadyComputed.get(restaurant);
        }

        var path = findPath(APPLETON_TOWER, restaurant.getLngLat());
        pathsAlreadyComputed.put(restaurant, path);
        return path;
    }
    protected List<Move> findPath(LngLat source, LngLat destination) {
        seenBefore.clear();
        CentralArea centralArea = CentralArea.getInstance();
        NoFlyZones noFlyZones = NoFlyZones.getInstance();

        PriorityQueue<SearchNode> nodes = new PriorityQueue<>();
        nodes.add(new SearchNode(source, 0, 0, null, null, 0));

        SearchNode finalSearchNode = null;

        while(true) {
            SearchNode currentNode = nodes.remove();

            if(currentNode.isSearchTooLong()) {
                return null;
            }
            if(currentNode.getLocation().closeTo(destination)) {
                finalSearchNode = currentNode;
                break;
            }
            SearchNode[] potentialNextNodes = currentNode.getNextPotentialNodes(destination);
            Arrays.sort(potentialNextNodes);
            for(var potentialNextNode : potentialNextNodes) {
                LngLat roundedLocation = potentialNextNode.getLocation().roundToNearestStep();
                if(seenBefore.contains(roundedLocation)) {
                    continue;
                }
                if(!centralArea.isPointInside(roundedLocation) || !noFlyZones.pointIsInNoFlyZone(roundedLocation)) {
                    nodes.add(potentialNextNode);
                    seenBefore.add(roundedLocation);
                }
            }
        }

        return finalSearchNode.toMoveArray();
    }

}

