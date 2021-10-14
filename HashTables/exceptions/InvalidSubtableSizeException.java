package Hashtables.exceptions;

public final class InvalidSubtableSizeException extends Exception {
  int size;

  public <T extends Number>InvalidSubtableSizeException(T m) {
    super();
    this.size = m.intValue();
  }

  public String toString() {
    return "\nInvalid subtable size \"m\": " + size;
  }
}
