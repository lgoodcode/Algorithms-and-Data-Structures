package data_structures.trees;

public class TreeNode<K, V> extends AbstractTreeNode<K, V> {
  protected TreeNode<K, V> parent = null;
  protected TreeNode<K, V> left = null;
  protected TreeNode<K, V> right = null;

  public TreeNode(K key, V value) {
    super(key, value);
  }
}