import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class WordNet {
    private final ST<String, Bag<Integer>> st;
    private final ArrayList<String> idList;
    private final Digraph map;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        st = new ST<String, Bag<Integer>>();
        idList = new ArrayList<String>();
        int count = 0;

        In fin = new In(synsets);
        while (fin.hasNextLine()) {
            String[] line = fin.readLine().split(",");
            String[] words = line[1].split(" ");
            for (int i = 0; i < words.length; i++) {
                if (st.contains(words[i])) {
                    st.get(words[i]).add(Integer.parseInt(line[0]));
                }
                else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(Integer.parseInt(line[0]));
                    st.put(words[i], bag);
                }
            }
            count++;
            idList.add(line[1]);
        }
        map = new Digraph(count);
        In fin2 = new In(hypernyms);
        boolean[] isNotRoot = new boolean[count];
        int rootNum = 0;
        while (fin2.hasNextLine()) {
            String[] line = fin2.readLine().split(",");
            isNotRoot[Integer.parseInt(line[0])] = true;
            for (int i = 1; i < line.length; i++) {
                map.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
            }
        }
        for (int i = 0; i < count; i++) {
            if (!(isNotRoot[i])) rootNum++;
        }
        DirectedCycle cycle  = new DirectedCycle(map);
        if (cycle.hasCycle() || rootNum > 1) throw new IllegalArgumentException();
    }

    public Iterable<String> nouns() {
        return st.keys();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return st.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB ==null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        SAP sap = new SAP(map);
        Bag<Integer> aBag = st.get(nounA);
        Bag<Integer> bBag = st.get(nounB);
        return sap.length(aBag, bBag);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB ==null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        SAP sap = new SAP(map);
        Bag<Integer> aBag = st.get(nounA);
        Bag<Integer> bBag = st.get(nounB);
        int root = sap.ancestor(aBag, bBag);
        return idList.get(root);
    }
}
