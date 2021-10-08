import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private String input;
    private Integer[] index;
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        input = s;
        index = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            index[i] = i;
        }
        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int p = o1, q = o2;
                for (int i = 0; i < input.length(); i++) {
                    if (p >= input.length()) p = 0;
                    if (q >= input.length()) q = 0;
                    if (input.charAt(p) > input.charAt(q)) return 1;
                    if (input.charAt(p) < input.charAt(q)) return -1;
                    p++;
                    q++;
                }
                return 0;
            }
        });
    }

    public int length() {return index.length;}
    public int index(int i) {
        if (i < 0 || i >= index.length) throw new IllegalArgumentException();
        return index[i];
    }
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i));
        }
    }
}
