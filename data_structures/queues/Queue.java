package data_structures.queues;

import java.util.NoSuchElementException;

/**
 * Creates a basic generic {@code Queue} which can hold any non-{@code null}
 * object values. It is reusable in that once it is filled and then emptied, it
 * can be filled again. It can also be instantiated directly from an array of
 * values or another {@code Queue}.
 */
public final class Queue<T> {
  private T[] queue;
  private int head, tail;

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
   * Determines whether the {@code Queue} is empty or not
   *
   * @return is the {@code Queue} empty
   */
  public boolean isEmpty() {
    return head == tail;
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
   * Checks if the queue is currently full.
   * 
   * @return whether the queue is full
   */
  public boolean isFull() {
    return head == 0 && tail == queue.length - 1;
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
   * </p>
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
    return item;
  }

  /**
   * Returns a string of the contents of the {@code Queue}.
   *
   * @return the object string of the {@code Queue}
   */
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    for (int i = head; i < tail; i++)
      sb.append("\"" + queue[i].toString() + "\"\n");

    return sb.toString() + "}";
  }
}