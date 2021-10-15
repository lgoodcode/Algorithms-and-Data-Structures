package Queues;

import Queues.exceptions.*;

/**
 * Creates a basic generic queue which can hold any non-{@code null} object
 * values. It is reusable in that once it is filled and then emptied, it can be
 * filled again. It can also be instantiated directly from an array of values or
 * another {@code Queue}.
 */
public class Queue<T> {
  private Entry<?>[] queue;
  private int head, tail;

  /**
   * Creates a queue of specified size.
   * 
   * @param size the capacity of the queue
   * 
   * @throws IllegalArgumentException if the size is less than 1
   */
  public Queue(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size given (>= 1).");

    queue = new Entry<?>[size];
    head = tail = 0;
  }

  /**
   * Creates a queue from a given array of values. The capacity will be equal to
   * the number of items contained in the array.
   * 
   * @param arr the array of values to initialize the queue with
   * 
   * @throws IllegalArgumentException if the array is empty
   */
  public Queue(T[] arr) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    queue = new Entry<?>[arr.length];
    head = tail = 0;

    for (int i=0; i<arr.length; ++i) {
      try {
        enqueue(arr[i]);
      } catch (QueueFullException e) {}
    }
  }

  /**
   * Constructs a queue from another {@code Queue} instance. Will copy all it's
   * values.
   * 
   * @param q the {@code Queue} instance to copy
   */
  public Queue(Queue<T> q) {
    queue = q.queue;
    head = q.head;
    tail = q.tail;
  }

  /**
   * Determines whether the queue is empty or not
   * 
   * @return is the queue empty
   */
  public synchronized boolean isEmpty() {
    return head == tail && head == 0;
  }

  /**
   * Returns the capacity of the queue
   * 
   * @return the capacity of the queue
   */
  public synchronized int size() {
    return queue.length;
  }

  /**
   * Inserts an item into the queue. If the head and tail properties of the queue
   * are both at the queue capacity, then the queue is empty and will reset the
   * counters back to 0 to start again.
   * 
   * @param item the item to insert into the queue
   * 
   * @throws QueueFullException       if attempting to insert an item when the
   *                                  queue is full
   * 
   * @throws IllegalArgumentException if the supplied item is {@code null} or
   *                                  blank
   */
  public synchronized void enqueue(T item) throws QueueFullException {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or blank.");
    if (head == tail)
      head = tail = 0;
    else if (tail == queue.length)
      throw new QueueFullException(queue.length);

    queue[tail++] = new Entry<T>(item);
  }

  /**
   * Returns the next item from the queue.
   * 
   * @return the next item from the queue
   * 
   * @throws QueueEmptyException if attempting to dequeue an item while the queue
   *                             is empty
   */
  @SuppressWarnings("unchecked")
  public synchronized T dequeue() throws QueueEmptyException {
    Entry<T> item = (Entry<T>) queue[head];

    if (item == null)
      throw new QueueEmptyException();

    queue[head] = null;

    if (head == tail)
      head = tail = 0;
    else
      head++;
    return item.getValue();
  }

  /**
   * Returns a string of the contents of the {@code Queue} in a JSON format.
   * 
   * @return the object string
   */
  public synchronized String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    for (int i=head; i<tail; i++)
      sb.append("\"" + queue[i].toString() + "\"\n");
    
    return sb.toString() + "}";
  }

  /**
   * A nested inner class that will be used to hold values for the {@code Queue}.
   */
  static class Entry<T> {
    private T value;
  
    public Entry(T value) {
      this.value = value;
    }
  
    public T getValue() {
      return value;
    }
  
    public String toString() {
      return value.toString();
    }
  }
}