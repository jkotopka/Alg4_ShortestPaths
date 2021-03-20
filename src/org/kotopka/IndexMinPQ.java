package org.kotopka;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * <code>IndexMinPQ</code> - Implements a basic indexed minimum priority queue.
 * This implementation uses fixed-size internal arrays which are constructed during object instantiation.
 * <br><br>
 * Adapted from <a href="https://algs4.cs.princeton.edu/home/">Altorithms 4th ed.</a> by Robert Sedgewick and Kevin Wayne
 */
public class IndexMinPQ<Key extends Comparable<Key>>{

    private final int capacity;
    private final Key[] keys;
    private final int[] pq;
    private final int[] qp;
    private int size;

    @SuppressWarnings("unchecked")
    public IndexMinPQ(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("Invalid capacity");

        this.capacity = capacity;

        /*
            keys[i] is the priority of index i
            pq[i] is the index of the key in *heap position* i
            qp[i] is the *heap position* of the key with index i

                 i  0  1  2  3  4  5  6  7  8
            ---------------------------------
            keys[i] A  S  O  R  T  I  N  G  -
              pq[i] -  0  6  7  2  1  5  4  3
              qp[i] 1  5  4  8  7  6  2  3  -

                                  1A
                           2N             3G
                      4O       5S     6I       7T
                  8R

           i = 3;
           qp[3] == 8   // the element at index 3 is the 8th heap position
           pq[8] == 3   // the 8th heap position references the value at keys[3]
           keys[3] == 'R'
         */

        this.keys = (Key[]) new Comparable[capacity + 1];
        this.pq = new int[capacity + 1];
        this.qp = new int[capacity + 1];

        Arrays.fill(qp, -1);
    }

    /**
     * <code>swap()</code> Private method, swaps the index pointers in pq[] and qp[]
     * @param a integer index of one item to be swapped
     * @param b integer index of the other item to be swapped
     */
    private void swap(int a, int b) {
        int temp = pq[a];
        pq[a] = pq[b];
        pq[b] = temp;

        // see discussion of these arrays in the constructor above
        // pq and qp hold inverse values:
        // pq[i] = j
        // qp[j] = i
        qp[pq[a]] = a;
        qp[pq[b]] = b;
    }

    /**
     * <code>isGreaterThan()</code> Private method, returns true if the element at keys[a] > keys[b]
     * @param a integer of the keys index for element a
     * @param b integer of the keys index for element b
     * @return boolean value, true if keys[a] > keys[b], false otherwise
     */
    private boolean isGreaterThan(int a, int b) {
        // pq[a] references the item at heap position "a"
        // pq[b] references the item at heap position "b"

        if (keys[pq[a]] == null || keys[pq[b]] == null) return false;

        return keys[pq[a]].compareTo(keys[pq[b]]) > 0;
    }

    /**
     * <code>sink()</code> Private method, puts the element "k" into the correct place to maintain the heap property.
     * The <code>sink()</code> method is used to restore order after a deletion.
     * @param k integer of the index where the item to swim originates
     */
    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;

            // find the smaller of the two children at nodes j and j + 1
            // "size" is the last VALID element in the heap
            if (j < size && isGreaterThan(j, j + 1)) { j++; }

            // if the heap property has been restored, break
            if (!isGreaterThan(k, j)) { break; }

            swap(j, k);
            k = j;
        }
    }

    /**
     * <code>swim()</code> - Private method, puts the element "k" into the correct place to maintain the heap property.
     * The <code>swim()</code> method is used to restore order after an insertion.
     * @param k integer of the index where the item to swim originates
     */
    private void swim(int k) {
        while(k > 1 && isGreaterThan(k/2, k)) {
            swap(k/2, k);
            k = k/2;
        }
    }

    /**
     * <code>validateIndex()</code> - Validates the index supplied as a method argument
     * @param index integer of the array index
     * @throws IllegalArgumentException if array index is invalid
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= capacity) {
            throw new IllegalArgumentException("Invalid index range");
        }
    }

    /**
     * <code>insert()</code> - Inserts a key into the priority queue at index i
     * @param i integer for the index
     * @param key Java generic value of the key to be inserted
     * @throws IllegalArgumentException if the index is invalid
     * @throws IllegalArgumentException if the key is null
     * @throws IllegalArgumentException if the index "i" is already in use
     */
    public void insert(int i, Key key) {
        validateIndex(i);
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (contains(i)) throw new IllegalArgumentException("Index already in use");

        // see discussion of these arrays in the constructor above
        size++;
        qp[i] = size;
        pq[size] = i;
        keys[i] = key;
        swim(size);
    }

    /**
     * <code>decreaseKey()</code> - Decreases the priority of the key associated with index "i"
     * @param i integer of the array index
     * @param key Java generic value of the key to be decreased
     * @throws IllegalArgumentException if the index is invalid
     * @throws IllegalArgumentException if the key is null
     * @throws NoSuchElementException if the index can't be found
     * @throws IllegalArgumentException if the key already exists in the priority queue
     * @throws IllegalArgumentException if the key is larger than the key already in the priority queue
     */
    public void decreaseKey(int i, Key key) {
        validateIndex(i);
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (!contains(i)) throw new NoSuchElementException("Index not found");
        if (keys[i].compareTo(key) == 0) throw new IllegalArgumentException("Key already found");
        if (keys[i].compareTo(key) < 0) throw new IllegalArgumentException("Smaller key already in queue");

        keys[i] = key;
        swim(qp[i]); // HAHA FOUND YOU, YOU PESKY LITTLE BUG!
    }

    /**
     * <code>changeKey()</code> - Changes the priority of the key associated with index "i"
     * @param i integer of the array index
     * @param key Java generic value of the key to be decreased
     * @throws IllegalArgumentException if the index is invalid
     * @throws IllegalArgumentException if the key is null
     * @throws NoSuchElementException if the index can't be found
     */
    public void changeKey(int i, Key key) {
        validateIndex(i);
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (!contains(i)) throw new NoSuchElementException("Index not found");

        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    /**
     * <code>contains()</code> - Returns boolean value if the index "i" is associated with a key in this priority queue
     * @param i integer of the array index
     * @return boolean true if this priority queue contains the key at index "i", false otherwise
     */
    public boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1;
    }

    /**
     * <code>delMin()</code> - Removes the minimum key and returns the index
     * @return integer index of the key removed
     * @throws NoSuchElementException if the priority queue is empty
     */
    public int delMin() {
        if (size == 0) throw new NoSuchElementException("Priority queue is empty");

        // see discussion of these arrays in the constructor above
        int min = pq[1];
        swap(1, size--);
        sink(1);
        qp[min] = -1;
        keys[min] = null;
        pq[size + 1] = -1; // not sure if accurate...
        return min;
    }

    public Key minKey() {
        return keys[pq[1]]; // pq[1] == first heap element, i.e. the smallest element
    }

    public Key key(int index) {
        return keys[index];
    }

    /**
     * <code>isEmpty()</code> - Returns true if the priority queue is empty, false otherwise.
     * @return boolean value, true if the priority queue is empty, false otherwise
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * <code>size()</code> - Returns the number of elements currently in this priority queue
     * @return integer representing the number of elements contained herein
     */
    public int size() { return size; }

}
