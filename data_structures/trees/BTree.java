package data_structures.trees;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.EmptyIterator;
import data_structures.queues.Queue;

/**
 * This is a basic 2-3-4 tree default implementation, unless otherwise specified
 * with the initializing minimum degree {@code t}. This includes a parallel
 * array to hold a value with the associated key. Since the data structure is
 * generic, it can be anything. A mention at the bottom includes more on this.
 *
 * <p>
 * B-Tree 'T' is a rooted tree having the following properties:
 * </p>
 *
 * <ol>
 * <li>Every node {@code x} has the following attributes:
 * <p>
 * a. {@code x.n} the number of keys currently stored in {@code x}
 * </p>
 * <p>
 * b. {@code x.key}, the {@code x.n} keys themselves store in nondecreasing
 * order such that: {@code x.key[1] <= x.key[2] <= ... <= x.key[x.n]}
 * </p>
 * <p>
 * c. {@code x.leaf}, a boolean value that flags whether the node is an internal
 * node or leaf
 * </p>
 * </li>
 * <li>Each internal node {@code x} also, contains {@code x.n + 1} pointers
 * {@code x.c[1], x.c[2], ...,
 * x.c[x.n+1]} to its children. Leaf nodes have no children, and so their
 * {@code c[i]} or children attributes are undefined.</li>
 * <li>The keys {@code x.key[i]} seperate the ranges of keys stored in each
 * subtree (x.c[i]): if {@code k[i]} is any key stored in the subtree with root
 * x then -
 * {@code k[1] <= x.key[1] <= k[2] <= x.key[2] <= ... <= k[x.n] <= x.key[x.n]}
 * </li>
 *
 * <li>All leaves have the same depth, which is the tree's height {@code h}</li>
 * <li>Nodes have lower bounds and upper bounds on the number of keys they can
 * contain. The bounds are expressed in terms of a fixed integer {@code t >= 2},
 * known as the "Minimum Degree" of the B-tree:
 *
 * <p>
 * a. Every node other than the root must have at least {@code t - 1} keys.
 * Every internal node other than the root must have at least {@code t}
 * children. If the tree is nonempty, the root must have at least one key.
 * </p>
 *
 * <p>
 * b. Every node may contain at most {@code 2t - 1} keys. Therefore, an internal
 * node may have at most 2t children. A node is "full" if it contains exactly
 * {@code 2t - 1} keys.
 * </p>
 * </li>
 * </ol>
 *
 * <p>
 * The simplest B-tree has minimum degree {@code t = 2}. Where every internal
 * node then has either 2, 3, or 4 children, also known as a {@code 2-3-4 tree}.
 * </p>
 *
 * <p>
 * Larger values of {@code t} yield B-trees with smaller height.
 * </p>
 *
 * <p>
 * The number of disk accesses required for most operations is proportional to
 * the height of the B-tree.
 * </p>
 *
 * <p>
 * All the procedures are "one-pass" algorithms that proceed downward from the
 * root of the tree, without having to back up.
 * </p>
 *
 * <p>
 * "Satellite information" associated with a key resides in the same node as the
 * key. In practice, one might actually store with each key just a pointer to
 * another disk page containing the satellite information for that key.
 * </p>
 */
public final class BTree<K, V> {
  public static class BTreeNode<K, V> {
    /**
     * The child {@code BTreeNodes}.
     */
    private BTreeNode<K, V>[] children;

    /**
     * The number of keys currently stored under this node.
     */
    private int count;

    /**
     * Whether this node is a leaf or internal node.
     */
    private boolean leaf;

    /**
     * The actual keys
     */
    private K[] keys;

    /**
     * The values
     */
    private V[] values;

    /**
     * Constructs an empty {@code BTreeNode} with the specified {@code t},
     * <i>maximum degree</i> to determine the maximum number of keys and child nodes
     * for a <i>full node</i>.
     *
     * @param t the maximum degree
     *
     * @throws IllegalArgumentException if the specified maximum degree is less than
     *                                  {@code 2}
     */
    @SuppressWarnings("unchecked")
    private BTreeNode(int t) {
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

    private void shiftKeys(int start, int end) {
      if (start < end) {
        for (int i = start, j = end; i < j; i++) {
          keys[i] = keys[i+1];
          values[i] = values[i+1];
        }
      }
      else {
        for (int i = start, j = end; i > j; i--) {
          keys[i] = keys[i-1];
          values[i] = values[i-1];
        }
      }
    }

    private void removeKeys(int i, int j) {
      if (i > j)
        throw new IllegalArgumentException("Start cannot be greater than end");
      for (; i < j; i++) {
        keys[i] = null;
        values[i] = null;
      }
    }

    private void removeKeys(int i) {
      removeKeys(i, keys.length);
    }

    private void removeChildren(int i) {
      if (i >= children.length)
        throw new IllegalArgumentException("Start cannot be >= children length.");
      for (; i<children.length; i++)
        children[i] = null;
    }

  }

  /**
   * The function used to compare two keys and returns a boolean value indicating
   * whether the first argument is less than the second argument.
   */
  private BiFunction<K, K, Boolean> compare;

  /**
   * The root of the tree
   */
  private BTreeNode<K, V> root;

  /**
   * The "Minimum Degree" of the B-Tree. Used to determine the bounds on the
   * number of keys that a {@code BTreeNode} can hold. {@code t >= 2}. Every node
   * must have at least {@code t - 1} keys and may contain at most {@code 2t - 1}
   * keys. Therefore, an internal node can have up to {@code 2t} children and a
   * "full" node contains exactly {@code 2t - 1} keys.
   */
  private int t;

  /**
   * The maximum children length {@code 2t}
   */
  private int C_LEN;

  /**
   * The maximum keys length {@code 2t - 1}
   */
  private int K_LEN;

  /**
   * Counter tracking the number of entries in the tree.
   */
  private int size;

  /**
   * The number of times this BTree has been structurally modified. Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the BTree fail-fast.
   *
   * @see ConcurrentModificationException
   */
  private int modCount;

  // Enumeration/iteration constants
  private int KEYS = 0;
  private int VALUES = 1;
  private int ENTRIES = 2;

  /**
   * Creates an empty, {@Code BTree}, using the specified compare function to
   * determine whether a given key is smaller than another. Initializes the
   * minimum degree of the tree as well.
   *
   * @param t the minimum degree
   * @param compare an anonymous function that compares two key
   */
  public BTree(int t, BiFunction<K, K, Boolean> compare) {
    if (t < 2)
      throw new IllegalArgumentException("Minimum degree must be >= 2");
    if (compare == null)
      throw new NullPointerException("Compare function cannot be null.");

    this.compare = compare;
    this.t = t;
    root = new BTreeNode<K, V>(t);
    C_LEN = 2 * t;
    K_LEN = C_LEN - 1;
  }

  /**
   * Creates an empty, {@Code BTree}, using the specified compare function to
   * determine whether a given key is smaller than another. Uses a default minimum
   * degree of {@code 2}.
   *
   * @param compare an anonymous function that compares two keys
   */
  public BTree(BiFunction<K, K, Boolean> compare) {
    this(2, compare);
  }

  /**
   * Creates an empty, {@Code BTree}, using the default compare function to
   * determine whether a given key is smaller than another. Initializes the
   * minimum degree to the number specified.
   *
   * @param t the minimum degree
   */
  public BTree(int t) {
    this(t, (K x, K y) -> x.hashCode() < y.hashCode());
  }

  /**
   * The default constructor that uses the default compare function that uses the
   * keys {@code hashCode()} and default minimum degree of {@code 2} then calls
   * the main constructor with these parameters.
   */
  public BTree() {
    this(2, (K x, K y) -> x.hashCode() < y.hashCode());
  }

  /**
   * The internal compare method used to determine if a key is smaller than the
   * other key.
   *
   * @param x key to compare
   * @param y the other key to compare
   * @return whether the first key is smaller than the other key
   *
   * @throws IllegalArgumentException if either key is {@code null} or blank
   */
  private boolean isLessThan(K x, K y) {
    checkKey(x);
    checkKey(y);
    return compare.apply(x, y);
  }

  /**
   * Checks the key to make sure it isn't {@code null} or blank.
   *
   * @param key the key to check
   *
   * @throws IllegalArgumentException if the key is {@code null}, or blank
   */
  private void checkKey(K key) {
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
  private void checkValue(V value) {
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
  private void checkDuplicate(K key) {
    if (hasKey(key))
      throw new IllegalArgumentException("Key already exists in the tree.");
  }

  /**
   * Checks to make sure the specified {@code BTreeNode} is not {@code null}.
   *
   * @param node the node to check
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  private void checkNode(BTreeNode<K, V> node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
  }

  /**
   * Returns the number of entries in the tree.
   *
   * @return the number of nodes
   */
  public int size() {
    return size;
  }

  /**
   * Returns a boolean value indicating whether the tree is empty or not.
   *
   * @return whether the tree is empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the root of the tree
   *
   * @return the tree root
   */
  public BTreeNode<K, V> getRoot() {
    return root;
  }

  /**
   * Removes all the elements from the tree by dereferencing the root.
   */
  public void clear() {
    root = new BTreeNode<K, V>(t);
    size = 0;
    modCount++;
  }

  /**
   * {@code (-)(t)}; the loops run for {@code O(t)} iterations
   *
   * <p>
   * Split-Child takes as input a "nonfull" internal node {@code x} (assumed to be
   * in main memory) and index {@code i} such that {@code x.children[i]} (also
   * assumed to be in main memory) is a "full" child of {@code x}. The procedure
   * then splits that full child in two and adds it to the children of {@code x}.
   * </p>
   *
   * <p>
   * To split a full root, we first make the root a child of a new empty root
   * node, so that we can then use Split-Child.
   * </p>
   *
   * <p>
   * The tree thus grows in height by one; splitting is the only means by which
   * the tree grows.
   * </p>
   *
   * @param node the {@code BTreeNode} to split
   * @param i    the index of the nodes children to split off
   */
  private void splitChild(BTreeNode<K, V> node, int i) {
    BTreeNode<K, V> z = new BTreeNode<K, V>(t);
    BTreeNode<K, V> y = node.children[i];
    int j, len;

    z.leaf = y.leaf;
    // Give new node z the largest t - 1 keys of y
    z.count = t - 1;
    for (j = 0, len = t - 1; j < len; j++) {
      z.keys[j] = y.keys[j + t];
      z.values[j] = y.values[j + t];
    }

    // If y, the node we are splitting, is an internal node, then it can
    // contain children. Loop through, copying the t - 1 largest children
    // from y to the new node z and remove them from y
    if (!y.leaf) {
      for (j = 0; j < t; j++)
        z.children[j] = y.children[j + t];
      y.removeChildren(j);
    }

    // Moves the children up one if needed to place child z if it has
    // larger values than the child before it
    for (j = node.count + 1; j > i; j--)
      node.children[j] = node.children[j-1];
    node.children[i + 1] = z;

    // Moves the keys around on the root for the new one to be inserted
    node.shiftKeys(node.count, i);

    // Inserts the median key of y to x to seperate y from z
    node.keys[i] = y.keys[t - 1];
    node.values[i] = y.values[t - 1];
    node.count++;

    // Adjust the count and remove the transferred keys and values
    y.count = t - 1;
    y.removeKeys(y.count);

    // Disk-Write(y)
    // Disk-Write(z)
    // Disk-Write(x)
  }

  /**
   * CPU time {@code O(th) = O(t log_t n)} Disk Accesses {@code O(h) = O(log_t n)}
   *
   * <p>
   * Inserts the key/value into a nonfull node
   * </p>
   *
   * @param node  the node to insert the key/value into
   * @param key   the key to insert
   * @param value the value to insert
   */
  private void insertNonfull(BTreeNode<K, V> node, K key, V value) {
    int i = 0;

    // If the node is a leaf, move the current keys up one until we reach a
    // position where the key is greater than a key
    if (node.leaf) {
      while (node.keys[i] != null && isLessThan(node.keys[i], key))
        i++;

      // If the new key is less than any of the current keys, we will
      // shift up the keys, up to the index of where the new key goes
      for (int j = node.count; j > i; j--) {
        node.keys[j] = node.keys[j - 1];
        node.values[j] = node.values[j - 1];
      }

      // Inserts the key/value into it position and adjust key count
      node.keys[i] = key;
      node.values[i] = value;
      node.count++;
      // Disk-Write(node)
    }

    // Node is an internal node, so 'i' will be the index of the child to
    // insert into
    else {
      while (node.keys[i] != null && isLessThan(node.keys[i], key))
        i++;

      // Disk-Read([i])

      // If the child node is full, split it
      if (node.children[i].count == (2 * t) - 1) {
        splitChild(node, i);

        // Adjust the index so that the key will be inserted into a spot that is
        // greater than the other keys
        while (i < node.count && isLessThan(node.keys[i], key))
          i++;
      }

      // Insert the key/value into the appropriate nonfull node
      insertNonfull(node.children[i], key, value);
    }
  }

  /**
   * CPU time {@code O(th) = O(t log_t n)} Disk Accesses {@code O(h) = O(log_t n)}
   *
   * <p>
   * Inserts a new key/value pair into the tree. Uses {@link #splitChild()} to
   * ensure that the recursion never descends to a full node.
   * </p>
   *
   * @param key   the key to insert
   * @param value the value to insert
   *
   * @throws IllegalArgumentException if the specified key or value is
   *                                  {@code null}, blank, or already exists in
   *                                  the tree
   */
  public void insert(K key, V value) {
    checkKey(key);
    checkValue(value);
    checkDuplicate(key);

    BTreeNode<K, V> r = root;

    // Checks if the root node is full (contains 2t - 1 keys). If full,
    // sets a new node as the root with the old root as its only child
    if (r.count == (2 * t - 1)) {
      root = new BTreeNode<K, V>(t);
      root.children[0] = r;
      root.leaf = false;

      // With the full node as a child, calls the splitChild() procesdure
      splitChild(root, 0);

      // After splitting the full node into two children, inserts the key/value
      // into the tree rooted at the nonfull root node
      insertNonfull(root, key, value);
    }
    // Root node isn't full, insert into into it
    else
      insertNonfull(r, key, value);

    size++;
    modCount++;
  }

  /**
   * {@code O(t log_t n)}
   *
   * <p>
   * Searching a B-Tree is much like searching a Binary Search Tree (BST), except
   * instead of making a binary, or "two-way", branching decision at each node, we
   * make a multiway branching decision according to the number of the node's
   * children.
   *
   * <p>
   * At each internal node {@code x}, we make an {@code (x.n + 1)-way} branching
   * decision.
   * </p>
   *
   * <p>
   * Search takes as input a pointer to the root node {@code x} of a subtree and a
   * key {@code k} to be searched for. If {@code k} is in the B-tree, it returns
   * the ordered pair {@code (y, i)} consisting of a node {@code y} and an index
   * {@code i} such that {@code y.key[i] = k}, otherwise it returns {@code null}.
   * </p>
   *
   * @param node the node to start the search from
   * @param key  the key of the node to search for
   * @return the {@code Pair} result node or {@code null} if not found
   *
   * @throws NullPointerException     if the specified node is {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  private Pair search(BTreeNode<K, V> node, K key) {
    checkNode(node);
    checkKey(key);
    return _search(node, key);
  }

  /**
   * Searches for a node beginning from the root with a specified key.
   *
   * @param key the key of the node to search for
   * @return the {@code Pair} result node or {@code null} not found or tree is
   *         empty
   *
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  private Pair search(K key) {
    return isEmpty() ? null : search(root, key);
  }

  /**
   * The main search function. Because the function is recursive, the
   * {@link #search()} performs the checks and this performs the actual lookup, so
   * the checks aren't performed on every rescursive call down the path.
   *
   * @param node the node to start the search from
   * @param key  the key of the node to search for
   * @return the {@code Pair} result node or {@code null} if not found
   */
  private Pair _search(BTreeNode<K, V> node, K key) {
    int i = 0;

    while (i < node.count && isLessThan(node.keys[i], key))
      i++;

    if (i < node.count && key == node.keys[i])
      return new Pair(node, i);
    else if (node.leaf)
      return null;
    // Disk-Read(node.children[i])
    return _search(node.children[i], key);
  }

  /**
   * Determines whether a specified key exists in the specified node or in any of
   * its subtrees.
   *
   * @param node the node to begin searching from
   * @param key  the key to find if it exists
   * @return whether the key exists in the node or its subtrees
   *
   * @throws NullPointerException     if the specified node is {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public boolean hasKey(BTreeNode<K, V> node, K key) {
    return search(node, key) != null;
  }

  /**
   * Determines whether a specified key exists in the tree.
   *
   * @param key the key to find if it exists
   * @return whether the key exists in the tree or not
   *
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public boolean hasKey(K key) {
    return search(key) != null;
  }

  /**
   * Retrieves the value from the specified key from the specified node or its
   * subtrees.
   *
   * @param node the node to begin looking for the value from
   * @param key  the key of the corresponding value to find
   * @return the value or {@code null} if not found
   *
   * @throws NullPointerException     if the specified node is {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public V get(BTreeNode<K, V> node, K key) {
    Pair pair = search(node, key);
    return pair != null ? pair.getValue() : null;
  }

  /**
   * Retrieves the value from the specified key from the tree.
   *
   * @param key the key of the corresponding value to find
   * @return the value or {@code null} if not found
   *
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public V get(K key) {
    return get(root, key);
  }

  /**
   * Retrieves the {@code BTreeNode} with the smallest key. From the specified
   * nodes subtree.
   *
   * @param node the node to start searching from
   * @return the node with the smallest key of the specified nodes subtree
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public BTreeNode<K, V> minimum(BTreeNode<K, V> node) {
    checkNode(node);
    return _minimum(node);
  }

  /**
   * Retrieves the {@code BTreeNode} with the smallest key in the tree.
   *
   * @return the node with the smallest key in the tree or {@code null} if tree is
   *         empty
   */
  public BTreeNode<K, V> minimum() {
    if (isEmpty())
      return null;
    return minimum(root);
  }

  /**
   * The main function minimum function. {@link #minimum()} performs the node
   * check so every recursive call down the path doesn't call it again.
   *
   * @param node the node start searching from
   * @return the node with the smallest key of the given nodes subtree
   */
  private BTreeNode<K, V> _minimum(BTreeNode<K, V> node) {
    if (!node.leaf)
      return _minimum(node.children[0]);
    return node;
  }

  /**
   * Retrieves the {@code BTreeNode} with the largest key. From the specified
   * nodes subtree.
   *
   * @param node the node to start searching from
   * @return the node with the largest key of the specified nodes subtree
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public BTreeNode<K, V> maximum(BTreeNode<K, V> node) {
    checkNode(node);
    return _maximum(node);
  }

  /**
   * Retrieves the {@code BTreeNode} with the largest key in the tree.
   *
   * @return the node with the largest key in the tree or {@code null} if tree is
   *         empty
   */
  public BTreeNode<K, V> maximum() {
    if (isEmpty())
      return null;
    return maximum(root);
  }

  /**
   * The main function maximum function. {@link #maximum()} performs the node
   * check so every recursive call down the path doesn't call it again.
   *
   * @param node the node start searching from
   * @return the node with the largest key of the given nodes subtree
   */
  private BTreeNode<K, V> _maximum(BTreeNode<K, V> node) {
    if (node.leaf)
      return node;
    return _maximum(node.children[node.count]);
  }

  /**
   * Finds the largest key {@code k'} that is less than the specified key from the
   * specified node. If there is no valid predecessor key, i.e., the key is the
   * smallest in the tree, it will return {@code null}.
   *
   * @param node   the node to start searching from
   * @param key    the key whose predecessor is being searched for
   * @param parent the parent of the specified node if the node is a leaf and
   *               doesn't contain a valid predecessor value
   * @return the predecessor key {@code k'} or {@code null} if none
   *
   * @throws NullPointerException     if the specified node or parent is
   *                                  {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public K predecessor(BTreeNode<K, V> node, K key, BTreeNode<K, V> parent) {
    checkNode(node);
    checkNode(parent);
    checkKey(key);
    return _predecessor(KEYS, node, key, parent);
  }

  /**
   * Finds the largest key {@code k'} that is less than the specified key from the
   * specified node. If there is no valid predecessor key, i.e., the key is the
   * smallest in the tree, it will return {@code null}. Returns a {@link Pair} of
   * the predecessor node and key.
   *
   * @param node   the node to start searching from
   * @param key    the key whose predecessor is being searched for
   * @param parent the parent of the specified node if the node is a leaf and
   *               doesn't contain a valid predecessor value
   * @return the predecessor {@link Pair} or {@code null} if none
   *
   * @throws NullPointerException     if the specified node or parent is
   *                                  {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public Pair predecessorPair(BTreeNode<K, V> node, K key, BTreeNode<K, V> parent) {
    checkNode(node);
    checkNode(parent);
    checkKey(key);
    return _predecessor(VALUES, node, key, parent);
  }

  /**
   * Finds the largest key {@code k'} in the tree that is less than the specified
   * key. If there is no valid predecessor key, i.e., the key is the smallest in
   * the tree, it will return {@code null}.
   *
   * @param key the key whose predecessor is being searched for
   * @return the predecessor key {@code k'} or {@code null} if none
   *
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public K predecessor(K key) {
    checkKey(key);

    if (isEmpty())
      return null;

    BTreeNode<K, V> node;
    int i = root.count, j;

    if (root.leaf) {
      while (i >= 0) {
        i--;
        if (isLessThan(root.keys[i], key) && root.keys[i] != key)
          return root.keys[i];
      }
      return null;
    }

    // Otherwise, check the immediate children and then the root key
    // to recursively travel down the tree until we reach a leaf
    while (i >= 0) {
      node = root.children[i];
      j = node.count - 1;

      while (j >= 0 && (isLessThan(key, node.keys[j]) || node.keys[j] == key))
        j--;

      if (j >= 0) {
        if (!node.leaf)
          return _predecessor(KEYS, node, key, root);
        return node.keys[j];
      }

      i--;

      if (i >= 0 && isLessThan(root.keys[i], key))
        return root.keys[i];
    }

    return null;
  }

  /**
   * The main predecessor function. {@link #predecessor()} performs the checks and
   * this performs the actual lookup since it is recursive, to prevent redundant
   * checks.
   *
   * @param <T>    generic to allow the return of the {@code K} key or
   *               {@link Pair}
   * @param type   whether the function is returning a key or {@link Pair}
   * @param node   the node to start searching from
   * @param key    the key to find the predecessor of
   * @param parent the parent of the specified node if the node is a leaf and
   *               doesn't contain a valid predecessor value
   * @return the predecessor key, {@link Pair}, or {@code null} if none
   */
  @SuppressWarnings("unchecked")
  public <T> T _predecessor(int type, BTreeNode<K, V> node, K key, BTreeNode<K, V> parent) {
    int i = 0, j = parent.count - 1;

    while (i < node.count && isLessThan(node.keys[i], key))
      i++;

    if (node.leaf && i > 0)
      return type == KEYS ? (T) node.keys[i-1] : (T) new Pair(node, i-1);
    // If node is a leaf, check the parent keys
    else if (node.leaf) {
      while (isLessThan(key, parent.keys[j]))
        j--;

      if (j == -1)
        return null;
      return type == KEYS ? (T) parent.keys[j] : (T) new Pair(parent, j);
    }
    // Node is an internal node, recurse down
    return _predecessor(type, node.children[i], key, node);
  }

  /**
   * Finds the smallest key {@code k'} that is greater than the specified key from
   * the specified node. If there is no valid successor key, i.e., the key is the
   * largest in the tree, it will return {@code null}.
   *
   * @param node   the node to start searching from
   * @param key    the key whose successor is being searched for
   * @param parent the parent of the specified node if the node is a leaf and
   *               doesn't contain a valid successor value
   * @return the successor key {@code k'} or {@code null} if none
   *
   * @throws NullPointerException     if the specified node or parent is
   *                                  {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public K successor(BTreeNode<K, V> node, K key, BTreeNode<K, V> parent) {
    checkNode(node);
    checkNode(parent);
    checkKey(key);
    return _successor(KEYS, node, key, parent);
  }

  /**
   * Finds the smallest key {@code k'} that is greater than the specified key from
   * the specified node. If there is no valid successor key, i.e., the key is the
   * largest in the tree, it will return {@code null}. Returns a {@link Pair} of
   * the successor node and key.
   *
   * @param node   the node to start searching from
   * @param key    the key whose successor is being searched for
   * @param parent the parent of the specified node if the node is a leaf and
   *               doesn't contain a valid successor value
   * @return the successor {@link Pair} or {@code null} if none
   *
   * @throws NullPointerException     if the specified node or parent is
   *                                  {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public Pair successorPair(BTreeNode<K, V> node, K key, BTreeNode<K, V> parent) {
    checkNode(node);
    checkNode(parent);
    checkKey(key);
    return _successor(VALUES, node, key, parent);
  }

  /**
   * Finds the smallest key {@code k'} in the tree that is greater than the
   * specified key. If there is no valid successor key, i.e., the key is the
   * largest in the tree, it will return {@code null}.
   *
   * @param key the key whose successor is being searched for
   * @return the successor key {@code k'} or {@code null} if none
   *
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public K successor(K key) {
    checkKey(key);

    if (isEmpty())
      return null;

    BTreeNode<K, V> node;
    int i = 0, j;

    if (root.leaf) {
      while (i >= 0) {
        if (isLessThan(key, root.keys[i]) && root.keys[i] != key)
          return root.keys[i];
        i++;
      }
      return null;
    }

    // Otherwise, check the immediate children and then the root key
    // to recursively travel down the tree until ;we reach a leaf
    while (i <= root.count) {
      node = root.children[i];
      j = 0;

      while (j < node.count && (isLessThan(node.keys[j], key) || node.keys[j] == key))
        j++;

      if (j < node.count) {
        if (!node.leaf)
          return _successor(KEYS, node, key, root);
        return node.keys[j];
      }

      if (i < root.count && isLessThan(key, root.keys[i]))
        return root.keys[i];

      i++;
    }

    return null;
  }

  /**
   * The main successor function. {@link #successor()} performs the checks and
   * this performs the actual lookup since it is recursive, to prevent redundant
   * checks.
   *
   * @param <T>    generic to allow the return of the {@code K} key or
   *               {@link Pair}
   * @param type   whether the function is returning a key or {@link Pair}
   * @param node   the node to start searching from
   * @param key    the key to find the successor of
   * @param parent the parent of the specified node if the node is a leaf and
   *               doesn't contain a valid successor value
   * @return the successor key, {@link Pair}, or {@code null} if none
   */
  @SuppressWarnings("unchecked")
  public <T> T _successor(int type, BTreeNode<K, V> node, K key, BTreeNode<K, V> parent) {
    int i = node.count, j = 0;

    while (i > 0 && isLessThan(key, node.keys[i-1]))
      i--;

    if (node.leaf && i < node.count)
      return type == KEYS ? (T) node.keys[i] : (T) new Pair(node, i);
    // If node is a leaf, check the parent keys
    else if (node.leaf) {
      while (isLessThan(parent.keys[j], key))
        j++;

      if (j == parent.count)
        return null;
      return type == KEYS ? (T) parent.keys[j] : (T) new Pair(parent, j);
    }
    // Node is an internal node, recurse down
    return _successor(type, node.children[i], key, node);
  }

  /**
   * CPU time {@code O(th) = O(t log_t n)} Disk Accesses {@code O(h) = O(log_t n)}
   *
   * <p>
   * The procedure guarantees that whenever it calls itself recursively on a node
   * {@code x}, the number of keys in {@code x} is at least the minimum degree
   * {@code t}. This requires that sometimes a key may have to be moved into a
   * child node before recursion descends to that child. This condition allows us
   * to delete a key from the tree in one downward pass without having to "back
   * up" (with one exception):
   * </p>
   *
   * <p>
   * When deleting a key in an internal node, the procedure makes a downward pass
   * through the tree but may have to return to the node from which the key was
   * deleted to replace the key with its predecessor or successor (cases 2a and
   * 2b).
   * </p>
   *
   * <p>
   * Since most of the keys are in the leaves, we may expect that in practice,
   * deletion operations are most often used to delete keys from leaves.
   * </p>
   *
   * @param node the node to start key removal process
   * @param key  the key to remove
   *
   * @throws NullPointerException     if the specified node is {@code null}
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public synchronized void delete(BTreeNode<K, V> node, K key) {
    checkNode(node);
    checkKey(key);
    _delete(node, key);
  }

  /**
   * Deletes a key from the tree. starting the recursion process from the root.
   *
   * @param key the key to remove
   *
   * @throws NoSuchElementException   if the tree is empty
   * @throws IllegalArgumentException if the specified key is {@code null} or
   *                                  blank
   */
  public synchronized void delete(K key) {
    if (isEmpty())
      throw new NoSuchElementException("No keys exist in tree.");
    delete(root, key);
  }

  /**
   * The main delete function. {@link #delete()} performs the checks and this
   * performs the deletion since it is recursive, to prevent redundant checks.
   *
   * @param node the node to start the deletion process from
   * @param key  the key to remove
   */
  private void _delete(BTreeNode<K, V> node, K key) {
    BTreeNode<K, V> y, z;
    int i = 0, j, k, len;
    Pair pair;

    // Case 1: Key k is in node x and x is a leaf, delete k from x
    if (node.leaf) {
      for (i = 0; i < node.count; i++) {
        if (node.keys[i] == key) {
          node.count--;
          node.shiftKeys(i, node.count);
          node.removeKeys(node.count);
          size--;
          modCount++;
          return;
        }
      }

      return;
    }

    while (i < node.count && isLessThan(node.keys[i], key))
      i++;

    // Case 2: If key k is in node x and x is an internal node
    if (i < node.count && node.keys[i] == key) {

      /**
       * Case 2a
       *
       * If the child y that precedes k in node x has at least t keys, then find
       * the predecessor k' of k in the subtree rooted at y. Replace key k with k'
       * and then recursively delete k' from the predecessor y.
       */
      if (node.children[i].count >= t) {
        pair = predecessorPair(node.children[i], key, node);
        node.keys[i] = pair.getKey();
        node.values[i] = pair.getValue();
        _delete(node.children[i], node.keys[i]);
        return;
      }

      /**
       * Case 2b
       *
       * If y has fewer than t keys, symmetrically examine child z that follows k
       * in node x. If z has at least t keys, then find the successor k' of k in
       * the subtree rooted at z. Replace k with k' and then recursively delete
       * k' from successor z.
       */
      if (node.children[i+1].count >= t) {
        pair = successorPair(node.children[i+1], key, node);
        node.keys[i] = pair.getKey();
        node.values[i] = pair.getValue();
        _delete(node.children[i+1], node.keys[i]);
        return;
      }

      /**
       * Case 2c
       *
       * Otherwise, if both y and z have only t-1 keys, merge k and all of z into y.
       * Node x loses both k and the pointer to z, and y now contains 2t-1 keys.
       * Free z and recursively delete k from y.
       */
      y = node.children[i];
      z = node.children[i+1];

      // Merge k (key in internal node x) in y
      y.keys[y.count] = key;

      // Merge keys from z to y
      for (j = 0, k = y.count + 1; j < z.count; j++) {
        y.keys[j + k] = z.keys[j];
        y.values[j + k] = z.values[j];
      }
      y.count += z.count + 1;

      // Remove pointer to z from x
      for (j = i + 1; j < node.count; j++)
        node.children[j] = node.children[j+1];
      node.removeChildren(j);

      // Adjust key count and remove k from x
      node.count--;
      node.shiftKeys(i, node.count);
      node.removeKeys(node.count);

      // Recursively delete key from the newly merged node y
      _delete(y, key);
      return;
    }

    /**
     * Case 3
     *
     * If key k is not present in internal node x, determine the root x.children[i],
     * y, of the appropriate subtree that must contain k, if k is in the tree at all.
     * If y has only t-1 keys, execute step 3a or 3b as necessary to guarantee that
     * we descend to a node containing at least t keys. Then finish by recursing on
     * the appropriate child of x.
     */

    // Case 3: child y has at least t keys, then we can recurse on it
    if (node.children[i].count >= t) {
      _delete(node.children[i], key);
      return;
    }

    /**
     * Case 3a
     *
     * If x.children[i], y, only has t-1 keys but has an immediate sibling with
     * at least t keys, give y an extra key by moving a key from x down into y,
     * moving a key from y's immediate left or right sibling up into x, and moving
     * the appropriate child pointer from the sibling into y.
     */
    if (node.children[i].count < t) {
      y = node.children[i];

      // If right sibling z exists and has enough keys
      if (i < node.count && node.children[i+1].count >= t) {
        z = node.children[i+1];

        // Move key from x into y
        y.keys[y.count] = node.keys[i];
        y.values[y.count] = node.values[i];
        y.count++;

        // Move key from right sibling z into x (successor key is > x.keys[i] so
        // we grab the first one)
        node.keys[i] = z.keys[0];
        node.values[i] = z.values[0];

        // Adjust keys in sibling z for the removed key
        z.count--;
        z.shiftKeys(0, z.count);
        z.removeKeys(z.count);

        // If the deficient node is an internal node, we need to take the child
        // corresponding to the seperator key
        if (!y.leaf) {
          y.children[C_LEN - 1] = z.children[0];

          // Remove pointer to the child removed from the sibling z
          for (j = 0, len = C_LEN; j < len; j++)
            z.children[j] = z.children[j+1];
          z.removeChildren(j);
        }

        // Recursively delete key from the node now that it has enough keys
        _delete(y, key);
        return;
      }
      // If left sibling exists and has enough keys
      else if (i > 0 && node.children[i-1].count >= t) {
        z = node.children[i-1];

        // Move all keys up one since we are grabbing a key from the left
        y.shiftKeys(y.count, 0);

        // Move key from x into y
        y.keys[0] = node.keys[i-1];
        y.values[0] = node.values[i-1];
        y.count++;

        // Move key from left sibling z into x (grabs the last key of the sibling)
        node.keys[i-1] = z.keys[z.count-1];
        node.values[i-1] = z.values[z.count-1];

        // Remove key from left sibling now that it is in x (last key, shrink array)
        z.count--;
        z.removeKeys(z.count);

        // If the deficient node is an internal node, we need to take the child
        // corresponding to the seperator key
        if (!y.leaf) {
          for (j = C_LEN - 1; j > 0; j--)
            y.children[j] = y.children[j-1];

          // Take last child - left sibling is predecessor
          y.children[0] = z.children[z.count+1];

          // Remove pointer of child removed from the sibling z
          z.removeChildren(j);
        }

        // Node now has enough keys, recursively delete key from it
        _delete(y, key);
        return;
      }
      /**
       * Case 3b
       *
       * If x.children[i], y, and both of y's immediate siblings have t-1 keys,
       * merge y with one sibling, which involves moving a key from x down
       * into the new merged node to become the median key for that node.
       */
      else {
        // If 'i' is not the last child, set z to be right sibling. Otherwise,
        // set y to be the left child and z to be the right child. This is to
        // preserve the keys order when merging z into y.
        if (i != node.count)
          z = node.children[i+1];
        else {
          y = node.children[i-1];
          z = node.children[i];
          i--;
        }

        // Move a key from x down to become the median key
        y.keys[y.count] = node.keys[i];
        y.values[y.count] = node.values[i];

        // Move keys from z to y
        for (j = 0, k = y.count + 1; j < z.count; j++) {
          y.keys[j + k] = z.keys[j];
          y.values[j + k] = z.values[j];
        }
        y.count += z.count + 1;

        // If the t-1 key nodes aren't leaves, merge the children too
        if (!z.leaf) {
          for (j = 0, k = y.count - z.count; j <= z.count; j++)
            y.children[j + k] = z.children[j];
        }

        // If node's only key was removed, set new root
        if (node.count - 1 == 0)
          root = node.children[0];
        // Otherwise, remove pointer to z from x and key from x that was passed down to y
        else {
          for (j = i + 1; j < node.count; j++)
            node.children[j] = node.children[j+1];
          node.removeChildren(j);
          node.count--;
          node.shiftKeys(i, node.count);
          node.removeKeys(node.count);
        }

        // Recursively delete k from the newly merged node y
        _delete(y, key);
      }
    }
  }

  /**
   * Traverse the tree by visiting the child nodes before the current node.
   *
   * <p>
   * Begins traversal from the supplied {@code BTreeNode} and performs an action
   * through side-effects.
   * </p>
   *
   * @param node     the node to start traversing from
   * @param callback the function with the given action to perform on each
   *                 {@code BTreeNode}
   */
  public void walk(BTreeNode<K, V> node, Consumer<BTreeNode<K, V>> callback) {
    if (node != null) {
      if (!node.leaf) {
        for (int i=0; i < node.children.length; i++) {
          if (node.children[i] != null)
            walk(node.children[i], callback);
        }
      }
      callback.accept(node);
    }
  }

  /**
   * Traverse the tree by visiting the child nodes before the current node.
   *
   * <p>
   * Begins traversal from the {@code root} of the tree.
   * </p>
   *
   * @param callback the function with the given action to perform on each
   *                 {@code BTreeNode}
   */
  public void walk(Consumer<BTreeNode<K, V>> callback) {
    walk(root, callback);
  }

  /**
   * Implemntation that uses the {@link #walk(Consumer)} traversal to create a
   * string of all the {@code BTreeNode} entries in the tree in order by key.
   *
   * @return the tree object string
   */
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    walk((BTreeNode<K, V> node) -> {
      for (int i=0; i < node.keys.length; i++)
        if (node.keys[i] != null)
          sb.append("\s\s\"" + node.keys[i] + " -> " + node.values[i] + "\",\n");
    });

    return sb.toString() + "}";
  }

  /**
   * Internal class to allow {@link BTree#search()} to return the
   * {@code BTreeNode} and index pair. Is used in the {@link BTree#predecessor()}
   * and {@link BTree#successor()} methods to retrieve the key and value.
   */
  protected class Pair {
    BTreeNode<K, V> node;
    int index;

    Pair(BTreeNode<K, V> node, int index) {
      this.node = node;
      this.index = index;
    }

    BTreeNode<K, V> getNode() {
      return node;
    }

    K getKey() {
      return node.keys[index];
    }

    V getValue() {
      return node.values[index];
    }

    int getIndex() {
      return index;
    }
  }

  /**
   * Returns an {@link Iterable} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  private <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr<>(type);
  }

  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  private <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr<>(type);
  }

  public Iterable<K> keys() {
    return getIterable(KEYS);
  }

  public Iterable<V> values() {
    return getIterable(VALUES);
  }

  public Iterable<BTreeNode<K, V>> entries() {
    return getIterable(ENTRIES);
  }

  public Iterator<K> keysIterator() {
    return getIterator(KEYS);
  }

  public Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public Iterator<BTreeNode<K, V>> entriesIterator() {
    return getIterator(ENTRIES);
  }

  /**
   * A tree Iterator class. This class implements the Enumeration, Iterator, and
   * Iterable interfaces, but individual instances can be created with the
   * Iterator methods disabled. This is necessary to avoid unintentionally
   * increasing the capabilities granted a user by passing an Enumeration.
   *
   * <p>
   * This differs from the {@link AbstractTree.Itr} because of the fact
   * that the {@code B-Tree} doesn't do 2-way splits like the others, which h s an
   * n-way split based on the specified minimum degree {@code t}.
   * </p>
   *
   * @param <T> the type of the object that is being enumerated
   */
  private class Itr<T> implements Iterator<T>, Iterable<T> {
    Queue<BTreeNode<K, V>> nodes;
    BTreeNode<K, V> lastNode;
    K key, lastKey;
    int type, index;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = BTree.this.modCount;

    /**
     * Constructs the Iterator that will be used to enumerate the values in the
     * tree.
     *
     * @param type the type of object to enumerate
     */
    private Itr(int type) {
      this.type = type;
      nodes = new Queue<>(BTree.this.size);

      walk((BTreeNode<K, V> node) -> nodes.enqueue(node));
    }

    // Iterable method
    public Iterator<T> iterator() {
      return this;
    }

    /**
     * Checks whether there are more elments to return.
     *
     * @return if this object has one or more items to provide or not
     */
    public boolean hasNext() {
      if (type == ENTRIES)
        return !nodes.isEmpty();

      // Use locals for faster looping
      BTreeNode<K, V> l = lastNode;
      K k = key;
      int i = index, len = K_LEN - 1;

      while (k == null) {
        if (l == null  || i == len)
          if (nodes.isEmpty())
            break;
          else {
            l = nodes.dequeue();
            i = -1;
          }

        k = l.keys[++i];
      }

      index = i;
      key = k;
      lastNode = l;

      return key != null;
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public T next() {
      if (BTree.this.modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (type == ENTRIES && !nodes.isEmpty())
        return (T) (lastNode = nodes.dequeue());

      // Use locals for faster looping
      BTreeNode<K, V> l = lastNode;
      K k = key;
      int i = index, len = K_LEN - 1;

      while (k == null) {
        if (l == null  || i == len)
          if (nodes.isEmpty())
            break;
          else {
            l = nodes.dequeue();
            i = -1;
          }

        k = l.keys[++i];
      }

      index = i;
      key = k;
      lastNode = l;

      if (key != null) {
        key = null;
        lastKey = k;

        return type == KEYS ? (T) k : (T) l.values[i];
      }

      throw new NoSuchElementException("BTree Iterator");
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
     * @throws IllegalStateException if the {@code next} method has not yet been
     *         called, or the {@code remove} method has already been called after
     *         the last call to the {@code next} method.
     *
     * @throws ConcurrentModificationException if a function modified this map
     *         during computation.
     */
    @Override
    public void remove() {
      if (lastKey == null)
        throw new IllegalStateException("Tree Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the tree object while removing entry
      synchronized (BTree.this) {
        BTree.this.delete(lastNode, lastKey);
        expectedModCount++;
        lastKey = null;

        // Key removed from node, decrement index to continue to next element
        index--;
      }
    }
  }

}
