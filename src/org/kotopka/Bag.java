package org.kotopka;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <code>Bag</code> - Holds generic values. Allows adding and iterating over the elements added.
 * This particular implementation uses a linked-list. Does not support removal of items. <br>
 * Inspired by code in <em>Algorithms 4th ed.</em> by Robert Sedgewick and Kevin Wayne.
 * @param <T> Any valid Java reference type
 */
public class Bag<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) { this.data = data; }
    }

    private Node<T> head;
    private int size;
    private int modCount;

    public Bag() {
        this.head = null;
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * add() - Add items to instance of Bag. Items are added on top of the most recently added item.
     * @param data the data item to be added
     */
    public void add(T data) {
        Node<T> node = new Node<>(data);
        node.next = head;
        head = node;
        size++;
        modCount++;
    }

    /**
     * isEmpty() - Check to see if bag is empty
     * @return boolean, true if empty, false otherwise
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * size() - The number of elements in the Bag.
     * @return ingeger of the number of elements in the bag
     */
    public int size() { return size; }

    /**
     * iterator() - Returns an iterator for the Bag instance.
     * @return object of type Iterator to iterate over the bag's elements in a LIFO order
     */
    public Iterator<T> iterator() {
        return new BagIterator(head);
    }

    private class BagIterator implements Iterator<T> {

        Node<T> current;
        int innerModCount;

        public BagIterator(Node<T> first) {
            this.current = first;
            this.innerModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            checkModCount();

            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException("Call to next() when iterator has no elements");
            checkModCount();

            T data = current.data;
            current = current.next;

            return data;
        }

        private void checkModCount() {
            if (innerModCount != modCount) throw new ConcurrentModificationException("Bag modified during iteration");
        }
    }
}
