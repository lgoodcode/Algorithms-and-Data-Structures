package Data_Structures.HashTables;

public class DuplicateKeyException extends Exception {
  Number key;

  public DuplicateKeyException(Number k) { key = k; }

  public String toString() {
    return "\nDuplicate key used: " + key;
  }
}
