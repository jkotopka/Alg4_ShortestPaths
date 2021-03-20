package org.kotopka;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * {@code HeapFourWay} - 4-ary minimum heap implementation using a 1-indexed array
 */
public class HeapFourWay<T extends Comparable<T>> {

    private T[] heap;
    private int size;

    public HeapFourWay() {
        this(1);
    }

    @SuppressWarnings("unchecked")
    public HeapFourWay(int capacity) {
        this.heap = (T[]) new Comparable[capacity + 1];
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= heap.length) throw new IllegalArgumentException("Invalid index: " + index);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        T[] newHeap = (T[]) new Comparable[newSize];

        System.arraycopy(heap, 1, newHeap, 1, size);
        heap = newHeap;
    }

    private void swap(int i, int j) {
        T temp  = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private boolean isGreaterThan(int i, int j) {
        return heap[i].compareTo(heap[j]) > 0;
    }

    private int findMinChild(int j) {
        // find smallest heap element from: j - 2, j - 1, j, j + 1
        int minChildIndex = j - 2;
        j--;

        for (int i = 0; i < 3; i++) {
            int childIndex = j + i;

            if (childIndex > size) break;

            if (isGreaterThan(minChildIndex, childIndex)) {
                minChildIndex = childIndex;
            }
        }

        return minChildIndex;
    }

    private void sink(int k) {
        // this is where the logic gets a bit complicated for 4-ary heaps
        int j = findMinChild(4 * k);

        while (j <= size) {
            if (isGreaterThan(j, k)) { break; } // heap property restored, done!

            swap(j, k);
            k = j;
            j = findMinChild(4 * k);
        }
    }

    private void swim(int k) {
        // (k + 2) / 4 is the parent of k
        while (k > 1 && isGreaterThan((k + 2) / 4, k)) {
            swap((k + 2) / 4, k);
            k = (k + 2) / 4;
        }
    }

    public void insert(T key) {
        if (key == null) throw new IllegalArgumentException("insert() method cannot accept null values");

        if (size + 1 == heap.length) resize(heap.length * 2);

        heap[++size] = key; // add key to end of heap...
        swim(size);         // ...swim it up to restore heap property
    }

    public T min() {
        return heap[1];
    }

    public T delMin() {
        if (isEmpty()) throw new NoSuchElementException("Heap is empty");

        T min = heap[1];

        swap(1, size--);
        sink(1);
        heap[size + 1] = null;

        if (size >= 1 && size <= heap.length / 4)  resize(heap.length / 2);

        return min;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public boolean contains(T key) {
        if (key == null) throw new IllegalArgumentException("contains() cannot accept null values");

        for (T k : heap) {
            if (k != null && key.compareTo(k) == 0) return true;
        }

        return false;
    }

    Comparable<T>[] getHeap() { return heap.clone(); }

    // some testing
    public static void main(String[] args) {
        HeapFourWay<Integer> h = new HeapFourWay<>();
        int count = 0;
        Random rand = new Random();

        for (int i = 0; i < 100; i++) {
            while (count < 20) {
                h.insert(rand.nextInt(50));
                count++;
            }

//            System.out.println(Arrays.toString(h.getHeap()));

            int last = -1;
            System.out.print("size: " + h.size() + " = ");
            while (!h.isEmpty()) {
//            System.out.println("size: " + h.size());
                int current = h.delMin();
                System.out.print(current + " ");
                count--;
                if (current < last) {
                    System.out.println("\nERROR: INVALID HEAP!");
                    System.out.println(Arrays.toString(h.getHeap()));
                    System.exit(-1);
                }
                last = current;
//            System.out.println(Arrays.toString(h.getHeap()));
            }
            System.out.println();
            System.out.println();
        }
    }

}
