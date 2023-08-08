import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.Queue;
import java.util.LinkedList;

public class KdTree {

    private static class KdTreeNode {
        private final Point2D point;
        private final boolean vertical;
        private KdTreeNode left;
        private KdTreeNode right;

        public KdTreeNode(Point2D p, boolean isVertical) {
            point = p;
            vertical = isVertical;
        }

    }

    private int size;
    private KdTreeNode root;

    public KdTree() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private KdTreeNode insertHelper(KdTreeNode node, Point2D point, int depth) {
        if (node == null) {
            return new KdTreeNode(point, depth % 2 == 0);
        }
        Point2D thisPoint = node.point;
        double cmp = node.vertical ? point.x() - thisPoint.x() : point.y() - thisPoint.y();
        if (cmp >= 0) {
            node.right = insertHelper(node.right, point, depth + 1);
        } else {
            node.left = insertHelper(node.left, point, depth + 1);
        }

        return node;
    }

    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("you cannot call insert() by pass a null pointer");
        }
        if (contains(point)) {
            return;
        }
        root = insertHelper(root, point, 0);
        size += 1;
    }

    private boolean containsHelper(KdTreeNode node, Point2D point) {
        if (node == null) {
            return false;
        }
        Point2D thisPoint = node.point;
        if (thisPoint.equals(point)) {
            return true;
        }
        double cmp = node.vertical ? point.x() - thisPoint.x() : point.y() - thisPoint.y();
        if (cmp >= 0) {
            return containsHelper(node.right, point);
        } else {
            return containsHelper(node.left, point);
        }
    }

    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("you cannot call contains() by pass a null pointer");
        }

        return containsHelper(root, point);
    }

    public void draw() {
        if (isEmpty()) {
            return;
        }
        Queue<KdTreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            KdTreeNode node = queue.remove();
            node.point.draw();
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }

    private void rangeHelper(RectHV rect, KdTreeNode node, LinkedList<Point2D> list) {
        if (node == null) {
            return;
        }

        Point2D point = node.point;
        if (rect.contains(point)) {
            list.add(point);
        }

        boolean searchRight = node.vertical ? point.x() <= rect.xmax() : point.y() <= rect.ymax();
        boolean searchLeft  = node.vertical ? point.x() > rect.xmin() : point.y() > rect.ymin();

        if (searchRight) {
            rangeHelper(rect, node.right, list);
        }
        if (searchLeft) {
            rangeHelper(rect, node.left, list);
        }

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("you cannot call range() by pass a null pointer");
        }
        LinkedList<Point2D> list = new LinkedList<>();
        rangeHelper(rect, root, list);

        return list;
    }

    private double distanceBetween(Point2D point, KdTreeNode node) {
        Point2D thisPoint = node.point;
        return node.vertical ?
                Math.pow(Math.abs(point.x() - thisPoint.x()), 2) :
                Math.pow(Math.abs(point.y() - thisPoint.y()), 2);
    }

    private Point2D nearestHelper(KdTreeNode node, Point2D point, Point2D nearest) {
        if (node == null) {
            return nearest;
        }
        Point2D thisPoint = node.point;
        if (nearest == null || point.distanceSquaredTo(thisPoint) < point.distanceSquaredTo(nearest)) {
            nearest = thisPoint;
        }

        KdTreeNode goodSide, badSide;
        if (node.vertical ? point.x() > thisPoint.x() : point.y() > thisPoint.y()) {
            goodSide = node.right;
            badSide = node.left;
        } else {
            goodSide = node.left;
            badSide = node.right;
        }

        nearest = nearestHelper(goodSide, point, nearest);

        if (distanceBetween(point, node) < point.distanceSquaredTo(nearest)) {
            nearest = nearestHelper(badSide, point, nearest);
        }

        return nearest;
    }

    public Point2D nearest(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("you cannot call insert() by pass a null pointer");
        }
        return nearestHelper(root, point, null);
    }

    public static void main(String[] args) {
        KdTree pointSet = new KdTree();

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
