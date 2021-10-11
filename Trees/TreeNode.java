package Trees;

public class TreeNode<K, V> {
  TreeNode<K, V> parent = null;
  TreeNode<K, V> left = null;
  TreeNode<K, V> right = null;
  private K key;
  private V value;

  TreeNode(K key, V value) {
    if (key.equals(null) || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value.equals(null) || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return this.key;
  }

  public V getValue() {
    return this.value;
  }

  public String toString() {
    return "Key: " + key + ", value: " + value;
  }
}