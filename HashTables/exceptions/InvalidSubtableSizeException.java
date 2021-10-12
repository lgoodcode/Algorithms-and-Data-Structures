package HashTables.exceptions;

public final class InvalidSubtableSizeException extends HashTableException {
  int size;

  public <T extends Number>InvalidSubtableSizeException(T m) {
    super();
    this.size = m.intValue();
  }

  public String toString() {
    return "\nInvalid subtable size \"m\": " + size;
  }
}
