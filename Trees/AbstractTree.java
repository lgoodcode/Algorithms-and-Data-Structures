package data_structures.trees;

import data_structures.queues.Queue;

import java.util.Objects;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

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

  protected int modCount;

  private int KEYS = 0;
  private int VALUES = 1;
  private int ENTRIES = 2;

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
   * Checks to make sure the specified {@code TreeNode} is not {@code null}.
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  protected final <Node extends TreeNode<K, V>> void checkNode(Node node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
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

    inorderTreeWalk((TreeNode<K, V> x) -> sb.append("\s\s\"" + x.toString() + "\",\n"));

    return sb.toString() + "}";
  }

  /**
   * Returns an {@link Iterable} of the specified type.
   * 
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  protected abstract <T> Iterable<T> getIterable(int type);
  
  /**
   * Returns an {@link Iterator} of the specified type.
   * 
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  protected abstract <T> Iterator<T> getIterator(int type);
  
  /**
   * Returns an {@link Enumeration} of the specified type.
   * 
   * @param <T>  Generic type to allow any type to be enumerated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Enumeration}
   */
  protected abstract <T> Enumeration<T> getEnumeration(int type);

  public Iterable<K> keys() {
    return getIterable(KEYS);
  }

  public Iterable<V> values() {
    return getIterable(VALUES);
  }

  public <E extends TreeNode<K, V>> Iterable<E> entries() {
    return getIterable(ENTRIES);
  }

  public Iterator<K> keysIterator() {
    return getIterator(KEYS);
  }

  public Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public <E extends TreeNode<K, V>> Iterator<E> entriesIterator() {
    return getIterator(ENTRIES);
  }

  public Enumeration<K> keysEnumeration() {
    return getEnumeration(KEYS);
  }

  public Enumeration<V> valuesEnumeration() {
    return getEnumeration(VALUES);
  }

  public <E extends TreeNode<K, V>> Enumeration<E> entriesEnumeration() {
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
  protected abstract class AbstractEnumerator<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    protected Queue<TreeNode<K, V>> entries;
    protected TreeNode<K, V> last;
    protected int type, size, index = 0;

    /**
     * Indicates whether this Enumerator is serving as an Iterator or an
     * Enumeration.
     */
    protected boolean iterator;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    protected int expectedModCount = AbstractTree.this.modCount;

    // Iterable method
    public final Iterator<T> iterator() {
      return iterator ? this : this.asIterator();
    }

    /**
     * Checks whether there are more elments to return.
     *
     * @return if this object has one or more items to provide or not
     */
    public final boolean hasMoreElements() {
      return entries.hasNextElement();
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     *
     * @throws NoSuchElementException if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public final T nextElement() {
      if (!hasNext())
        throw new NoSuchElementException("Queue enumerator. No items in queue.");
      last = entries.dequeue();
      return type == KEYS ? (T) last.getKey() : (type == VALUES ? (T) last.getValue() : (T) last);
    }

    /**
     * The Iterator method; the same as Enumeration.
     */
    public final boolean hasNext() {
      return hasMoreElements();
    }

    /**
     * Iterator method. Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     */
    public final T next() {
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
    public final void remove() {
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

   /**
   * This class creates an empty {@code Iterable} that has no elements.
   *
   * <ul>
   * <li>{@link Iterator#hasNext} always returns {@code false}.</li>
   * <li>{@link Iterator#next} always throws {@link NoSuchElementException}.</li>
   * </ul>
   *
   * <p>
   * Implementations of this method are permitted, but not required, to return the
   * same object from multiple invocations.
   * </p>
   *
   * @param <T> the class of the objects in the iterable
   * @since 1.1
   */
  protected static final class EmptyIterable<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    // TODO: need to disable the warning here for unused variable
    // static final EmptyIterable<?> EMPTY_ITERABLE = new EmptyIterable<>();
    public EmptyIterable() {}

    // Enumeration methods
    public boolean hasMoreElements() {
      return false;
    }

    public T nextElement() {
      throw new NoSuchElementException();
    }

    // Iterator methods
    public boolean hasNext() {
      return false;
    }

    public T next() {
      throw new NoSuchElementException();
    }

    public void remove() {
      throw new IllegalStateException();
    }

    // Iterable method
    public Iterator<T> iterator() {
      return this;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      Objects.requireNonNull(action);
    }
  }

}
