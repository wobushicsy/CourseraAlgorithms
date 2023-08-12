import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    // a hashtable between synset and id
    private final HashMap<String, HashSet<Integer>> synset;
    private final HashMap<Integer, String> idset;

    // SAP
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkArgument(synsets);
        checkArgument(hypernyms);

        // initialize class variables
        synset = new HashMap<>();
        idset = new HashMap<>();

        // initialize local variables
        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);
        String buffer;
        String[] splitedBuffer;
        int cnt = 0;
        Digraph graph;

        // process synsets data
        while (!synsetsIn.isEmpty()) {
            // cannot assume a line only contains two commas
            cnt += 1;
            buffer = synsetsIn.readLine();
            splitedBuffer = buffer.split(",");

            // sp[0] = "{num}", sp[1] = {noun synset}, sp[2:] = {gloss}(not relevant)
            int id = Integer.parseInt(splitedBuffer[0]);
            for (String s: splitedBuffer[1].split(" ")) {
                if (synset.containsKey(s)) {
                    HashSet<Integer> set = synset.get(s);
                    if (set.contains(id)) {
                        continue;
                    } else {
                        set.add(id);
                    }
                } else {
                    HashSet<Integer> set = new HashSet<>();
                    set.add(id);
                    synset.put(s, set);
                }
            }
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
                int hyperIndex = Integer.parseInt(splitedBuffer[i]);
                graph.addEdge(index, hyperIndex);
            }
        }

        // initialize SAP
        sap = new SAP(graph);

        checkDAG(graph);
    }

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

    private void checkDAG(Digraph G) {
        DirectedCycle graph = new DirectedCycle(G);
        if (graph.hasCycle()) {
            throw new IllegalArgumentException("not a DAG");
        }

        int roots = 0;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (!G.adj(vertex).iterator().hasNext()) {
                roots++;
            }
        }
        if (roots != 1) {
            throw new IllegalArgumentException("not a DAG");
        }
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

    private Iterable<Integer> getId(String noun) {
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

        Iterable<Integer> idA = getId(nounA);
        Iterable<Integer> idB = getId(nounB);

        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkArgument(nounA);
        checkArgument(nounB);
        checkNoun(nounA);
        checkNoun(nounB);

        Iterable<Integer> idA = getId(nounA);
        Iterable<Integer> idB = getId(nounB);

        int ancID = sap.ancestor(idA, idB);

        return idset.get(ancID);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");

        StdOut.println(wordNet.distance("spillway", "grinder"));
    }
}
