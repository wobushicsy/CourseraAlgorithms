import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.FibonacciMinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private Node firstSentinel;
    private Node lastSentinel;
    private int size;

    private class Node {
        Node last;
        Item item;
        Node next;

        public Node() {
            last = null;
            item = null;
            next = null;
        }

        public Node(Node last, Item item, Node next) {
            this.last = last;
            this.item = item;
            this.next = next;
        }
    }

    // construct an empty deque
    public Deque() {
        firstSentinel = new Node();
        lastSentinel = new Node();
        firstSentinel.next = lastSentinel;
        lastSentinel.last = firstSentinel;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    private void validate(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't add a null pointer to Deque");
        }
    }

    // add the item to the front
    public void addFirst(Item item) {
        validate(item);
        size += 1;
        Node oldFirst = firstSentinel.next;
        Node newFirst = new Node(firstSentinel, item, oldFirst);
        firstSentinel.next = newFirst;
        oldFirst.last = newFirst;
    }

    // add the item to the back
    public void addLast(Item item) {
        validate(item);
        size += 1;
        Node oldLast = lastSentinel.last;
        Node newLast = new Node(oldLast, item, lastSentinel);
        lastSentinel.last = newLast;
        oldLast.next = newLast;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException("No element in Deque");
        }
        size -= 1;
        Node nextNode = firstSentinel.next;
        Node next2Node = nextNode.next;
        firstSentinel.next = next2Node;
        next2Node.last = firstSentinel;

        return nextNode.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size <= 0) {
            throw new NoSuchElementException("No element in Deque");
        }
        size -= 1;
        Node lastNode = lastSentinel.last;
        Node last2Node = lastNode.last;
        last2Node.next = lastSentinel;
        lastSentinel.last = last2Node;

        return lastNode.item;
    }

    // return an iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        Node pointer;

        public DequeIterator() {
            pointer = firstSentinel.next;
        }

        @Override
        public boolean hasNext() {
            return pointer.item != null;
        }

        @Override
        public Item next() {
            Item item = pointer.item;
            pointer = pointer.next;

            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // constructor test
        Deque<String> stringDeque = new Deque<>();

        // addFirst and addLast tests
        stringDeque.addFirst("Hello");
        stringDeque.addLast(", ");
        stringDeque.addLast("World");

        // iterator tests
        for (String s : stringDeque) {
            StdOut.print(s);
        }

        StdOut.println("\n");

        // size and isEmpty tests
        StdOut.println(stringDeque.size());
        StdOut.println(stringDeque.isEmpty());

        StdOut.println();

        // removeFirst and removeLast tests
        String first = stringDeque.removeFirst();
        String last = stringDeque.removeLast();
        StdOut.println(first);
        StdOut.println(last);

        StdOut.println();

        // size and isEmpty tests
        StdOut.println(stringDeque.size());
        StdOut.println(stringDeque.isEmpty());
        stringDeque.removeFirst();
        StdOut.println(stringDeque.isEmpty());


    }

}
