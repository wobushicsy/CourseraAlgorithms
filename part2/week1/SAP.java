import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class SAP {

    private Digraph graph;

    private void checkArgument(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("you can't pass a null to function");
        }
    }

    private void checkVertex(int vertex) {
        if (vertex >= graph.V()) {
            throw new IllegalArgumentException("Segmentation fault: vertex not included");
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkArgument(G);

        graph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkVertex(v);
        checkVertex(w);

        int ancestor = -1;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(v);
        Hashtable<Integer, Integer> distanceA = new Hashtable<>();
        distanceA.put(v, 0);

        // bfs add nounA's hyper
        while (!queue.isEmpty()) {
            int id = queue.remove();
            int dis = distanceA.get(id);
            for (int neighbor: graph.adj(id)) {
                if (distanceA.containsKey(neighbor)) {
                    continue;
                }
                distanceA.put(neighbor, dis + 1);
                queue.add(neighbor);
            }
        }

        queue.add(w);
        Hashtable<Integer, Integer> distanceB = new Hashtable<>();
        distanceB.put(w, 0);
        // search nounB's hyper
        while (!queue.isEmpty()) {
            int id = queue.remove();
            int dis = distanceB.get(id);
            if (distanceA.containsKey(id)) {
                ancestor = id;
                break;
            }
            for (int neighbor: graph.adj(id)) {
                if (distanceB.containsKey(neighbor)) {
                    continue;
                }
                distanceB.put(neighbor, dis + 1);
                queue.add(neighbor);
            }
        }

        if (ancestor == -1) {
            return -1;
        }

        int disV = distanceA.get(ancestor);
        int disW = distanceB.get(ancestor);

        return disV + disW;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkVertex(v);
        checkVertex(w);

        int ancestor = -1;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(v);
        Hashtable<Integer, Integer> distanceA = new Hashtable<>();
        distanceA.put(v, 0);

        // bfs add nounA's hyper
        while (!queue.isEmpty()) {
            int id = queue.remove();
            int dis = distanceA.get(id);
            for (int neighbor: graph.adj(id)) {
                if (distanceA.containsKey(neighbor)) {
                    continue;
                }
                distanceA.put(neighbor, dis + 1);
                queue.add(neighbor);
            }
        }

        queue.add(w);
        Hashtable<Integer, Integer> distanceB = new Hashtable<>();
        distanceB.put(w, 0);
        // search nounB's hyper
        while (!queue.isEmpty()) {
            int id = queue.remove();
            int dis = distanceB.get(id);
            if (distanceA.containsKey(id)) {
                ancestor = id;
                break;
            }
            for (int neighbor: graph.adj(id)) {
                if (distanceB.containsKey(neighbor)) {
                    continue;
                }
                distanceB.put(neighbor, dis + 1);
                queue.add(neighbor);
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkArgument(v);
        checkArgument(w);
        for (Integer i: v) {
            checkArgument(i);
            checkVertex(i);
        }
        for (Integer i: w) {
            checkArgument(i);
            checkVertex(i);
        }

        int length = Integer.MAX_VALUE;
        int len;
        boolean hasPath = false;

        for (int i: v) {
            for (int j: w) {
                len = length(i, j);
                if (len == -1) {
                    continue;
                }
                if (len < length) {
                    length = len;
                }
                hasPath = true;
            }
        }

        return hasPath ? length : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkArgument(v);
        checkArgument(w);
        for (Integer i: v) {
            checkArgument(i);
            checkVertex(i);
        }
        for (Integer i: w) {
            checkArgument(i);
            checkVertex(i);
        }

        int length = Integer.MAX_VALUE;
        int minI = 0;
        int minJ = 0;
        boolean hasAncestor = false;

        for (int i: v) {
            for (int j: w) {
                int len = length(i, j);
                if (len == -1) {
                    continue;
                }
                if (len < length) {
                    minI = i;
                    minJ = j;
                }
                hasAncestor = true;
            }
        }

        return hasAncestor ? ancestor(minI, minJ) : -1;
    }

    // do unit testing of this class
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

//        LinkedList<Integer> A = new LinkedList<>();
//        A.add(3);
//        A.add(9);
//        A.add(7);
//        A.add(1);
//        LinkedList<Integer> B = new LinkedList<>();
//        B.add(11);
//        B.add(12);
//        B.add(2);
//        B.add(6);
//        StdOut.printf("length = %d, ancestor = %d\n", sap.length(A, B), sap.ancestor(A, B));
    }
}
