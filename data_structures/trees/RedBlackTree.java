package data_structures.trees;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * A Red-Black tree is a binary search tree with one extra bit of storage per
 * node: its {@code color}, which can be either {@code BLACK (0)} or
 * {@code RED (1)}. By constraining the node colors on any simple path from the
 * root to a leaf, red-black trees ensure that no such path is more than twice
 * as long as any other, so that the tree is approximately "Balanced".
 *
 * <p>
 * A Red-Black tree is a binary tree that satisfies the "Red-Black Properties":
 * </p>
 *
 * <ol>
 * <li>Every node is either red or black</li>
 * <li>The root is black</li>
 * <li>Every leaf (NIL) is black
 * <li>If a node is red, then both its children are black</li>
 * <li>For each node, all simple paths from the node to descendant leaves
 * contain the same number of black nodes.</li>
 * </ol>
 *
 * <p>
 * Black-Height: The number of black nodes on any simple path from, but not
 * including, a node {@code x} down to a leaf, denoted {@code bh(x)}.
 * </p>
 *
 * <p>
 * By property 5, the notion of black-height is well defined, since all
 * descending simple paths from the node have the same number of black nodes.
 * The black-height of a node is the black-height of root.
 * </p>
 *
 * <p>
 * A red-black tree with {@code n} internal nodes has height at most
 * {@code 2 lg(n + 1)}. Since the height of a tree is {@code h}, the
 * black-height of the root must be at least {@code h / 2}.
 * </p>
 */
public class RedBlackTree<K, V> extends AbstractTree<K, V> {
  public static class RBNode<T, E> extends Node<T, E> {
    protected RBNode<T, E> parent;
    protected RBNode<T, E> left;
    protected RBNode<T, E> right;
  
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
    protected static final RBNode<?, ?> NIL = new RedBlackTree.RBNode<>();
  
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
    protected RBNode(T key, E value) {
      super(key, value);
      parent = left = right = new RBNode<>();
    }
  
    /**
     * Creates a circular {@code NIL} tree node that points to itself and has no key
     * or value. It is simply used as an indicator that no node exists for another
     * {@code RedBlackTreeNode} to prevent errors from occuring and for the tree to
     * perform its operations.
     */
    protected RBNode() {
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

  /**
   * The sentinel {@code NIL}.
   */
  private final RBNode<K, V> NIL = new RBNode<>();

  /**
   * The root of the tree which is initially the {@code NIL} sentinel.
   */
  private RBNode<K, V> root;

  // Color constants
  private final int BLACK = 0;
  private final int RED = 1;

  /**
   * Creates an empty, BinaryTree, using the specified compare function to
   * determine whether a given {@code RedBlackTreeNode} is smaller than another.
   *
   * @param compare an anonymous function that compares two
   *                  {@code RedBlackTreeNode} objects
   */
  public RedBlackTree(BiFunction<K, K, Boolean> compare) {
    super(compare);
    root = NIL;
  }

  /**
   * The default constructor that uses the default compare function and calls the
   * main constructor.
   */
  public RedBlackTree() {
    super();
    root = NIL;
  }

  /**
   * Internal method to verify the {@code TreeNode} used for methods are
   * {@code RBNode} that require it.
   *
   * @param <TreeNode> {@link RBNode}
   * @param node       the node to verify
   *
   * @throws IllegalArgumentException if the supplied node is not an instance of
   *                                  {@code RBNode}
   */
  private <TreeNode extends Node<K, V>> void checkType(TreeNode node) {
    if (node != null && node.getClass() != RBNode.class)
      throw new IllegalArgumentException("TreeNode must be an instance of RedBlackTree.RBNode");
  }

  /**
   * {@inheritDoc}
   * 
   * @param <TreeNode> {@link RBNode}
   */
  @SuppressWarnings("unchecked")
  public final RBNode<K, V> getRoot() {
    return root;
  }

  private void leftRotate(RBNode<K, V> x) {
    RBNode<K, V> y = x.right;

    // Make x's right subtree be y's left subtree
    x.right = y.left;

    if (!y.left.isNIL())
      y.left.parent = x;

    // Link x's parent to y
    y.parent = x.parent;
    if (x.parent.isNIL())
      root = y;
    else if (x == x.parent.left)
      x.parent.left = y;
    else
      x.parent.right = y;

    // Put x on y's left
    y.left = x;
    x.parent = y;
  }

  private void rightRotate(RBNode<K, V> x) {
    RBNode<K, V> y = x.left;

    x.left = y.right;

    if (!y.right.isNIL())
      y.right.parent = x;
    y.parent = x.parent;
    if (x.parent.isNIL())
      root = y;
    else if (x == x.parent.right)
      x.parent.right = y;
    else
      x.parent.left = y;

    y.right = x;
    x.parent = y;

  }

  /**
   * {@inheritDoc}
   *
   * This inserts a new node into the tree the same as if it were an ordinary
   * binary search tree and set the color to red. The difference is that we call
   * insertFixup to guarantee that the red-black properties are preserved, to
   * recolor nodes and perform rotations.
   *
   * <p>
   * This differs from the normal tree insert in four ways:
   * </p>
   *
   * <ol>
   * <li>Instances of null are replaced with the sentinel {@code T.NIL}</li>
   * <li>Set {@code z.left} and {@code z.right} to {@code T.NIL} in order to
   * maintain the proper tree structure</li>
   * <li>Color {@code z} red</li>
   * <li>Call insertFixup because colouring red may cause a violation of one of
   * the red-black properties.</li>
   * </ol>
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    count++;

    RBNode<K, V> x = root;
    RBNode<K, V> y = NIL;
    RBNode<K, V> z = new RBNode<>(key, value);

    while (!x.isNIL()) {
      y = x;

      if (isLessThan(z, x))
        x = x.left;
      else
        x = x.right;
    }

    z.parent = y;

    if (y.isNIL())
      root = z;
    else if (isLessThan(z, y))
      y.left = z;
    else
      y.right = z;

    z.color = RED;
    insertFixup(z);
  }

  /**
   * Prior to the operation, the properties 1 and 3 hold since both children of
   * the newly inserted red node are the sentinel {@code T.NIL}. Property 5 holds
   * as we haven't added or removed any black nodes because node {@code z}
   * replaces the (black) sentinel and node {@code z} is red with sentinel
   * children.
   *
   * <p>
   * The only properties that might be violated are property 2, which requires the
   * root to be black, and property 4, which says that a red node cannot have a
   * red child. Both possible violations are due to z being colored red.
   * </p>
   *
   * <p>
   * Property 2 is violated if {@code z} is the root, and property 4 is violated
   * if {@code z}'s parent is red.
   * </p>
   */
  private void insertFixup(RBNode<K, V> z) {
    RBNode<K, V> y;

    while (z.parent.color == RED) {
      if (z.parent == z.parent.parent.left) {
        y = z.parent.parent.right;
        /**
         * Case 1: z's uncle y is red
         *
         * This occurs when both z.p and y are red. Because z.p.p is black, we can color
         * both z.p and y black, thereby fixing the problem of z and z.p both being red,
         * and we can color z.p.p red, therby maintaining property 5. The while loop is
         * repeated with z.p.p as the new node z.
         */
        if (y.color == RED) {
          z.parent.color = BLACK;
          y.color = BLACK;
          z.parent.parent.color = RED;
          z = z.parent.parent;
        } else {
          /**
           * Case 2: z's uncle y is black and z is a right child
           *
           * C / \ A V <- y / \ U B <- z
           *
           * We immidiately use a left rotation to transform the situation into case 3, in
           * which node z is a left child.
           */
          if (z == z.parent.right) {
            z = z.parent;
            leftRotate(z);
          }
          /**
           * Case 3: z's uncle y is black and z is a left child
           *
           * C B / \ / \ B V <- y -> Right-Rotate(C) -> A C / A <- z
           *
           * Because both z and z.p are red, the rotation affects neither the black-height
           * of nodes nor property 5. Whether we enter case 3 directly or through case 2,
           * z's uncle y is black, since otherwise we would have executed case 1.
           */
          z.parent.color = BLACK;
          z.parent.parent.color = RED;
          rightRotate(z.parent.parent);
        }
      } else {
        /**
         * Same as above except flipped left and right
         */
        y = z.parent.parent.left;

        if (y.color == RED) {
          z.parent.color = BLACK;
          y.color = BLACK;
          z.parent.parent.color = RED;
          z = z.parent.parent;
        } else {
          if (z == z.parent.left) {
            z = z.parent;
            rightRotate(z);
          }

          z.parent.color = BLACK;
          z.parent.parent.color = RED;
          leftRotate(z.parent.parent);
        }
      }
    }

    root.color = BLACK;
  }

  /**
   * {@inheritDoc}
   *
   * @param <TreeNode> {@link RBNode}
   * @throws IllegalArgumentException {@inheritDoc}, or if the supplied node is
   *                                  not an {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode search(TreeNode node, K key) {
    checkType(node);
    checkKey(key);

    RBNode<K, V> _node = (RBNode<K, V>) node;

    if (_node.isNIL())
      return null;
    if (key == _node.getKey())
      return (TreeNode) _node;
    if (isLessThan(key, _node.getKey()))
      return (TreeNode) search(_node.left, key);
    return (TreeNode) search(_node.right, key);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Because of the use of the {@code NIL} sentinel we have to perform a seperate
   * check for {@code null} node and then if the node is {@code NIL}.
   * </p>
   *
   * @param <TreeNode> {@link RBNode}
   * 
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode minimum(TreeNode node) {
    checkType(node);

    RBNode<K, V> _node = (RBNode<K, V>) node;

    if (_node == null || _node.isNIL())
      return null;
    if (!_node.left.isNIL())
      return (TreeNode) minimum(_node.left);
    return (TreeNode) _node;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Because of the use of the {@code NIL} sentinel we have to perform a seperate
   * check for {@null} node and then if the node is {@code NIL}.
   * </p>
   *
   * @param <TreeNode> {@link RBNode}
   * 
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode maximum(TreeNode node) {
    checkType(node);

    RBNode<K, V> _node = (RBNode<K, V>) node;

    if (_node == null || _node.isNIL())
      return null;
    if (!_node.right.isNIL())
      return (TreeNode) maximum(_node.right);
    return (TreeNode) _node;
  }

  /**
   * {@inheritDoc}
   * 
   * @param <TreeNode> {@link RBNode}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> void transplant(TreeNode x, TreeNode y) {
    RBNode<K, V> _x = (RBNode<K, V>) x;
    RBNode<K, V> _y = (RBNode<K, V>) y;

    if (_x.parent.isNIL())
      root = _y;
    else if (_x == _x.parent.left)
      _x.parent.left = _y;
    else
      _x.parent.right = _y;

    _y.parent = _x.parent;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * We set {@code y} to {@code z} so we can later move {@code y} into {@code z}'s
   * position. Because {@code y}'s color might change, we keep the original stored
   * before any changes occur to track it.
   * </p>
   *
   * <p>
   * When {@code z} has two children, then {@code y != z} and node {@code y} moves
   * into node {@code z}'s original position. We keep track of the node {@code x}
   * that moves into node {@code y}'s original position. The assignments
   * {@code x = z.right}, {@code x = z.left}, {@code x = y.right}, set {@code x}
   * to point to either {@code y}'s only child or, if {@code y} has no children,
   * the sentinel {@code T.NIL}.
   * </p>
   *
   * <p>
   * Since node {@code x} moves into node {@code y}'s original position, the
   * attribute {@code x.parent} is always set to point to the original position in
   * the tree of {@code y}'s parent, even if x is the sentinel {@code T.NIL}.
   * Unless {@code z} is {@code y}'s original parent (which occurs only when
   * {@code z} has two children and its successor {@code y} is {@code z}'s right
   * child), the assignment to x.p takes place in line 6 of RB-Transplant. (In
   * RB-Transplant, the third parameter passed is the same as {@code x}.)
   * </p>
   *
   * <p>
   * When {@code y}'s original parent is {@code z}, we do not want
   * {@code x.parent} to point to {@code y}'s original parent, since we are
   * removing that node from the tree. Because node {@code y} will move up to take
   * {@code z}'s position in the tree, setting {@code x.parent} to {@code y}
   * causes {@code x.parent} to point to the original position of {@code y}'s
   * parent, even if {@code x = T.NIL}.
   * </p>
   *
   * <p>
   * If node {@code y} was black, we might have violated one of the red-black
   * properties, so we call {@link #deleteFixup()} at the end to restore the
   * red-black properties. If {@code y} was red, the properties hold when
   * {@code y} is removed or moved for the following reasons:
   * </p>
   *
   * <ol>
   * <li>No black-heights in the tree have changed.</li>
   * <li>No red nodes have been made adjacent</li>
   * <li>Since {@code y} could not have been the root if it was red, the root
   * remains black</li>
   * </ol>
   *
   * @param <TreeNode> {@link RBNode}
   * 
   * @throws NullPointerException {@inheritDoc}, or if the supplied node is not an
   *                              {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public synchronized <TreeNode extends Node<K, V>> void deleteNode(TreeNode node) {
    if (node == null)
      throw new NullPointerException("TreeNode cannot be null.");

    checkType(node);
    count--;

    RBNode<K, V> x, y = (RBNode<K, V>) node, _node = y;
    int y_color = y.color;

    if (_node.left.isNIL()) {
      x = _node.right;
      transplant(_node, _node.right);
    } else if (_node.right.isNIL()) {
      x = _node.left;
      transplant(_node, _node.left);
    } else {
      y = minimum(_node.right);

      y_color = y.color;
      x = y.right;

      if (y.parent == _node)
        x.parent = y;
      else {
        transplant(y, y.right);
        y.right = _node.right;
        y.right.parent = y;
      }

      transplant(_node, y);
      y.left = _node.left;
      y.left.parent = y;
      y.color = _node.color;
    }

    if (y_color == BLACK)
      deleteFixup(y);
  }

  /**
   * Restores properties 1, 2, and 4. The goal of the while loop is to move the
   * extra black up the tree until:
   *
   * <ol>
   * <li>{@code x} points to a red-and-black node, in which case we color
   * {@code x} (singly) black</li>
   * <li>{@code x} points to the {@code root}, in which case we simply "remove"
   * the extra black; or</li>
   * <li>having performed suitable rotations and recolorings, we exit the
   * loop</li>
   * </ol>
   *
   * <p>
   * Within the while loop, {@code x} always points to a nonroot doubly black
   * node. We determine if {@code x} is a left or a right child of its parent
   * {@code x.parent}. We maintain a pointer w to the sibling of {@code x}. Since
   * the node {@code x} is doubly black, node w cannot be {@code T.NIL}, because
   * otherwise, the number of blacks on the simple path from {@code x.parent} to
   * the (singly black) leaf w would be smaller than the number on the simple path
   * from {@code x.parent} to {@code x}.
   * </p>
   *
   * @param node the node to fix the properties from the removal process
   */
  private void deleteFixup(RBNode<K, V> node) {
    RBNode<K, V> w;

    while (node != root && node.color == BLACK) {
      if (node == node.parent.left) {
        w = node.parent.right;
        /**
         * Case 1: x's sibling w is red
         *
         * Since w must have black childdren, we can switch the colors of w and x.p and
         * then perform a left-rotation on x.p without violating any of the red-black
         * properties. The new sibling of x, which is one of w's children prior to the
         * rotation, is now black, and thus converted case 1 into case 2, 3, or 4.
         */
        if (w.color == RED) {
          w.color = BLACK;
          node.parent.color = RED;
          leftRotate(node.parent);
          w = node.parent.right;
        }
        /**
         * Case 2: x's sibling w is black, and both of w's children are black
         *
         * Since w is also black, we take one black off both x and w, leaving x with
         * only one black and leaving w red. To compensate for removing one black from x
         * and w, we would like to add an extra black to x.p, which was originally
         * either red or black. We do so by repeating the while loop with x.p as the new
         * node x.
         *
         * If we entered case 2 from case 1, the new node x is red-and-black, since the
         * original x.p was red. The color of the attribute of the new node x is red,
         * and the loop terminates when it tests the loop condition. We then color the
         * new node x (singly) black at the very end.
         */
        if (w.left.color == BLACK && w.right.color == BLACK) {
          w.color = RED;
          node = node.parent;
        } else {
          /**
           * Case 3: x's sibling w is black, w's left child is red, and w's right child is
           * black
           *
           * We can switch the colors of w and its left child w.left and then perform a
           * right rotation on w without violating any of the red-black properties. the
           * new sibling w of x is now a black node with a red right child, transforming
           * case 3 into case 4
           */
          if (w.right.color == BLACK) {
            w.left.color = BLACK;
            w.color = RED;
            rightRotate(w);
            w = node.parent.right;
          }
          /**
           * Case 4: x's sibling w is black, and w's right child is red
           *
           * By making some color changes and performing a left-rotation on x.p, we can
           * remove the extra black on x, making it singly black, without violating any of
           * the red-black properties. Setting x to be the root causes the while loop to
           * terminate when it tests the condition.
           */
          w.color = node.parent.color;
          node.parent.color = BLACK;
          w.right.color = BLACK;
          leftRotate(node.parent);
          node = root;
        }
      } else {
        w = node.parent.left;

        if (w.color == RED) {
          w.color = BLACK;
          node.parent.color = RED;
          rightRotate(node.parent);
          w = node.parent.left;
        }
        if (w.right.color == BLACK && w.left.color == BLACK) {
          w.color = RED;
          node = node.parent;
        } else {
          if (w.left.color == BLACK) {
            w.right.color = BLACK;
            w.color = RED;
            leftRotate(w);
            w = node.parent.left;
          }

          w.color = node.parent.color;
          node.parent.color = BLACK;
          w.left.color = BLACK;
          rightRotate(node.parent);
          node = root;
        }
      }
    }

    node.color = BLACK;
  }

  /**
   * {@inheritDoc}
   *
   * @param <TreeNode> {@link RBNode}
   * @throws NullPointerException {@inheritDoc}, or if the supplied node is not an
   *                              {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode successor(TreeNode node) {
    if (node == null)
      throw new NullPointerException("TreeNode cannot be null.");

    checkType(node);
    RBNode<K, V> y, _node = (RBNode<K, V>) node;

    if (_node.isNIL())
      return null;
    if (!_node.right.isNIL())
      return (TreeNode) minimum(_node.right);

    y = _node.parent;

    while (!y.isNIL() && _node == y.right) {
      _node = y;
      y = y.parent;
    }

    return (TreeNode) y;
  }

  /**
   * {@inheritDoc}
   *
   * @param <TreeNode> {@link RBNode}
   * @throws NullPointerException {@inheritDoc}, or if the supplied node is not an
   *                              {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode predecessor(TreeNode node) {
    if (node == null)
      throw new NullPointerException("TreeNode cannot be null.");

    checkType(node);
    RBNode<K, V> y, _node = (RBNode<K, V>) node;

    if (_node.isNIL())
      return null;
    if (!_node.left.isNIL())
      return (TreeNode) maximum(_node.left);

    y = _node.parent;

    while (!y.isNIL() && _node == y.left) {
      _node = y;
      y = y.parent;
    }

    return (TreeNode) y;
  }

  /**
   * {@inheritDoc}
   *
   * @param <TreeNode> {@link RBNode}
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> void inorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    checkType(node);
    RBNode<K, V> _node = (RBNode<K, V>) node;

    if (_node != null && !_node.isNIL()) {
      this.inorderTreeWalk((TreeNode) _node.left, callback);
      callback.accept((TreeNode) _node);
      this.inorderTreeWalk((TreeNode) _node.right, callback);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param <TreeNode> {@link RBNode}
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> void preorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    checkType(node);
    RBNode<K, V> _node = (RBNode<K, V>) node;

    if (_node != null && !_node.isNIL()) {
      callback.accept((TreeNode) _node);
      this.preorderTreeWalk((TreeNode) _node.left, callback);
      this.preorderTreeWalk((TreeNode) _node.right, callback);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param <TreeNode> {@link RBNode}
   * @throws IllegalArgumentException if the supplied node is not an
   *                                  {@code RedBlackTreeNode}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> void postorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    checkType(node);
    RBNode<K, V> _node = (RBNode<K, V>) node;

    if (_node != null && !_node.isNIL()) {
      this.postorderTreeWalk((TreeNode) _node.left, callback);
      this.postorderTreeWalk((TreeNode) _node.right, callback);
      callback.accept((TreeNode) _node);
    }
  }

}
