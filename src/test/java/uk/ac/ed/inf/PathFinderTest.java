package uk.ac.ed.inf;

import org.junit.Test;

public class PathFinderTest {
    @Test
    public void testFindPath() {
        PathFinder pathFinder = new PathFinder();
        LngLat at = new LngLat(	-3.1869, 	55.9445);
        LngLat civ = new LngLat(-3.1940, 	55.9439);

        var x = pathFinder.findPath(at, civ);

        System.out.println("asdkljf;asldf");
    }

}
