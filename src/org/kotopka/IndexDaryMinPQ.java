package org.kotopka;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * {@code IndexDaryMinPQ} - d-ary minimum heap indexed priority queue implementation using a 0-indexed array. {@code d} must be between 2 and 4, inclusive
 */

public class IndexDaryMinPQ<T extends Comparable<T>> {

    private final int d;
    private final T[] keys;
    private final int[] pq;
    private final int[] qp;
    private int size;

    /**
     * {@code IndexDaryMinPQ()} - Constructor. Creates a new {@code d} d-ary indexed minimum priority queue with space for {@code capacity} elements.
     * @param d the dimension of the heap, between 2 for binary heap, and 4 for 4-ary heap
     * @param capacity the maximum number of elements in this priority queue
     * @throws IllegalArgumentException if the argument {@code d} is not between 2 and 4, inclusive
     * @throws IllegalArgumentException if the capacity is not a positive value
     */
    @SuppressWarnings("unchecked")
    public IndexDaryMinPQ(int d, int capacity) {
        if (d < 2 || d > 4) throw new IllegalArgumentException("d-ary heap must be between 2 and 4 inclusive");
        if (capacity < 1) throw new IllegalArgumentException("Capacity must be a positive value");

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

        this.d    = d;
        this.keys = (T[]) new Comparable[capacity];
        this.pq   = new int[capacity];
        this.qp   = new int[capacity];

        Arrays.fill(qp, -1);
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= keys.length) throw new IllegalArgumentException("Invalid index: " + index);
    }

    private void swap(int i, int j) {
        int temp  = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        // see discussion of these arrays in the constructor above
        // pq and qp hold inverse values:
        // pq[i] = j
        // qp[j] = i
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private boolean isGreaterThan(int i, int j) {
        // pq[i] references the item at heap position "i"
        // pq[j] references the item at heap position "j"

//        if (keys[pq[i]] == null || keys[pq[j]] == null) return false;

        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private int findMinChild(int j) {
        int minChildIndex = j;

        for (int i = 1; i < d; i++) {
            int childIndex = j + i;

            if (childIndex >= size) break;

            if (isGreaterThan(minChildIndex, childIndex)) {
                minChildIndex = childIndex;
            }
        }

        return minChildIndex;
    }

    private void sink(int k) {
        // this is where the logic gets a bit complicated for d-ary heaps
        int j = findMinChild(d * k + 1);

        while (j < size) {
            if (isGreaterThan(j, k)) { break; } // heap property restored, done!

            swap(j, k);

            k = j;
            j = findMinChild(d * k + 1);
        }
    }

    private void swim(int k) {
        // (k - 1) / d is the parent of k
        while (k > 0 && isGreaterThan((k - 1) / d, k)) {
            swap((k - 1) / d, k);

            k = (k - 1) / d;
        }
    }

    /**
     * {@code insert()} - Insert {@code key} associated with {@code index} into the priority queue.
     * @param index index of the key
     * @param key item to be inserted
     * @throws IllegalArgumentException if the {@code index} is invalid
     * @throws IllegalArgumentException if the {@code key} is {@code null}
     */
    public void insert(int index, T key) {
        validateIndex(index);
        if (key == null) throw new IllegalArgumentException("insert() method cannot accept null values");

        qp[index]   = size;
        pq[size]    = index;
        keys[index] = key;  // add key to end of heap...

        swim(size++);       // ...swim it up to restore heap property
    }

    /**
     * {@code changeKey()} - Change the {@code key} associated with the {@code index}.
     * @param index {@code index} of the {@code key}
     * @param key new {@code key} to overwrite the previous {@code key} associated with this index
     * @throws IllegalArgumentException if the {@code index} is invalid
     * @throws IllegalArgumentException if the new {@code key} is {@code null}
     * @throws NoSuchElementException if the {@code key} is not found
     */
    public void changeKey(int index, T key) {
        validateIndex(index);
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (!contains(index)) throw new NoSuchElementException("Index " + index + " not found");

        keys[index] = key;

        // only one of these will change the state of the internal heap
        swim(qp[index]);
        sink(qp[index]);
    }

    /**
     * {@code delMin()} - Removes the minimum key and returns the index
     * @return index of the key removed
     * @throws NoSuchElementException if the priority queue is empty
     */
    public int delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");

        int min = pq[0];

        swap(0, --size);
        sink(0);

        keys[min] = null;
        qp[min]   = -1;
        pq[size]  = -1;

        return min;
    }

    /**
     * {@code minKey()} - Get the minimum {@code key} of this priority queue. Does not remove the {@code key}.
     * @return minimum {@code key} in this priority queue
     * @throws NoSuchElementException if the priority queue is empty
     */
    public T minKey() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");

        return keys[pq[0]];
    }

    /**
     * {@code key()} - Get the {@code key} associated with the {@code index}.
     * @param index the {@code index} of the {@code key} to retrieve
     * @return the {@code key} associated with the {@code index} provided
     * @throws IllegalArgumentException if the {@code index} is invalid
     * @throws NoSuchElementException if the priority queue is empty
     * @throws NoSuchElementException if the {@code index} is not found
     */
    public T key(int index) {
        validateIndex(index);
        if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
        if (!contains(index)) throw new NoSuchElementException("Index not found");

        return keys[index];
    }

    /**
     * {@code isEmpty()} - Returns {@code true} if the priority queue is empty, {@code false} otherwise.
     * @return boolean {@code true} if the priority queue is empty, {@code false} otherwise
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * {@code size()} - Returns the number of elements currently in this priority queue.
     * @return the number of elements contained herein
     */
    public int size() { return size; }

    public boolean contains(int index) {
        validateIndex(index);

        return qp[index] != -1;
    }

    /**
     * {@code containsKey()} - Returns {@code true} if the priority queue contains the {@code key}, {@code false} otherwise.
     * @param key the key to query
     * @return boolean {@code true} if the {@code key} is found, {@code false} otherwise
     * @throws IllegalArgumentException if the {@code key} provided is {@code null}
     */
    public boolean containsKey(T key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        for (T k : keys) {
            if (k != null && key.compareTo(k) == 0) {
                return true;
            }
        }

        return false;
    }

}
