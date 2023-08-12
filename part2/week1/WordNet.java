import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class WordNet {

    // a hashtable between synset and id
    private Hashtable<String, Integer> synset;
    private Hashtable<Integer, String> idset;

    // Digraph
    private Digraph graph;

    // SAP
    private SAP sap;


    private void checkArgument(Object s) {
        if (s == null) {
            throw new IllegalArgumentException("you can't pass a null to function");
        }
    }

    private void checkNoun(String s) {
        if (!isNoun(s)) {
            throw new IllegalArgumentException("you can't pass a not noun to function");
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkArgument(synsets);
        checkArgument(hypernyms);

        // initialize class variables
        synset = new Hashtable<>();
        idset = new Hashtable<>();

        // initialize local variables
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);
        String buffer;
        String[] splitedBuffer;
        int cnt = 0;

        // process synsets data
        while (!synsetsIn.isEmpty()) {
            // cannot assume a line only contains two commas
            cnt += 1;
            buffer = synsetsIn.readLine();
            splitedBuffer = buffer.split(",");

            // sp[0] = "{num}", sp[1] = {noun synset}, sp[2:] = {gloss}(not relevant)
            int id = Integer.parseInt(splitedBuffer[0]);
            synset.put(splitedBuffer[1], id);
            idset.put(id, splitedBuffer[1]);
        }

        // initialize hypernyms
        graph = new Digraph(cnt);

        // process hypernyms data
        while (!hypernymsIn.isEmpty()) {
            buffer = hypernymsIn.readLine();
            splitedBuffer = buffer.split(",");
            int index = Integer.parseInt(splitedBuffer[0]);
            for (int i = 1; i < splitedBuffer.length; i += 1) {
                graph.addEdge(index, Integer.parseInt(splitedBuffer[i]));
            }
        }

        // initialize SAP
        sap = new SAP(graph);

//        StdOut.println(synset.get("AND_circuit AND_gate"));
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synset.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkArgument(word);

        return synset.containsKey(word);
    }

    private int getId(String noun) {
        checkArgument(noun);
        checkNoun(noun);

        return synset.get(noun);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkArgument(nounA);
        checkArgument(nounB);
        checkNoun(nounA);
        checkNoun(nounB);

        int idA = getId(nounA);
        int idB = getId(nounB);

        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkArgument(nounA);
        checkArgument(nounB);
        checkNoun(nounA);
        checkNoun(nounB);

        int idA = getId(nounA);
        int idB = getId(nounB);

        int ancID = sap.ancestor(idA, idB);

        return idset.get(ancID);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");

        // isNoun() test
        StdOut.println(wordNet.isNoun("AND_circuit AND_Gate"));
        StdOut.println(wordNet.isNoun("AND_circuit AND_gate"));
        StdOut.println(wordNet.isNoun("gate logic_gate"));
        StdOut.println(wordNet.isNoun("wobushicsy"));
    }
}
