import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;
    private int count;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.size = 1;
        this.items = (Item[]) new Object[size];
        this.count = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == count) {
            resize(2 * size);
        }
        items[count++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int index = randomIndex();
        int lastIndex = size() - 1;
        Item item = items[index];
        items[index] = items[lastIndex];
        items[lastIndex] = null;
        count--;
        if (count <= size / 4) {
            resize(size / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return items[randomIndex()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

    private void resize(int cap) {
        if (isEmpty()) {
            return;
        }
        Item[] temp = (Item[]) new Object[cap];
        size = cap;
        for (int i = 0; i < count; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    private int randomIndex() {
        return StdRandom.uniformInt(size());
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final RandomizedQueue<Item> copy;

        RandomizedQueueIterator() {
            copy = new RandomizedQueue<Item>();
            for (int i = 0; i < size(); i++) {
                copy.enqueue(items[i]);
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return copy.count > 0;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public Item next() {
            if (copy.isEmpty() || !hasNext()) {
                throw new NoSuchElementException();
            }
            return copy.dequeue();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
