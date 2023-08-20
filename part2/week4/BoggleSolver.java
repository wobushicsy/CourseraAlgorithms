import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {

    private final PrefixTrie prefixTrie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        prefixTrie = new PrefixTrie();
        for (String word : dictionary) {
            prefixTrie.put(word, score(word));
        }
    }

    private int score(String word) {
        int length = word.length();
        if (length < 3)         return 0;
        else if (length <= 4)   return 1;
        else if (length == 5)   return 2;
        else if (length == 6)   return 3;
        else if (length == 7)   return 5;
        else                    return 11;
    }

    private boolean onBoard(BoggleBoard board, int row, int col) {
        return row >= 0 && row < board.rows() && col >= 0 && col < board.cols();
    }

    private void dfs(BoggleBoard board, int row, int col, boolean[][] searched,
                            HashSet<String> validWords, String s) {
        searched[row][col] = true;
        char c = board.getLetter(row, col);
        String word;
        if (c == 'Q') {
            word = s + c + 'U';
        } else {
            word = s + c;
        }
        if (prefixTrie.get(word) > 0) {
            validWords.add(word);
        }
        if (!prefixTrie.hasPrefixOf(word)) {
            return;
        }
        for (int i = -1; i <= 1; i += 1) {
            for (int j = -1; j <= 1; j += 1) {
                int indexI = row + i;
                int indexJ = col + j;
                if (!onBoard(board, indexI, indexJ) || searched[indexI][indexJ]) {
                    continue;
                }
                boolean[][] newSearched = new boolean[board.rows()][board.cols()];
                for (int index = 0; index < newSearched.length; index += 1) {
                    System.arraycopy(searched[index], 0, newSearched[index], 0, board.cols());
                }
                dfs(board, indexI, indexJ, newSearched, validWords, word);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> validWords = new HashSet<>();
        int rows = board.rows();
        int cols = board.cols();
        for (int i = 0; i < rows; i += 1) {
            for (int j = 0; j < cols; j += 1) {
                dfs(board, i, j, new boolean[rows][cols], validWords, "");
            }
        }

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = prefixTrie.get(word);
        return score == -1 ? 0 : score;
    }

    public static void main(String[] args) {
//        args = new String[2];
//        args[0] = "dictionary-algs4.txt";
//        args[1] = "board4x4.txt";
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
