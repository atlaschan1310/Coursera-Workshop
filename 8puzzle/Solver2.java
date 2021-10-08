
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver2 {
    private SearchNode currentNode; //input
    private SearchNode twinCurrentNode;  //tuning
    private Stack<Board> solution;  //output

    public class SearchNode implements Comparable<SearchNode> {
        public Board currentBoard;
        public SearchNode preSearchNode;
        public int moves;
        public int priority;

        public SearchNode(Board myCurrentBoard, SearchNode myPreSearchNode) {
            currentBoard = myCurrentBoard;
            preSearchNode = myPreSearchNode;
            if (myPreSearchNode == null) {
                moves = 0;
            }
            else {
                moves = myPreSearchNode.moves + 1;
            }
            priority = moves + currentBoard.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.priority, o.priority);
        }
    }



    // find a solution to the initial board (using the A* algorithm)
    public Solver2(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        StdOut.println(initial);
        currentNode = new SearchNode(initial, null);
        StdOut.println(initial);
        StdOut.println(initial.twin());
        twinCurrentNode = new SearchNode(initial.twin(), null);
        StdOut.println(initial);
        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<SearchNode>();
        StdOut.println("Secd");
        StdOut.println(currentNode.currentBoard);
        StdOut.println(twinCurrentNode.currentBoard);
        priorityQueue.insert(currentNode);
        twinPriorityQueue.insert(twinCurrentNode);
        StdOut.println("In constructor:");
        StdOut.println(currentNode.currentBoard);
        StdOut.println(twinCurrentNode.currentBoard);
        while (true) {
            currentNode = priorityQueue.delMin();
            if (currentNode.currentBoard.isGoal()) {
                break;
            }
            Iterable<Board> neighbors = currentNode.currentBoard.neighbors();
            for (Board neighbor : neighbors) {
                if (currentNode.preSearchNode == null ||
                        !neighbor.equals(currentNode.preSearchNode.currentBoard)) {
                    priorityQueue.insert(new SearchNode(neighbor, currentNode));
                }
            }
            twinCurrentNode = twinPriorityQueue.delMin();
            if (twinCurrentNode.currentBoard.isGoal()) {
                break;
            }
            Iterable<Board> twinNeighbors = twinCurrentNode.currentBoard.neighbors();
            for (Board twinNeighbor : twinNeighbors) {
                if (twinCurrentNode.preSearchNode == null ||
                        !twinNeighbor.equals(twinCurrentNode.preSearchNode.currentBoard)) {
                    twinPriorityQueue.insert(new SearchNode(twinNeighbor, twinCurrentNode));
                }
            }
        }
    }



    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return currentNode.currentBoard.isGoal();
    }

    // min number of moves to solve initial board
    public int moves() {
        if (currentNode.currentBoard.isGoal()) {
            return currentNode.moves;
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (currentNode.currentBoard.isGoal()) {
            solution = new Stack<Board>();
            SearchNode node = currentNode;
            while (node != null) {
                solution.push(node.currentBoard);
                node = node.preSearchNode;
            }
            return solution;
        }
        else {
            return null;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver2 solver = new Solver2(initial);

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