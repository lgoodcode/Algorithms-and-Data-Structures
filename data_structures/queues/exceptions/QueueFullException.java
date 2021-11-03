package data_structures.queues.exceptions;

public final class QueueFullException extends Exception {
  private int size;

  public QueueFullException(int size) { 
    super();
    this.size = size; 
  }

  public String toString() {
    return "\nQueue is full. Maxmimum size is: " + size;
  }
}