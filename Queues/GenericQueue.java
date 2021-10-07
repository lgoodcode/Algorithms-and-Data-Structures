package Queues;

interface IGenQueue<T> {
  public abstract void enqueue(T o) throws QueueFullException;
  public abstract T dequeue() throws QueueEmptyException;
}

class QueueFullException extends Exception {
  int size;

  QueueFullException(int s) { size = s; }

  public String toString() {
    return "\nQueue is full. Maxmimum size is: " + size;
  }
}

class QueueEmptyException extends Exception {
  public String toString() {
    return "\nQueue is empty.";
  }
}

class GenericQueue<T> implements IGenQueue<T> {
  private T[] queue;
  private int head, tail;

  // Construct an empty queue
  GenericQueue(T[] arr) {
    this.queue = arr;
    this.head = this.tail = 0;
  }

  // Construct a queue from initial values
  GenericQueue(T[] arr, T[] a) {
    this.queue = arr;

    for (int i=0; i<a.length; ++i) {
      try {
        this.enqueue(a[i]);
      } catch (QueueFullException err) {
        System.out.println(err);
      }
    }
  }

  // Construct a queue from another queue
  GenericQueue(T[] arr, GenericQueue<T> q) {
    this.queue = arr;
    this.head = q.head;
    this.tail = q.tail;

    // Verify the new queue can hold all the elements
    try {
      if (this.queue.length < q.queue.length) {
        throw new QueueFullException(this.queue.length);
      }
    } catch (QueueFullException err) {
      System.out.println(err);
    }

    // Copy elements
    for (int i=0; i<q.queue.length; ++i)
      this.queue[i] = q.queue[i];
  }

  public void enqueue(T item) throws QueueFullException {
    if (this.head == this.queue.length) {
      throw new QueueFullException(this.queue.length);
    }

    this.queue[this.head++] = item;
  }

  public T dequeue() throws QueueEmptyException {
    if (this.head == this.tail) {
      throw new QueueEmptyException();
    }

    return this.queue[this.tail++];
  }
}