package data_structures.heaps.exceptions;

public final class HeapEmptyException extends Exception {
  public HeapEmptyException() { super(); }

  public String toString() {
    return "\nHeap is empty.";
  }
}
