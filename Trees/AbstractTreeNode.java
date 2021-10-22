package data_structures.trees;

public abstract class AbstractTreeNode<K, V> {
  private K key;
  private V value;

  /**
   * Default constructor that initializes a tree node with the specified key and
   * value.
   * 
   * @param key   the key of the node
   * @param value the value of the node
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  protected AbstractTreeNode(K key, V value) {
    if (key.equals(null) || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value.equals(null) || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    this.key = key;
    this.value = value;
  }

  protected AbstractTreeNode() {}

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
    return "\"" + key + " -> " + value + "\"";
  }
}