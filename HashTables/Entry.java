package Data_Structures.HashTables;

/**
 * This class creates entries that hold a key/value pair.
 * 
 * @param <V> type parameter for the value to hold any {@code Object}
 */
public final class Entry<V> {
  private final int key;
  private final V value;

  public Entry(int key, V value) {
    this.key = key;
    this.value = value;
  }

  public int getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
    return "\nEntry key: " + key + " value: " + value;
  }
}