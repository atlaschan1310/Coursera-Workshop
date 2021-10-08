/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size = 0;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    private void resize(int cap) {
        Item[] temp = (Item[]) new Object[cap];
        for (int i = 0; i < size; i++) {
            temp[i] = items[i];
        }
        items = temp;
     }

     public void enqueue(Item item) {
        if (item == null) throw new NoSuchElementException();
        items[size++] = item;
        if (size == items.length) {
            resize (2 * items.length);
        }
     }

     public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, size);
        Item delItem = items[index];
        items[index] = items[size - 1];
        items[--size] = null;
        if (size > 0 && size == items.length / 4) {
            resize (items.length / 2);
        }
        return delItem;
     }

     public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, size);
        return items[index];
     }

     public Iterator<Item> iterator() {
        return new RandQueIterator();
     }

     private class RandQueIterator implements Iterator<Item> {
        private final int[] randIndex = StdRandom.permutation(size);
        private int rank = 0;

         public boolean hasNext() {return rank < size;}
         public void remove() {throw new UnsupportedOperationException();}
         public Item next() {
             if (!hasNext()) {
                 throw new NoSuchElementException();
             }
             else {
                 return items[randIndex[rank++]];
             }
         }
     }

    public static void main(String[] args) {
        RandomizedQueue queue = new RandomizedQueue();
        System.out.println(queue.size());
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        queue.dequeue();
        if (!queue.isEmpty()) System.out.println(queue.size());
        System.out.println(queue.sample());
        Iterator it = queue.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }
}
