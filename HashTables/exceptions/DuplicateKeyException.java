package Hashtables.exceptions;

public final class DuplicateKeyException extends Exception {
  Object key;
  String str;

  public DuplicateKeyException(Object key) { 
    super();
    this.key = key;
  }

  public DuplicateKeyException(String str) {
    super();
    this.str = str;
  }

  public String toString() {
    if (key != null)
      return "\nDuplicate key used: " + key;
    return str;
  }
}
