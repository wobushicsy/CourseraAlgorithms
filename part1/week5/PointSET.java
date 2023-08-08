import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private final Set<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("you cannot call insert() by pass a null pointer");
        }
        if (pointSet.contains(point)) {
            return;
        }
        pointSet.add(point);
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("you cannot call contains() by pass a null pointer");
        }
        return pointSet.contains(point);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point: pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("you cannot call range() by pass a null pointer");
        }
        LinkedList<Point2D> list = new LinkedList<>();
        for (Point2D point: pointSet) {
            if (rect.contains(point)) {
                list.add(point);
            }
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("you cannot call nearest() by pass a null pointer");
        }
        Point2D nearestPoint = null;
        for (Point2D p: pointSet) {
            if (nearestPoint == null) {
                nearestPoint = p;
                continue;
            }
            if (point.distanceSquaredTo(p) < point.distanceSquaredTo(nearestPoint)) {
                nearestPoint = p;
            }
        }

        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();

        // insert test
        pointSet.insert(new Point2D(0.7, 0.2));
        pointSet.insert(new Point2D(0.5, 0.4));
        pointSet.insert(new Point2D(0.2, 0.3));
        pointSet.insert(new Point2D(0.4, 0.7));
        pointSet.insert(new Point2D(0.9, 0.6));


        // isEmpty test
        StdOut.println(pointSet.isEmpty());
        StdOut.println();


        // size test
        StdOut.println(pointSet.size());
        StdOut.println();


        // contains test
        StdOut.println(pointSet.contains(new Point2D(0.1, 0.1)));
        StdOut.println(pointSet.contains(new Point2D(0.7, 0.2)));
        StdOut.println();


        // range test
        for (Point2D point: pointSet.range(new RectHV(0.45, 0.1, 0.75, 0.45))) {
            StdOut.println(point);
        }
        StdOut.println();


        // nearest test
        StdOut.println(pointSet.nearest(new Point2D(0.55, 0.4)));
        StdOut.println((new PointSET()).nearest(new Point2D(0.0, 0.0)));


    }
}
