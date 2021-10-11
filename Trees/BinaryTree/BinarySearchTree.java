package Trees.BinaryTree;

import Trees.TreeNode;
import Trees.util.*;

public class BinarySearchTree<K, V> {
  private TreeNode<K, V> root = null;
  private Compare<K> compareFn = null;
  private int count = 0;

  /**
   * Creates an empty, BinaryTree, using the specified compare function to
   * determine whether a given {@code TreeNode} is smaller than another.
   * 
   * @param compareFn an anonymous function that compares two {@TreeNode}s
   */
  public BinarySearchTree(Compare<K> compareFn) {
    this.compareFn = compareFn;
  }

  /**
   * The default constructor that uses a general compare function and calls
   * the main constructor.
   */
  public BinarySearchTree() {
    this((K x, K y) -> x.hashCode() < y.hashCode());
  }

  /**
   * The internal compare method used to determine if the key of a
   * {@code TreeNode} is smaller than the other {@code TreeNode} key.
   * 
   * @param x {@code TreeNode} to compare
   * @param y the other {@code TreeNode} to compare
   * @return whether the first node key is smaller than the other node key
   */ 
  private boolean isLessThan(TreeNode<K, V> x, TreeNode<K, V> y) {
    return compareFn.compare(x.getKey(), y.getKey());
  }

  /**
   * The internal compare method used to determine if a key is smaller
   * than the other key.
   * 
   * @param x key of a {@code TreeNode} to compare
   * @param y the other key of a {@code TreeNode} to compare
   * @return whether the first key is smaller than the other key
   */
  private boolean isLessThan(K x, K y) {
    return compareFn.compare(x, y);
  }

  /**
   * Returns the number of {@code TreeNode}s in the tree.
   * 
   * @return the number of nodes
   */
  public int size() {
    return count;
  }

  /**
   * Insert O(h)
   *
   * Begins at the root and traces a simple path downward looking for a 
   * NIL to replace with z. The procedure maintains a "Trailing Pointer" 
   * y as the parent of x.
   *
   * Tree-Insert(T, z)
   * 1   y = NIL
   * 2   x = T.root
   * 3   while x != NIL
   * 4       y = x
   * 5       if z.key < x.key
   * 6           x = x.left
   * 7       else x = x.right
   * 8   z.p = y
   * 9   if y == NIL
   * 10      T.root = z
   * 11  elseif z.key < y.key
   * 12      y.left = z
   * 13  else y.right = z
   */

  /**
   * Inserts a new {@code TreeNode} into the tree with the given key and value.
   * 
   * @param key   the key of the new key/value node to insert
   * @param value the value of the new key/value node to insert
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or 
   *                                  blank.
   */
  public void insert(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank value.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank value.");

    TreeNode<K, V> x = root;
    TreeNode<K, V> y = null;
    TreeNode<K, V> z = new TreeNode<>(key, value);

    while (x != null) {
      y = x;

      if (isLessThan(z, x))
        x = x.left;
      else 
        x = x.right;
    }

    z.parent = y;

    if (y == null)
      root = z; // Tree was empty
    else if (isLessThan(z, y))
      y.left = z;
    else 
      y.right = z;

    count++;
  }

  /**
   * Maximum O(h) / Minimum O(h)
   *
   * This just follows the left/right child pointers until we reach NIL.
   *
   * Tree-Minimum(x)
   * 1   while x.left != NIL
   * 2       x = x.left
   * 3   return x
   */

  /**
   * Finds the {@code TreeNode} with the smallest key by recursively traversing
   * down the left subtree.
   *
   * @param x the {@code TreeNode} the tree node to start traversing at
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   */
  public TreeNode<K, V> minimum(TreeNode<K, V> x) {
    if (x == null)
      return null;

    if (x.left != null)
      return minimum(x.left);
    return x;
  }  

  /**
   * Finds the {@code TreeNode} with the smallest key by recursively traversing
   * down the left subtree starting at the root of the tree.
   *
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   */
  public TreeNode<K, V> minimum() {
    return minimum(root);
  }

  /**
   * Finds the {@code TreeNOde} with the largest key by recursively traversing
   * down the right subtree.
   *
   * @param x the {@code TreeNode} the tree node to start traversing at
   * @return the {@code TreeNode} with the largest key or {@null} if none
   */
  public TreeNode<K, V> maximum(TreeNode<K, V> x) {
    if (x == null)
      return null;

    if (x.right != null)
      return maximum(x.right);
    return x;
  }

  /**
   * Finds the {@code TreeNode} with the largest key by recursively traversing
   * down the left subtree starting at the root of the tree.
   *
   * @return the {@code TreeNode} with the largest key or {@null} if none
   */
  public TreeNode<K, V> maximum() {
    return maximum(root);
  }

  /**
   * Search O(h)
   *
   * Traces a simple path downward, comparing keys until we reach NIL or the node
   * is found
   *
   * Tree-Search(x, k)
   * 1   if x == NIL or k == x.key
   * 2       return x
   * 3   if k < x.key
   * 4       return Tree-Search(x.left, k)
   * 5   else return Tree-Search(x.right, k)
   */

  /**
   * Commpares the given key to the left and right node of the current
   * {@code TreeNode} so that it can descend further until either the the correct
   * node is found with the matching key, or we reach the end.
   * 
   * @param x   the current tree node
   * @param key the key of the node to find
   * @return the tree node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null}
   */
  public TreeNode<K, V> search(TreeNode<K, V> x, K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
      
    if (x == null || key.equals(x.getKey()))
      return x;
    if (isLessThan(key, x.getKey()))
      return search(x.left, key);
    else 
      return search(x.right, key);
  } 

  /**
   * Search for a node for the specified key starting at the root.
   * 
   * @param key the key of the node to find
   * @return the tree node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public TreeNode<K, V> search(K key) {
    if (key == null || key.toString().isBlank()) 
      throw new IllegalArgumentException("Key cannot be null or blank.");
    return search(root, key);
  }

  /**
   * Retrieves the value for the corresponding {@code TreeNode} of the given key
   * or {@code null} if not found.
   * 
   * @param key the key of the tree node
   * @return the value or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null}
   */
  public V get(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");

    TreeNode<K, V> node = search(key);

    if (node != null && node.getKey().equals(key))
      return node.getValue();

    return null;
  }

  /**
   * Transplant
   *
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   *
   * This does not update y.left or y.right, this is the responsibility of the
   * Transplant caller.
   *
   * Transplant(T, u, v)
   * 1   if u.p == NIL
   * 2       T.root = v 
   * 3   elseif u == u.p.left
   * 4       u.p.left = v 
   * 5   else u.p.right = v
   * 6   if v != NIL
   * 7       v.p = u.p
   */

  /**
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   * 
   * @param x {@code TreeNode}
   * @param y {@code TreeNode}
   */
  private void transplant(TreeNode<K, V> x, TreeNode<K, V> y) {
    // If x is the root of the tree, make y the root
    if (x.parent == null)
      root = y;
    // Updates x.parent.left if x is a left child
    else if (x.equals(x.parent.left))
      x.parent.left = y;
    // Otherwise, updates x.parent.right because x must be a right child
    else 
      x.parent.right = y;

    // Updates y.parent if y is not null.
    if (y != null)
      y.parent = x.parent;
  }
  
  /**
   * Delete O(h)
   *
   * Tree-Delete(T, z)
   * 1   if z.left == NIL
   * 2       Transplant(T, z, z.right)
   * 3   elseif z.right == NIL
   * 4       Transplant(T, z, z.left)
   * 5   else y = Tree-Minimum(z.right)
   * 6       if y.p != z
   * 7           Transplant(T, y, y.right)
   * 8           y.right = z.right
   * 9           y.right.p = y
   * 10      Transplant(T, z, y)
   * 11      y.left = z.left
   * 12      y.left.p y
   */

  /**
   * Deletes the specified {@code TreeNode} from the tree. Calls the
   * {@code transplant()} method to adjust the tree nodes to replace the removed
   * node.
   * 
   * @param z the {@code TreeNode} to remove
   * @see #transplant()
   * 
   * @throws NullPointerException if the {@code TreeNode} is {@code null}
   */
  public void deleteNode(TreeNode<K, V> z) {
    if (z == null)
      throw new NullPointerException("Node to delete cannot be null.");

    /**
     * Case 1: z has no left child
     *
     * We replace z by its right child, which may or may not be NIL.
     */
    if (z.left == null)
      transplant(z, z.right);
    /**
     * Case 2: z has no right child
     *
     * We replace z by its left child
     */
    else if (z.right == null)
      transplant(z, z.left);
    else {
      /**
       * Case 3: z has two children
       *
       * Need to find z's successor y - which must in turn be in z's right subtree -
       * and have y take z's position in the tree. The rest of z's original right
       * subtree becomes y's new right subtree, and z's left subtree becomes y's new
       * left subtree. This case is tricky because it matters whether y is z's right
       * child.
       */

      /**
       * Find node y; successor of z. Because z has a nonempty right subtree, its
       * successor must be the node in that subtree with the smallest key; hence the
       * call to minimum(z.right).
       */
      TreeNode<K, V> y = minimum(z.right);

      if (y.parent != z) {
        /**
         * Case 3a: y is in z's right subtree but is not z's right child. We need to
         * replace y by its own right child, and then replace z by y.
         *
         * In this case, z's successor y, in z's right subtree, will have no left child.
         */
        transplant(y, y.right);
        y.right = z.right;
        y.right.parent = y;
      }

      /**
       * y's right subtree stays the same because y was either z's right child or the
       * found successor that was transplanted for case 3a. We now make z's parent and
       * left child pointers be the same for y, to take z's place.
       */
      transplant(z, y);
      y.left = z.left;
      y.left.parent = y;
    }

    // Decrement size counter
    count--;
  }

  /**
   * Deletes a {@code TreeNode} using a specified key to first find the
   * corresponding node and then passing it to the {@code delete()} method
   * 
   * @param key the key of the {@code TreeNode} to delete
   * 
   * @throws IllegalArgumentException if the key is {@code null}
   */
  public void delete(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
    deleteNode(search(key));
  }

  /**
   * Successor O(h) / Predecessor O(h)
   *
   * The structure of the binary search tree allows us to determine the successor
   * without ever comparing keys. The procedure returns x if it exists and NIL if
   * x is the largest key in the tree.
   *
   * Tree-Successor(x)
   * 1   if x.right != NIL
   * 2       return Tree-Maximum(x.right)
   * 3   y = x.p 
   * 4   while y != NIL and x == y.right
   * 5       x = y
   * 6       y = y.p 
   * 7   return y
   */

  /**
   * Finds the node that will immediately succeed the given {@code TreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the largest key down the right subtree.
   * 
   * @param x the {@code TreeNode} to find the successor of
   * @return the successor or {@code null} if none
   */
  public TreeNode<K, V> successor(TreeNode<K, V> x) {
    if (x.right != null)
      return minimum(x.right);

    TreeNode<K, V> y = x.parent;
    
    while (y != null && x.equals(y.right)) {
      x = y;
      y = y.parent;
    }

    return y;
  }

  /**
   * Finds the node that will immediately precede the given {@code TreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the smallest key down the left subtree.
   * 
   * @param x the {@code TreeNode} to find the predecessor of
   * @return the predecessor or {@code null} if none
   */
  public TreeNode<K, V> predecessor(TreeNode<K, V> x) {
    if (x.left != null)
      return maximum(x.left);

    TreeNode<K, V> y = x.parent;

    while (y != null && x.equals(y.left)) {
      x = y;
      y = y.parent;
    }

    return y;
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
  public void inorderTreeWalk(TreeNode<K, V> x, Callback<TreeNode<K, V>> callback) {
    if (x != null) {
      this.inorderTreeWalk(x.left, callback);
      callback.action(x);
      this.inorderTreeWalk(x.right, callback);
    }
  }

  public void inorderTreeWalk(Callback<TreeNode<K, V>> callback) {
    inorderTreeWalk(root, callback);
  }

  public void preorderTreeWalk(TreeNode<K, V> x, Callback<TreeNode<K, V>> callback) {
    if (x != null) {
      callback.action(x);
      this.preorderTreeWalk(x.left, callback);
      this.preorderTreeWalk(x.right, callback);
    }
  }

  public void preorderTreeWalk(Callback<TreeNode<K, V>> callback) {
    preorderTreeWalk(root, callback);
  } 

  public void postorderTreeWalk(TreeNode<K, V> x, Callback<TreeNode<K, V>> callback) {
    if (x != null) {
      this.postorderTreeWalk(x.left, callback);
      this.postorderTreeWalk(x.right, callback);  
      callback.action(x); 
    }
  }

  public void postorderTreeWalk(Callback<TreeNode<K, V>> callback) {
    postorderTreeWalk(root, callback);
  }

  /**
   * Implemntation that uses the inorderTreeWalk traversal to create
   * a string of all the {@code TreeNode} entries in the tree in order
   * by key.
   * 
   * Displays the object string in JSON format.
   * 
   * @return the tree string 
   */
  public String toString() {
    if (count == 0)
      return "{}";
    
    StringBuilder str = new StringBuilder();
    
    str.append("{");
    
    inorderTreeWalk((TreeNode<K, V> x) -> str.append("\n\"" + x.toString() + "\""));
    
    str.append("\n}");

    return str.toString();
  }
}