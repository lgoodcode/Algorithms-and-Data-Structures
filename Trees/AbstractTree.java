package data_structures.trees;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class AbstractTree<K, V> {
  /**
   * The function used to compare two keys and returns a boolean value indicating
   * whether the first argument is less than the second argument.
   */
  protected BiFunction<K, K, Boolean> compareFn;

  /**
   * Counter tracking the number of entries in the tree.
   */
  protected int count;

  /**
   * Creates an empty, tree, using the specified compare function to determine
   * whether a given node's key is smaller than another.
   *
   * @param compareFn an anonymous function that compares two tree node objects
   */
  protected AbstractTree(BiFunction<K, K, Boolean> compareFn) {
    this.compareFn = compareFn;
    count = 0;
  }

  /**
   * The default constructor that uses implements a default comparison function by
   * comparing two keys using their {@code hashCode()} values.
   */
  protected AbstractTree() {
    this((K x, K y) -> x.hashCode() < y.hashCode());
  }

  /**
   * The internal compare method used to determine if a key is smaller than the
   * other key.
   *
   * @param x key of a tree node to compare
   * @param y the other key of a tree node to compare
   * @return whether the first key is smaller than the other key
   *
   * @throws IllegalArgumentException if either key is {@code null} or blank
   */
  protected final boolean isLessThan(K x, K y) {
    checkKey(x);
    checkKey(y);
    return compareFn.apply(x, y);
  }

  /**
   * The internal compare method used to determine if the key of a tree node is
   * smaller than the other tree node key.
   *
   * @param x tree node to compare
   * @param y the other tree node to compare
   * @return whether the first node key is smaller than the other node key
   *
   * @throws NullPointerException     if the either node is {@code null}
   * @throws IllegalArgumentException if either key is {@code null} or blank
   */
  protected final <Node extends TreeNode<K, V>> boolean isLessThan(Node x, Node y) {
    if (x == null || y == null)
      throw new NullPointerException("Node cannot be null.");
    return isLessThan(x.getKey(), y.getKey());
  }

  /**
   * Checks the key to make sure it isn't {@code null} or blank.
   *
   * @param key the key to check
   *
   * @throws IllegalArgumentException if the key is {@code null}, or blank
   */
  protected final void checkKey(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
  }

  /**
   * Checks the value to make sure it isn't {@code null} or blank
   *
   * @param value the value to check
   *
   * @throws IllegalArgumentException if the value is {@code null} or blank
   */
  protected final void checkValue(V value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
  }

  /**
   * Checks the key to make sure it doesn't already exist in the tree. A duplicate
   * key will not insert properly and if is inserted, one of the duplicates will
   * not be found when searched for.
   *
   * @param key the key to check
   *
   * @throws IllegalArgumentException if the key already exists in the tree
   */
  protected final void checkDuplicate(K key) {
    if (hasKey(key))
      throw new IllegalArgumentException("Key already exists in the tree.");
  }

  /**
   * Returns the number of tree nodes in the tree.
   *
   * @return the number of nodes
   */
  public final int size() {
    return count;
  }

  /**
   * Returns a boolean value indicating whether the tree is empty or not.
   *
   * @return whether the tree is empty
   */
  public final boolean isEmpty() {
    return count == 0;
  }

  /**
   * Retrieves the {@code root} {@code TreeNode}.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @return the {@code root} {@code TreeNode}
   */
  public abstract <Node extends TreeNode<K, V>> Node getRoot();

  /**
   * Inserts a new {@code TreeNode} into the tree with the given key and value.
   *
   * @param key   the key of the new node to insert
   * @param value the value of the new node to insert
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or
   *                                  blank, or if the key already exists in the
   *                                  tree
   */
  public abstract void insert(K key, V value);

  /**
   * Commpares the given key to the left and right node of the current
   * {@code TreeNode} so that it can descend further until either the the correct
   * node is found with the matching key, or we reach the end.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the current tree node
   * @param key    the key of the node to find
   * @return the tree node or {@code null} if not found
   *
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public abstract <Node extends TreeNode<K, V>> Node search(Node node, K key);

  /**
   * Search for a node for the specified key starting at the {@code root}.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param key    the key of the node to find
   * @return the tree node or {@code null} if not found
   *
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public final <Node extends TreeNode<K, V>> Node search(K key) {
    return search(getRoot(), key);
  }

  /**
   * Minimum O(h)
   *
   * This just follows the left child pointers until we reach NIL.
   *
   * Tree-Minimum(x) 1 while x.left != NIL 2 x = x.left 3 return x
   */

  /**
   * Finds the {@code TreeNode} with the smallest key by recursively traversing
   * down the left subtree.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the {@code TreeNode} to start traversing from
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   */
  public abstract <Node extends TreeNode<K, V>> Node minimum(Node node);

  /**
   * Finds the {@code TreeNode} with the smallest key by recursively traversing
   * down the left subtree starting at the root of the tree.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   */
  public <Node extends TreeNode<K, V>> Node minimum() {
    return minimum(getRoot());
  }

  /**
   * Maximum O(h)
   *
   * This just follows the right child pointers until we reach NIL.
   *
   * Tree-Maximum(x) 1 while x.right != NIL 2 x = x.right 3 return x
   */

  /**
   * Finds the {@code TreeNode} with the largest key by recursively traversing
   * down the right subtree.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the {@code TreeNode} to start traversing at
   * @return the {@code TreeNode} with the largest key or {@null} if none
   */
  public abstract <Node extends TreeNode<K, V>> Node maximum(Node node);

  /**
   * Finds the {@code TreeNode} with the largest key by recursively traversing
   * down the left subtree starting at the root of the tree.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @return the {@code TreeNode} with the largest key or {@null} if none
   */
  public <Node extends TreeNode<K, V>> Node maximum() {
    return maximum(getRoot());
  }

  /**
   * Returns a boolean indicating whether the tree contains an entry with the
   * specified key.
   *
   * @param key the key to search for
   * @return whether a node in the tree contains the specified key
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public boolean hasKey(K key) {
    return search(key) != null;
  }

  /**
   * Retrieves the value for the corresponding tree node of the given key or
   * {@code null} if not found.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param key    the key of the tree node
   * @return the value or {@code null} if not found
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public <Node extends TreeNode<K, V>> V get(K key) {
    Node node = search(key);
    return node != null ? node.getValue() : null;
  }

  /**
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param x      {@code TreeNode}
   * @param y      {@code TreeNode}
   */
  protected abstract <Node extends TreeNode<K, V>> void transplant(Node x, Node y);

  /**
   * Deletes the specified {@code TreeNode} from the tree. Calls the
   * {@code transplant()} method to adjust the tree nodes to replace the removed
   * node.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the {@code TreeNode} to remove
   * @see #transplant()
   *
   * @throws NullPointerException if the {@code TreeNode} is {@code null}
   */
  public abstract <Node extends TreeNode<K, V>> void deleteNode(Node node);

  /**
   * Deletes a tree node with the specified key.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param key    the key of the tree node to delete
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public final <Node extends TreeNode<K, V>> void delete(K key) {
    Node node = search(key);
    if (node != null)
      deleteNode(node);
  }

  /**
   * Finds the node that will immediately succeed the given {@code TreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the largest key down the right subtree.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the {@code TreeNode} to find the successor of
   * @return the successor or {@code null} if none
   *
   * @throws NullPointerException if the node specified is {@code null}
   */
  public abstract <Node extends TreeNode<K, V>> Node successor(Node node);

  /**
   * Finds the node that will immediately precede the given {@code TreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the smallest key down the left subtree.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @param node   the {@code TreeNode} to find the predecessor of
   * @return the predecessor or {@code null} if none
   *
   * @throws NullPointerException if the node specified is {@code null}
   */
  public abstract <Node extends TreeNode<K, V>> Node predecessor(Node node);

  /**
   * Tree-Walk takes (-)(n) time to walk an n-node binary tree, since after the
   * initial call, the procedure calls itself recursively exactly twice for each
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

  /**
   * Traverse the tree by visiting the left most nodes, which will be in sorted
   * order, if the tree isn't managed in some form.
   *
   * <p>
   * Begins traversal from the supplied {@code TreeNode} and performs an action
   * through side-effects.
   * </p>
   *
   * @param <Node>   {@link TreeNode} or a subclass of
   * @param node     the node to start traversing from
   * @param callback the function with the given action to perform on each
   *                 {@code TreeNode}
   */
  public abstract <Node extends TreeNode<K, V>> void inorderTreeWalk(Node node, Consumer<Node> callback);

  /**
   * Traverse the tree by visiting the current node before the child nodes.
   *
   * <p>
   * Begins traversal from the supplied {@code TreeNode} and performs an action
   * through side-effects.
   * </p>
   *
   * @param <Node>   {@link TreeNode} or a subclass of
   * @param node     the node to start traversing from
   * @param callback the function with the given action to perform on each
   *                 {@code TreeNode}
   */
  public abstract <Node extends TreeNode<K, V>> void preorderTreeWalk(Node node, Consumer<Node> callback);

  /**
   * Traverse the tree by visiting the child nodes before the current node.
   *
   * <p>
   * Begins traversal from the supplied {@code TreeNode} and performs an action
   * through side-effects.
   * </p>
   *
   * @param <Node>   {@link TreeNode} or a subclass of
   * @param node     the node to start traversing from
   * @param callback the function with the given action to perform on each
   *                 {@code TreeNode}
   */
  public abstract <Node extends TreeNode<K, V>> void postorderTreeWalk(Node node, Consumer<Node> callback);

  /**
   * Traverse the tree by visiting the left most nodes, which will be in sorted
   * order, if the tree isn't managed in some form.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param <Node>   {@link TreeNode} or a subclass of
   * @param callback the function with the given action to perform on each
   *                 {@code TreeNode}
   */
  public final <Node extends TreeNode<K, V>> void inorderTreeWalk(Consumer<Node> callback) {
    inorderTreeWalk(getRoot(), callback);
  }

  /**
   * Traverse the tree by visiting the current node before the child nodes.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param <Node>   {@link TreeNode} or a subclass of
   * @param callback the function with the given action to perform on each
   *                 {@code TreeNode}
   */
  public final <Node extends TreeNode<K, V>> void preorderTreeWalk(Consumer<Node> callback) {
    preorderTreeWalk(getRoot(), callback);
  }

  /**
   * Traverse the tree by visiting the child nodes before the current node.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param <Node>   {@link TreeNode} or a subclass of
   * @param callback the function with the given action to perform on each
   *                 {@code TreeNode}
   */
  public final <Node extends TreeNode<K, V>> void postorderTreeWalk(Consumer<Node> callback) {
    postorderTreeWalk(getRoot(), callback);
  }

  /**
   * Bridge method for the {@link #toString()} so that it can be generic for any
   * type of {@code BinaryTree}.
   *
   * @param <Node> {@link TreeNode} or a subclass of
   * @return the object string
   */
  protected abstract <Node extends TreeNode<K, V>> String _toString();

  /**
   * Implemntation that uses the {@link #inorderTreeWalk(Consumer)} traversal to
   * create a string of all the {@code TreeNode} entries in the tree in order by
   * key.
   *
   * @return the tree object string
   */
  public final String toString() {
    return _toString();
  }

}
