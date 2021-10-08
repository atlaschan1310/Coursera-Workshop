import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph diagraph;

    public SAP(Digraph _map) {
        if (_map == null) throw new IllegalArgumentException();
        diagraph = new Digraph(_map);
    }

    private int[] SAP_length(int v, int w) {
        if (v < 0 || w < 0 || v >= diagraph.V() || w > diagraph.V()) {
            throw new IllegalArgumentException();
        }
        int min_dis = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths vSearch = new BreadthFirstDirectedPaths(diagraph, v);
        BreadthFirstDirectedPaths wSearch = new BreadthFirstDirectedPaths(diagraph, w);
        for (int i = 0; i < diagraph.V(); i++) {
            if (vSearch.hasPathTo(i) && wSearch.hasPathTo(i)) {
                int cur_dis = vSearch.distTo(i) + wSearch.distTo(i);
                if (cur_dis < min_dis) {
                    min_dis = cur_dis;
                    ancestor = i;
                }
            }
        }
        int[] ans = new int[2];
        if (min_dis == Integer.MAX_VALUE) {
            ans[0] = -1;
            ans[1] = -1;
        }
        else {
            ans[0] = min_dis;
            ans[1] = ancestor;
        }
        return ans;
    }

    public int length(int v ,int w) {
        return SAP_length(v, w)[0];
    }

    public int ancestor(int v, int w) {
        return SAP_length(v, w)[1];
    }

    private int[] SAP_length_It(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (int i : v) {
            if (i < 0 || i >= diagraph.V()) {
                throw new IllegalArgumentException();
            }
        }
        for (int i : w) {
            if (i < 0 || i >= diagraph.V()) {
                throw new IllegalArgumentException();
            }
        }
        int min_dis = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths vSearch = new BreadthFirstDirectedPaths(diagraph, v);
        BreadthFirstDirectedPaths wSearch = new BreadthFirstDirectedPaths(diagraph, w);
        for (int i = 0; i < diagraph.V(); i++) {
            if (vSearch.hasPathTo(i) && wSearch.hasPathTo(i)) {
                int cur_dis = vSearch.distTo(i) + wSearch.distTo(i);
                if (cur_dis < min_dis) {
                    min_dis = cur_dis;
                    ancestor = i;
                }
            }
        }
        int[] ans = new int[2];
        if (min_dis == Integer.MAX_VALUE) {
            ans[0] = -1;
            ans[1] = -1;
        }
        else {
            ans[0] = min_dis;
            ans[1] = ancestor;
        }
        return ans;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return SAP_length_It(v, w)[0];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
       return SAP_length_It(v, w)[1];
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
