import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWorld {
    public static void main(String[] args) {
        String winner = null;
        String tmp = null;
        int p = 1;
        while (!StdIn.isEmpty()) {
            tmp = StdIn.readString();
            if (StdRandom.bernoulli(p)) {
                winner = tmp;
            }
        }
        StdOut.println(winner);
    }
}
