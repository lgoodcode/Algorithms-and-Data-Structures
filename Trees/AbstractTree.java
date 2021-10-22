package data_structures.trees;

import java.util.function.BiFunction;

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
   * The default constructor that uses implements a default comparison function
   * by comparing two keys using their {@code hashCode()} values.
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
   * Inserts a new tree node into the tree with the given key and value.
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
   * Returns a boolean indicating whether the tree contains an entry with the
   * specified key.
   *
   * @param key the key to search for
   * @return whether a node in the tree contains the specified key
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract boolean hasKey(K key);  

  /**
   * Retrieves the value for the corresponding tree node of the given key or
   * {@code null} if not found.
   * 
   * @param key the key of the tree node
   * @return the value or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract V get(K key);

  /**
   * Deletes a tree node with the specified key.
   * 
   * @param key the key of the tree node to delete
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract void delete(K key);
  
}
