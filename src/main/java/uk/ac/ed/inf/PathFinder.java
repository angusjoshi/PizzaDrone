package uk.ac.ed.inf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class PathFinder {
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private HashSet<LngLat> seenBefore;
    private HashMap<Restaurant, Move[]> pathsAlreadyComputed;
    public PathFinder() {
        seenBefore = new HashSet<>();
        pathsAlreadyComputed = new HashMap<>();
    }
    public Move[] findPathToRestaurant(Restaurant restaurant) {
        if(pathsAlreadyComputed.containsKey(restaurant)) {
            return pathsAlreadyComputed.get(restaurant);
        }
        return findPath(APPLETON_TOWER, restaurant.getLngLat());
    }
    public Move[] findPath(LngLat source, LngLat destination) {
        //search for each compass direction
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

