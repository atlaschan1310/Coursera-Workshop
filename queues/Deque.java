/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    private class Node {
        Item item;
        Node prev;
        Node next;
    }

    public Deque () {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node cur_head = new Node();
        cur_head.item = item;
        cur_head.prev = null;
        if (isEmpty()) {
            cur_head.next = null;
            this.first = cur_head;
            this.last = cur_head;
        }
        else {
            cur_head.next = this.first;
            this.first = cur_head;
        }
        this.size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node cur_tail = new Node();
        cur_tail.item = item;
        cur_tail.next = null;
        if (isEmpty()) {
            cur_tail.prev = null;
            this.first = cur_tail;
            this.last = cur_tail;
        }
        else {
            cur_tail.prev = this.last;
            this.last = cur_tail;
        }
        this.size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = this.first.item;
        this.first = this.first.next;
        this.size--;
        if (isEmpty()) {
            this.last = this.first;
        }
        else {
            this.first.prev = null;
        }
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = this.last.item;
        this.last = this.last.prev;
        this.size--;
        if (isEmpty()) {
            this.first = this.last;
        }
        else {
            this.last.next = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() { return current != null;}
        public boolean hasPre() {return current != null;}
        public void remove() { throw new UnsupportedOperationException();}
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque queue = new Deque();
        System.out.println(queue.size());
        queue.addFirst("a");
        queue.addLast("b");
        queue.addFirst("c");
        if (!queue.isEmpty()) {
            System.out.println(queue.size());
        }
        Iterator it = queue.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        queue.removeFirst();
        queue.removeLast();
        queue.removeFirst();
        if (queue.isEmpty()) System.out.println("Empty.");
    }
}
