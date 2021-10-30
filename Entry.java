package data_structures;

/**
 * This class creates entries that hold a key/value pair. It allows 
 * any object of subclass {@code Object} to be used as a key or value,
 * which works for everything since all {@code Object} is the superclass
 * of any object.
 * 
 * @param <K> type parameter for the key. Can hold any {@code Object}.
 * @param <V> type parameter for the value. Can hold any {@code Object}
 */
public class Entry<K, V> {
  private K key;
  private V value;

  public Entry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
    return key + " -> " + value;
  }
}