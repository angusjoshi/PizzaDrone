package uk.ac.ed.inf.areas;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * General implementation of the IPolygon interface. Implements polygons as a collection of vertices,
 * ordered anticlockwise starting from the top-left. Analogous to how polygons are represented in the rest
 * server data.
 */
public class Polygon implements IPolygon {
    private IPolygon boundingRectangle;
    private LngLat outsidePolygon;
    private final LngLat[] vertices;

    /**
     * Constructs a polygon from a 2d array of doubles. Each coordinate is taken to be an array with size 2
     * @param coordinates array of coordinates. Each coordinate satisfies coordinate[0] = long, coordinate[1] = lat.
     *                    Order is taken to be anticlockwise from the top-left.
     */
    public Polygon(double[][] coordinates) {
        this.vertices = new LngLat[coordinates.length];
        for(int i = 0; i < coordinates.length; i++) {
            this.vertices[i] = new LngLat(coordinates[i][0], coordinates[i][1]);
        }

        processVertices();
    }

    /**
     * Constructs a polygon from an array of LngLat vertices
     * @param vertices Vertices of the polygon in anticlockwise order
     */
    public Polygon(LngLat[] vertices) {
        this.vertices = vertices;
        processVertices();
    }

    private void processVertices() {
        if(vertices.length == 0) {
            return;
        }
        var minLng = vertices[0].lng();
        var maxLng = vertices[0].lng();
        var minLat = vertices[0].lat();
        var maxLat = vertices[0].lat();

        for(var vertex : vertices) {
            minLng = min(minLng, vertex.lng());
            maxLng = max(maxLng, vertex.lng());
            minLat = min(minLat, vertex.lat());
            maxLat = max(maxLat, vertex.lat());
        }

        var boundingRectangleTopLeft = new LngLat(minLng, minLat);
        var boundingRectangleTopRight = new LngLat(maxLng, maxLat);

        this.boundingRectangle = new Rectangle(boundingRectangleTopLeft, boundingRectangleTopRight);

        this.outsidePolygon = new LngLat(maxLng + LngLat.STEP_LENGTH, minLat + ((maxLat - minLat) / 2));
    }

    /**
     * implementation of the IPolygon method to check for inclusion in the polygon
     * @param point point to be checked for inclusion in the polygon
     * @return true if point is inside the polygon, false otherwise.
     */
    @Override
    public boolean isPointInside(LngLat point) {
        if(!boundingRectangle.isPointInside(point)) {
            return false;
        }

        //draw a line from the point to a point we know is outside the polygon
        //count the number of intersections of this line with edges of the polygon.
        int edgeIntersections = 0;
        for(int i = 0; i < vertices.length - 1; i++) {
            var vertexOne = vertices[i];
            var vertexTwo = vertices[i + 1];
            if(LngLat.lineSegmentsIntersect(outsidePolygon, point,  vertexOne, vertexTwo)) {
                edgeIntersections++;
            }
        }

        //odd number of intersections iff point is inside this polygon.
        return edgeIntersections % 2 == 1;
    }


}
