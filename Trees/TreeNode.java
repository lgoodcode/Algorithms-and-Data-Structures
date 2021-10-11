package Trees;

public class TreeNode<K, V> extends AbstractTreeNode<K, V> {
  public TreeNode<K, V> parent = null;
  public TreeNode<K, V> left = null;
  public TreeNode<K, V> right = null;
  private K key;
  private V value;

  public TreeNode(K key, V value) {
    super();

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