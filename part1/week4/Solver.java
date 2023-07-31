import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Comparator;

public class Solver {

    private boolean isSolvable;
    private int moves;
    private LinkedList<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Cannot add a null to Solver constructor");
        }

        // initialize variables
        MinPQ<SearchNode> searchNodePQ = new MinPQ<>(new SearchNodeComparator<>());
        SearchNode initialNode = new SearchNode(initial, 0, null);
        searchNodePQ.insert(initialNode);
        moves = -1;
        solution = new LinkedList<>();

        SearchNode last = null;

        while (true) {
            // A* search body
            SearchNode minNode = searchNodePQ.delMin();

            if (minNode.getState().isGoal()) {
                isSolvable = true;
                last = minNode;
                break;
            }

            Board currentBoard = minNode.getState();
            for (Board neighbor: currentBoard.neighbors()) {
                if (minNode.getParent() != null
                        && neighbor.equals(minNode.getParent().getState())) {
                    continue;
                }
                SearchNode neighborNode =
                        new SearchNode(neighbor, minNode.getMoves() + 1, minNode);
                searchNodePQ.insert(neighborNode);
            }
        }

        SearchNode pointer = last;

        while (pointer != null) {
            solution.push(pointer.getState());
            moves += 1;
            pointer = pointer.getParent();
        }
    }

    private static class SearchNode {

        private Board state;
        private int moves;
        private SearchNode parent;

        public SearchNode(Board state, int moves, SearchNode parent) {
            this.state = state;
            this.moves = moves;
            this.parent = parent;
        }

        public Board getState() {
            return state;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getParent() {
            return parent;
        }

    }

    private static class SearchNodeComparator<T extends SearchNode> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            Board board1 = o1.getState();
            Board board2 = o2.getState();
            int moves1 = o1.getMoves() + board1.manhattan();
            int moves2 = o2.getMoves() + board2.manhattan();

            return moves1 - moves2;
        }
    }

    // is the initial board solvable? (see below)
    // we may infer that if the hamming of a board is 2, then the board is unsolvable
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client
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