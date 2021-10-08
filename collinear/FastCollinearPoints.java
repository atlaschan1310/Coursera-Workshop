/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class FastCollinearPoints {
    private int segNum;
    private LineSegment[] lines;

    public FastCollinearPoints(Point points[]) {
        if (points == null) throw new IllegalArgumentException();
        int len = points.length;
        Point[] copy1 = new Point[len];
        Point[] copy2 = new Point[len];
        LineSegment[] lineSegs = new LineSegment[len * len];
        copy(points, copy1);
        if (isDuplicated(copy1)) throw new IllegalArgumentException();
        Arrays.sort(copy1);
        copy(copy1, copy2);
        for (int i = 0; i < len; i++) {
            Arrays.sort(copy2, copy1[i].slopeOrder());
            Point max = copy2[0];
            Point min = copy2[0];
            int count = 1;
            for (int j = 1; j < len; j++) {
                if (Double.compare(copy1[i].slopeTo(copy2[j]), copy1[i].slopeTo(copy2[j - 1]))
                        == 0) {
                    if (copy2[j].compareTo(max) > 0) {
                        max = copy2[j];
                    }
                    else if (copy2[j].compareTo(min) < 0) {
                        min = copy2[j];
                    }
                    count++;
                    if (j == (len - 1) && count >= 3 && min.compareTo(copy1[i]) > 0) {
                        lineSegs[segNum++] = new LineSegment(copy1[i], max);
                    }
                }
                else {
                    if (count >= 3 && min.compareTo(copy1[i]) > 0) {
                        lineSegs[segNum++] = new LineSegment(copy1[i], max);
                    }
                    max = copy2[j];
                    min = copy2[j];
                    count = 1;
                }
            }
        }
        lines = Arrays.copyOf(lineSegs, segNum);
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

    private static void copy(Point[] origin, Point[] result) {
        if (origin == null) throw new IllegalArgumentException();
        int n = origin.length;
        for (int i = 0; i < n; i++) {
            if (origin[i] == null) throw new NoSuchElementException();
            result[i] = origin[i];
        }
    }

    private static boolean isDuplicated(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        int n = points.length;
        for (int i = 0; i < n - 1; i++) {
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        System.out.println(collinear.numberOfSegments());

    }
}
