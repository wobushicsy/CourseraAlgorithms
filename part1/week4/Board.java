import edu.princeton.cs.algs4.StdOut;

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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(length).append("\n");
        for (int i = 0; i < length; i += 1) {
            stringBuilder.append(" ");
            for (int j = 0; j < length; j += 1) {
                stringBuilder.append(board[i][j]).append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
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
                int refI = val / length;
                int refJ = val % length - 1;
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

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        // toString() test
        StdOut.println("toString() test: ");
        Board board1 = new Board(tiles);
        StdOut.println(board1.toString());

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
        StdOut.println(board1.equals(new Board(tiles1)));

    }

}
