import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Board {

    private final int length;
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        length = tiles.length;
        board = new int[length][length];
        for (int i = 0; i < length; i += 1) {
            for (int j = 0; j < length; j += 1) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = dimension();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return length;
    }

    // number of tiles out of place
    public int hamming() {
        int cnt = 0;
        int ref = 1;
        for (int i = 0; i < length; i += 1) {
            for (int j = 0; j < length; j += 1) {
                if (board[i][j] != ref) {
                    cnt += 1;
                }
                ref += 1;
            }
        }

        return cnt - 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int cnt = 0;
        int ref = 1;
        for (int i = 0; i < length; i += 1) {
            for (int j = 0; j < length; j += 1) {
                int val = board[i][j];
                if (val == ref++ || val == 0) {
                    continue;
                }
                int refI = (val - 1) / length;
                int refJ = (val - 1) % length;
                cnt += Math.abs(i - refI) + Math.abs(j - refJ);
            }
        }

        return cnt;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!this.getClass().equals(y.getClass())) {
            return false;
        }
        Board board1 = (Board) y;

        return this.toString().equals(board1.toString());
    }

    // Is the position on board?
    private boolean inBoard(int i, int j) {
        return i >= 0 && i < length && j >= 0 && j < length;
    }

    private int[][] copyBoard(int[][] srcBoard) {
        int[][] tiles = new int[length][length];
        for (int i = 0; i < length; i += 1) {
            System.arraycopy(srcBoard[i], 0, tiles[i], 0, length);
        }
        return tiles;
    }

    private Board getNeighbor(int[][] srcBoard, int pos0I, int pos0J, int exI, int exJ) {
        int[][] tiles = copyBoard(srcBoard);
        int tmp = tiles[pos0I][pos0J];
        tiles[pos0I][pos0J] = tiles[exI][exJ];
        tiles[exI][exJ] = tmp;

        return new Board(tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // return an array of integers to represent neighbors
        // e.g. neighbor of (1, 1) is 1, 3, 5, 7
        LinkedList<Board> neighbors = new LinkedList<>();

        // get the position of 0
        int posI = -1;
        int posJ = -1;
        for (int i = 0; i < length; i += 1) {
            for (int j = 0; j < length; j += 1) {
                if (board[i][j] != 0) {
                    continue;
                }
                posI = i;
                posJ = j;
            }
        }


        // exchange with up
        if (inBoard(posI + 1, posJ)) {
            neighbors.add(getNeighbor(board, posI, posJ, posI + 1, posJ));
        }
        // exchange with down
        if (inBoard(posI - 1, posJ)) {
            neighbors.add(getNeighbor(board, posI, posJ, posI - 1, posJ));
        }
        // exchange with right
        if (inBoard(posI, posJ + 1)) {
            neighbors.add(getNeighbor(board, posI, posJ, posI, posJ + 1));
        }
        // exchange with left
        if (inBoard(posI, posJ - 1)) {
            neighbors.add(getNeighbor(board, posI, posJ, posI, posJ - 1));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // if hamming() != 2, return a board with one row exchange 2 values
        // else, exchange two values on wrong position
        int[][] newBoard = copyBoard(board);
        if (hamming() != 2) {
            boolean changed = false;
            for (int i = 0; i < length; i += 1) {
                if (changed) {
                    break;
                }
                for (int j = 0; j < length - 1; j += 1) {
                    if (!(newBoard[i][j] > 0 && newBoard[i][j + 1] > 0)) {
                        continue;
                    }
                    int tmp = newBoard[i][j];
                    newBoard[i][j] = newBoard[i][j + 1];
                    newBoard[i][j + 1] = tmp;
                    changed = true;
                    break;
                }
            }
        } else {
            int posI1 = -1, posJ1 = -1;
            int posI2 = -1, posJ2 = -1;
            int ref = 1;
            for (int i = 0; i < length; i += 1) {
                for (int j = 0; j < length; j += 1) {
                    if (board[i][j] != ref && board[i][j] != 0) {
                        if (posI1 == -1) {
                            posI1 = i;
                            posJ1 = j;
                        } else {
                            posI2 = i;
                            posJ2 = j;
                        }
                    }
                    ref += 1;
                }
            }
            return getNeighbor(newBoard, posI1, posJ1, posI2, posJ2);

        }

        return new Board(newBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        // toString() test
        StdOut.println("toString() test: ");
        Board board1 = new Board(tiles);
        StdOut.println(board1.toString());

        // dimension() test
        StdOut.println("dimension() test: ");
        StdOut.println(board1.dimension());
        StdOut.println();

        // Hamming() test
        StdOut.println("Hamming() test: ");
        StdOut.println(board1.hamming());
        StdOut.println();

        // Manhattan() test
        StdOut.println("Manhattan() test: ");
        StdOut.println(board1.manhattan());
        StdOut.println();

        // isGoal() test
        StdOut.println("isGoal() test: ");
        StdOut.println(board1.isGoal());
        StdOut.println();

        // equals() test
        StdOut.println("equals() test: ");
        StdOut.println(board1.equals(board1));
        StdOut.println(board1.equals(new Board(tiles)));
        int[][] tiles1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board2 = new Board(tiles1);
        StdOut.println(board1.equals(board2));
        StdOut.println();

        // neighbors() test
        StdOut.println("neighbors() test: ");
        StdOut.println("board1: \n" + board1);
        for (Board neighbor: board1.neighbors()) {
            StdOut.println(neighbor);
        }
//        StdOut.println("board2: \n" + board2);
//        for (Board neighbor: board2.neighbors()) {
//            StdOut.println(neighbor);
//        }
        StdOut.println();

        // twin() test
        StdOut.println("twin() test: ");
        int[][] tiles2 = {{0, 2}, {1, 3}};
        Board board3 = new Board(tiles2);
        StdOut.println(board3.twin());
        StdOut.println();

        int[][] array0 = {{1, 0}, {2, 3}};
        Board b0 = new Board(array0);
        b0.manhattan();

    }

}
