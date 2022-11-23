package uk.ac.ed.inf;

import java.util.HashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Polygon implements IPolygon {
    private IPolygon boundingRectangle;
    private LngLat outsidePolygon;
    private LngLat[] vertices;
    private HashMap<LngLat, Boolean> checkedPoints;

    public Polygon(double[][] coordinates) {
        this.vertices = new LngLat[coordinates.length];
        for(int i = 0; i < coordinates.length; i++) {
            this.vertices[i] = new LngLat(coordinates[i][0], coordinates[i][1]);
        }

        processVertices();

        this.checkedPoints = new HashMap<>();
    }

    public Polygon(LngLat[] vertices) {
        this.vertices = vertices;

        processVertices();

        this.checkedPoints = new HashMap<>();
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

    @Override
    public boolean isPointInside(LngLat point) {
        return boundingRectangle.isPointInside(point);
        /*if(!boundingRectangle.isPointInside(point)) {
            return false;
        }
        LngLat roundedPoint = point.roundToNearestStep();

        if(checkedPoints.containsKey(roundedPoint)) {
            return checkedPoints.get(roundedPoint);
        }

        //draw a line from the point to a point we know is outside the polygon
        //count the number of intersections of this line with edges of the polygon.
        int edgeIntersections = 0;
        for(int i = 0; i < vertices.length; i++) {
            var vertexOne = vertices[i];
            var vertexTwo = vertices[(i + 1) % vertices.length];
            if(LngLat.lineSegmentsIntersect(outsidePolygon, roundedPoint,  vertexOne, vertexTwo)) {
                edgeIntersections++;
            }
        }

        //odd number of intersections iff point is inside this polygon.
        boolean pointIsInside = edgeIntersections % 2 == 1;

        checkedPoints.put(roundedPoint, pointIsInside);
        return pointIsInside;*/
        //TODO: revisit polygon inclusion checking.
        //could be that bounded rectangle is best, or maybe even check for line intersections
        //between line seg formed by move and the edges (this would require significant redesign)
    }


}
