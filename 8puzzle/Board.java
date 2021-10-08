/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Board {
    private int[][]  board;
    private int dim;

    public Board(int[][] tiles) {
        dim = tiles.length;
        board = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0 ; j < dim; j++) {
                s.append(String.format("%2d", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        int dis = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != 0 && board[i][j] != i * dim + j + 1)
                    dis++;
            }
        }
        return dis;
    }

    public int manhattan() {
        int dis = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != 0) {
                    int cur_val = board[i][j] - 1;
                    int tar_row = cur_val / dim;
                    int tar_col = cur_val % dim;
                    dis += Math.abs(i - tar_row) + Math.abs(j - tar_col);
                }
            }
        }
        return dis;
    }

    public boolean isGoal () {
        return (this.hamming() == 0);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board target = (Board) y;
        if (target.dim != dim) return false;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != target.board[i][j]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int zeroRow = -1;
        int zeroCol = -1;
        int [][] clone = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
                clone[i][j] = board[i][j];
            }
        }
        Stack<Board> neighbors = new Stack<Board>();
        if (zeroRow > 0) {
            clone[zeroRow][zeroCol] = clone[zeroRow - 1][zeroCol];
            clone[zeroRow - 1][zeroCol] = 0;
            neighbors.push(new Board(clone));
            clone[zeroRow - 1][zeroCol] = clone[zeroRow][zeroCol];
            clone[zeroRow][zeroCol] = 0;
        }
        if (zeroRow < dim - 1) {
            clone[zeroRow][zeroCol] = clone[zeroRow + 1][zeroCol];
            clone[zeroRow + 1][zeroCol] = 0;
            neighbors.push(new Board(clone));
            clone[zeroRow + 1][zeroCol] = clone[zeroRow][zeroCol];
            clone[zeroRow][zeroCol] = 0;
        }
        if (zeroCol > 0) {
            clone[zeroRow][zeroCol] = clone[zeroRow][zeroCol - 1];
            clone[zeroRow][zeroCol - 1] = 0;
            neighbors.push(new Board(clone));
            clone[zeroRow][zeroCol - 1] = clone[zeroRow][zeroCol];
            clone[zeroRow][zeroCol] = 0;
        }
        if (zeroCol < dim - 1) {
            clone[zeroRow][zeroCol] = clone[zeroRow][zeroCol + 1];
            clone[zeroRow][zeroCol + 1] = 0;
            neighbors.push(new Board(clone));
            clone[zeroRow][zeroCol + 1] = clone[zeroRow][zeroCol];
            clone[zeroRow][zeroCol] = 0;
        }
        return neighbors;
    }

    public Board twin() {
        Board twin = new Board(board);
        int row = 0, col = 0;
        if (twin.board[0][0] == 0) col = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (twin.board[i][j] != twin.board[row][col] && twin.board[i][j] != 0) {
                    int temp = twin.board[i][j];
                    twin.board[i][j] = twin.board[row][col];
                    twin.board[row][col] = temp;
                    return twin;
                }
            }
        }
        return twin;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for(int i = 0; i < n; i ++) {
            for(int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);

        StdOut.println(board.toString());
        StdOut.println(board.dimension());
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
        StdOut.println(board.isGoal());
        StdOut.println(board.twin());
    }
}
