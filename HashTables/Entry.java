package Data_Structures.HashTables;

public final class Entry<V> {
  private final int hash, key;
  private final V value;

  public Entry(int hash, int key, V value) {
    this.hash = hash;
    this.key = key;
    this.value = value;
  }

  public Entry(int key, V value) {
    this.hash = -1;
    this.key = key;
    this.value = value;
  }

  public int getHash() {
    return hash;
  }

  public int getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
    return "\nEntry hash: " + hash + " key: " + key + " value: " + value;
  }
}