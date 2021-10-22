package data_structures.trees;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * The AVL tree is a self-balancing tree. The heights of two child subtrees of
 * any node differ by at most one; if at any time they differ by more than one,
 * rebalancing is done to restore this property. Lookup, insertion, and deletion
 * all take {@code O(log2 n)} time in both average and worst cases.
 *
 * <p>
 * AVL trees are often compared with Red-Black trees because both support the
 * same set of operations and take {@code O(log2 n)} time for the basic
 * operations.
 * </p>
 * 
 * <p>
 * For lookup-intensive applications, the AVL trees are faster than Reb-Black
 * trees because they are more strictly balanced.
 * </p>
 *
 * <p>
 * A binary tree is an AVL tree if the balanceFactor of every node in the tree
 * is in the range of {@code [-1, 1]}
 * </p>
 *
 * <p>
 * A node with a balanceFactor {@code < 0} is called "left-heavy", balanceFactor
 * {@code > 0} is called "right-heavy" and a balanceFactor of {@code 0} is
 * "balanced".
 * </p>
 */
public class AVLTree<K, V> extends AbstractTree<K, V> {
  /**
   * The root of the tree
   */
  private AVLTreeNode<K, V> root = null;

  /**
   * Constant used for {@link #retracing()} in {@link #insert()} with a key.
   */
  private final int INSERT = 0;

  /**
   * Constant used for {@link #retracing()} in {@link #delete()} without a key.
   */
  private final int DELETE = 1;


  /**
   * Creates an empty, BinaryTree, using the specified compare function to
   * determine whether a given {@code AVLTreeNode} is smaller than another.
   * 
   * @param compareFn an anonymous function that compares two {@code AVLTreeNode}
   *                  objects
   */
  public AVLTree(BiFunction<K, K, Boolean> compareFn) {
    super(compareFn);
  }

  /**
   * The default constructor that uses the default compare function and calls the
   * main constructor.
   */
  public AVLTree() {
    super();
  }

  /**
   * The internal compare method used to determine if the key of a
   * tree node is smaller than the other tree node key.
   * 
   * @param x tree node to compare
   * @param y the other tree node to compare
   * @return whether the first node key is smaller than the other node key
   * 
   * @throws NullPointerException     if the either node is {@code null}
   * @throws IllegalArgumentException if either key is {@code null} or blank
   */ 
  private boolean isLessThan(AVLTreeNode<K, V> x, AVLTreeNode<K, V> y) {
    if (x == null || y == null)
      throw new NullPointerException("Node cannot be null.");
    return isLessThan(x.getKey(), y.getKey());
  }  

  /**
   * The Balance Factor of a node is defined to be the height difference of its
   * two child sub-trees.
   * 
   * @param node the node whose balance factor we want to get
   * @return the balance factor of the node
   */
  private int balanceFactor(AVLTreeNode<K, V> node) {
    return height(node.left) - height(node.right);
  }

  /**
   * Determines the height of the node, relative to height of its child nodes.
   * 
   * @param node the node whose height we want
   * @return the height of the node
   */
  private int height(AVLTreeNode<K, V> node) {
    if (node == null)
      return -1;
    return Math.max(height(node.left), height(node.right)) + 1;
  }

  /**
   * Finds the {@code AVLTreeNode} with the smallest key by recursively traversing
   * down the left subtree.
   *
   * @param node the {@code AVLTreeNode} the tree node to start traversing at
   * @return the {@code AVLTreeNode} with the smallest key or {@null} if none
   */
  public AVLTreeNode<K, V> minimum(AVLTreeNode<K, V> node) {
    if (node == null)
      return null;

    if (node.left != null)
      return minimum(node.left);
    return node;
  }

  /**
   * Finds the {@code AVLTreeNode} with the smallest key by recursively traversing
   * down the left subtree starting at the {@code root} of the tree.
   *
   * @return the {@code AVLTreeNode} with the smallest key or {@null} if none
   */
  public AVLTreeNode<K, V> minimum() {
    return minimum(root);
  }

  /**
   * Finds the {@code AVLTreeNOde} with the largest key by recursively traversing
   * down the right subtree.
   *
   * @param node the {@code AVLTreeNode} the tree node to start traversing at
   * @return the {@code AVLTreeNode} with the largest key or {@null} if none
   */
  public AVLTreeNode<K, V> maximum(AVLTreeNode<K, V> node) {
    if (node == null)
      return null;

    if (node.right != null)
      return maximum(node.right);
    return node;
  }

  /**
   * Finds the {@code AVLTreeNode} with the largest key by recursively traversing
   * down the left subtree starting at the {@code root} of the tree.
   *
   * @return the {@code AVLTreeNode} with the largest key or {@null} if none
   */
  public AVLTreeNode<K, V> maximum() {
    return maximum(root);
  }

  private AVLTreeNode<K, V> rotateLeft(AVLTreeNode<K, V> x, AVLTreeNode<K, V> z) {
    AVLTreeNode<K, V> y = z.left;

    x.right = y;

    if (y != null)
      y.parent = x;

    z.left = x;
    x.parent = z;

    return z;
  }

  private AVLTreeNode<K, V> rotateRight(AVLTreeNode<K, V> x, AVLTreeNode<K, V> z) {
    AVLTreeNode<K, V> y = z.right;

    x.left = y;

    if (y != null)
      y.parent = x;

    z.right = x;
    x.parent = z;

    return z;
  }

  private AVLTreeNode<K, V> rotateLeftRight(AVLTreeNode<K, V> x, AVLTreeNode<K, V> z) {
    AVLTreeNode<K, V> y = z.right;

    z.right = y.left;

    if (y.left != null)
      y.left.parent = z;

    y.left = z;
    z.parent = y;
    x.left = y.right;

    if (y.right != null)
      y.right.parent = x;

    y.right = x;
    x.parent = y;

    return y;
  }

  private AVLTreeNode<K, V> rotateRightLeft(AVLTreeNode<K, V> x, AVLTreeNode<K, V> z) {
    AVLTreeNode<K, V> y = z.left;

    z.left = y.right;

    if (y.right != null)
      y.right.parent = z;

    y.right = z;
    z.parent = y;
    x.right = y.left;

    if (y.left != null)
      y.left.parent = x;

    y.left = x;
    x.parent = y;

    return y;
  }

  /**
   * Checks each of the node's ancestors for consistency with the invariants of
   * AVL trees.
   * 
   * <p>
   * This is used in the {@link #insert()} and {@link #deleteNode()} processes.
   * Because the {@code insert()} method uses a key, the parameter is placed at
   * the end and when used it for {@code delete()} it simply sets the value to
   * {@code null}
   * </p>
   * 
   * <p>
   * The loop begins at the node inserted with the parent and works its way up to
   * the root at worst, checking the balance factor of each ancestor to the
   * current node to make sure the AVL invariant is held. If an ancestor is
   * unbalanced {@code (-2 or +2)}, then an appropriate rotation is made and then
   * the parent link is fixed. If an ancestor node is balanced {@code (0)}, then
   * the insertion did not increase the height of the tree and the AVL invariant
   * is held and we can stop checking the balance.
   * </p>
   * 
   * g - The grandparent of current node 
   * n - The returned node after a rotation, if a rotation is made 
   * x - The parent of the current node z 
   * b - The balance factor of node x
   *
   * @param key the key of the newly inserted node
   * @param z   the newly inserted node
   */
  private void retracing(int type, AVLTreeNode<K, V> node, K key) {
    AVLTreeNode<K, V> g, n, x = node.parent;
    
    for (int b; x != null; node = x, x = node.parent) {
      b = balanceFactor(x);
      g = x.parent;
      n = null;

      if (b == 0)
        break;
      if (b > 1) {
        if (type == INSERT) {
          if (isLessThan(key, x.left.getKey()))
            n = rotateRight(x, node);
          else 
            n = rotateLeftRight(x, node);       
        }
        else {
          if (balanceFactor(x.left) > 0)
            n = rotateRight(x, node);
          else if (balanceFactor(x.left) < 0)
            n = rotateLeftRight(x, node);        
        }
      }      
      else if (b < -1) {
        if (type == INSERT) {
          if (isLessThan(x.right.getKey(), key))
            n = rotateLeft(x, node);
          else 
            n = rotateRightLeft(x, node);
        }
        else {
          if (balanceFactor(x.right) < 0)
            n = rotateLeft(x, node);
          else if (balanceFactor(x.right) > 0) 
            n = rotateRightLeft(x, node);
        }
      }

      if (n != null) {
        n.parent = g;

        if (g != null) {
          if (x == g.left)
            g.left = n;
          else 
            g.right = n;
        }
        else 
          root = n;

        break;
      }
    }
  }

  /**
   * When inserting a node into the AVL tree, it initially is the same process as
   * inserting into a {@code BinarySearchTree}. If empty, node {@code z} becomes
   * the {@code root}, otherwise, it will end up becoming a left or right child.
   * After insertion if a tree becomes unbalanced, only ancestors of the newly
   * inserted node are unbalanced. This is because only those nodes have their
   * sub-trees altered. So it is necessary to check each of the node's ancestors
   * for consistency with the invariants of AVL trees: this is called "Retracing"
   * and is achieved by getting the Balance Factor of each node.
   *
   * <p>
   * The AVL tree's height cannot increase by more than one within an insertion
   * operation, so the temporary balance factor of a node after insertion will be
   * in the range {@code [-2, +2]}. For each node checked, if the temporary
   * balance factor remains in the range from {@code -1} to {@code +1} then only
   * an update of the balance factor and no rotation is necessary - slightly
   * unbalanced and still satisfies the AVL property. However, if less than or
   * greater than {@code -1} or {@code +1}, the subtree rooted at this node is AVL
   * unbalanced, and a rotation is needed.
   * </p>
   * 
   * <p>
   * The retracing is consisted of the for-loop where we begin at the newly
   * inserted node {@code z} and retrace its way back up to the {@code root} or
   * once the balance factor becomes {@code 0}, implying that the height of that
   * subtree is unchanged - balanced. Otherwise, if the temporary balance factor
   * is {@code -2} or {@code +2}, a rotation is required.
   * </p>
   * 
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    count++;

    AVLTreeNode<K, V> z = new AVLTreeNode<>(key, value);
    AVLTreeNode<K, V> y = null;
    AVLTreeNode<K, V> q = root;

    while (q != null) {
      y = q;

      if (isLessThan(z, q))
        q = q.left;
      else 
      q = q.right;
    }

    z.parent = y;
    if (y == null)
      root = z;
    else if (isLessThan(z, y))
      y.left = z;
    else
      y.right = z;

    retracing(INSERT, z, key);
  }

  /**
   * Commpares the given key to the left and right node of the current
   * {@code AVLTreeNode} so that it can descend further until either the the
   * correct node is found with the matching key, or we reach the end.
   * 
   * @param node the current tree node
   * @param key  the key of the node to find
   * @return the tree node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */  
  public AVLTreeNode<K, V> search(AVLTreeNode<K, V> node, K key) {
    checkKey(key);

    if (node == null || key == node.getKey())
      return node;
    if (isLessThan(key, node.getKey()))
      return search(node.left, key);
    return search(node.right, key);    
  } 

  /**
   * Search for a node for the specified key starting at the {@code root}.
   * 
   * @param key the key of the node to find
   * @return the tree node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public AVLTreeNode<K, V> search(K key) {
    return search(root, key);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public boolean hasKey(K key) {
    return search(key) != null;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public V get(K key) {
    AVLTreeNode<K, V> node = search(key);
    return node != null ? node.getValue() : null;
  }

  /**
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   * 
   * @param x {@code AVLTreeNode}
   * @param y {@code AVLTreeNode}
   */  
  private void transplant(AVLTreeNode<K, V> x, AVLTreeNode<K, V> y) {
    if (x.parent == null)
      root = y;
    else if (x == x.parent.left)
      x.parent.left = y;
    else 
      x.parent.right = y;

    if (y != null)
      y.parent = x.parent;
  }

  /**
   * Deletes the specified {@code AVLTreeNode} from the tree. Calls the
   * {@code transplant()} method to adjust the tree nodes to replace the removed
   * node.
   * 
   * <p>
   * The deletion process, similar to the insertion process, begins with the
   * normal deletion in a {@code BinarySearchTree}. After the node has been
   * properly removed, the balance is checked similar to the process in the
   * insertion balance check.
   * </p>
   *
   * <p>
   * Since the height of the AVL tree can only decrease at most by {@code 1} in a
   * single deletion operation, the temporary balance factor of a node will range
   * from {@code [-2, +2]}. Unlike insertion, where the rotation always balances
   * the tree, after deletion, there may be {@code balanceFactor(z) != 0}, the
   * {@code root} of the subtree won't be balanced. So after the appropriate
   * single or double rotation, the height of the rebalanced subtree decreases by
   * {@code 1}, meaning that the tree has to be rebalanced again on the next
   * higher level.
   * </p>
   * 
   * @param node the {@code AVLTreeNode} to remove
   * @see #transplant()
   * 
   * @throws NullPointerException if the {@code AVLTreeNode} is {@code null}
   */
  public synchronized void deleteNode(AVLTreeNode<K, V> node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");

    count--;

    AVLTreeNode<K, V> y;

    if (node.left == null)
      transplant(node, node.right);
    else if (node.right == null)
      transplant(node, node.left);
    else {
      y = minimum(node.right);

      if (y.parent != node) {
        transplant(y, y.right);
        y.right = node.right;
        y.right.parent = y;
      }
      transplant(node, y);
      y.left = node.left;
      y.left.parent = y;
    }

    retracing(DELETE, node, null);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void delete(K key) {
    AVLTreeNode<K, V> node = search(key);

    if (node != null)
      deleteNode(node);
  }

 /**
   * Finds the node that will immediately succeed the given {@code AVLTreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the largest key down the right subtree.
   * 
   * @param node the {@code AVLTreeNode} to find the successor of
   * @return the successor or {@code null} if none
   * 
   * @throws NullPointerException if the node specified is {@code null}
   */
  public AVLTreeNode<K, V> successor(AVLTreeNode<K, V> node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
    if (node.right != null)
      return minimum(node.right);

    AVLTreeNode<K, V> y = node.parent;
    
    while (y != null && node.equals(y.right)) {
      node = y;
      y = y.parent;
    }

    return y;
  }

  /**
   * Finds the node that will immediately precede the given {@code AVLTreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the smallest key down the left subtree.
   * 
   * @param node the {@code AVLTreeNode} to find the predecessor of
   * @return the predecessor or {@code null} if none
   * 
   * @throws NullPointerException if the node specified is {@code null}
   */
  public AVLTreeNode<K, V> predecessor(AVLTreeNode<K, V> node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
    if (node.left != null)
      return maximum(node.left);

    AVLTreeNode<K, V> y = node.parent;

    while (y != null && node.equals(y.left)) {
      node = y;
      y = y.parent;
    }

    return y;
  }

  /**
   * Implemntation that uses the inorderTreeWalk traversal to create
   * a string of all the {@code AVLTreeNode} entries in the tree in order
   * by key.
   * 
   * Displays the object string in JSON format.
   * 
   * @return the tree string 
   */
  public String toString() {
    if (isEmpty())
      return "{}";
    
    StringBuilder sb = new StringBuilder("{\n");
    
    inorderTreeWalk((AVLTreeNode<K, V> node) -> sb.append("\s\s" + node.toString() + ",\n"));
    
    return sb.toString() + "}";
  }

  /**
   * Tree-Walk takes (-)(n) time to walk an n-node binary tree, since after the
   * initial callm the procedure calls itself recursively exactly twice for each
   * node in the tree.
   *
   * inorder tree walk - Visits all nodes in sorted order. This does so by
   * visiting the root between the values of its left subtree and the right
   * subtree.
   *
   * preorder tree walk - Visits the root before all other values in either
   * subtree.
   *
   * postorder tree walk - Visits the root after the values in its subtrees.
   * 
   */
  public void inorderTreeWalk(AVLTreeNode<K, V> x, Consumer<AVLTreeNode<K, V>> callback) {
    if (x != null) {
      this.inorderTreeWalk(x.left, callback);
      callback.accept(x);
      this.inorderTreeWalk(x.right, callback);
    }
  }

  public void preorderTreeWalk(AVLTreeNode<K, V> x, Consumer<AVLTreeNode<K, V>> callback) {
    if (x != null) {
      callback.accept(x);
      this.preorderTreeWalk(x.left, callback);
      this.preorderTreeWalk(x.right, callback);
    }
  }
  
  public void postorderTreeWalk(AVLTreeNode<K, V> x, Consumer<AVLTreeNode<K, V>> callback) {
    if (x != null) {
      this.postorderTreeWalk(x.left, callback);
      this.postorderTreeWalk(x.right, callback);  
      callback.accept(x); 
    }
  }

  public void inorderTreeWalk(Consumer<AVLTreeNode<K, V>> callback) {
    inorderTreeWalk(root, callback);
  }

  public void preorderTreeWalk(Consumer<AVLTreeNode<K, V>> callback) {
    preorderTreeWalk(root, callback);
  }
  
  public void postorderTreeWalk(Consumer<AVLTreeNode<K, V>> callback) {
    postorderTreeWalk(root, callback);
  }
}
