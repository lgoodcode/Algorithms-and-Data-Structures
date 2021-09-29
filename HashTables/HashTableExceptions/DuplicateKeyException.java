package Data_Structures.HashTables.HashTableExceptions;

public final class DuplicateKeyException extends HashTableException {
  int key;

  public DuplicateKeyException(int k) { 
    super();
    key = k;
  }

  public String toString() {
    return "\nDuplicate key used: " + key;
  }
}
