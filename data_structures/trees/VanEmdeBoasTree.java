package data_structures.trees;

import java.util.NoSuchElementException;

/**
 * <h4>Recurrence: {@code T(u) <= T(upper sqrt (u) + O(1))}</h4>
 *
 * <p>
 * The Van Emde Boas (vEB) universe size {@code u}, is the total possible number
 * of elements that can be inserted, can be any exact power of {@code 2}. The
 * proto-vEB structure had the assumption that the universe size
 * {@code u = 2 ** 2k} for some integer {@code k}.
 * </p>
 * 
 * <p>
 * When {@code sqrt(u)} is not an integer, that is, if {@code u} is an odd power
 * of {@code 2} such that {@code u = 2 pow 2k+1 integer k >= 0} then, we divide
 * the {@code lg u} bits of a number into the most significant bits
 * {@code ceil((lg u) / 2)} and least significant bits
 * {@code floor((lg u) / 2)}.
 * </p>
 *
 * <p>
 * Upper square root of u, {@code sqrtU(u)}, is {@code 2 pow ceil((lg u) / 2)}
 * and lower square root of u, {@code sqrtL(u)}, is
 * {@code 2 pow floor((lg u) / 2)} such that, {@code sqrtU(u) * sqrtL(u) = u}
 * </p>
 * 
 * <p>
 * When {@code u} is an even power of {@code 2 (u = 2 pow 2k)},
 * {@code sqrtU(u) = sqrtL(u) = sqrt(u)}
 * </p>
 *
 * <p>
 * When creating a vEB tree, the universe size must be known and must be a power
 * of {@code 2}. If {@code u} is {@code 2}, the base size, then it will only the
 * <i>min</i> and <i>max</i> properties.
 * </p>
 * 
 * <p>
 * The vEB tree only accepts integer keys because math operations are required
 * to navigate through the tree. The order of the elements in the tree is
 * determined by the key the element is assigned when inserted. It does not
 * compare the values.
 * </p>
 *
 * <p>
 * The <i><b>summary</b></i> property is a nested vEB tree with a universe size
 * {@code u} of {@code sqrtU(u)} - {@code vEB(2 pow ceil(lg(u) / 2))}. The
 * summary tree is used to represent the number of clusters present.
 * </p>
 * 
 * <p>
 * A <i><b>cluster</b></i> is {@code sqrtU(u)} arrays of {@code sqrtL(u)} vEB
 * trees.
 * </p>
 *
 * <p>
 * When a vEB tree contains more than two elements, the min and max properties
 * are treated differently: the element stored in min doesn't appear in any of
 * the clusters, unless the vEB tree contains just one element (min == max), the
 * element stored in max, however, does.
 * </p>
 *
 * <p>
 * Total space requirement is {@code O(u)} and is created in {@code (-)(u)}
 * time.
 * </p>
 * 
 * <p>
 * vEB support priority queue operations and dynamic set operations in
 * {@code O(lg lg u)} time but, the keys must be integers in the range of
 * {@code 0 to n - 1}, with no duplicates allowed.
 * </p>
 * 
 * <p>
 * A Red-Black tree can be created in constant time {@code O(1)}, is more
 * favorable versus the {@code O(u)} time for vEB Trees, if, only performing a
 * small number of operations on the vEB Tree.
 * </p>
 * 
 * <p>
 * A Red-Black tree can be faster for all operations other than the
 * search/member operation if the number {@code n} of elements stored is much
 * smaller than the universe size {@code u}.
 * </p>
 */
public final class VanEmdeBoasTree<T> {
  private static class Entry<T> {
    int key;
    T item;

    Entry(int key, T item) {
      this.key = key;
      this.item = item;
    }

    public String toString() {
      return key + " -> " + item;
    }
  }

  /**
   * The universe size of the tree.
   */
  private int u;

  /**
   * Holds the smallest key and the element if {@code u == 2} otherwise, it holds
   * the index for the cluster where the minimum element resides.
   */
  private Entry<T> min;

  /**
   * Holds the largest key and the element if {@code u == 2} otherwise, it holds
   * the index for the cluster where the maximum element resides.
   */
  private Entry<T> max;

  /**
   * vEB trees which contains an overview of keys present in the clusters array.
   * Has a universe size of {@code sqrtU(u) = 2 pow ceil(lg(u) / 2)}.
   */
  private VanEmdeBoasTree<T> summary;

  /**
   * {@code sqrtU(u)} array of {@code sqrtL(u)} vEB trees containing the elements
   * stored.
   */
  private VanEmdeBoasTree<T>[] clusters;

  /**
   * The number of elements stored in this tree.
   */
  private int size;

  /**
   * The number of times this Tree has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the Tree fail-fast.
   *
   * @see ConcurrentModificationException
   */
  private int modCount;
  
  /**
   * Constructs an empty Van Emde Boas tree with the specified universe size.
   * 
   * @param universe the universe size
   * 
   * @throws IllegalArgumentException if the specified universe size is less than,
   *                                  or not a power of {@code 2}
   */
  @SuppressWarnings("unchecked")
  public VanEmdeBoasTree(int universe) {
    if (universe < 2)
      throw new IllegalArgumentException("Universe size must be greater than or equal to 2.");
    if (Integer.highestOneBit(universe) != universe) 
      throw new IllegalArgumentException("Universe size must be a power of 2.");

    // Base case vEB tree has size of 2 for min and max
    u = universe;

    if (universe > 2) {
      int len = sqrtUpper(universe);
      int size = sqrtLower(universe);

      // Summary trees are sqrtU(u) sized to represent the number of clusters
      summary = new VanEmdeBoasTree<>(len);
      clusters = (VanEmdeBoasTree<T>[]) new VanEmdeBoasTree<?>[len];

      // Clusters are sqrtU(u) length arrays of sqrtL(u) sized vEB trees
      for (int i = 0; i < len; i++)
        clusters[i] = new VanEmdeBoasTree<>(size);
    }
  }

  /**
   * Calculates the log base 2 of a number by multiplying log of that
   * number by log2(<i>e</i>).
   */
  private static double log2(int n) {
    return Math.log(n) * 1.4426950408889634;
  }

  /**
   * The upper square root of a number, which is the most significant log2(u)
   * bits: {@code 2 pow ceil((lg u) / 2)}.
   * 
   * @param n the number to get the upper square root of
   * @return the upper square root
   */
  private static int sqrtUpper(int n) {
    return 1 << (int) Math.ceil(log2(n) / 2);
  }

  /**
   * The lower square root of a number, which is the least significant log2(u)
   * bits: {@code 2 pow floor((lg u) / 2)}.
   * 
   * @param n the number to get the lower square root of
   * @return the lower square root
   */
  private static int sqrtLower(int n) {
    return 1 << (int) Math.floor(log2(n) / 2);
  }

  /**
   * Gets the high index value for the key or specified position {@code k}
   * respective to the current tree universe size {@code u}.
   * 
   * <p>
   * Determines the cluster number for which the key is present.
   * </p>
   * 
   * <p>
   * {@code floor(k / 2 pow floor(lg(u) / 2))} which is equivalent to
   * {@code floor(k / sqrtL(u))}
   * </p>
   * 
   * @param k the current position/key
   * @return the high end index
   */
  private int high(int k) {
    return (int) Math.floor(k / sqrtLower(u));
  }

  /**
   * Gets the low index value for the key or specified position {@code k}
   * respective to the current tree universe size {@code u}.
   * 
   * <p>
   * Used to determine the position of the key in a cluster.
   * </p>
   * 
   * {@code x mod 2 pow floor(lg(u) / 2)} which is equivalent to
   * {@code x mod sqrtL(u)}
   * 
   * @param k the current position/key
   * @return the low end index
   */
  private int low(int k) {
    return k % sqrtLower(u);
  }

  /**
   * Gets the key by calculating the lower significant bits of the cluster number
   * index and the lower index number. {@code (x pow floor(lg(u) / 2)) + y}
   * 
   * @param x the cluster number index
   * @param y the offset
   * @return the index for the key for the given parameters
   */
  private int index(int x, int y) {
    return (x << (int) Math.floor(log2(u) / 2)) + y;
  }

  /**
   * Checks the key specified to ensure it is valid by not being a negative
   * integer or exceed the universe size, where the functions will not work and
   * will cause an out of bounds exception.
   * 
   * @param key the key to check
   * 
   * @throws IllegalArgumentException if the specified key is negative or exceeds
   *                                  the universe size
   */
  private void checkKey(int key) {
    if (key < 0)
      throw new IllegalArgumentException("Key cannot be negative.");
    if (key >= u)
      throw new IllegalArgumentException("Key cannot exceed universe size.");
  }

  /**
   * Checks whether the tree is empty or not.
   * 
   * @return whether the tree is empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the number of elements in the tree.
   * 
   * @return the number of elements in the tree
   */
  public int size() {
    return size;
  }

  /**
   * Returns the universe size of the Van Emde Boas tree.
   * 
   * @return the universe size
   */
  public int universe() {
    return u;
  }

  /**
   * Removes all the elements from the tree. Because the base case universe size
   * of {@code 2} which doesn't have a summary tree and clusters, we have to
   * perform a check before attempting to clear them.
   */
  public void clear() {
    min = max = null;

    if (summary != null)
      summary.clear();

    if (clusters != null) {
      for (VanEmdeBoasTree<T> tree : clusters)
        tree.clear();
    }

    size = 0;
    modCount++;
  }

  /**
   * Returns the smallest element in the tree
   * 
   * @return the smallest element or {@code null} if empty
   */
  public T minimum() {
    return min != null ? min.item : null;
  }

  /**
   * Returns the largest element in the tree
   * 
   * @return the largest element or {@code null} if empty
   */
  public T maximum() {
    return max != null ? max.item : null;
  }

  /**
   * Insertion {@code O(lg lg u)}
   * 
   * <p>
   * Inserts an element with the specified key integer.
   * </p>
   * 
   * @param key  the key to insert the element under
   * @param item the element to insert
   * 
   * @throws IllegalArgumentException if the specified key is negative or exceeds
   *                                  the universe size or already exists in the
   *                                  tree
   */
  public synchronized void insert(int key, T item) {
    checkKey(key);

    if (member(key))
      throw new IllegalArgumentException("Duplicate key: " + key + " already exists in the tree.");

    _insert(key, item);
    size++;
    modCount++;
  }

  /**
   * Insertion {@code (lg lg u)}
   * 
   * <p>
   * If the tree is empty, it will set the min and max to the new entry.
   * Otherwise, it checks if the new entry key is less than the current minimum,
   * if so, it swaps the new entry to be the minimum and the old minimum because
   * the entry to be inserted into the proper spot.
   * </p>
   * 
   * <p>
   * If not in a base case vEB, then we check if the cluster number for the
   * current {@code k} is empty. If empty, we insert the new item into the summary
   * vEB tree to represent the new entry in the cluster that will now contain a
   * value. Then, whether the cluster was empty or not, we insert the item into
   * the cluster with the cluster index, the low index of {@code k}. If it was
   * empty, it simply executes the empty vEB statement where it sets both the min
   * and max of that cluster to the new entry.
   * </p>
   * 
   * <p>
   * Because of the <i>Lazy Propagation</i> where the minimum element is only th
   * min property and not stored in the clusters, when a smaller element th the
   * urrent minimum is inserted, we swap the elements and re-insert the minimum
   * When swapped, the key and and the item have to be swapped so that the ele ent
   * can be inserted into its appropriate position. This requires the
   * {@link Entry} class so that the key can be set to the correct key when
   * recursively inserting deeper into the structure with a different key for the
   * smaller universe size {@code u} of the vEB tree.
   * </p>
   * 
   * <p>
   * The element is passed by reference since it is an object, so each
   * {@link Entry} creation doesn't create a copy and doesn't waste space, which
   * helps allow the insertion process to work, by setting the key to the
   * appropriate key based on the universe size.
   * </p>
   * 
   * <p>
   * Whether in a base case vEB or not, we check if the new entry is larger than
   * the current max of this vEB tree. If so, it sets the new max.
   * </p>
   * 
   * <p>
   * Modification: it differs from the normal implementation in that whether the
   * cluster is empty or not, it still calls the recursive insert rather than
   * simply using the if-else statement and doing an empty tree insert (first
   * statement where {@code min == null}). This is because the global key differs
   * when the entry is placed into a cluster and needs to have the low key, which
   * represents the position within the cluster.
   * </p>
   * 
   * @param key  the current key of the element
   * @param item the new element to insert
   */
  private synchronized void _insert(int key, T item) {
    if (min == null)
      min = max = new Entry<T>(key, item);
    else {
      Entry<T> entry = new Entry<T>(key, item);

      if (key < min.key) {
        swap(entry, min);
        key = entry.key;
        item = entry.item;  
      }

      if (u > 2) {
        int high = high(key); 
        // If the cluster for k is empty, update the summary for the new cluster number
        if (clusters[high].min == null)
          summary._insert(high, item);
        // Insert the item into the cluster number of k with the low index key
        clusters[high]._insert(low(key), item);
      }

      if (key > max.key)
        max = entry;
    }
  }

  private void swap(Entry<T> x, Entry<T> y) {
    int key = x.key;
    T item = x.item;
    x.key = y.key;
    x.item = y.item;
    y.key = key;
    y.item = item;    
  }

  /**
   * Member {@code O(lg lg u)}
   * 
   * <p>
   * Returns a boolean value indicating if the specified key exists within the
   * tree.
   * </p>
   *
   * @param key the key to check if exists
   * @return whether the key exists or not and {@code false} if tree is empty
   */
  public boolean member (int key) {
    checkKey(key);
    return !isEmpty() ? _member(key) : false;
  }

  /**
   * Checks the min and max properties, which would hold the actual entry. If not
   * found, it will get the high index of {@code k}, which is the cluster number,
   * and recursively looks in that cluster with the low index of {@code k} for the
   * position (min or max of that cluster). It does this until we reach a base
   * case tree with a universe size {@code u} of {@code 2}, which only has the min
   * and max properties to check.
   * 
   * @param k the current cluster number
   * @return whether the key exists in this cluster or not
   */
  private boolean _member(int k) {
    if ((min != null && min.key == k) || (max != null && max.key == k))
      return true;
    if (u == 2)
      return false;
    return clusters[high(k)]._member(low(k));
  }

  /**
   * Retrieves an element from the tree with the corresponding key.
   * 
   * @param key the key of the element to retrieve
   * @return the element with the specified key or {@code null} if doesn't exist
   */
  public T get(int key) {
    checkKey(key);
    return !isEmpty() ? _get(key) : null;
  }

  private T _get(int k) {
    if (min != null && min.key == k)
      return min.item;
    if (max != null && max.key == k)
      return max.item;
    if (u == 2)
      return null;
    return clusters[high(k)]._get(low(k));
  }

  /**
   * Successor(k) | FindNext(k) {@code O(log2 log2 u)}
   *
   * <p>
   * Finds the next smallest key that is greater than specified key.
   * </p>
   * 
   * @param key the key to find the successor of
   * @return the successor key or {@code -1} if none
   */
  public int successor(int key) {  
    checkKey(key);
    if (isEmpty())
      throw new NoSuchElementException("VanEmdeBoasTree is empty. No possible successor.");
    return _successor(key);
  }

  private int _successor(int k) {
    // Base case tree
    if (u == 2)
      // If k is 0 and there is a max element, return 1 for the max or -1 for no successor
      return k == 0 && max != null ? 1 : -1;
    // If k is less than the minimum, then it is the successor
    else if (min != null && k < min.key)
      return min.key;
    // Successor is equal to or greater than min
    else {
      int high = high(k), low = low(k);
      // Assign max-low the maximum element in k's cluster, x
      Entry<T> maxLow = clusters[high].max;
      // If cluster x contains some element greater than k, by checking if the index of k
      // is less than the index of the maximum in x, then cluster x contains the successor
      if (maxLow != null && low < maxLow.key) {
        int offset = clusters[high]._successor(low);
        // Determine the index of the successor within k's cluster
        return index(high, offset);
      }
      // Otherwise, k is greater than or equal to the greatest element in its cluster, x
      else {
        // Find the immediate cluster greater than cluster x
        int succCluster = summary._successor(high);
        // If there is no cluster greater than cluster x, there is no successor
        if (succCluster == -1)
          return -1;
        // Otherwise, determine the index of the successor within the successorCluster
        else {
          int offset = clusters[succCluster].min.key;
          return index(succCluster, offset);
        }
      }
    }
  }

  /**
   * Predecessor(k) | FindPrev(k) {@code O(log2 log2 u)}
   *
   * <p>
   * Finds the next largest key that is smaller than specified key.
   * </p>
   * 
   * @param key the key to find the predecessor of
   * @return the predecessor key or {@code -1} if none
   */
  public int predecessor(int key) {  
    checkKey(key);
    if (isEmpty())
      throw new NoSuchElementException("VanEmdeBoasTree is empty. No possible predecessor.");
    return _predecessor(key);
  }

  private int _predecessor(int k) {
    // Base case tree
    if (u == 2)
      // If k is 1 and there is a min element, return 0 for the min or -1 for no predecessor
      return k == 1 && min != null ? 0 : -1;
    // If k is greater than the maximum, then it is the predecessor
    else if (max != null && k > max.key)
      return max.key;
    // predecessor is equal to or greater than min
    else {
      int high = high(k), low = low(k);
      // Assign min-low the minimum element in k's cluster, x
      Entry<T> minLow = clusters[high].min;
      // If cluster x contains some element less than k, by checking if the index of k
      // is greater than the index of the maximum in x, then cluster x contains the predecessor
      if (minLow != null && low > minLow.key) {
        int offset = clusters[high]._predecessor(low);
        // Determine the index of the predecessor within k's cluster
        return index(high, offset);
      }
      // Otherwise, k is less than or equal to the smallest element in its cluster, x
      else {
        // Find the immediate cluster less than cluster x
        int predCluster = summary._predecessor(high);
        // If there is no cluster less than cluster x, check min of the vEB tree
        if (predCluster == -1) {
          // If no predCluster, check the min (lazy propagation doesn't place min in a cluster)
          if (min != null && k > min.key)
            return min.key;
          // Otherwise, no predecessor
          return -1;
        }
        // Otherwise, determine the index of the predecessor within the predecessorCluster
        else {
          int offset = clusters[predCluster].max.key;
          return index(predCluster, offset);
        }
      }
    }
  }

  /**
   * Delete(k) | Remove(k) {@code O(lg lg u)}
   *
   * <p>
   * Because of a base case vEB with only two possible elements, the key will be
   * the original key, and not a high or low of the key. When a max or min is used
   * from a cluster to be the new global max or min, a new {@link Entry} has to be
   * made which still references the same item, but needs a different key to
   * distinguish it from a cluster number key and a global key.
   * </p>
   * 
   * <p>
   * A single call to Delete() can make two recursive calls: one on to delete
   * {@code k} from its cluster and another on the summary tree if the cluster
   * became empty after deleting it from the cluster. In order for the second
   * recursive call on the summary tree to occur, cluster {@cdoe x}, where
   * {@code k} was deleted from is now empty. The only way that cluster {@cdoe x}
   * can be empty is if {@code k} was the only element in its cluster when the
   * first recursive call happened. If {@cdoe k} was the only element in its
   * cluster, then that recursive call took {@code O(1)} time, because it executed
   * the first two lines for the base case.
   * <p>
   *
   * <p>
   * This gives two mutually exclusive possibilities:
   * <ul>
   * <li>The first recursive call took constant time</li>
   * <li>The second recursive call on the summary did not occur</li>
   * </ul>
   * 
   * Either case, the recurrence {@code (T(u) <= T(upper sqrt (u) + O(1)))}
   *  characterizes the running time {@code O(lg lg u)}
   * </p>
   *
   * @param key the key to remove
   * 
   * @throws NoSuchElementException if the key doesn't exist in the tree
   */
  public synchronized void delete(int key) {
    checkKey(key);
    if (!member(key))
      throw new NoSuchElementException("Key doesn't exist in tree.");
    _delete(key);
    size--;
  }

  /**
   * Because of the fact that the key values are not references, when moving a min
   * or max from a cluster to the global min or max, such as in the if statement
   * {@code k == min.key}, we need to make a new {@link Entry} but, still
   * referencing the same element but with a different key. This is so when an
   * entry is moved to the global min or max, where the key is the actual key and
   * not a high or low key, simply referencing that entry as the min or max and
   * changing the key will affect the cluster entry, giving it an invalid key for
   * a cluster.
   */
  private synchronized void _delete(int k) {
    // If tree only contains one element
    if (min == max)
      min = max = null;
    // Base case, set min and max to remaining element
    else if (u == 2) {
      // If removing the min, set to max
      if (k == 0)
        min = max;
      // Otherwise, removing the max, set to min
      else
        max = min;
    }
    // We now assume the tree has more than two elements with u >= 4
    // and will have to delete an element from a cluster.
    else {
      // If k is the min, get second lowest element, make it the min and delete old min
      if (k == min.key) {
        // Get the cluster that is the lowest element other than min
        int firstCluster = summary.min.key;
        // Set k to the value of the lowest element in that cluster
        k = index(firstCluster, clusters[firstCluster].min.key);
        // Set the min to the second lowest element to get rid of the old min
        min = new Entry<T>(k, clusters[firstCluster].min.item);
      }

      int high = high(k), low = low(k);
      // Delete k from its cluster whether it was the original k value passed or
      // the element becoming the new minimum.
      clusters[high]._delete(low);
      // If cluster is empty after deleting the element
      if (clusters[high].min == null) {
        // Remove the cluster k's cluster number from the summary
        summary._delete(high);
        // If the maximum element in the summary was the element we removed
        if (k == max.key) {
          // Get the max non-empty cluster number in the summary
          int summaryMax = summary.max != null ? summary.max.key : -1;
          // If all the clusters are empty, then the only remaining element is min
          if (summaryMax == -1)
            max = min;
          // Otherwise, set the max to the maximum element in the max non-empty cluster
          else {
            int maxKey = index(summaryMax, clusters[summaryMax].max.key);
            max = new Entry<T>(maxKey, clusters[summaryMax].max.item);
          }
        }
      }
      // Otherwise, if cluster is not empty and deleted the max element
      else if (k == max.key) {
        // Update max to be the maximum element of the cluster
        int maxKey = index(high, clusters[high].max.key);
        max = new Entry<T>(maxKey, clusters[high].max.item);
      }
    }
  }
}
