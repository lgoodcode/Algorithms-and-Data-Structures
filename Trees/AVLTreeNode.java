package data_structures.trees;

public class AVLTreeNode<K, V> extends AbstractTreeNode<K, V> {
  protected AVLTreeNode<K, V> parent;
  protected AVLTreeNode<K, V> left;
  protected AVLTreeNode<K, V> right;
  
  /**
   * Determines the level of how deep the node resides compared to the root.
   */
  protected int height = 0;

  public AVLTreeNode(K key, V value) {
    super(key, value);
  }
}
