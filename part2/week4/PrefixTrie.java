import edu.princeton.cs.algs4.StdOut;

public class PrefixTrie {

    private Node root;

    public PrefixTrie() {
        root = new Node();
    }

    private static class Node {
        private int value;
        private Node[] next;

        public Node() {
            value = -1;
            next = new Node[26];
        }
    }

    public void put(String s, int value) {
        int length = s.length();
        Node p = root;
        for (int i = 0; i < length; i += 1) {
            int index = s.charAt(i) - 'A';
            p.next[index] = p.next[index] == null ? new Node() : p.next[index];
            p = p.next[index];
        }

        p.value = value;
    }

    public int get(String s) {
        int length = s.length();
        Node p = root;
        for (int i = 0; i < length; i += 1) {
            int index = s.charAt(i) - 'A';
            p = p.next[index];
            if (p == null) {
                return -1;
            }
        }

        return p.value;
    }

    public boolean hasPrefixOf(String s) {
        int length = s.length();
        Node p = root;
        for (int i = 0; i < length; i += 1) {
            int index = s.charAt(i) - 'A';
            p = p.next[index];
            if (p == null) {
                return false;
            }
        }

        for (int i = 0; i < 26; i += 1) {
            if (p.next[i] != null) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        PrefixTrie prefixTrie = new PrefixTrie();
        prefixTrie.put("HELLO", 2);
        StdOut.println(prefixTrie.get("ASD"));
        StdOut.println(prefixTrie.hasPrefixOf("HELLO"));
    }
}
