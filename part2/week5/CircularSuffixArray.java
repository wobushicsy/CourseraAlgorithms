import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private final int length;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        // corner case check
        if (s == null) {
            throw new IllegalArgumentException("Illegal command line argument");
        }

        // initialize variables
        length = s.length();
        index = new int[length];
        if (length == 0) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(s);
        String[] strings = new String[length];
        strings[0] = s;
        Str[] strs = new Str[length];
        strs[0] = new Str(s, 0);

        for (int i = 1; i < length; i += 1) {
            char firstChar = stringBuilder.charAt(0);
            stringBuilder.append(firstChar);
            stringBuilder.deleteCharAt(0);
            strings[i] = stringBuilder.toString();
            strs[i] = new Str(strings[i], i);
        }

        Arrays.sort(strs);

        for (int i = 0; i < length; i += 1) {
            index[i] = strs[i].index;
        }
    }

    private static class Str implements Comparable<Str> {
        private final String string;
        private final int index;

        public Str(String s, int i) {
            string = s;
            index = i;
        }

        @Override
        public int compareTo(Str o) {
            return string.compareTo(o.string);
        }

    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) {
            throw new IllegalArgumentException("Index is out of bound");
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < circularSuffixArray.length(); i += 1) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }

}
