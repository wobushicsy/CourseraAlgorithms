import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String winner = null;
        String tmp;
        int p = 1;
        while (!StdIn.isEmpty()) {
            tmp = StdIn.readString();
            if (StdRandom.bernoulli(1.0 / p)) {
                winner = tmp;
            }
            p += 1;
        }
        StdOut.println(winner);
    }
}
