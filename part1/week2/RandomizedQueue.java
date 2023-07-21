import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int first;
    private int last;
    private int size;
    private int capacity;
    private Item[] items;
    private static final int MIN_SIZE = 8;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = MIN_SIZE;
        size = 0;
        first = 0;
        last = 0;
        items = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int newCapacity) {
        Item[] newItems = (Item[]) new Object[newCapacity];
        if (first < last) {
            for (int i = first; i < last; i += 1) {
                newItems[i - first] = items[i];
            }
        } else {
            int firstCopyItemsSize = capacity - first;
            for (int i = first; i < capacity; i += 1) {
                newItems[i - first] = items[i];
            }
            for (int i = 0; i < last; i += 1) {
                newItems[i + firstCopyItemsSize] = items[i];
            }
        }
        first = 0;
        last = size;
        capacity = newCapacity;
        items = newItems;
    }

    private boolean isFull() {
        return size == capacity;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't add a null pointer to RandomizedQueue");
        }
        if (isFull()) {
            resize(capacity * 2);
        }
        items[last] = item;
        last = (last + 1) % capacity;
        size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("No element in RandomizedQueue");
        }
        if (size <= capacity / 4 && capacity > MIN_SIZE) {
            resize(capacity / 2);
        }
        int randomPos = (StdRandom.uniform(size) + first) % capacity;
        Item item = items[randomPos];
        items[randomPos] = items[first];
        items[first] = null;
        first = (first + 1) % capacity;
        size -= 1;

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("No element in RandomizedQueue");
        }
        int randomPos = (StdRandom.uniform(size) + first) % capacity;
        return items[randomPos];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        Item[] itemsInIterator;
        int pointer;
        int size;

        public RandomizedQueueIterator() {
            size = size();
            itemsInIterator = (Item[]) new Object[size];
            pointer = 0;
            int index;
            for (int i = 0; i < size; i += 1) {
                index = (first + i) % capacity;
                itemsInIterator[i] = items[index];
            }
        }

        @Override
        public boolean hasNext() {
            return pointer != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No element left in iterator");
            }
            int randomPos = StdRandom.uniform(pointer, size);
            Item item = itemsInIterator[randomPos];
            itemsInIterator[randomPos] = itemsInIterator[pointer];
            itemsInIterator[pointer] = null;
            pointer += 1;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Unsupported operation: remove()");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // constructor test
        RandomizedQueue<String> stringRandomizedQueue = new RandomizedQueue<>();

        // enqueue tests
        stringRandomizedQueue.enqueue("Hello");
        stringRandomizedQueue.enqueue(", ");
        stringRandomizedQueue.enqueue("World");
        stringRandomizedQueue.enqueue("!");

        try {
            stringRandomizedQueue.enqueue(null);
        } catch (IllegalArgumentException e) {
            StdOut.println("Catch enqueue null pointer exception\n");
        }

        // size and isEmpty tests
        StdOut.println("size and isEmpty tests: ");
        StdOut.println(stringRandomizedQueue.size());
        StdOut.println(stringRandomizedQueue.isEmpty());

        StdOut.println();

        // iterator test
        StdOut.println("iterator test: ");
        for (String s : stringRandomizedQueue) {
            StdOut.println(s);
        }

        StdOut.println();

        // dequeue tests
        StdOut.println("dequeue tests: ");
        StdOut.println(stringRandomizedQueue.dequeue());
        StdOut.println(stringRandomizedQueue.dequeue());
        StdOut.println(stringRandomizedQueue.dequeue());
        StdOut.println(stringRandomizedQueue.dequeue());

        StdOut.println();

        // exception tests
        try {
            stringRandomizedQueue.sample();
        } catch (NoSuchElementException e) {
            StdOut.println("Catch NoSuchElementException\n");
        }

        // size and isEmpty tests
        StdOut.println("size and isEmpty tests: ");
        StdOut.println(stringRandomizedQueue.size());
        StdOut.println(stringRandomizedQueue.isEmpty());
        StdOut.println();

        // enqueue tests(resize)
        stringRandomizedQueue.enqueue("Alice");
        stringRandomizedQueue.enqueue("Bob");
        stringRandomizedQueue.enqueue("Cat");
        stringRandomizedQueue.enqueue("Dog");
        stringRandomizedQueue.enqueue("Ethernet");
        stringRandomizedQueue.enqueue("Frank");
        stringRandomizedQueue.enqueue("GOD");
        stringRandomizedQueue.enqueue("Hof");
        stringRandomizedQueue.enqueue("Icy");
        stringRandomizedQueue.enqueue("Josh");
        stringRandomizedQueue.enqueue("Knight");
        stringRandomizedQueue.enqueue("Lan");

        // iterator test
        StdOut.println("iterator test again: ");
        for (String s : stringRandomizedQueue) {
            StdOut.println(s);
        }

        StdOut.println();

        // dequeue tests(resize)
        StdOut.println("dequeue tests(resize): ");
        for (int i = 0; i < 9; i += 1) {
            stringRandomizedQueue.dequeue();
        }
    }

}
