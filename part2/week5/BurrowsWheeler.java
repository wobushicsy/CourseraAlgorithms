import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.ArrayList;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String inputString = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(inputString);
        StringBuilder outputStringBuilder = new StringBuilder();
        int length = circularSuffixArray.length();

        for (int i = 0; i < length; i += 1) {
            int index = circularSuffixArray.index(i);
            if (index == 0) {
                BinaryStdOut.write(i);
            }
            outputStringBuilder.append(inputString.charAt(index == 0 ? length - 1 : index - 1));
        }
        BinaryStdOut.write(outputStringBuilder.toString());

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        ArrayList<Character> t = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            t.add(BinaryStdIn.readChar());
        }
        char[] firstColumn = new char[t.size()];
        int[] next = new int[t.size()];
        for (int i = 0; i < t.size(); i++) {
            firstColumn[i] = t.get(i);
        }
        Arrays.sort(firstColumn);
        for (int i = 0; i < t.size(); ) {
            int j = i + 1;
            while (j < t.size() && firstColumn[j] == firstColumn[j - 1]) j++;
            for (int k = 0; k < t.size(); k++) {
                if (t.get(k) == firstColumn[i]) {
                    next[i++] = k;
                    if (i == j) break;
                }
            }
        }
        int pointer = first;
        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(firstColumn[pointer]);
            pointer = next[pointer];
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Illegal command line argument");
        }
        if (args[0].equals("-"))            transform();
        else if (args[0].equals("+"))       inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}