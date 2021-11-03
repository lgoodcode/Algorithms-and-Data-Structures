package data_structures.queues;

public abstract class AbstractQueue<T> {
  protected T[] queue;
  protected int head, tail;

  /**
   * Determines whether the {@code Queue} is empty or not
   *
   * @return is the {@code Queue} empty
   */
  public boolean isEmpty() {
    return head == tail && head == 0;
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
}
