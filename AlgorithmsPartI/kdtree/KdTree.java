import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final int LEFT = 1;
    private static final int RIGHT = -1;
    private Node root;
    private int size;


    private class Node {
        Point2D point2d;
        RectHV rectHV;
        Node leftNode;
        Node rigthtNode;
        int depth;

        public Node(Point2D point2d, RectHV rectHV, int depth) {
            this.point2d = point2d;
            this.rectHV = rectHV;
            this.depth = depth;
        }
    }


    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();

        if (contains(p)) return;
        root = insert(root, null, p);
        size++;
    }

    private Node insert(Node insertNode, Node perNode, Point2D p) {
        if (insertNode == null) {
            if (size == 0) return new Node(p, new RectHV(0, 0, 1, 1), 1);

            else {
                int cmp = compare(perNode, p);
                RectHV rectHV = null;

                if (perNode.depth % 2 == 0) {
                    if (cmp == LEFT)
                        rectHV = new RectHV(perNode.rectHV.xmin(), perNode.rectHV.ymin(),
                                            perNode.rectHV.xmax(), perNode.point2d.y());

                    else if (cmp == RIGHT)
                        rectHV = new RectHV(perNode.rectHV.xmin(), perNode.point2d.y(),
                                            perNode.rectHV.xmax(), perNode.rectHV.ymax());
                }
                else {
                    if (cmp == LEFT)
                        rectHV = new RectHV(perNode.rectHV.xmin(), perNode.rectHV.ymin(),
                                            perNode.point2d.x(), perNode.rectHV.ymax());

                    else if (cmp == RIGHT)
                        rectHV = new RectHV(perNode.point2d.x(), perNode.rectHV.ymin(),
                                            perNode.rectHV.xmax(), perNode.rectHV.ymax());
                }
                return new Node(p, rectHV, perNode.depth + 1);
            }


        }
        else {
            int cmp = compare(insertNode, p);

            if (cmp == LEFT)
                insertNode.leftNode = insert(insertNode.leftNode, insertNode, p);
            if (cmp == RIGHT)
                insertNode.rigthtNode = insert(insertNode.rigthtNode, insertNode, p);
            return insertNode;
        }
    }

    private int compare(Node node, Point2D p) {
        if (node == null || p == null)
            throw new java.lang.IllegalArgumentException();

        if (p.compareTo(node.point2d) == 0)
            return 0;

        if (node.depth % 2 != 0) {
            if (Double.compare(node.point2d.x(), p.x()) == 1)
                return LEFT;
            else
                return RIGHT;
        }
        else {
            if (Double.compare(node.point2d.y(), p.y()) == 1)
                return LEFT;
            else
                return RIGHT;
        }
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return containsP(p, root);
    }

    private boolean containsP(Point2D p, Node node) {
        if (node == null) return false;
        int cmp = compare(node, p);
        if (cmp == LEFT)
            return containsP(p, node.leftNode);
        if (cmp == RIGHT)
            return containsP(p, node.rigthtNode);
        return true;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        draw(x.leftNode);
        draw(x.rigthtNode);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point2d.draw();
        StdDraw.setPenRadius();
        if (x.depth % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point2d.x(), x.rectHV.ymin(), x.point2d.x(), x.rectHV.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rectHV.xmin(), x.point2d.y(), x.rectHV.xmax(), x.point2d.y());
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    private void range(Node currentNode, Queue<Point2D> queue, RectHV rectHV) {
        if (rectHV.contains(currentNode.point2d)) queue.enqueue(currentNode.point2d);

        if (currentNode.leftNode != null && currentNode.leftNode.rectHV.intersects(rectHV)) {
            range(currentNode.leftNode, queue, rectHV);
        }

        if (currentNode.rigthtNode != null && currentNode.rigthtNode.rectHV.intersects(rectHV)) {
            range(currentNode.rigthtNode, queue, rectHV);
        }

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException();

        Queue<Point2D> queue = new Queue<>();
        if (root == null) {
            return queue;
        }
        range(root, queue, rect);

        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    private Node nearest(Node currentNode, Node nearestNode, Point2D p) {
        if (currentNode == null)
            return nearestNode;

        double nearstDistance = Double.POSITIVE_INFINITY;
        double currentDistance = p.distanceSquaredTo(currentNode.point2d);

        if (nearestNode != null) {
            nearstDistance = p.distanceSquaredTo(nearestNode.point2d);
        }
        else {
            nearstDistance = currentDistance;
            nearestNode = currentNode;
        }

        if (currentDistance < nearstDistance) {
            nearestNode = currentNode;
            nearstDistance = currentDistance;
        }

        int cmp = compare(currentNode, p);

        if (cmp == LEFT) {
            nearestNode = nearest(currentNode.leftNode, nearestNode, p);

            if (currentNode.rigthtNode != null &&
                    currentNode.rigthtNode.rectHV.distanceSquaredTo(p) < p.distanceSquaredTo(
                            nearestNode.point2d))
                nearestNode = nearest(currentNode.rigthtNode, nearestNode, p);
        }
        else if (cmp == RIGHT) {
            nearestNode = nearest(currentNode.rigthtNode, nearestNode, p);

            if (currentNode.leftNode != null &&
                    currentNode.leftNode.rectHV.distanceSquaredTo(p) < p.distanceSquaredTo(
                            nearestNode.point2d))
                nearestNode = nearest(currentNode.leftNode, nearestNode, p);
        }
        return nearestNode;
    }

    public Point2D nearest(Point2D p) {
        if (root == null)
            return null;

        return nearest(root, null, p).point2d;
    }

}
