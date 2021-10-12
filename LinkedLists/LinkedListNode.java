package LinkedLists;

public class LinkedListNode<K, V> {
  private K key;
  private V value;
  protected LinkedListNode<K, V> next = null;

  public LinkedListNode(K key, V value) {
    if (key.equals(null) || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value.equals(null) || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

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
    return "Key: " + key + ", value: " + value;
  }
}
