import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;
    public Outcast (WordNet _wordnet) {
        wordnet = _wordnet;
    }
    public String outcast(String[] nouns) {
        int length = nouns.length;
        int[][] distance = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = i; j < length; j++) {
                distance[i][j] = wordnet.distance(nouns[i], nouns[j]);
            }
        }
        int max_dis = 0;
        int sum = 0;
        int num = 0;
        for (int i = 0; i < length; i++) {
            sum = 0;
            for (int j = 0; j < length; j++) {
                if (i < j) {
                    sum += distance[i][j];
                }
                else {
                    sum += distance[j][i];
                }
            }
            if (sum > max_dis) {
                max_dis = sum;
                num = i;
            }
        }
        return nouns[num];
    }
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
