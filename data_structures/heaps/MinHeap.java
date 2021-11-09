package data_structures.heaps;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import data_structures.Entry;

/**
 * Simple Binary Minimum Heap implementation. Uses the {@link Entry} class to
 * hold the MinHeap key identifier and the value.
 */
public class MinHeap<T> {
  /**
   * The comparison function used to determine whether an element is less than
   * another to retain the minimum for the extractMin operation.
   */
  private BiFunction<T, T, Boolean> compare;

  /**
   * The array to hold the elements in the heap.
   */
  private Entry<Integer, T>[] heap;

  /**
   * The number of elements in the heap.
   */
  private int size = 0;

  /**
   * Constructs an empty MinHeap with the specified maximum capacity of elements
   * and the custom compare function to determine the minimum element.
   * 
   * @param capacity the capacity of the heap
   * @param compare  the comparison function
   * 
   * @throws IllegalArgumentException if the specified capacity is less than
   *                                  {@code 1}
   */
  @SuppressWarnings("unchecked")
  public MinHeap(int capacity, BiFunction<T, T, Boolean> compare) {
    if (capacity < 1)
      throw new IllegalArgumentException("Illegal capacity, must be greater than 1.");
    heap = (Entry<Integer, T>[]) new Entry<?, ?>[capacity];
    this.compare = compare;
  }

  /**
   * Constructs an empty MinHeap with the specified maximum capacity of elements
   * and uses the default compare function which compares the elements
   * {@code hashCode()}.
   * 
   * @param capacity the capacity of the heap
   * 
   * @throws IllegalArgumentException if the specified capacity is less than
   *                                  {@code 1}
   */
  public MinHeap(int capacity) {
    this(capacity, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  /**
   * Constructs a MinHeap from the values in an array and sets the capacity to the
   * length of the array.
   * 
   * @param array the array whose values to initalize the heap with
   * 
   * @throws NullPointerException     if the array is {@code null}
   * @throws IllegalArgumentException if the array is empty
   */
  @SuppressWarnings("unchecked")
  public MinHeap(T[] array) {
    if (array == null)
      throw new NullPointerException("The array cannot be null.");
    if (array.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    heap = (Entry<Integer, T>[]) new Entry<?, ?>[array.length];

    for (int i = 0; i < array.length; i++)
      insert(array[i]);
  }

  /**
   * Constructs a copy of another {@code MinHeap}.
   * 
   * @param heap the heap to make a copy of
   * 
   * @throws NullPointerException if the {@code MinHeap} is {@code null}
   */
  public MinHeap(MinHeap<T> heap) {
    if (heap == null)
      throw new NullPointerException("Heap cannot be null");

    this.heap = heap.heap;
    compare = heap.compare;
    size = heap.size;
  }

  /**
   * The internal method that uses the compare function to determine which
   * {@code Entry} is lesser.
   * 
   * @param x the first entry to compare
   * @param y the second entry to compare
   * @return whether the first entry is less than the other
   */
  private boolean isLessThan(Entry<Integer, T> x, Entry<Integer, T> y) {
    return compare.apply(x.getValue(), y.getValue());
  }

  /**
   * Internal method to swap two heap elements positions.
   * 
   * @param a first item index
   * @param b second item index
   */
  private void swap(int a, int b) {
    Entry<Integer, T> temp = heap[a];
    heap[a] = heap[b];
    heap[b] = temp;
  }

  private int left(int i) {
    return 2 * i;
  }
    
  private int right(int i) {
    return 2 * i + 1; 
  }
    
  private int parent(int i) {
    return (int) Math.floor(i / 2);
  }

  /**
   * Determines whether heap is empty or not
   * 
   * @return whether the heap is empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the number of elements in the heap.
   * 
   * @return the number of elements in the heap
   */
  public int size() {
    return size;
  }

  /**
   * The maximum number of elements in the heap.
   * 
   * @return the maximum number of elements in the heap
   */
  public int capacity() {
    return heap.length;
  }

  /**
   * Returns the minimum element in the heap without removing it.
   * 
   * @return the minimum element or {@code null} if empty
   */
  public T peek() {
    if (isEmpty())
      return null;
    return (T) heap[0].getValue();
  }

  /**
   * Inserts a new element into the heap.
   * 
   * @param value the element to insert
   * 
   * @throws IllegalArgumentException if the value is {@code null} or blank
   * @throws IllegalStateException    if the heap is full
   */
  public synchronized void insert(T value)  {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank.");
    if (size == heap.length)
      throw new IllegalStateException("MinHeap is full.");
    
    heap[size] = new Entry<Integer, T>(Integer.MIN_VALUE, value);
    increaseKey(size, size);
    size++;
  }

  /**
   * Increases the key of an {@code Entry} at the specified index.
   * 
   * @param i      the index of the element whose key is to be increased
   * @param newKey the new key
   * 
   * @throws IllegalArgumentException if the new key is smaller than the current
   *                                  key
   */
  public synchronized void increaseKey(int i, int newKey) {
    if (heap[i].getKey() > newKey)
      throw new IllegalArgumentException("New key is smaller than current key");

    int parent = parent(i);
    heap[i] = new Entry<Integer, T>(newKey, heap[i].getValue());

    while (i > 0 && isLessThan(heap[i], heap[parent])) {
      swap(i, parent);
      i = parent;
      parent = parent(i);
    }
  }
    
  /**
   * Retrieves and removes from the heap the current smallest element in the heap.
   * 
   * @returns the smallest element in the heap
   * 
   * @throws NoSuchElementException if the heap is empty
   */
  public synchronized T extractMin() {
    if (size < 1)
      throw new NoSuchElementException("MinHeap is empty.");

    Entry<Integer, T> min = heap[0];
    heap[0] = heap[--size];
    heap[size] = null;

    minHeapify(0);

    return min.getValue();
  }

  /**
   * Internal method to sift the elements around to retain a tree-like structure
   * where the smaller elements will be placed at a lower position within the
   * heap.
   * 
   * @param i the index of the element to start the process from
   */
  private void minHeapify(int i) {                             
    int l = left(i);
    int r = right(i);
    int smallest = i;
    
    if (l < size && isLessThan(heap[l], heap[i]))
      smallest = l;
    if (r < size && isLessThan(heap[r], heap[smallest]))
      smallest = r;
    if (smallest != i) {
      swap(i, smallest);
      minHeapify(smallest);
    }
  }

  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    for (int i=0; i<heap.length; i++)
      sb.append("\"" + heap[i].getValue() + "\"\n");

    return sb.toString() + "}";
  }
}