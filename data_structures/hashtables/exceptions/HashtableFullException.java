package data_structures.hashtables.exceptions;

public class HashtableFullException extends Exception {
  private int size;

  public HashtableFullException(int size) {
    super();
    this.size = size;
  }

  public String toString() {
    return "\nMaximum hashtable capacity reached of " + size;
  }
}
