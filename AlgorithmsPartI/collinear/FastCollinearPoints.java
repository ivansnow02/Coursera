import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) {
        isLegal(points);
        Point[] copyOfPoints = Arrays.copyOf(points, points.length);
        lineSegments = new ArrayList<>();
        Arrays.sort(copyOfPoints);
        for (int i = 0; i < copyOfPoints.length - 3; i++) {
            Point point0 = points[i];
            double[] prevSlopes = new double[i];
            Point[] postPoints = new Point[copyOfPoints.length - i - 1];
            for (int j = 0; j < i; j++) {
                prevSlopes[j] = point0.slopeTo(copyOfPoints[j]);
            }
            for (int j = 0; j < copyOfPoints.length - i - 1; j++) {
                postPoints[j] = copyOfPoints[i + j + 1];
            }
            Arrays.sort(prevSlopes);
            Arrays.sort(postPoints, point0.slopeOrder());
            findLineSegments(prevSlopes, point0, postPoints);
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[numberOfSegments()]);
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

    private void isLegal(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

    }


    private boolean isSubLine(double tempSlope, double[] beforeSlope) {
        return Arrays.binarySearch(beforeSlope, tempSlope) >= 0;
    }

    private void findLineSegments(double[] prevSlopes, Point point0, Point[] postPoints) {
        double currentSlope;
        double beforeSlope = Double.NEGATIVE_INFINITY;
        int cnt = 1;

        for (int i = 0; i < postPoints.length; i++) {
            currentSlope = point0.slopeTo(postPoints[i]);
            if (beforeSlope != currentSlope) {
                if (cnt >= 3 && !isSubLine(beforeSlope, prevSlopes)) {
                    lineSegments.add(new LineSegment(point0, postPoints[i - 1]));
                }
                cnt = 1;
            }
            else {
                cnt++;
            }
            beforeSlope = currentSlope;
        }
        if (cnt >= 3 && !isSubLine(beforeSlope, prevSlopes)) {
            lineSegments.add(new LineSegment(point0, postPoints[postPoints.length - 1]));
        }
    }


}
