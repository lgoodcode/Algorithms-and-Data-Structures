package data_structures.queues;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.EmptyIterator;

/**
 * Creates a basic generic {@code Queue} which can hold any non-{@code null}
 * object values. It is reusable in that once it is filled and then emptied, it
 * can be filled again. It can also be instantiated directly from an array of
 * values or another {@code Queue}.
 */
public final class Queue<T> implements java.io.Serializable {
  @java.io.Serial
  private static final long serialVersionUID = 199208284839394805L;

  /**
   * Holds the elements in the queue.
   */
  private transient T[] queue;

  /**
   * The markers for the current first item of the queue to be dequeued and the
   * position of the last item in the queue.
   */
  private transient int head, tail;

  /**
   * The number of times this Queue has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the Queue fail-fast.
   *
   * @see ConcurrentModificationException
   */
  private transient int modCount;

  /**
   * Creates a {@code queue} of specified size.
   *
   * @param size the capacity of the {@code queue}
   *
   * @throws IllegalArgumentException if the size is less than 1
   */
  @SuppressWarnings("unchecked")
  public Queue(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size given (>= 1).");

    queue = (T[]) new Object[size];
    head = tail = 0;
  }

  /**
   * Creates a {@code Queue} from a given array of values. The capacity will be
   * equal to the number of items contained in the array.
   *
   * @param arr the array of values to initialize the {@code Queue} with
   *
   * @throws IllegalArgumentException if the array is empty
   */
  @SuppressWarnings("unchecked")
  public Queue(T[] arr) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    queue = (T[]) new Object[arr.length];
    head = tail = 0;

    for (int i = 0; i < arr.length; ++i)
      enqueue(arr[i]);
  }

  /**
   * Constructs a {@code Queue} from another {@code Queue} instance. Will copy all
   * it's values.
   *
   * @param queue the {@code Queue} instance to copy
   *
   * @throws NullPointerException if the queue is {@code null}
   */
  public Queue(Queue<T> queue) {
    if (queue == null)
      throw new NullPointerException("Queue cannot be null.");

    this.queue = queue.queue;
    head = queue.head;
    tail = queue.tail;
  }

  /**
   * Returns the number of items currently stored in the {@code Queue}.
   *
   * @return the number of items in the {@code Queue}
   */
  public int size() {
    return tail - head;
  }

  /**
   * Returns the capacity of the {@code Queue}
   *
   * @return the capacity of the {@code Queue}
   */
  public int capacity() {
    return queue.length;
  }
  /**
   * Determines whether the {@code Queue} is empty or not
   *
   * @return is the {@code Queue} empty
   */
  public boolean isEmpty() {
    return head == tail;
  }

  /**
   * Checks if the queue is currently full.
   *
   * @return whether the queue is full
   */
  public boolean isFull() {
    return head == 0 && tail == queue.length;
  }

  /**
   * Clears all elements from the queue.
   */
  public void clear() {
    for (int i = head; i < tail; i++)
      queue[i] = null;
    head = tail = 0;
    modCount++;
  }

  /**
   * Returns the next item to be dequeued
   *
   * @return the next item to be dequeued or {@code null} if none
   */
  public T peek() {
    return queue[head];
  }

  /**
   * Returns the last item in the queue
   *
   * @return the next last item in the queue or {@code null} if none
   */
  public T peekLast() {
    return isEmpty() ? null : queue[tail-1];
  }

  /**
   * Checks whether the specified item exists in the queue or not.
   *
   * @param item the item to check
   * @return whether the item exists or not in the queue
   */
  public boolean has(T item) {
    for (int i = head; i < tail; i++)
      if (queue[i] == item)
        return true;
    return false;
  }

  /**
   * Inserts an item into the {@code Queue}. If the {@code head} and {@code tail}
   * properties of are both at the capacity and there is no item stored at that
   * position, then the {@code Queue} is empty and will reset the counters back to
   * {@code 0} to start again.
   *
   * <p>
   * Performs a check when the {@code tail} reaches the internal queue array
   * length. If the {@code head} position is at {@code 0}, then the queue is full.
   * Otherwise, there is still space in the queue and shifts all the elements back
   * at the bottom so more elements can be queued without an exception being
   * thrown falsely.
   *
   * @param item the item to insert into the queue
   *
   * @throws IllegalArgumentException if the supplied item is {@code null} or
   *                                  blank
   * @throws IllegalStateException    if attempting to insert an item when the
   *                                  queue is full
   */
  public synchronized void enqueue(T item) {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or blank.");
    if (head == tail && (head == queue.length || queue[head] == null))
      head = tail = 0;
    else if (tail == queue.length) {
      if (head == 0)
        throw new IllegalStateException("Queue capacity exceeded.");

      int i, j;
      for (i = head, j = 0; i < tail; i++, j++) {
        queue[j] = queue[i];
        queue[i] = null;
      }

      head = 0;
      tail = j;
    }

    queue[tail++] = item;
    modCount++;
  }

  /**
   * Returns the next item from the {@code Queue}.
   *
   * @return the next item from the {@code Queue}
   *
   * @throws NoSuchElementException if attempting to dequeue an item while empty
   */
  public synchronized T dequeue() {
    if (isEmpty())
      throw new NoSuchElementException("No items in queue.");

    T item = queue[head];

    queue[head] = null;

    if (head == tail)
      head = tail = 0;
    else
      head++;

    modCount++;
    return item;
  }

  /**
   * Returns an array containing all of the elements in this queue
   * in proper sequence (from first to last element).
   *
   * <p>The returned array will be "safe" in that no references to it are
   * maintained by this queue.  (In other words, this method must allocate
   * a new array).  The caller is thus free to modify the returned array.
   *
   * <p>This method acts as bridge between array-based and collection-based
   * APIs.
   *
   * @return an array containing all of the elements in this queue
   *         in proper sequence
   */
  public Object[] toArray() {
    Object[] arr = new Object[size()];

    for (int i = head, j = 0; i < tail; i++, j++)
      arr[j] = queue[i];
    return arr;
  }

  /**
   * Returns a string of the contents of the {@code Queue}.
   *
   * @return the object string of the {@code Queue}
   */
  public String toString() {
    if (isEmpty())
      return "[]";

    StringBuilder sb = new StringBuilder("[");

    for (int i = head, len = tail - 1; i < len; i++)
      sb.append(queue[i].toString() + ", ");
    sb.append(queue[tail-1].toString() + "]");

    return sb.toString();
  }

  /**
   * Saves the state of this {@code Queue} instance to a stream (that is,
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
    stream.writeInt(size());

    // Write out all elements in the proper order.
    for (int i = head; i < tail; i++)
      stream.writeObject(queue[i]);
  }

  /**
   * Reconstitutes this {@code Queue} instance from a stream (that is,
   * deserializes it).
   */
  @SuppressWarnings("unchecked")
  @java.io.Serial
  private void readObject(java.io.ObjectInputStream stream)
throws java.io.IOException, ClassNotFoundException {
    // Read in any hidden serialization magic
    stream.defaultReadObject();

    // Read in size
    int size = stream.readInt();

    // Read in all elements in the proper order.
    for (int i = 0; i < size; i++)
      enqueue((T) stream.readObject());
  }

  /**
   * Returns an {@link Iterable} of the elements in the queue
   *
   * @return the {@code Iterable}
   */
  public final Iterable<T> iterable() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * Returns an {@link Iterator} of the elements in the queue
   *
   * @return the {@code Iterator}
   */
  public final Iterator<T> iterator() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * A queue iterator class. This class implements the interfaces. It simply
   * keeps a reference to a single {@link Node} to retain the current position and
   * not have to sequentially have to find the next node from the start or end
   * again.
   *
   * <p>
   * Doesn't need to be generic or static because it will be accessing the
   * queue directly, not creating a list of entries
   * </p>
   *
   * <p>
   * Will throw a {@link ConcurrentModificationException} if the queue was
   * modified outside of the iterator.
   * </p>
   */
  private class Itr implements Iterator<T>, Iterable<T> {
    /**
     * Tracks the current node index position.
     */
    int cursor = head;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = modCount;

    /**
     * Tracks whether the iterator has processed an element for there to be a
     * previous element and if that element hasn't been removed.
     */
    boolean last = false;

    public Iterator<T> iterator() {
      return this;
    }

    public boolean hasNext() {
      return cursor < queue.length && queue[cursor] != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    public T next() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("Queue Iterator");
      last = true;
      return queue[cursor++];
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
        throw new IllegalStateException("Queue Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the queue object while removing entry
      synchronized (Queue.this) {
        // Shift all items down one
        for (int i = cursor - 1, len = tail - 1; i < len; i++)
          queue[i] = queue[i+1];
        queue[--tail] = null;
        cursor--;

        expectedModCount = modCount;
        last = false;
      }
    }

  }
}