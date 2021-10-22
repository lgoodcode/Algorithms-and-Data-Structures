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
  /**
   * The sentinel {@code NIL}.
   */
  @SuppressWarnings("unchecked")
  private final RedBlackTreeNode<K, V> NIL = (RedBlackTreeNode<K, V>) RedBlackTreeNode.NIL;

  /**
   * The root of the tree which is initially the {@code NIL} sentinel.
   */
  private RedBlackTreeNode<K, V> root = NIL;

  // Color constants
  private final int BLACK = 0;
  private final int RED = 1;
  
  /**
   * Creates an empty, BinaryTree, using the specified compare function to
   * determine whether a given {@code RedBlackTreeNode} is smaller than another.
   * 
   * @param compareFn an anonymous function that compares two
   *                  {@code RedBlackTreeNode} objects
   */
  public RedBlackTree(BiFunction<K, K, Boolean> compareFn) {
    super(compareFn);
  }

  /**
   * The default constructor that uses the default compare function and calls the
   * main constructor.
   */
  public RedBlackTree() {
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
  protected boolean isLessThan(RedBlackTreeNode<K, V> x, RedBlackTreeNode<K, V> y) {
    if (x == null || y == null)
      throw new NullPointerException("Node cannot be null.");
    return compareFn.apply(x.getKey(), y.getKey());
  }

  /**
   * Finds the {@code RedBlackTreeNode} with the smallest key by recursively traversing
   * down the left subtree. Because of the use of the {@code NIL} sentinel we have to
   * perform a seperate check for {@null} node and then if the node is {@code NIL}.
   *
   * @param node the {@code RedBlackTreeNode} the tree node to start traversing at
   * @return the {@code RedBlackTreeNode} with the smallest key or {@null} if none
   */
  public RedBlackTreeNode<K, V> minimum(RedBlackTreeNode<K, V> node) {
    if (node == null)
      return null;
    if (node.isNIL())
      return node;

    if (!node.left.isNIL())
      return minimum(node.left);
    return node;
  }

  /**
   * Finds the {@code RedBlackTreeNode} with the smallest key by recursively traversing
   * down the left subtree starting at the {@code root} of the tree.
   *
   * @return the {@code RedBlackTreeNode} with the smallest key or {@null} if none
   */
  public RedBlackTreeNode<K, V> minimum() {
    return minimum(root);
  }

  /**
   * Finds the {@code RedBlackTreeNode} with the largest key by recursively traversing
   * down the left subtree. Because of the use of the {@code NIL} sentinel we have to
   * perform a seperate check for {@null} node and then if the node is {@code NIL}.
   *
   * @param node the {@code RedBlackTreeNode} the tree node to start traversing at
   * @return the {@code RedBlackTreeNode} with the largest key or {@null} if none
   */
  public RedBlackTreeNode<K, V> maximum(RedBlackTreeNode<K, V> node) {
    if (node == null)
      return null;
    if (node.isNIL())
      return node;

    if (!node.right.isNIL())
      return maximum(node.right);
    return node;
  }

  /**
   * Finds the {@code RedBlackTreeNode} with the largest key by recursively traversing
   * down the left subtree starting at the {@code root} of the tree.
   *
   * @return the {@code AVLTreeNode} with the largest key or {@null} if none
   */
  public RedBlackTreeNode<K, V> maximum() {
    return maximum(root);
  }

  private void leftRotate(RedBlackTreeNode<K, V> x) {
    RedBlackTreeNode<K, V> y = x.right;

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
  
  private void rightRotate(RedBlackTreeNode<K, V> x) {
    RedBlackTreeNode<K, V> y = x.left;

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
   */
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    count++;
    
    RedBlackTreeNode<K, V> x = root;
    RedBlackTreeNode<K, V> y = NIL;
    RedBlackTreeNode<K, V> z = new RedBlackTreeNode<>(key, value);

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
  private void insertFixup(RedBlackTreeNode<K, V> z) { 
    RedBlackTreeNode<K, V> y;

    while (z.parent.color == RED) {
      if (z.parent == z.parent.parent.left) {
        y = z.parent.parent.right;
        /**
         *  Case 1: z's uncle y is red
         *
         *  This occurs when both z.p and y are red. Because z.p.p is black, we can color both z.p and y
         *    black, thereby fixing the problem of z and z.p both being red, and we can color z.p.p red,
         *    therby maintaining property 5. The while loop is repeated with z.p.p as the new node z.
         */
        if (y.color == RED) {
          z.parent.color = BLACK;
          y.color = BLACK;
          z.parent.parent.color = RED;
          z = z.parent.parent;
        }
        else {
          /**
           *  Case 2: z's uncle y is black and z is a right child
           *
           *       C                            
           *      / \                         
           *     A   V <- y
           *    / \                            
           *   U   B <- z                           
           *
           *  We immidiately use a left rotation to transform the situation into case 3, in which node z 
           *    is a left child.
           */          
          if (z == z.parent.right) {
            z = z.parent;
            leftRotate(z);
          }
          /**
           *  Case 3: z's uncle y is black and z is a left child
           *
           *       C                                   B                    
           *      / \                                 / \                  
           *     B   V <- y  -> Right-Rotate(C) ->   A   C
           *    /                                                   
           *   A <- z          
           *
           *   Because both z and z.p are red, the rotation affects neither the black-height of nodes nor 
           *    property 5. Whether we enter case 3 directly or through case 2, z's uncle y is black, since 
           *    otherwise we would have executed case 1.
           */
          z.parent.color = BLACK;
          z.parent.parent.color = RED;
          rightRotate(z.parent.parent);
        }
      }
      else {
        /**
         *    Same as above except flipped left and right
         */
        y = z.parent.parent.left;

        if (y.color == RED) {
          z.parent.color = BLACK;
          y.color = BLACK;
          z.parent.parent.color = RED;
          z = z.parent.parent;
        }
        else {
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
   * Commpares the given key to the left and right node of the current
   * {@code RedBlackTreeNode} so that it can descend further until either the the
   * correct node is found with the matching key, or we reach the end.
   * 
   * @param node the current tree node
   * @param key  the key of the node to find
   * @return the tree node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */  
  public RedBlackTreeNode<K, V> search(RedBlackTreeNode<K, V> node, K key) {
    checkKey(key);

    if (node.isNIL())
      return null;
    if (key == node.getKey())
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
  public RedBlackTreeNode<K, V> search(K key) {
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
    RedBlackTreeNode<K, V> node = search(key);
    return node != null ? node.getValue() : null;
  }

  /**
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   * 
   * <p>
   * The RB-Transplant differs from the ordinary transplant in two ways:
   * </p>
   * 
   * <ol>
   * <li>Line 1 references the sentinel {@code T.NIL}</li>
   * <li>The assignment to {@code y.parent} occurs unconditionally: we can assign
   * to {@code y.parent} even if {@code y} points to sentinel.</li>
   * </ol>
   * 
   * @param x {@code AVLTreeNode}
   * @param y {@code AVLTreeNode}
   */  
  private void transplant(RedBlackTreeNode<K, V> x, RedBlackTreeNode<K, V> y) {
    if (x.parent.isNIL())
      root = y;
    else if (x == x.parent.left)
      x.parent.left = y;
    else
      x.parent.right = y;
    
    y.parent = x.parent;
  }

  /**
   * We set {@code y} to {@code z} so we can later move {@code y} into {@code z}'s
   * position. Because {@code y}'s color might change, we keep the original stored
   * before any changes occur to track it.
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
   * @param node the node to delete
   * 
   * @throws NullPointerException if the node is {@code null}
   */
  public synchronized void deleteNode(RedBlackTreeNode<K, V> node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");

    RedBlackTreeNode<K, V> x, y = node;
    int y_color = y.color;
    
    if (node.left.isNIL()) {
      x = node.right;
      transplant(node, node.right);
    }
    else if (node.right.isNIL()) {
      x = node.left;
      transplant(node, node.left);
    }
    else {
      y = minimum(node.right);

      y_color = y.color;
      x = y.right;

      if (y.parent == node)
        x.parent = y;
      else {
        transplant(y, y.right);
        y.right = node.right;
        y.right.parent = y;
      }

      transplant(node, y);
      y.left = node.left;
      y.left.parent = y;
      y.color = node.color;
    }

    if (y_color == BLACK)
      deleteFixup(y);

    count--;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void delete(K key) {
    RedBlackTreeNode<K, V> node = search(key);

    if (node != null)
      deleteNode(node);
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
  private void deleteFixup(RedBlackTreeNode<K, V> node) {
    RedBlackTreeNode<K, V> w;

    while (node != root && node.color == BLACK) {
      if (node == node.parent.left) {
        w = node.parent.right;
        /**
         *  Case 1: x's sibling w is red
         *
         *  Since w must have black childdren, we can switch the colors of w and x.p and then perform a 
         *    left-rotation on x.p without violating any of the red-black properties. The new sibling of
         *    x, which is one of w's children prior to the rotation, is now black, and thus converted
         *    case 1 into case 2, 3, or 4.
         */
        if (w.color == RED) {
          w.color = BLACK;
          node.parent.color = RED;
          leftRotate(node.parent);
          w = node.parent.right;
        }
        /**
         *  Case 2: x's sibling w is black, and both of w's children are black
         *
         *  Since w is also black, we take one black off both x and w, leaving x with only one black and 
         *    leaving w red. To compensate for removing one black from x and w, we would like to add an extra
         *    black to x.p, which was originally either red or black. We do so by repeating the while loop
         *    with x.p as the new node x. 
         *
         *  If we entered case 2 from case 1, the new node x is red-and-black, since the original x.p was red.
         *    The color of the attribute of the new node x is red, and the loop terminates when it tests the 
         *    loop condition. We then color the new node x (singly) black at the very end.    
         */
        if (w.left.color == BLACK && w.right.color == BLACK) {
          w.color = RED;
          node = node.parent;
        }
        else {
          /**
           *  Case 3: x's sibling w is black, w's left child is red, and w's right child is black
           *
           *  We can switch the colors of w and its left child w.left and then perform a right rotation on 
           *    w without violating any of the red-black properties. the new sibling w of x is now a black
           *    node with a red right child, transforming case 3 into case 4
           */
          if (w.right.color == BLACK) {
            w.left.color = BLACK;
            w.color = RED;
            rightRotate(w);
            w = node.parent.right;
          }
          /**
           *  Case 4: x's sibling w is black, and w's right child is red
           *
           *  By making some color changes and performing a left-rotation on x.p, we can remove the extra
           *    black on x, making it singly black, without violating any of the red-black properties. 
           *    Setting x to be the root causes the while loop to terminate when it tests the condition.
           */
          w.color = node.parent.color;
          node.parent.color = BLACK;
          w.right.color = BLACK;
          leftRotate(node.parent);
          node = root;
        }
      }
      else {
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
        }
        else {
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
  * Finds the node that will immediately succeed the given
  * {@code RedBlackTreeNode} without comparing keys. This is done by simply
  * returning the child node with the largest key down the right subtree.
  * 
  * @param node the {@code RedBlackTreeNode} to find the successor of
  * @return the successor or {@code null} if none
  *
  * @throws NullPointerException if the node specified is {@code null} or
  *                              sentinel {@code NIL}
  */
  public RedBlackTreeNode<K, V> successor(RedBlackTreeNode<K, V> node) {
    if (node == null || node.isNIL())
      throw new NullPointerException("Node cannot be null or NIL sentinel.");
    if (!node.right.isNIL())
      return minimum(node.right);

    RedBlackTreeNode<K, V> y = node.parent;
    
    while (!y.isNIL() && node == y.right) {
      node = y;
      y = y.parent;
    }

    return y;
  }

  /**
   * Finds the node that will immediately precede the given
   * {@code RedBlackTreeNode} without comparing keys. This is done by simply
   * returning the child node with the smallest key down the left subtree.
   * 
   * @param node the {@code RedBlackTreeNode} to find the predecessor of
   * @return the predecessor or {@code null} if none
   * 
   * @throws NullPointerException if the node specified is {@code null} or
   *                              sentinel {@code NIL}
   */
  public RedBlackTreeNode<K, V> predecessor(RedBlackTreeNode<K, V> node) {
    if (node == null || node.isNIL())
      throw new NullPointerException("Node cannot be null or NIL sentinel.");
    if (!node.left.isNIL())
      return maximum(node.left);

    RedBlackTreeNode<K, V> y = node.parent;

    while (!y.isNIL() && node == y.left) {
      node = y;
      y = y.parent;
    }

    return y;
  }

  /**
   * Implemntation that uses the inorderTreeWalk traversal to create
   * a string of all the {@code RedBlackTreeNode} entries in the tree in order
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
    
    inorderTreeWalk((RedBlackTreeNode<K, V> x) -> sb.append("\s\s\"" + x.toString() + "\",\n"));
    
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
  public void inorderTreeWalk(RedBlackTreeNode<K, V> node, Consumer<RedBlackTreeNode<K, V>> callback) {
    if (!node.isNIL()) {
      this.inorderTreeWalk(node.left, callback);
      callback.accept(node);
      this.inorderTreeWalk(node.right, callback);
    }
  }

  
  public void preorderTreeWalk(RedBlackTreeNode<K, V> node, Consumer<RedBlackTreeNode<K, V>> callback) {
    if (!node.isNIL()) {
      callback.accept(node);
      this.preorderTreeWalk(node.left, callback);
      this.preorderTreeWalk(node.right, callback);
    }
  }
  
  public void postorderTreeWalk(RedBlackTreeNode<K, V> node, Consumer<RedBlackTreeNode<K, V>> callback) {
    if (!node.isNIL()) {
      this.postorderTreeWalk(node.left, callback);
      this.postorderTreeWalk(node.right, callback);  
      callback.accept(node); 
    }
  }

  public void inorderTreeWalk(Consumer<RedBlackTreeNode<K, V>> callback) {
    inorderTreeWalk(root, callback);
  }

  public void preorderTreeWalk(Consumer<RedBlackTreeNode<K, V>> callback) {
    preorderTreeWalk(root, callback);
  }
  
  public void postorderTreeWalk(Consumer<RedBlackTreeNode<K, V>> callback) {
    postorderTreeWalk(root, callback);
  }

}

