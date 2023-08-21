import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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

    private static String sortString(String input) {
        char[] tempArray = input.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        String inputString = BinaryStdIn.readString();
        int first = Integer.parseInt(inputString.substring(0, 4));
        String sortedSuffixLastCol = inputString.substring(4);
        String sortedSuffixFirstCol = sortString(sortedSuffixLastCol);
        int length = sortedSuffixLastCol.length();

        int[] next = new int[length];
        HashMap<Character, Integer> appearOnce = new HashMap<>();
        for (int i = 0; i < length; i += 1) {
            char c = sortedSuffixLastCol.charAt(i);
            if (appearOnce.containsKey(c)) {
                appearOnce.put(c, appearOnce.get(c) + 1);
            } else {
                appearOnce.put(c, 1);
            }
        }

        for (char key : appearOnce.keySet()) {
            if (appearOnce.get(key) != 1) {
                continue;
            }
            int firstIndex = 0;
            for (int i = 0; i < length; i += 1) {
                if (sortedSuffixFirstCol.charAt(i) == key) {

                }
            }
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