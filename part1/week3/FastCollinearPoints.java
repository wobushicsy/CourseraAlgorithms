import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
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
        LinkedList<Point> searchedPoints = new LinkedList<>();

        for (int i = 0; i < N; i += 1) {
            Point p = sortedPoints[i];
            if (searchedPoints.contains(p)) {
                continue;
            }
            Point[] slopeSortedPoints = sortedPoints.clone();
            Arrays.sort(slopeSortedPoints, p.slopeOrder());
            int cnt;

            // p.slopeTo(p) = Double.NEGATIVE_INFINITY, so we start by 1
            for (int j = 1; j < N; j += cnt) {
                cnt = 1;
                double slope = p.slopeTo(slopeSortedPoints[j]);
                for (int k = j + 1; k < N; k += 1) {
                    if (p.slopeTo(slopeSortedPoints[k]) == slope)
                        cnt += 1;
                    else
                        break;
                }
                if (cnt >= 3) {
                    Point[] tmp = new Point[cnt + 1];
                    for (int k = 0; k < cnt; k += 1) {
                        searchedPoints.add(slopeSortedPoints[j + k]);
                        tmp[k] = slopeSortedPoints[j + k];
                    }
                    tmp[cnt] = p;
                    searchedPoints.add(p);
                    Arrays.sort(tmp);
                    list.add(new LineSegment(tmp[0], tmp[cnt]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
