package Queues.exceptions;

public class QueueEmptyException extends Exception {
  public QueueEmptyException() { super(); }

  public String toString() {
    return "\nQueue is empty.";
  }
}
