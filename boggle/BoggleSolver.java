/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    //Trie
    private class Trie {
        private final int R = 26;
        private class Node {
            private boolean hasWord = false;
            private Node[] next = new Node[R];
        }
        private Node root;

        public Node put(Node root, String key, int depth) {
            if (key == null) throw new IllegalArgumentException();
            if (root == null) root = new Node();
            if (depth == key.length()) {
                root.hasWord = true;
            }
            else {
                int c = key.charAt(depth);
                root.next[c - 'A'] = put(root.next[c - 'A'], key, depth + 1);
            }
            return root;
        }

        public Node get(Node root, String key, int depth) {
            if (key == null) throw new IllegalArgumentException();
            if (root == null) return null;
            if (depth == key.length()) return root;
            int c = key.charAt(depth);
            return get(root.next[c - 'A'], key, depth + 1 );
        }

        public boolean contains(String key) {
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.hasWord;
        }

        public boolean hasPrefix(String key) {
            if (key == null) throw new IllegalArgumentException();
            return get(root, key, 0) != null;
        }

        public void put(String key) {
            if (key == null) throw new IllegalArgumentException();
            root = put(root, key, 0);
        }
    }
    // BoggleSolver

    private Trie Tree = new Trie();
    private int row, column;
    private boolean marked[][];

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        for (int i = 0; i < dictionary.length; i++) {
            Tree.put(dictionary[i]);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> set = new HashSet<String>();
        row = board.rows();
        column = board.cols();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j ++) {
                marked = new boolean[row][column];
                dfs(set, board, i, j, "");
            }
        }
        return set;
    }

    private boolean is_Marked(int i, int j) {
        if (i < 0 || i >= row || j < 0 || j >= column) return true;
        return marked[i][j];
    }

    private void dfs(HashSet<String> set, BoggleBoard board, int i, int j, String word) {
        char c = board.getLetter(i, j);
        if (c == 'Q') word += "QU";
        else word += c;
        if (!Tree.hasPrefix(word)) return;
        marked[i][j] = true;
        if (word.length() > 2 && Tree.contains(word)){
            set.add(word);
        }
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                if (!is_Marked(i + dx, j + dy)) {
                    dfs(set, board, i + dx, j + dy, word);
                }
            }
        }
        marked[i][j] = false;
    }

    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (!Tree.contains(word)) return 0;
        int length  = word.length();
        if (length < 3) return 0;
        if (length  < 5) return 1;
        else if (length == 5) return 2;
        else if (length == 6) return 3;
        else if (length == 7) return 5;
        else return 11;
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
