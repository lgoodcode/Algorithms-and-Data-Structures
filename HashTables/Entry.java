package Data_Structures.HashTables;

public class Entry<K extends Number, V> {
  private final int hash;
  private final K key;
  private final V value;

  public Entry(int hash, K key, V value) {
    this.hash = hash;
    this.key = key;
    this.value = value;
  }

  public int getHash() {
    return hash;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }
}