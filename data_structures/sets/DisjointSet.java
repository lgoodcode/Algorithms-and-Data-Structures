package data_structures.sets;

/**
 * DisjointSet Forests: an implementation of disjoint sets, representing the
 * sets by rooted trees with each node containing one member and each tree
 * representing one set.
 *
 * <p>
 * Each member points only to its parent. The root of each tree contains the
 * representative and its own parent.
 * </p>
 *
 * <p>
 * Disjoint-set operations:
 * </p>
 * <ul>
 * <li>Make-Set: (constructor)
 * <p>
 * Simply creates a tree consisting of one node
 * </p>
 * </li>
 * <li>Find-Set:
 * <p>
 * Follows parent pointers until we find the root of the tree
 * </p>
 * </li>
 * <li>Union:
 * <p>
 * Causes the root of one tree to point to the root of the other
 * </p>
 * </li>
 * </ul>
 *
 * <p>
 * Heuristics to improve the running time:
 * </p>
 *
 * <ul>
 * <li>Union by Rank
 * <p>
 * Similar to the weighted-union heuristic (the smaller size set is the set that
 * is absorbed into the other). The root of the tree with the fewer nodes points
 * to the root of the tree with more nodes.
 * </p>
 * </li>
 * <li>Path Compression
 * <p>
 * Used in the Find-Set operation to make each node on the find path point
 * directly to the root. This does not change any ranks.
 * </p>
 * </li>
 * </ul>
 *
 * <p>
 * To implement a disjoint-set forest with the union-by-rank heuristic, we must
 * keep track of ranks. With each node {@code x}, we maintain the integer value
 * {@code x.rank}, which is an upper bound on the height of {@code x} (the
 * number of edges in the longest simple path from a descendant leaf to
 * {@code x}).
 * </p>
 * 
 * <p>
 * The Union operation has two cases, depending on whether the roots of the
 * trees have equal rank. If unequal rank, the root with the higher rank becomes
 * the parent of the root of lower rank. If equal ranks, arbitrarily choose one
 * of the two roots as the parent and increment its rank.
 * </p>
 */
public final class DisjointSet<T> {
  /**
   * The parent disjoint-set tree. Will point to itself if it is the root.
   */
  protected DisjointSet<T> parent;

  /**
   * The key of the disjoint-set
   */
  protected T key;

  /**
   * The rank of the tree to determine the root tree of the forest.
   */
  protected int rank;

  /**
   * Creates a disjoint-set with the given key to identify it.
   * 
   * @throws NullPointerException if the key is {@code null}
   */
  public DisjointSet(T key) {
    if (key == null)
      throw new NullPointerException("Key cannot be null.");

    this.key = key;
    parent = this;
    rank = 0;
  }

  /**
   * Returns the parent disjoint-set tree of the forest.
   * 
   * @return the parent disjoint-set
   */
  public DisjointSet<T> getParent() {
    return parent;
  }

  /**
   * Returns the identifying key of the tree.
   * 
   * @return the key of the disjoint-set
   */
  public T getKey() {
    return key;
  }

  /**
   * Returns the rank of the tree within the forest.
   * 
   * @return the rank of the disjoint-set
   */
  public int getRank() {
    return rank;
  }

  /**
   * Links together two disjoint-set trees. Makes the disjoint-set with the larger
   * rank the parent of the two. If their ranks are equal, it will make the second
   * set the parent and increase its rank to distinguish it as the parent of the
   * two.
   * 
   * @param set1 the first disjoint-set to link
   * @param set2 the second disjoint-set to link
   * 
   * @throws NullPointerException if either set is {@code null}
   */
  public static <T> void link(DisjointSet<T> set1, DisjointSet<T> set2) {
    if (set1 == null)
      throw new NullPointerException("Set1 is null");
    if (set2 == null)
      throw new NullPointerException("Set2 is null");

    if (set1.rank > set2.rank)
      set2.parent = set1;
    else {
      set1.parent = set2;

      if (set1.rank == set2.rank)
        set2.rank++;
    }
  }

  /**
   * Finds the root disjoint-set tree within the forest by recursively going up
   * the parent of the set. Once the root is found, it returns it.
   * 
   * @param <T> the type of the key of the disjoint-set
   * @param set the set to find the root of
   * @return the root disjoint-set
   * 
   * @throws NullPointerException if the disjoint-set is {@code null}
   */
  public static <T> DisjointSet<T> findSet(DisjointSet<T> set) {
    if (set == null)
      throw new NullPointerException("Set cannot be null");

    if (set != set.parent)
      set.parent = findSet(set.parent);
    return set.parent;
  }

  /**
   * Finds the root disjoint-set tree within the forest by iteratively going up
   * the parent of each set. Once the root is found, it returns it.
   * 
   * @param <T> the type of the key of the disjoint-set
   * @param set the set to find the root of
   * @return the root disjoint-set
   * 
   * @throws NullPointerException if the disjoint-set is {@code null}
   */
  public static <T> DisjointSet<T> findsetIterative(DisjointSet<T> set) {
    DisjointSet<T> x = set;

    while (x != x.parent)
      x = x.parent;
    return x.parent;
  }

  /**
   * Combines two disjoint-set trees by having the parents point to the set with
   * the larger rank.
   * 
   * @param <T>  the type of the key of the disjoint-set
   * @param set1 the first disjoint-set to union
   * @param set2 the second disjoint-set to union
   */
  public static <T> void union(DisjointSet<T> set1, DisjointSet<T> set2) {
    if (set1 == null)
      throw new NullPointerException("Set1 is null");
    if (set2 == null)
      throw new NullPointerException("Set2 is null");
    link(findSet(set1), findSet(set2));
  }
}
