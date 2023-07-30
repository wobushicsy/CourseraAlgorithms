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

    // test client (see below)
    public static void main(String[] args) {
        // solvable test
        int[][] solvableInitial = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board solvableInitialBoard = new Board(solvableInitial);
        Solver solvableSolver = new Solver(solvableInitialBoard);
        StdOut.println(solvableSolver.isSolvable);
        StdOut.println(solvableSolver.moves());
        for (Board path: solvableSolver.solution()) {
            StdOut.println(path.hamming());
        }

        StdOut.println("\n\n\n");

        // unsolvable test
        int[][] unsolvableInitial = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
        Board unsolvableInitialBoard = new Board(unsolvableInitial);
        Solver unsolvableSolver = new Solver(unsolvableInitialBoard);
        StdOut.println(unsolvableSolver.isSolvable);
        StdOut.println(unsolvableSolver.moves());
    }

}