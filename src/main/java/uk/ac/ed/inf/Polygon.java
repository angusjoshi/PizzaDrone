package uk.ac.ed.inf;

import java.util.HashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Polygon implements IPolygon {
    private IPolygon boundingRectangle;
    private LngLat outsidePolygon;
    private LngLat[] vertices;
    private HashMap<LngLat, Boolean> checkedPoints;

    public Polygon(LngLat[] vertices) {
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

        this.outsidePolygon = new LngLat(maxLng + 0.005, (maxLat - minLat) / 2);

        this.checkedPoints = new HashMap<>();
        this.vertices = vertices;
    }

    @Override
    public boolean isPointInside(LngLat point) {
        if(!boundingRectangle.isPointInside(point)) {
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
        return pointIsInside;
    }


}
