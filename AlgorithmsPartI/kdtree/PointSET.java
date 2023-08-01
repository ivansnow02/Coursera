import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> point2DSET;

    // construct an empty set of points
    public PointSET() {
        point2DSET = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return point2DSET.isEmpty();
    }

    // number of points in the set
    public int size() {
        return point2DSET.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) point2DSET.add(p);

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return point2DSET.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : point2DSET) {
            StdDraw.point(p.x(), p.y());
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : point2DSET) {
            if (rect.contains(p)) queue.enqueue(p);
        }
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D q : point2DSET) {
            double distance = p.distanceSquaredTo(q);
            if (distance < minDistance) {
                nearest = q;
                minDistance = distance;
            }
        }
        return nearest;
    }

}