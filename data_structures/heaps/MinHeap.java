package data_structures.heaps;

import java.util.function.BiFunction;

import data_structures.Entry;
import data_structures.heaps.exceptions.*;

public class MinHeap<T> {
  private BiFunction<T, T, Boolean> compare;
  private Entry<Integer, T>[] heap;
  private int size = 0;

  @SuppressWarnings("unchecked")
  public MinHeap(int size, BiFunction<T, T, Boolean> compare) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size, must be greater than 1.");
    heap = (Entry<Integer, T>[]) new Entry<?, ?>[size];
    this.compare = compare;
  }

  public MinHeap(int size) {
    this(size, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  @SuppressWarnings("unchecked")
  public MinHeap(T[] arr) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    heap = (Entry<Integer, T>[]) new Entry<?, ?>[arr.length];

    for (int i = 0; i < arr.length; i++) {
      try {
        insert(arr[i]);
      } catch (HeapFullException e) {}
    }
  }

  public MinHeap(MinHeap<T> h) {
    heap = h.heap;
    size = h.size;
  }

  private boolean isLessThan(Entry<Integer, T> x, Entry<Integer, T> y) {
    return compare.apply(x.getValue(), y.getValue());
  }

  private synchronized void swap(int a, int b) {
    Entry<Integer, T> temp = heap[a];
    heap[a] = heap[b];
    heap[b] = temp;
  }

  private synchronized int left(int i) {
    return 2 * i;
  }
    
  private synchronized int right(int i) {
    return 2 * i + 1; 
  }
    
  private synchronized int parent(int i) {
    return (int) Math.floor(i / 2);
  }

  public synchronized boolean isEmpty() {
    return size == 0;
  }

  public synchronized int size() {
    return size;
  }

  public int capacity() {
    return heap.length;
  }

  public synchronized T minimum() {
    if (isEmpty())
      return null;
    return (T) heap[0].getValue();
  }

  public synchronized void insert(T value) throws HeapFullException {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank.");
    if (size == heap.length)
      throw new HeapFullException(size);
    
    heap[size] = new Entry<Integer, T>(Integer.MIN_VALUE, value);
    increaseKey(size, size);
    size++;
  }

  public synchronized void increaseKey(int i, int newKey) {
    if (heap[i].getKey() > newKey)
      throw new IllegalArgumentException("New key is smaller than current key");

    int parent = parent(i);
    heap[i] = new Entry<Integer, T>(newKey, heap[i].getValue());

    while (i > 0 && heap[parent].getKey() > heap[i].getKey()) {
      swap(i, parent);
      i = parent;
      parent = parent(i);
    }
  }
    
  public synchronized T extractMin() throws HeapEmptyException{
    if (size < 1)
      throw new HeapEmptyException();

    Entry<Integer, T> min = heap[0];
    heap[0] = heap[--size];
    heap[size] = null;

    minHeapify(0);

    return min.getValue();
  }

  private synchronized void minHeapify(int i) {                             
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
      sb.append("\"" + heap[i].toString() + "\"\n");

    return sb.toString() + "}";
  }
}