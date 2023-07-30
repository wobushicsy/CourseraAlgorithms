import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class Solver {

    private Board target;
    private boolean isSolvable;
    private int move;

    // board variables
    private final MinPQ<Board> pq;
    private final Map<Board, Board> parent;
    private final Map<Board, Integer> moves;
    private final HashSet<Board> searched;
    private Deque<Board> solution;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Cannot add a null to Solver constructor");
        }
        // init variables
        pq = new MinPQ<>(new pqComparator<>());
        parent = new HashMap<>();
        parent.put(initial, null);
        moves = new HashMap<>();
        moves.put(initial, 0);
        searched = new HashSet<>();
        move = -1;
        solution = null;

        // solve the problem
        astar(initial);

        // get moves and solution
        if (isSolvable) {
            move = moves.get(target);
            solution = new LinkedList<>();
            Board path = target;
            while (path != null) {
                solution.push(path);
                path = parent.get(path);
            }
        }
    }

    private class pqComparator<T extends Board> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            int dis1 = moves.get(o1) + o1.manhattan();
            int dis2 = moves.get(o2) + o2.manhattan();

            return dis1 - dis2;
        }
    }

    private void astar(Board state) {
        if (state.hamming() == 2) {
            if (state.twin().isGoal()) {
                isSolvable = false;
                return;
            }
        }
        if (state.isGoal()) {
            target = state;
            isSolvable = true;
            return;
        }

        // mark state as searched
        searched.add(state);

        // insert neighbors to priority queue
        for (Board neighbor: state.neighbors()) {
            if (searched.contains(neighbor)) {
                continue;
            }
            moves.put(neighbor, moves.get(state) + 1);
            pq.insert(neighbor);
            parent.put(neighbor, state);
        }

        Board minBoard = pq.delMin();

        astar(minBoard);
    }

    // is the initial board solvable? (see below)
    // we may infer that if the hamming of a board is 2, then the board is unsolvable
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return move;
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