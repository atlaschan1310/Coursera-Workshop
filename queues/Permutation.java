/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue queue = new RandomizedQueue();
        int k = Integer.parseInt(args[0]);
        int count = 0;
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
            count++;
        }
        while (k > 0 && count > 0) {
            System.out.println(queue.dequeue());
            k--;
            count--;
        }
    }
}
