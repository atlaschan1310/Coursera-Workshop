/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int segNum = 0;
    private LineSegment[] lines;
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        Point[] pts = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            pts[i] = points[i];
        }
        if (isDuplicated(pts)) throw new IllegalArgumentException();
        Arrays.sort(pts);
        LineSegment[] temp = new LineSegment[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j ++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (Double.compare(pts[i].slopeTo(pts[j]), pts[k].slopeTo(pts[j])) == 0
                        && Double.compare(pts[j].slopeTo(pts[k]), pts[l].slopeTo(pts[k])) == 0) {
                            temp[segNum++] = new LineSegment(pts[i], pts[l]);
                        }
                    }
                }
            }
        }
        lines = Arrays.copyOf(temp, segNum);
        temp = null;
    }

    public int numberOfSegments() {
        return segNum;
    }

    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[segNum];
        for (int i = 0; i < segNum; i++) {
            result[i] = lines[i];
        }
        return result;
    }

    private boolean isDuplicated(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) return true;
        }
        return false;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
    }

