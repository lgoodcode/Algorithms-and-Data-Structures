package data_structures.heaps.exceptions;

public final class HeapFullException extends Exception {
  int size;

  public HeapFullException(int size) {
    super();
    this.size = size;
  }

  public String toString() {
    return "\nHeap is full. Max capacity is: " + size;
  }
}
