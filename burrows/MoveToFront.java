import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.List;

public class MoveToFront {
    public static void encode() {
        List<Character> list = new ArrayList<Character>();
        for (int i = 0; i < 256; i++) {
            list.add((char)i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int index = list.indexOf(ch);
            BinaryStdOut.write(index, 8);
            list.remove(index);
            list.add(0, ch);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        List<Character> list = new ArrayList<Character>();
        for (int i = 0; i < 256; i++) {
            list.add((char)i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char ch = list.get(index);
            BinaryStdOut.write(ch);
            list.remove(index);
            list.add(0, ch);
        }
        BinaryStdOut.close();
    }

    public static void main(String args[]) {
        if (args[0].equals("+")) encode();
        if (args[0].equals("-")) decode();
    }
}
