/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode currentNode;
    private SearchNode twinNode;
    private Stack<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {
        public Board currentBoard;
        public SearchNode prevNode;
        public int moves;
        public int priority;

        public SearchNode (Board cur_Board, SearchNode prev_Search) {
            currentBoard = cur_Board;
            prevNode = prev_Search;
            if (prev_Search == null) {
                moves = 0;
            }
            else {
                moves = prev_Search.moves + 1;
                priority = moves + currentBoard.manhattan();
            }
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(priority, that.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        currentNode = new SearchNode(initial, null);
        twinNode = new SearchNode(initial.twin(), null);
        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<SearchNode>();
        priorityQueue.insert(currentNode);
        twinPriorityQueue.insert(twinNode);
        while (true) {
            currentNode = priorityQueue.delMin();
            if (currentNode.currentBoard.isGoal()) {
                break;
            }
            Iterable<Board> neighbors = currentNode.currentBoard.neighbors();
            for (Board neighbor : neighbors) {
                if (currentNode.prevNode == null ||
                        !neighbor.equals(currentNode.prevNode.currentBoard)) {
                    priorityQueue.insert(new SearchNode(neighbor, currentNode));
                }
            }
            twinNode = twinPriorityQueue.delMin();
            if (twinNode.currentBoard.isGoal()) {
                break;
            }
            Iterable<Board> twin_neighbors = twinNode.currentBoard.neighbors();
            for (Board twin_neibor : twin_neighbors) {
                if (twinNode.prevNode == null || !twin_neibor.equals(twinNode.prevNode.currentBoard)) {
                    twinPriorityQueue.insert(new SearchNode(twin_neibor, twinNode));
                }
            }
        }
    }

    public boolean isSolvable() {
        return currentNode.currentBoard.isGoal();
    }
    public int moves() {
        if (this.isSolvable()) {
            return currentNode.moves;
        }
        else {
            return -1;
        }
    }

    public Iterable<Board> solution() {
        if (this.isSolvable()) {
            solution = new Stack<Board>();
            SearchNode cur = currentNode;
            while(cur != null) {
                solution.push(cur.currentBoard);
                cur = cur.prevNode;
            }
            return solution;
        }
        else {
            return null;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
