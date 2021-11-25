package data_structures.heaps;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.EmptyIterator;

/**
 * Simple Binary Minimum Heap implementation.
 */
public final class MinHeap<T> implements java.io.Serializable {
  @java.io.Serial
  private static final long serialVersionUID = 199208284839394831L;

  /**
   * The comparison function used to determine whether an element is less than
   * another to retain the minimum for the extractMin operation.
   */
  private transient Comparator<? super T> compare;

  /**
   * The array to hold the elements in the heap.
   */
  private transient T[] heap;

  /**
   * The number of elements in the heap.
   */
  private transient int size = 0;

  /**
   * The number of times this heap has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the heap fail-fast.
   *
   * @see ConcurrentModificationException
   */
  private transient int modCount = 0;

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
  public MinHeap(int capacity, Comparator<? super T> compare) {
    if (capacity < 1)
      throw new IllegalArgumentException("Illegal capacity, must be greater than 1.");
    heap = (T[]) new Object[capacity];
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
    this(capacity, (Comparator<T>) (T x, T y) -> x.hashCode() < y.hashCode() ? -1 : x.hashCode() > y.hashCode() ? 1 : 0);
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

    heap = (T[]) new Object[array.length];

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
      throw new NullPointerException("Heap cannot be null.");

    this.heap = heap.heap;
    compare = heap.compare;
    size = heap.size;
  }

  /**
   * The internal method that uses the compare function to determine which element
   * is lesser.
   *
   * @param x the first entry to compare
   * @param y the second entry to compare
   * @return whether the first entry is less than the other
   */
  private boolean isLessThan(T x, T y) {
    return compare.compare(x, y) < 0;
  }

  /**
   * Swap two heap elements positions.
   *
   * @param a first item index
   * @param b second item index
   */
  private void swap(int a, int b) {
    T temp = heap[a];
    heap[a] = heap[b];
    heap[b] = temp;
  }

  private int left(int i) {
    return i << 1; // 2 * i
  }

  private int right(int i) {
    return (i << 1) + 1; // 2 * i + 1;
  }

  private int parent(int i) {
    return i >>> 1; // floor(i / 2)
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
   * Determines whether the heap is at max capacity or not
   *
   * @return whether the heap is full
   */
  public boolean isFull() {
    return size == heap.length;
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
   * Removes all the elements in the heap.
   */
  public void clear() {
    for (int i = 0; i < size; i++)
      heap[i] = null;
    size = 0;
    modCount++;
  }

  /**
   * Returns the minimum element in the heap without removing it.
   *
   * @return the minimum element or {@code null} if empty
   */
  public T peek() {
    return isEmpty() ? null : (T) heap[0];
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

    int k = size, parent = parent(k);
    heap[k] = value;

    while (k > 0 && isLessThan(heap[k], heap[parent])) {
      swap(k, parent);
      k = parent;
      parent = parent(k);
    }

    size++;
    modCount++;
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

    T min = heap[0];
    heap[0] = heap[--size];
    heap[size] = null;

    siftDown(0);
    modCount++;
    return min;
  }

  /**
   * Internal method to sift the elements around to retain a tree-like structure
   * where the smaller elements will be placed at a lower position within the
   * heap.
   *
   * @param i the index of the element to start the process from
   */
  private void siftDown(int i) {
    int l = left(i);
    int r = right(i);
    int smallest = i;

    if (l < size && isLessThan(heap[l], heap[i]))
      smallest = l;
    if (r < size && isLessThan(heap[r], heap[smallest]))
      smallest = r;
    if (smallest != i) {
      swap(i, smallest);
      siftDown(smallest);
    }
  }

  /**
   * Removes the element at the specified position.
   *
   * @param index the position of the element to remove
   *
   * @throws IndexOutOfBoundsException if the specified index is less than
   *                                   {@code 0} or greater than the heap capacity
   */
  public synchronized void removeAt(int index) {
    if (index < 0)
      throw new IndexOutOfBoundsException("Index cannot be less than 0.");
    if (index >= size)
      throw new IndexOutOfBoundsException("Index " + index + "exceeds heap length of " + capacity());

    int s = --size;

    // If removing the last item
    if (s == index)
      heap[index] = null;
    // Otherwise, place last item at removed position and siftDown
    else {
      heap[index] = heap[s];
      heap[s] = null;
      siftDown(index);
    }

    modCount++;
  }

  /**
   * Returns the heap string elements in an array format.
   *
   * @return the heap string in an array format
   */
  public String toString() {
    if (isEmpty())
      return "[]";

    StringBuilder sb = new StringBuilder("[");

    for (int i=0; i<heap.length; i++)
      sb.append(heap[i] + ", ");
    return sb.delete(sb.length() - 2, sb.length()) + "]";
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element).
   *
   * <p>
   * The returned array will be "safe" in that no references to it are maintained
   * by this list. (In other words, this method must allocate a new array). The
   * caller is thus free to modify the returned array.
   * </p>
   *
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   * </p>
   *
   * @return an array containing all of the elements in this list in proper
   *         sequence
   */
  public Object[] toArray() {
    if (isEmpty())
      return new Object[0];

    Object[] arr = new Object[size];

    for (int i = 0, len = size; i < len; i++)
      arr[i] = heap[i];
    return arr;
  }

  /**
   * Saves the state of this {@code LinkedList} instance to a stream (that is,
   * serializes it).
   *
   * @param stream the {@link java.io.ObjectOutputStream} to write to
   *
   * @throws java.io.IOException if serialization fails
   *
   * @serialData The size of the list (the number of elements it contains) is
   *             emitted (int), followed by all of its elements (each an Object)
   *             in the proper order.
   */
  @java.io.Serial
  private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
    // Write out any hidden serialization magic
    stream.defaultWriteObject();

    // Write out size
    stream.writeInt(size);

    if (isEmpty())
      return;

    // Write out all elements
    for (int i = 0, len = size; i < len; i++)
      stream.writeObject(heap[i]);
  }

  /**
   * Reconstitutes this {@code MinHeaep} instance from a stream (that is,
   * deserializes it).
   */
  @java.io.Serial
  @SuppressWarnings("unchecked")
  private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
    // Read in any hidden serialization magic
    stream.defaultReadObject();

    // Read in size
    int size = stream.readInt();

    // Read in all elements
    for (int i = 0; i < size; i++)
      insert((T) stream.readObject());
  }

  /**
   * Returns an {@link Iterable} of the elements in the MinHeap
   *
   * @return the {@code Iterable}
   */
  public Iterable<T> iterable() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * Returns an {@link Iterator} of the elements in the MinHeap
   *
   * @return the {@code Iterator}
   */
  public Iterator<T> iterator() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * A MinHeap iterator class. This class implements the {@link Iterator} and
   * {@link Iterable} interfaces. It uses a {@link Queue} to walk the heap and
   * hold all the elements because of the CircularLinkedList internal structure,
   * it would be difficult to directly iterate through the nodes.
   *
   * <p>
   * Will throw a {@link ConcurrentModificationException} if the MinHeap was
   * modified outside of the iterator.
   * </p>
   */
  private class Itr implements Iterator<T>, Iterable<T> {
    /**
     * The current position of the iterator in the heap.
     */
    int cursor = 0;

    /**
     * Whether the iterator has a returned element to remove.
     */
    boolean last = false;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = modCount;

    public Iterator<T> iterator() {
      return this;
    }

    public boolean hasNext() {
      return cursor < size && heap[cursor] != null;
    }

    /**
     * Returns the next element, if it has one to provide.
     *
     * @return the next element
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    public T next() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("MinHeap Iterator");
      last = true;
      return heap[cursor++];
    }

    /**
     * {@inheritDoc}
     *
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     *
     * @throws IllegalStateException if the {@code next} method has not yet been
     *         called, or the {@code remove} method has already been called after
     *         the last call to the {@code next} method.
     *
     * @throws ConcurrentModificationException if a function modified this map
     *         during computation.
     */
    @Override
    public void remove() {
      if (!last)
        throw new IllegalStateException("MinHeap Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the minheap object while removing entry
      synchronized (MinHeap.this) {
        // Decrement cursor to get new position after removal
        removeAt(--cursor);
        last = false;
        expectedModCount++;
      }
    }
  }
}