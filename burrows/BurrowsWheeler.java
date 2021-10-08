import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

import java.util.Arrays;


public class BurrowsWheeler {
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);
        for (int i = 0; i < csa.length(); i++){
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(input.charAt((csa.index(i) + csa.length() - 1) % csa.length()));
        }
        BinaryStdOut.close();
    }
    public static void inverseTransform()  {
        int first = BinaryStdIn.readInt();
        String chars = BinaryStdIn.readString();
        char[] t = chars.toCharArray();
        int size = t.length;
        ST<Character, Queue<Integer>> st = new ST<Character, Queue<Integer>>();
        for (int i = 0; i < size; i++) {
            if(st.contains(t[i])) st.get(t[i]).enqueue(i);
            else {
                Queue<Integer> q = new Queue<Integer>();
                q.enqueue(i);
                st.put(t[i],q);
            }
        }
        Arrays.sort(t);
        int[] next = new int[size];
        for (int i = 0; i < size; i++) {
            next[i] = st.get(t[i]).dequeue();
        }
        for (int i = 0; i < size; i++) {
            BinaryStdOut.write(t[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("+")) transform();
        if(args[0].equals("-")) inverseTransform();
    }
}