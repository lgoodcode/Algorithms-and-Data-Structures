package data_structures.heaps;

public final class FibonacciNode<K, V> {
  private K key;
  private V value;
  protected FibonacciNode<K, V> parent = null;
  protected FibonacciNode<K, V> child = null;
  protected FibonacciNode<K, V> left = null;
  protected FibonacciNode<K, V> right = null;
  protected int degree = 0;
  protected boolean mark = false;

  public FibonacciNode(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank.");

    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  protected void setKey(K key) {
    this.key = key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
    return "Key: " + key + ", value: " + value;
  }
}