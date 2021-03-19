package org.kotopka;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@code Stack} - Stack data structure, holds generic Java reference types.
 */
public class Stack<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) { this.data = data; }
    }

    private Node<T> top;
    private int modCount;
    private int size;

    public Stack() {
        this.top = null;
        this.modCount = 0;
        this.size = 0;
    }

    /**
     * {@code push()} - Pushes data onto the top of the stack.
     * @param data data to be pushed on the stack
     */
    public void push(T data) {
        if (data == null) throw new IllegalArgumentException("Null data");

        Node<T> node = new Node<>(data);

        node.next = top;
        top = node;
        size++;
        modCount++;
    }

    /**
     * {@code pop()} - Removes the data from the top of the stack and returns it.
     * @return data from the top of the stack
     */
    public T pop() {
        if (isEmpty()) throw new IllegalArgumentException("Stack is empty");

        T data = top.data;
        top.data = null;
        top = top.next;
        size--;
        modCount++;

        return data;
    }

    /**
     * {@code size()} - The number of items currently in the stack.
     * @return the number of items currently in the stack
     */
    public int size() { return size; }

    /**
     * {@code isEmpty()} - Is this stack currently empty?
     * @return boolean {@code true} if the stack is empty, {@code false} otherwise
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * {@code iterator()} - Returns an {@code Iterator} for this stack instance.
     * @return {@code Iterator} for this stack
     */
    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }

    private class StackIterator implements Iterator<T> {

        int iteratorModCount = modCount;
        Node<T> current;

        @Override
        public boolean hasNext() {
            if (iteratorModCount != modCount) throw new ConcurrentModificationException("Stack modified during iteration");

            return current.next != null;
        }

        @Override
        public T next() {
            if (iteratorModCount != modCount) throw new ConcurrentModificationException("Stack modified during iteration");
            if (!hasNext()) throw new NoSuchElementException("Call to next() when iterator has no elements");

            T data = current.data;
            current = current.next;

            return data;
        }
    }

}
