package uk.ac.ed.inf.pathing;

import uk.ac.ed.inf.areas.CentralArea;
import uk.ac.ed.inf.areas.LngLat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uk.ac.ed.inf.areas.LngLat.STEP_LENGTH;

/**
 * Class to be used in conjunction with PathFinder. Each SearchNode instance represents a node in the
 * pathfinding search.
 */
public class SearchNode implements Comparable<SearchNode> {
    private final CompassDirection prevDirection;
    private final double searchWeight;
    private final LngLat location;
    private final double pathLength;
    private final int nSteps;
    private final SearchNode prevNode;

    private final boolean inCentral;

    /**
     * Basic constructor for SearchNode
     * @param location LngLat location of the node
     * @param pathLength length of the path computed up to this node
     * @param searchWeight computed heuristic path length from source to destination including this node
     * @param prevNode previous node in the search for this path
     * @param prevDirection direction taken from the prevNode to reach this one
     * @param nSteps number of steps in the path from the source to this node
     * @param inCentral if the point in this search node is in the central area
     */
    public SearchNode(LngLat location, double pathLength, double searchWeight, SearchNode prevNode,
                      CompassDirection prevDirection, int nSteps, boolean inCentral) {
        this.location = location;
        this.pathLength = pathLength;
        this.prevNode = prevNode;
        this.searchWeight = searchWeight;
        this.prevDirection = prevDirection;
        this.nSteps = nSteps;
        this.inCentral = inCentral;
    }


    /**
     * Get adjacent nodes to be used in search
     * @param destination to be used in computing the heuristic path length including the next node.
     * @return An array with exactly one element for each of the CompassDirections.
     */
    public List<SearchNode> getNextPotentialNodes(LngLat destination) {
        List<SearchNode> nodes = new ArrayList<>();
        CompassDirection[] directions = CompassDirection.values();
        CentralArea centralArea = CentralArea.getInstance();

        for (CompassDirection direction : directions) {
            LngLat nextLocation = this.location.nextPosition(direction);
            boolean nextInCentral = centralArea.isPointInside(nextLocation);

            if (reEnteringCentral(nextInCentral)) {
                continue;
            }

            double nextSearchWeight = pathLength + 1 + nextLocation.distanceTo(destination);

            SearchNode node = new SearchNode(nextLocation, pathLength + STEP_LENGTH,
                    nextSearchWeight, this, direction, this.nSteps + 1,
                    nextInCentral);

            nodes.add(node);
        }
        return nodes;
    }
    private boolean reEnteringCentral(boolean nextInCentral) {
        if(this.prevNode == null) {
            return false;
        }
        return !prevNode.inCentral && nextInCentral;
    }


    /**
     * Compares this to another SearchNode. This is to implement the A* search algorithm;
     * order is computed as a sum of the path length to the node plus a computed heuristic path distance to the
     * destination. At the moment, the heuristic is just euclidean distance.
     * @param other the object to be compared.
     * @return  -1 if this less than other, 0 if this == other, 1 if this greater than other
     */
    @Override
    public int compareTo(SearchNode other) {
        return (int) Math.signum(this.searchWeight - other.searchWeight);
    }

    /**
     * Gets the location of the point reached in this node of the search.
     * @return LngLat type representing the location of the point.
     */
    public LngLat getLocation() {
        return this.location;
    }

    /**
     * Check if the path found in the current search is too long to be viable for the service.
     * This is to be used to prevent the search running indefinitely.
     * @return True if the path is deemed to be too long to be viable
     */
    public boolean isSearchTooLong() {
        return pathLength > 2000;
    }

    /**
     * Convert the linked-list style search node list into an array of Move type. Reverses the list
     * to the correct direction in the process.
     * @return Array of moves in the correct order
     */
    public List<Move> toMoveList() {
        Move[] moves = new Move[nSteps];
        SearchNode currentNode = this;
        int counter = nSteps;
        while(counter > 0) {
            var prevNode = currentNode.prevNode;
            Move move = new Move(prevNode.getLocation(), currentNode.location,
                    currentNode.getPrevDirection(), CalculationTimer.getTicksSinceCalculationStarted());
            moves[counter - 1] = move;
            counter--;
            currentNode = prevNode;
        }
        return new ArrayList<>(Arrays.asList(moves));
    }

    private CompassDirection getPrevDirection() {
        return this.prevDirection;
    }
}

