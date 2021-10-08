/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D>  points;
    public PointSET() {
        points = new SET<Point2D>();
    }
    public boolean isEmpty() {
        return points.isEmpty();
    }
    public int size() {
        return points.size();
    }
    public void insert(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        points.add(point);
    }
    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        return points.contains(point);
    }
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                queue.enqueue(point);
            }
        }
        return queue;
    }

    public Point2D nearest (Point2D that_point) {
        if (that_point == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D ret = null;
        double min_dis = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            double cur_dis = point.distanceSquaredTo(that_point);
            if (cur_dis < min_dis) {
                ret = point;
                min_dis = cur_dis;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(Double.compare(0.2, 0.3));
        PointSET pointSET = new PointSET();
        Point2D [] point2ds = new Point2D[8];
        for (int i = 0; i < point2ds.length; i++) {
            point2ds[i] = new Point2D(i/10.0, (i+1)/10.0);
            pointSET.insert(point2ds[i]);
        }

        System.out.println(pointSET.size());

        System.out.println(pointSET.contains(new Point2D(0.3, 0.4)));
        System.out.println(pointSET.nearest(new Point2D(0.3, 0.6)));
        RectHV rectHV = new RectHV(0.2, 0.2, 0.6, 0.9);
        Iterable<Point2D> pQueue = pointSET.range(rectHV);

        for (Point2D point2d : pQueue) {
            System.out.println(point2d);
        }

    }
}
