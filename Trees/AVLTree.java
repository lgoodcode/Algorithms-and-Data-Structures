package data_structures.trees;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import data_structures.queues.Queue;
import data_structures.queues.exceptions.QueueFullException;

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
   * Internal method to verify the {@code TreeNode} used for methods are
   * {@code AVLTreeNodes} that require it.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the node to verify
   *
   * @throws IllegalArgumentException if the supplied node is not an instance of
   *                                  {@code AVLTreeNode}
   */
  private <Node extends TreeNode<K, V>> void checkType(Node node) {
    if (node != null && node.getClass() != AVLTreeNode.class)
      throw new IllegalArgumentException("Node must be an instance of AVLTreeNode.");
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public final <Node extends TreeNode<K, V>> Node getRoot() {
    return (Node) root;
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
    checkDuplicate(key);

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
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}, or if the supplied node is
   *                                  not an {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> Node search(Node node, K key) {
    checkType(node);
    checkKey(key);

    AVLTreeNode<K, V> _node = (AVLTreeNode<K, V>) node;

    if (_node == null || key == _node.getKey())
      return (Node) _node;
    if (isLessThan(key, _node.getKey()))
      return (Node) search(_node.left, key);
    return (Node) search(_node.right, key);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> Node minimum(Node node) {
    checkType(node);

    AVLTreeNode<K, V> _node = (AVLTreeNode<K, V>) node;

    if (_node == null)
      return null;
    if (_node.left != null)
      return (Node) minimum(_node.left);
    return (Node) _node;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> Node maximum(Node node) {
    checkType(node);

    AVLTreeNode<K, V> _node = (AVLTreeNode<K, V>) node;

    if (_node == null)
      return null;
    if (_node.right != null)
      return (Node) maximum(_node.right);
    return (Node) _node;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  protected <Node extends TreeNode<K, V>> void transplant(Node x, Node y) {
    AVLTreeNode<K, V> _x = (AVLTreeNode<K, V>) x;
    AVLTreeNode<K, V> _y = (AVLTreeNode<K, V>) y;

    if (_x.parent == null)
      root = _y;
    else if (_x == _x.parent.left)
      _x.parent.left = _y;
    else
      _x.parent.right = _y;

    if (_y != null)
      _y.parent = _x.parent;
  }

  /**
   * {@inheritDoc}
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
   * @throws NullPointerException {@inheritDoc}, or if the supplied node is not an
   *                              {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public synchronized <Node extends TreeNode<K, V>> void deleteNode(Node node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");

    checkType(node);
    count--;

    AVLTreeNode<K, V> y, _node = (AVLTreeNode<K, V>) node;

    if (_node.left == null)
      transplant(_node, _node.right);
    else if (_node.right == null)
      transplant(_node, _node.left);
    else {
      y = minimum(_node.right);

      if (y.parent != _node) {
        transplant(y, y.right);
        y.right = _node.right;
        y.right.parent = y;
      }
      transplant(_node, y);
      y.left = _node.left;
      y.left.parent = y;
    }

    retracing(DELETE, _node, null);
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}, or if the supplied node is not an
   *                              {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> Node successor(Node node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");

    checkType(node);

    AVLTreeNode<K, V> y, _node = (AVLTreeNode<K, V>) node;

    if (_node.right != null)
      return (Node) minimum(_node.right);

    y = _node.parent;

    while (y != null && _node.equals(y.right)) {
      _node = y;
      y = y.parent;
    }

    return (Node) y;
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}, or if the supplied node is not an
   *                              {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> Node predecessor(Node node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");

    checkType(node);

    AVLTreeNode<K, V> y, _node = (AVLTreeNode<K, V>) node;

    if (_node.left != null)
      return (Node) maximum(_node.left);

    y = _node.parent;

    while (y != null && _node == y.left) {
      _node = y;
      y = y.parent;
    }

    return (Node) y;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> void inorderTreeWalk(Node node, Consumer<Node> callback) {
    if (node == null)
      return;

    checkType(node);
    AVLTreeNode<K, V> _node = (AVLTreeNode<K, V>) node;

    this.inorderTreeWalk((Node) _node.left, callback);
    callback.accept((Node) _node);
    this.inorderTreeWalk((Node) _node.right, callback);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> void preorderTreeWalk(Node node, Consumer<Node> callback) {
    if (node == null)
      return;

    checkType(node);
    AVLTreeNode<K, V> _node = (AVLTreeNode<K, V>) node;

    callback.accept((Node) _node);
    this.preorderTreeWalk((Node) _node.left, callback);
    this.preorderTreeWalk((Node) _node.right, callback);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code AVLTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <Node extends TreeNode<K, V>> void postorderTreeWalk(Node node, Consumer<Node> callback) {
    if (node == null)
      return;

    checkType(node);
    AVLTreeNode<K, V> _node = (AVLTreeNode<K, V>) node;

    this.postorderTreeWalk((Node) _node.left, callback);
    this.postorderTreeWalk((Node) _node.right, callback);
    callback.accept((Node) _node);
  }

  protected <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterable<>();
    return new Enumerator<>(type, true);
  }

  protected <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return Collections.emptyIterator();
    return new Enumerator<>(type, true);
  }

  protected <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return Collections.emptyEnumeration();
    return new Enumerator<>(type, false);
  }

  /**
   * The {@code Enumerator} constructor to place all the nodes in the tree into an
   * {@link Queue} to be enumerated.
   */
  protected class Enumerator<T> extends AbstractEnumerator<T> {
    Enumerator(int type, boolean iterator) {
      this.size = AVLTree.this.count;
      this.iterator = iterator;
      this.type = type;
      entries = new Queue<>(size);

      inorderTreeWalk((AVLTreeNode<K, V> node) -> {
        try {
          entries.enqueue(node);
        } catch (QueueFullException e) {}
      });
    }
  }

}
