package data_structures.queues;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class MinPriorityQueue<T> {
  private BiFunction<T, T, Boolean> compare;
  private Entry<?>[] heap;
  private int size = 0;

  public MinPriorityQueue(int size, BiFunction<T, T, Boolean> compare) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size, must be greater than 1.");
    heap = new Entry<?>[size];
    this.compare = compare;
  }

  public MinPriorityQueue(int size) {
    this(size, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  public MinPriorityQueue(T[] arr) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    heap = new Entry<?>[arr.length];

    for (int i=0; i<arr.length; i++)
      insert(arr[i]);
  }

  public MinPriorityQueue(MinPriorityQueue<T> h) {
    heap = h.heap;
    size = h.size;
  }

  @SuppressWarnings("unchecked")
  private boolean isLessThan(Entry<?> x, Entry<?> y) {
    return compare.apply((T) x.value, (T) y.value);
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
    return (T) heap[0].value;
  }

  public synchronized void insert(T value){
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank.");
    if (size == heap.length)
      throw new IllegalStateException("MinPriorityQueue is full.");
    
    heap[size] = new Entry<T>(Integer.MIN_VALUE, value);
    increaseKey(size, size);
    size++;
  }

  public synchronized void increaseKey(int i, int newKey) {
    if (heap[i].key > newKey)
      throw new IllegalArgumentException("New key is smaller than current key");

    int parent = parent(i);
    heap[i].key = newKey;

    while (i > 0 && heap[parent].key > heap[i].key) {
      swap(i, parent);
      i = parent;
      parent = parent(i);
    }
  }
    
  /**
   * Because it is a priority queue and the items in the queue can be modified,
   * we have to double check the new min value after the minHeapify operation.
   */
  @SuppressWarnings("unchecked")
  public synchronized T extractMin() {
    if (size < 1)
      throw new NoSuchElementException("MinPriorityQueue is empty.");

    Entry<T> temp, min = (Entry<T>) heap[0];
    heap[0] = heap[--size];
    heap[size] = null;

    minHeapify(0);

    if (!isEmpty() && isLessThan(heap[0], min)) {
      temp = (Entry<T>) heap[0];
      heap[0] = min;
      min = temp;
    }

    return min.value;
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

  /**
   * A nested inner class that will be used to hold key and value for the
   * {@code MinHeap} entries.
   */
  private static class Entry<T> {
    private int key;
    private T value;
  
    protected Entry(int key, T value) {
      this.key = key;
      this.value = value;
    }
  
    public String toString() {
      return value.toString();
    }
  }
}
