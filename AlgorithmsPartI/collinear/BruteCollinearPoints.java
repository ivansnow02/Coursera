import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        isLegal(points);
        Point[] copyOfPoints = Arrays.copyOf(points, points.length);
        lineSegments = new ArrayList<>();
        Arrays.sort(copyOfPoints);
        for (int i = 0; i < copyOfPoints.length - 3; i++) {
            for (int j = i + 1; j < copyOfPoints.length - 2; j++) {
                for (int k = j + 1; k < copyOfPoints.length - 1; k++) {
                    if (!isCollinear(copyOfPoints, i, j, k)) {
                        continue;
                    }
                    for (int m = k + 1; m < copyOfPoints.length; m++) {
                        if (isCollinear(copyOfPoints, i, k, m)) {
                            lineSegments.add(new LineSegment(copyOfPoints[i], copyOfPoints[m]));
                        }
                    }
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[numberOfSegments()]);
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

    private boolean isCollinear(Point[] points, int i, int j, int k) {
        double slope1 = points[i].slopeTo(points[j]);
        double slope2 = points[i].slopeTo(points[k]);
        return slope1 == slope2;
    }

}
