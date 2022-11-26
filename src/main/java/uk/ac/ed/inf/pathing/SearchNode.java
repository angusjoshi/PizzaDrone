package uk.ac.ed.inf.pathing;

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
    private final String orderNo;

    /**
     * Basic constructor for SearchNode
     * @param location LngLat location of the node
     * @param pathLength length of the path computed up to this node
     * @param searchWeight computed heuristic path length from source to destination including this node
     * @param prevNode previous node in the search for this path
     * @param prevDirection direction taken from the prevNode to reach this one
     * @param nSteps number of steps in the path from the source to this node
     */
    public SearchNode(LngLat location, double pathLength, double searchWeight, SearchNode prevNode,
                      CompassDirection prevDirection, int nSteps, String orderNo) {
        this.location = location;
        this.pathLength = pathLength;
        this.prevNode = prevNode;
        this.searchWeight = searchWeight;
        this.prevDirection = prevDirection;
        this.nSteps = nSteps;
        this.orderNo = orderNo;
    }


    /**
     * Get adjacent nodes to be used in search
     * @param destination to be used in computing the heuristic path length including the next node.
     * @return An array with exactly one element for each of the CompassDirections.
     */
    public SearchNode[] getNextPotentialNodes(LngLat destination) {
        SearchNode[] nodes = new SearchNode[16];
        CompassDirection[] directions = CompassDirection.values();

        for(int i = 0; i < directions.length; i++) {
            LngLat nextLocation = this.location.nextPosition(directions[i]);
            double nextSearchWeight = pathLength + 1 + nextLocation.distanceTo(destination);

            SearchNode node = new SearchNode(nextLocation, pathLength + STEP_LENGTH,
                    nextSearchWeight, this, directions[i], this.nSteps + 1, orderNo);

            nodes[i] = node;
        }
        return nodes;
    }


    /**
     * Compares this to another SearchNode. This is to implement the A* search algorithm;
     * order is computed as a sum of the path length to the node plus a computed heuristic path distance to the
     * destination. At the moment, the heuristic is just euclidean distance.
     * @param other the object to be compared.
     * @return  -1 if this < other, 0 if this == other, 1 if this > other
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
     * Gets the number of steps required to get to this node in the search
     * @return the number of steps as an int
     */
    public int getNSteps() {
        return nSteps;
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
            Move move = new Move(currentNode.getOrderNo(), prevNode.getLocation(), currentNode.location,
                    currentNode.getPrevDirection(), CalculationTimer.getTicksSinceCalculationStarted());
            moves[counter - 1] = move;
            counter--;
            currentNode = prevNode;
        }
        return new ArrayList<>(Arrays.asList(moves));
    }

    private String getOrderNo() {
        return orderNo;
    }

    private CompassDirection getPrevDirection() {
        return this.prevDirection;
    }
}

