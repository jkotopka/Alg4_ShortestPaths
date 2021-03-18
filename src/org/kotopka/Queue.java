package org.kotopka;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * <code>Queue</code> - Implements the queue data type
 * @param <T> Any generic type
 */
public class Queue<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    public Queue() {
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * <code>isEmpty()</code> - Returns true if queue is empty, false otherwise
     * @return boolean, true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * <code>size()</code> - Returns the number of elements currently in this queue.
     * @return integer, of number of items in the queue
     */
    public int size() {
        return size;
    }

    /**
     * <code>enqueue()</code> - Adds item to the end of the FIFO queue
     * @param data Java generic, reference type specified during instantiation of this <code>Queue</code> object
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        size++;
        modCount++;
    }

    /**
     * <code>peekFirst()</code> - Returns data at head of FIFO queue without removing it.
     * @return Java generic, reference type specified during instantiation of this <code>Queue</code> object
     */
    public T peekFirst() {
        if (isEmpty()) throw new IllegalArgumentException("Queue is empty");

        return head.data;
    }

    /**
     * <code>dequeue()</code> - Remove and return item at head of FIFO queue.
     * @return Java generic, reference type specified during instantiation of this <code>Queue</code> object
     */
    public T dequeue() {
        if (isEmpty()) throw new IllegalArgumentException("Queue is empty");

        T data = head.data;
        head.data = null;
        head = head.next;
        size--;
        modCount++;

        return data;
    }

    /**
     * <code>peekLast()</code> - Returns item at tail of FIFO queue without removing it.
     * @return data from the tail of the queue, without removing it
     */
    public T peekLast() {
        if (isEmpty()) throw new IllegalArgumentException("Queue is empty");

        return tail.data;
    }

    /**
     * <code>iterator()</code> - Returns an Iterator of this queue.
     * @return an iterator for all current items in the queue
     */
    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<T> {

        Node<T> current;
        int innerModCount;

        private QueueIterator() {
            this.current = head;
            this.innerModCount = modCount;
        }

        /**
         * <code>hasNext()</code> - Returns boolean value if there are more items to iterate.
         * @return boolean, true if there are more items to iterate, false otherwise
         * @throws ConcurrentModificationException if the queue is modified while the iterator is still live
         */
        @Override
        public boolean hasNext() {
            if (innerModCount != modCount) throw new ConcurrentModificationException("Queue modified during iteration");

            return current != null;
        }

        /**
         * <code>next()</code> - Returns the next <code>Queue</code> item to iterate.
         * @return Java generic, reference type specified during instantiation of this <code>Queue</code> object
         * @throws ConcurrentModificationException if the queue is modified while the iterator is still live
         * @throws IllegalArgumentException if the queue is empty
         */
        @Override
        public T next() {
            if (innerModCount != modCount) throw new ConcurrentModificationException("Queue modified during iteration");
            if (current == null) throw new IllegalArgumentException("Queue is empty");

            T data = current.data;
            current = current.next;

            return data;
        }
    }

}
