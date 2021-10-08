/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private static class Node {
        Point2D point;
        RectHV ret;
        Node left;
        Node right;
        int depth;
        public Node (Point2D _point, RectHV _ret, int _depth) {
            point = _point;
            ret = _ret;
            left = null;
            right = null;
            depth = _depth;
        }
    }
    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
    private Node tree_insert(Node insertPlace, Node prevNode, Point2D thisPoint) {
        if (insertPlace == null) {
            if (isEmpty()) return new Node(thisPoint, new RectHV(0,0,1,1), 1);
            else {
                int cmp = compare(prevNode, thisPoint);
                RectHV rectHV = null;
                if (prevNode.depth % 2 == 0) {
                    if (cmp > 0) {
                        rectHV = new RectHV(prevNode.ret.xmin(), prevNode.ret.ymin(),
                                            prevNode.ret.xmax(), prevNode.point.y());
                    }
                    if (cmp < 0) {
                        rectHV = new RectHV(prevNode.ret.xmin(), prevNode.point.y(),
                                            prevNode.ret.xmax(), prevNode.ret.ymax());
                    }
                }
                else {
                    if (cmp > 0) {
                        rectHV = new RectHV(prevNode.ret.xmin(), prevNode.ret.ymin(),
                                            prevNode.point.x(), prevNode.ret.ymax());
                    }
                    if (cmp < 0) {
                        rectHV = new RectHV(prevNode.point.x(), prevNode.ret.ymin(),
                                            prevNode.ret.xmax(), prevNode.ret.ymax());
                    }
                }
                return new Node(thisPoint, rectHV, prevNode.depth + 1);
            }
        }
        else {
            int cmp = compare(insertPlace, thisPoint);
            if (cmp > 0) {
                insertPlace.left = tree_insert(insertPlace.left, insertPlace, thisPoint);
            }
            if (cmp < 0) {
                insertPlace.right = tree_insert(insertPlace.right, insertPlace, thisPoint);
            }
            return insertPlace;
        }
    }

    private int compare(Node pNode, Point2D point) {
        if (pNode == null) throw new IllegalArgumentException();
        if (point == null) throw new IllegalArgumentException();
        if (pNode.point.equals(point)) return 0;
        if (pNode.depth % 2 != 0) {
            if (Double.compare(pNode.point.x(), point.x()) == 1) {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            if (Double.compare(pNode.point.y(), point.y()) == 1) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
    public void insert(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        if (contains(point)) return;
        root = tree_insert(root, null, point);
        size++;
    }

    private void tree_draw(Node x) {
        if (x == null) return;
        tree_draw(x.left);
        tree_draw(x.right);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw();
        StdDraw.setPenRadius();
        if (x.depth % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point.x(), x.ret.ymin(), x.point.x(), x.ret.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.ret.xmin(), x.point.y(), x.ret.xmax(), x.point.y());
        }
    }

    public void draw() {
        tree_draw(root);
    }


    private boolean tree_contains(Point2D point, Node cmpNode) {
        if (cmpNode == null) return false;
        int cmp = compare(cmpNode, point);
        if (cmp > 0) return tree_contains(point, cmpNode.left);
        if (cmp < 0) return tree_contains(point, cmpNode.right);
        return true;
    }

    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        return tree_contains(point, root);
    }

    private void tree_range(Node cur_node, Queue<Point2D> queue, RectHV rectHV) {
        if (rectHV.contains(cur_node.point)) queue.enqueue(cur_node.point);
        if (cur_node.left != null && cur_node.left.ret.intersects(rectHV)) {
            tree_range(cur_node.left, queue, rectHV);
        }
        if (cur_node.right != null && cur_node.right.ret.intersects(rectHV)) {
            tree_range(cur_node.right, queue, rectHV);
        }
    }

    public Iterable<Point2D> range(RectHV rectHV) {
        if (rectHV == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Queue<Point2D> queue = new Queue<Point2D>();
        tree_range(root, queue, rectHV);
        return queue;
    }

    private Node tree_nearest(Node cur_node, Node nearest_node, Point2D point) {
        if (cur_node == null) return nearest_node;
        double nearestDistance = Double.POSITIVE_INFINITY;
        double curDistance = point.distanceSquaredTo(cur_node.point);
        if (nearest_node != null) {
            nearestDistance = point.distanceSquaredTo(nearest_node.point);
        }
        else {
            nearest_node = cur_node;
            nearestDistance = curDistance;
        }
        if (curDistance < nearestDistance) {
            nearestDistance = curDistance;
            nearest_node = cur_node;
        }
        int cmp = compare(cur_node, point);
        if (cmp > 0) {
            nearest_node = tree_nearest(cur_node.left, nearest_node, point);
            if (cur_node.right != null &&
                    cur_node.right.ret.distanceSquaredTo(point) < point.distanceSquaredTo(nearest_node.point)) {
                nearest_node = tree_nearest(cur_node.right, nearest_node, point);
            }
        }
        if (cmp < 0) {
            nearest_node = tree_nearest(cur_node.right, nearest_node, point);
            if (cur_node.left != null &&
            cur_node.left.ret.distanceSquaredTo(point) < point.distanceSquaredTo(nearest_node.point)) {
                nearest_node = tree_nearest(cur_node.left, nearest_node, point);
            }
        }
        return nearest_node;
    }

    public Point2D nearest(Point2D point) {
        if (root == null) return null;
        return tree_nearest(root, null, point).point;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.25, 0.8125));
        tree.insert(new Point2D(0.0625, 0.75));
        tree.insert(new Point2D(0.8125, 0.375));
        RectHV ret = new RectHV(0.5, 0.3125, 1.0, 0.625);
        Iterable<Point2D> queue = tree.range(ret);
        for (Point2D point : queue) {
            System.out.println(point);
        }

    }
}
