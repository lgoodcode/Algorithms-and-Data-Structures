package data_structures.trees;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.EmptyEnumerator;
import data_structures.queues.Queue;

public abstract class AbstractTree<K, V> {
  public static class Node<T, E> {
    protected Node<T, E> parent;
    protected Node<T, E> left;
    protected Node<T, E> right;
    private T key;
    private E value;

    /**
     * Constructor that initializes a tree node with the specified key and value.
     *
     * @param key   the key of the node
     * @param value the value of the node
     *
     * @throws IllegalArgumentException if the key or value is {@code null} or blank
     */
    protected Node(T key, E value) {
      if (key == null || key.toString().isBlank())
        throw new IllegalArgumentException("Key cannot be null or empty.");
      if (value == null || value.toString().isBlank())
        throw new IllegalArgumentException("Value cannot be null or empty.");

      this.key = key;
      this.value = value;
    }

    /**
     * Default constructor used for {@code RedBlackTree} sentinel.
     */
    protected Node() {}

    public final T getKey() {
      return key;
    }

    public final E getValue() {
      return value;
    }

    public final String toString() {
      return key + " -> " + value;
    }
  }

  /**
   * The function used to compare two keys and returns a boolean value indicating
   * whether the first argument is less than the second argument.
   */
  protected BiFunction<K, K, Boolean> compare;

  /**
   * Counter tracking the number of entries in the tree.
   */
  protected int count;

  /**
   * The number of times this LinkedList has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete).  This field
   * is used to make iterators on Collection-views of the LinkedList fail-fast.
   *
   * @see ConcurrentModificationException
   */
  protected int modCount;

  // Enumeration/iteration constants
  private int KEYS = 0;
  private int VALUES = 1;
  private int ENTRIES = 2;

  /**
   * Creates an empty, tree, using the specified compare function to determine
   * whether a given node's key is smaller than another.
   *
   * @param compare an anonymous function that compares two tree node objects
   */
  protected AbstractTree(BiFunction<K, K, Boolean> compare) {
    this.compare = compare;
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
    return compare.apply(x, y);
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
  protected final <T extends Node<K, V>> boolean isLessThan(T x, T y) {
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

  protected final <TreeNode extends Node<K, V>> void checkNode(TreeNode node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
  }

  /**
   * Internal method to verify the {@code TreeNode} used for methods is not
   * {@code null} and is of the proper type for the type of tree being used.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or subclass of
   * @param node       the node to verify
   *
   * @throws IllegalArgumentException if the supplied node is not the proper tree
   *                                  node
   */
  protected abstract <TreeNode extends Node<K, V>> void checkType(TreeNode node);

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
   * Retrieves the root {@code TreeNode}.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @return the root {@code TreeNode}
   */
  public abstract <TreeNode extends Node<K, V>> TreeNode getRoot();

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
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the current tree node
   * @param key        the key of the node to find
   * @return the tree node or {@code null} if not found
   *
   * @throws IllegalArgumentException if the tree node type is invalid or if the
   *                                  key is {@code null} or blank
   */
  public final <TreeNode extends Node<K, V>> TreeNode search(TreeNode node, K key) {
    checkType(node);
    checkKey(key);
    return _search(node, key);
  }

  /**
   * The main function for the search method. This is to prevent redundant and
   * useless node checks after the initial check from the client code because, as
   * we traverse the tree, there is no need to constantly check each node again.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the current tree node
   * @param key        the key of the node to find
   * @return the tree node or {@code null} if not found
   */
  protected abstract <TreeNode extends Node<K, V>> TreeNode _search(TreeNode node, K key);

  /**
   * Search for a node for the specified key starting at the {@code root}.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param key        the key of the node to find
   * @return the tree node or {@code null} if not found
   *
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public final <TreeNode extends Node<K, V>> TreeNode search(K key) {
    return search(getRoot(), key);
  }

  /**
   * Minimum O(h)
   *
   * This just follows the left child pointers until we reach NIL.
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
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to start traversing from
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public final <TreeNode extends Node<K, V>> TreeNode minimum(TreeNode node) {
    checkNode(node);
    checkType(node);
    return _minimum(node);
  }

  /**
   * The main function for the minimum method. Performs the actual lookup so the
   * {@code TreeNode} is only checked once from the argument.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to start traversing from
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   */
  protected abstract <TreeNode extends Node<K, V>> TreeNode _minimum(TreeNode node);

  /**
   * Finds the {@code TreeNode} with the smallest key by recursively traversing
   * down the left subtree starting at the root of the tree.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @return the {@code TreeNode} with the smallest key or {@null} if none
   */
  public final <TreeNode extends Node<K, V>> TreeNode minimum() {
    return _minimum(getRoot());
  }

  /**
   * Maximum O(h)
   *
   * This just follows the right child pointers until we reach NIL.
   *
   * Tree-Maximum(x)
   * 1   while x.right != NIL
   * 2      x = x.right
   * 3   return x
   */

  /**
   * Finds the {@code TreeNode} with the largest key by recursively traversing
   * down the right subtree.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to start traversing at
   * @return the {@code TreeNode} with the largest key or {@null} if none
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public final <TreeNode extends Node<K, V>> TreeNode maximum(TreeNode node) {
    checkNode(node);
    checkType(node);
    return _maximum(node);
  }

  /**
   * The main function for the maximum method. Performs the actual lookup so the
   * {@code TreeNode} is only checked once from the argument.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to start traversing from
   * @return the {@code TreeNode} with the largest key or {@null} if none
   */
  protected abstract <TreeNode extends Node<K, V>> TreeNode _maximum(TreeNode node);

  /**
   * Finds the {@code TreeNode} with the largest key by recursively traversing
   * down the left subtree starting at the root of the tree.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @return the {@code TreeNode} with the largest key or {@null} if none
   */
  public final <TreeNode extends Node<K, V>> TreeNode maximum() {
    return _maximum(getRoot());
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
  public final boolean hasKey(K key) {
    return search(key) != null;
  }

  /**
   * Retrieves the value for the corresponding tree node of the given key or
   * {@code null} if not found.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param key        the key of the tree nodes
   * @return the value or {@code null} if not found
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public final <TreeNode extends Node<K, V>> V get(K key) {
    TreeNode node = search(key);
    return node != null ? node.getValue() : null;
  }

  /**
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param x      {@code TreeNode}
   * @param y      {@code TreeNode}
   */
  protected abstract <TreeNode extends Node<K, V>> void transplant(TreeNode x, TreeNode y);

  /**
   * Deletes the specified {@code TreeNode} from the tree. Calls the
   * {@code transplant()} method to adjust the tree nodes to replace the removed
   * node.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to remove
   * @see #transplant()
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public abstract <TreeNode extends Node<K, V>> void deleteNode(TreeNode node);

  /**
   * Deletes a tree node with the specified key.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param key        the key of the tree node to delete
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public final <TreeNode extends Node<K, V>> void delete(K key) {
    TreeNode node = search(key);
    if (node != null)
      deleteNode(node);
  }

  /**
   * Finds the node that will immediately succeed the given {@code TreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the largest key down the right subtree.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to find the successor of
   * @return the successor or {@code null} if none
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public abstract <TreeNode extends Node<K, V>> TreeNode successor(TreeNode node);

  /**
   * Finds the node that will immediately precede the given {@code TreeNode}
   * without comparing keys. This is done by simply returning the child node with
   * the smallest key down the left subtree.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the {@code TreeNode} to find the predecessor of
   * @return the predecessor or {@code null} if none
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public abstract <TreeNode extends Node<K, V>> TreeNode predecessor(TreeNode node);

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
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the node to start traversing from
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public final <TreeNode extends Node<K, V>> void inorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    checkNode(node);
    checkType(node);
    _inorderTreeWalk(node, callback);
  }

  /**
   * The main inordertreewalk traversal method.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the node to start traversing from
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> void _inorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    if (node != null) {
      _inorderTreeWalk((TreeNode) node.left, callback);
      callback.accept(node);
      _inorderTreeWalk((TreeNode) node.right, callback);
    }
  }
  /**
   * Traverse the tree by visiting the current node before the child nodes.
   *
   * <p>
   * Begins traversal from the supplied {@code TreeNode} and performs an action
   * through side-effects.
   * </p>
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the node to start traversing from
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public final <TreeNode extends Node<K, V>> void preorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    checkNode(node);
    checkType(node);
    _preorderTreeWalk(node, callback);
  }

  /**
   * The main predordertreewalk traversal method.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the node to start traversing from
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> void _preorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    if (node != null) {
      callback.accept(node);
      _preorderTreeWalk((TreeNode) node.left, callback);
      _preorderTreeWalk((TreeNode) node.right, callback);
    }
  }

  /**
   * Traverse the tree by visiting the child nodes before the current node.
   *
   * <p>
   * Begins traversal from the supplied {@code TreeNode} and performs an action
   * through side-effects.
   * </p>
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the node to start traversing from
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   *
   * @throws NullPointerException     if the tree node is {@code null}
   * @throws IllegalArgumentException if the tree node type is invalid
   */
  public final <TreeNode extends Node<K, V>> void postorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    checkNode(node);
    checkType(node);
    _postorderTreeWalk(node, callback);
  }

  /**
   * The main postordertreewalk traversal method.
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param node       the node to start traversing from
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> void _postorderTreeWalk(TreeNode node, Consumer<TreeNode> callback) {
    if (node != null) {
      _postorderTreeWalk((TreeNode) node.left, callback);
      _postorderTreeWalk((TreeNode) node.right, callback);
      callback.accept(node);
    }
  }

  /**
   * Traverse the tree by visiting the left most nodes, which will be in sorted
   * order, if the tree isn't managed in some form.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   */
  public final <TreeNode extends Node<K, V>> void inorderTreeWalk(Consumer<TreeNode> callback) {
    _inorderTreeWalk(getRoot(), callback);
  }

  /**
   * Traverse the tree by visiting the current node before the child nodes.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   */
  public final <TreeNode extends Node<K, V>> void preorderTreeWalk(Consumer<TreeNode> callback) {
    _preorderTreeWalk(getRoot(), callback);
  }

  /**
   * Traverse the tree by visiting the child nodes before the current node.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param <TreeNode> {@link AbstractTree.Node} or a subclass of
   * @param callback   the function with the given action to perform on each
   *                   {@code TreeNode}
   */
  public final <TreeNode extends Node<K, V>> void postorderTreeWalk(Consumer<TreeNode> callback) {
    _postorderTreeWalk(getRoot(), callback);
  }

  /**
   * Implemntation that uses the {@link #inorderTreeWalk(Consumer)} traversal to
   * create a string of all the {@code TreeNode} entries in the tree in order by
   * key.
   *
   * @return the tree object string
   */
  public final String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    inorderTreeWalk((Node<K, V> x) -> sb.append("\s\s\"" + x.toString() + "\",\n"));

    return sb.toString() + "}";
  }

  /**
   * Returns an {@link Iterable} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  private final <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, true);
  }

  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  private final <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, true);
  }

  /**
   * Returns an {@link Enumeration} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be enumerated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Enumeration}
   */
  private final <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, false);
  }

  public final Iterable<K> keys() {
    return getIterable(KEYS);
  }

  public final Iterable<V> values() {
    return getIterable(VALUES);
  }

  public final <E extends Node<K, V>> Iterable<E> entries() {
    return getIterable(ENTRIES);
  }

  public final Iterator<K> keysIterator() {
    return getIterator(KEYS);
  }

  public final Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public final <E extends Node<K, V>> Iterator<E> entriesIterator() {
    return getIterator(ENTRIES);
  }

  public final Enumeration<K> keysEnumeration() {
    return getEnumeration(KEYS);
  }

  public final Enumeration<V> valuesEnumeration() {
    return getEnumeration(VALUES);
  }

  public final <E extends Node<K, V>> Enumeration<E> entriesEnumeration() {
    return getEnumeration(ENTRIES);
  }

  /**
   * A tree enumerator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   *
   * @param <T> the type of the object that is being enumerated
   */
  private class Enumerator<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    private Queue<Node<K, V>> entries;
    private Node<K, V> last;
    private int type, size;

    /**
     * Indicates whether this Enumerator is serving as an Iterator or an
     * Enumeration.
     */
    private boolean iterator;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    private int expectedModCount = AbstractTree.this.modCount;

    /**
     * Constructs the enumerator that will be used to enumerate the values in the
     * tree.
     *
     * @param type     the type of object to enumerate
     * @param iterator whether this will serve as an {@code Enumeration} or
     *                 {@code Iterator}
     */
    private Enumerator(int type, boolean iterator) {
      this.size = AbstractTree.this.count;
      this.iterator = iterator;
      this.type = type;
      entries = new Queue<>(size);

      inorderTreeWalk((Node<K, V> node) -> entries.enqueue(node));
    }

    // Iterable method
    public Iterator<T> iterator() {
      return iterator ? this : this.asIterator();
    }

    /**
     * Checks whether there are more elments to return.
     *
     * @return if this object has one or more items to provide or not
     */
    public boolean hasMoreElements() {
      return !entries.isEmpty();
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     *
     * @throws NoSuchElementException if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public T nextElement() {
      if (!hasNext())
        throw new NoSuchElementException("Tree enumerator. No items in queue.");
      last = entries.dequeue();
      return type == KEYS ? (T) last.getKey() : (type == VALUES ? (T) last.getValue() : (T) last);
    }

    /**
     * The Iterator method; the same as Enumeration.
     */
    public boolean hasNext() {
      return hasMoreElements();
    }

    /**
     * Iterator method. Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     */
    public T next() {
      if (AbstractTree.this.modCount != expectedModCount)
        throw new ConcurrentModificationException();
      return nextElement();
    }

    /**
     * {@inheritDoc}
     *
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     *
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this iterator, e.g., if the object is an
     *         {@code Enumeration}.
     *
     * @throws IllegalStateException if the {@code next} method has not yet been
     *         called, or the {@code remove} method has already been called after
     *         the last call to the {@code next} method.
     *
     * @throws ConcurrentModificationException if a function modified this map
     *         during computation.
     */
    @Override
    public void remove() {
      if (!iterator)
        throw new UnsupportedOperationException();
      if (last == null)
        throw new IllegalStateException("Tree Enumerator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the tree object while removing entry
      synchronized (AbstractTree.this) {
        // Pass the current index to remove the last item
        AbstractTree.this.deleteNode(last);
        expectedModCount++;
        last = null;
      }
    }
  }

}
