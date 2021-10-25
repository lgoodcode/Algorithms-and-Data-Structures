package data_structures.heaps;

import data_structures.heaps.exceptions.*;

public class MinHeap<T> {
  private Entry<?>[] heap;
  private int size = 0;

  public MinHeap(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size, must be greater than 1.");
    heap = new Entry<?>[size];
  }

  public MinHeap(T[] arr) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    heap = new Entry<?>[arr.length];

    for (int i=0; i<arr.length; i++) {
      try {
        insert(arr[i]);
      } catch (HeapFullException e) {}
    }
  }

  public MinHeap(MinHeap<T> h) {
    heap = h.heap;
    size = h.size;
  }

  private synchronized void swap(int a, int b) {
    Entry<?> temp = heap[a];
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

  @SuppressWarnings("unchecked")
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
    
    heap[size] = new Entry<T>(Integer.MIN_VALUE, value);
    increaseKey(size, value.hashCode());
    size++;
  }

  public synchronized void increaseKey(int i, int newKey) {
    if (!heap[i].isLessThan(newKey))
      throw new IllegalArgumentException("New key is smaller than current key");

    int parent = parent(i);
    heap[i].key = newKey;

    while (i > 0 && heap[parent].key > heap[i].key) {
      swap(i, parent);
      i = parent;
      parent = parent(i);
    }
  }
    
  @SuppressWarnings("unchecked")
  public synchronized T extractMin() throws HeapEmptyException {
    if (size < 1)
      throw new HeapEmptyException();

    Entry<T> min = (Entry<T>) heap[0];

    heap[0] = heap[--size];

    heap[size] = null;

    minHeapify(0);

    return min.getValue();
  }

  private synchronized void minHeapify(int i) {                             
    int l = left(i);
    int r = right(i);
    int smallest = i;
    
    if (l < size && heap[l].isLessThan(heap[i]))
      smallest = l;
    if (r < size && heap[r].isLessThan(heap[smallest]))
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

  /**
   * A nested inner class that will be used to hold values for the {@code MinHeap}.
   */
  static class Entry<T> {
    private int key;
    private T value;
  
    public Entry(int key, T value) {
      this.key = key;
      this.value = value;
    }

    public boolean isLessThan(Entry<?> e) {
      return key < e.key;
    }

    public boolean isLessThan(int key) {
      return this.key < key;
    }
  
    public T getValue() {
      return value;
    }
  
    public String toString() {
      return value.toString();
    }
  }
}