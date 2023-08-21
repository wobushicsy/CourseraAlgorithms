import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static StringBuilder initializeStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 256; i += 1) {
            stringBuilder.append(ExtendedAscii.getAscii(i));
        }

        return stringBuilder;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        StringBuilder stringBuilder = initializeStringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = getIndex(stringBuilder, c);
            BinaryStdOut.write(index, 8);
            moveToFront(stringBuilder, index);
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        StringBuilder stringBuilder = initializeStringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readChar(8);
            int index = getIndex(stringBuilder, (char)c);
            BinaryStdOut.write(c, 8);
            moveToFront(stringBuilder, index);
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static int getIndex(StringBuilder stringBuilder, char c) {
        int index = -1;
        for (int i = 0; i < 256; i += 1) {
            if (stringBuilder.charAt(i) == c) {
                index = i;
                break;
            }
        }

        return index;
    }

    private static void moveToFront(StringBuilder stringBuilder, int index) {
        if (index >= stringBuilder.length() || index < 0) {
            return;
        }
        char c = stringBuilder.charAt(index);
        stringBuilder.deleteCharAt(index);
        stringBuilder.insert(0, c);
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Illegal command line argument");
        }
        initializeStringBuilder();
        if (args[0].equals("-"))            encode();
        else if (args[0].equals("+"))       decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
