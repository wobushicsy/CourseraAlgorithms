import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("The constructor of BruteCollinearPoints is given a null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Item in Points is a null");
            }
        }
        int N = points.length;
        for (int i = 0; i < N - 1; i += 1) {
            for (int j = i + 1; j < N; j += 1) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("The argument to the constructor contains a repeated point");
                }
            }
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        LinkedList<LineSegment> list = new LinkedList<>();

        for (int i = 0; i < N - 3; i += 1) {
            Point p0 = sortedPoints[i];
            for (int j = i + 1; j < N - 2; j += 1) {
                Point p1 = sortedPoints[j];
                double slope01 = p0.slopeTo(p1);
                for (int k = j + 1; k < N - 1; k += 1) {
                    Point p2 = sortedPoints[k];
                    double slope02 = p0.slopeTo(p2);
                    if (slope02 != slope01)
                        break;
                    for (int l = k + 1; l < N; l += 1) {
                        Point p3 = sortedPoints[l];
                        double slope03 = p0.slopeTo(p3);
                        if (slope01 != slope03)
                            break;
                        list.add(new LineSegment(p0, p3));
                    }
                }
            }
        }

        lineSegments = list.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
