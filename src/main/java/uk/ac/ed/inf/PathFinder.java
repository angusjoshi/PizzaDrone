package uk.ac.ed.inf;

public class PathFinder {
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    //some type of path reversal would be nice

    //want to be able to find a path between appleton tower and a restaurant (can do path to a LngLat instead)

    //would be nice to have some way to remember paths already calculated.

    //maybe have this class working closely with CentralArea

    // can make another class called NoFlyZones, with a method to quickly determine if a point is
    //in a no fly zone?

    // either way it's important to be able to determine if points are in no fly zones quickly,
    // same with centralArea.

    // I'm thinking about storing some kind of matrix which represents the map split up into grids.
    // then calculate these props for each grid square. Finally having some quick way of mapping to these
    //grid squares from an arbitrary LngLat would work.

    //maybe split into STEP_LENGTH x STEP_LENGTH grid squares.
}

