package data_structures.trees;


public class BTreeNode<K, V> {
  /**
   * The child {@code BTreeNodes}.
   */
  protected BTreeNode<K, V>[] children;
  
  /**
   * The number of keys currently stored under this node.
   */
  protected int count;

  /**
   * Whether this node is a leaf or internal node.
   */
  protected boolean leaf; 

  /**
   * The actual keys 
   */
  protected K[] keys;

  /**
   * The values
   */
  protected V[] values;

  @SuppressWarnings("unchecked")
  public BTreeNode(int t) {
    if (t < 1)
      throw new IllegalArgumentException("Minimum degree must be >= 2");
    
    int full = 2 * t - 1;
    children = (BTreeNode<K, V>[]) new BTreeNode<?, ?>[2 * t]; 
    keys = (K[]) new Object[full];
    values = (V[]) new Object[full];
    count = 0;
    leaf = true;
  }

  public K getMinKey() {
    return keys[0];
  }

  public K getMaxKey() {
    return count > 0 ? keys[count - 1] : null;
  }

  public V getMinValue() {
    return values[0];
  }

  public V getMaxValue() {
    return count > 0 ? values[count - 1] : null;
  }

}
