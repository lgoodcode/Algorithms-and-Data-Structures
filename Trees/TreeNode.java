package data_structures.trees;

public class TreeNode<K, V> {
  protected TreeNode<K, V> parent;
  protected TreeNode<K, V> left;
  protected TreeNode<K, V> right;
  private K key;
  private V value;

  /**
   * Constructor that initializes a tree node with the specified key and value.
   *
   * @param key   the key of the node
   * @param value the value of the node
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public TreeNode(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    this.key = key;
    this.value = value;
  }

  /**
   * Default constructor used for {@code RedBlackTree} sentinel.
   */
  protected TreeNode() {}

  public final K getKey() {
    return key;
  }

  public final V getValue() {
    return value;
  }

  public final String toString() {
    return key + " -> " + value;
  }
}