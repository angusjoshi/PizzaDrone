package uk.ac.ed.inf;

import static uk.ac.ed.inf.LngLat.STEP_LENGTH;

public class SearchNode implements Comparable<SearchNode> {
    private final CompassDirection prevDirection;
    private double searchWeight;
    private LngLat location;
    private double pathLength;
    private int nSteps;
    private SearchNode prevNode;

    public SearchNode(LngLat location, double pathLength, double searchWeight, SearchNode prevNode,
                      CompassDirection prevDirection, int nSteps) {
        this.location = location;
        this.pathLength = pathLength;
        this.prevNode = prevNode;
        this.searchWeight = searchWeight;
        this.prevDirection = prevDirection;
        this.nSteps = nSteps;
    }


    public SearchNode[] getNextPotentialNodes(LngLat destination) {
        SearchNode[] nodes = new SearchNode[16];
        CompassDirection[] directions = CompassDirection.values();

        for(int i = 0; i < directions.length; i++) {
            LngLat nextLocation = this.location.nextPosition(directions[i]);
            double nextSearchWeight = pathLength + 1 + nextLocation.distanceTo(destination);

            SearchNode node = new SearchNode(nextLocation, pathLength + STEP_LENGTH,
                    nextSearchWeight, this, directions[i], this.nSteps + 1);

            nodes[i] = node;
        }
        return nodes;
    }


    @Override
    public int compareTo(SearchNode other) {
        return (int) Math.signum(this.searchWeight - other.searchWeight);
    }

    public LngLat getLocation() {
        return this.location;
    }
    public boolean isSearchTooLong() {
        return pathLength > 2000;
    }

    public int getNSteps() {
        return nSteps;
    }
    public SearchNode getPrevNode() {
        return prevNode;
    }
    public Move[] toMoveArray() {
        Move[] moves = new Move[nSteps + 1];
        int counter = nSteps;
        SearchNode currentNode = this;
        SearchNode prevNode = this.prevNode;
        while(nSteps > 0) {
            prevNode = currentNode.prevNode;
            Move move = new Move(prevNode.getLocation(), this.location, currentNode.getPrevDirection());
            moves[nSteps] = move;
            nSteps--;
            currentNode = prevNode;
        }
        return moves;
    }

    private CompassDirection getPrevDirection() {
        return this.prevDirection;
    }
}

