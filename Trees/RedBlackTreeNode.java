package data_structures.trees;

public class RedBlackTreeNode<K, V> extends TreeNode<K, V> {
  protected RedBlackTreeNode<K, V> parent;
  protected RedBlackTreeNode<K, V> left;
  protected RedBlackTreeNode<K, V> right;
  
  /**
   * Used to determine the type of {@code RedBlackTreeNode}. The two types are
   * {@code Black (0)} and {@code Red (1)}.
   */
  protected int color = 0;

  /**
   * Since the {@code RedBlackTree} may need to check the parent of a parent of a
   * node and/or the color of that node, which may not even exist, it requires a
   * circular structure {@code NIL} value which will hold pointers to itself and a
   * default color of {@code Black}.
   * 
   * <p>
   * This is a static property which is used to initialize the
   * {@code RedBlackTree} {@code NIL} value so it is only instantiated once as a
   * constant and then referenced throughout the data struture to reduce memory
   * usage.
   * </p>
   */
  protected static final RedBlackTreeNode<?, ?> NIL = new RedBlackTreeNode<>();
  
  /**
   * Creates a tree node with the specified key and value. It initializes the
   * pointers of the {@code parent}, {@code left}, and {@code right} to an initial
   * {@code NIL} node. This is to prevent {@code NullPointerException} when
   * attempting to access its members if none exist yet.
   * 
   * @param key   the key of the node
   * @param value the value of the node
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public RedBlackTreeNode(K key, V value) {
    super(key, value);
    parent = left = right = new RedBlackTreeNode<>();
  }

  /**
   * Creates a circular {@code NIL} tree node that points to itself and has no key
   * or value. It is simply used as an indicator that no node exists for another
   * {@code RedBlackTreeNode} to prevent errors from occuring and for the tree to
   * perform its operations.
   */
  private RedBlackTreeNode() {
    parent = left = right = this;
  }

  /**
   * Determines whether the current {@code RedBlackTreeNode} is a {@code NIL} node
   * or not since the tree cannot perform relation checks if the a node is
   * {@code null}.
   * 
   * @return if the node is {@code NIL} or not
   */
  protected boolean isNIL() {
    return getKey() == null;
  }

}
