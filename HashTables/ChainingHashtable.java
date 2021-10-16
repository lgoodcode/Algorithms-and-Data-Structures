package Hashtables;

import LinkedLists.LinkedList;

/**
 * A hash table is a table that maps the universe U of keys into the slots
 * {@code T[0.. m-1] h: U -> [0, 1, ..., m-1]},
 *
 * <p>
 * where the size m of the hash table is typically much less than the universe
 * {@code |U|}.
 * </p>
 *
 * <p>
 * It uses a hash function {@code h(k)} which computes a deterministic result
 * for a given key k and then places the element into the slot at
 * {@code T[h(k)]}.
 * </p>
 *
 * <p>
 * {@code Collision:} Two keys may hash to the same value, so if both were to be
 * placed in the table it would be a collision.
 * </p>
 *
 * <h4>Collision resolution by Chaining:</h4>
 *
 * <p>
 * We place all the elements that hash to the same slot into the same linked
 * list.
 * </p>
 *
 * <p>
 * The worst case running time for insertion is {@code O(1)} because the
 * operation involves taking the key and hashing, which occurs in {@code O(1)}
 * time, then placing it at the head of the list. Deletion is {@code O(1)}
 * because of the doubly linked lists, we can just modify the pointers. Search
 * has a worst case running time proportional to the length of the list.
 * </p>
 *
 * <p>
 * {@code Load factor:} Given a hash table {@code T} with m slots that stores
 * {@code n} elements, load factor is {@code n / m}, that is, the average number
 * of elements stored in a chain.
 * </p>
 * 
 * <p>
 * The worse-case behavior of hashing with chaining is terrible: all {@code n}
 * keys hash to the same slot, creating a list of length {@code n}, therefore,
 * causing the time of the search operation to be {@code (-)(n)} plus the time
 * to compute the hash function - no better than if we used a linked list for
 * all the elements.
 * </p>
 * 
 * <p>
 * The average-case performance of hashing depends on how well the hash function
 * h distributes the set of keys to be stored among the m slots, on the average.
 * </p>
 *
 * <h4>Hashing functions:</h4>
 *
 * <p>
 * {@code Division Method:} map a key {@code k} into one of {@code m} slots by
 * taking the remainder of {@code k / m : h(k) = k mod m}
 * </p>
 *
 * <p>
 * When using the division method, {@code m} should not be a power of {@code 2},
 * since if {@code m = 2^p}, then {@code h(k)} is just the {@code p}
 * lowest-order bits of {@code k}. A prime not too close to an exact power of
 * {@code 2} is often a good choice for {@code m}.
 * </p>
 *
 * <p>
 * {@code Multiplication Method:} Operates in two steps. First, multiply the key
 * {code k} by a constant {@code A} in the range {@code 0 < A < 1} and extract
 * the fractional part of {@code k * A}. Then multiply this value by {@code m}
 * and take the floor of the result.
 * </p>
 *
 * <p>
 * {@code h(k) = floor[m (kA mod 1)]}, where {@code (k * A) mod 1} means the
 * fractional part of {@code k * A}, that is, {@code k * A - floor[k * A]}
 * </p>
 *
 * <p>
 * An advantage of the multiplication method is that the value of {@code m} is
 * not critical. We typically choose it to be a power of {@code 2}
 * ({@code m = 2^p} for some integer {@code p}), since we can then easily
 * implement the function on most computers.
 * </p>
 */
public final class ChainingHashtable<K, V> {
  LinkedList<?, ?>[] table;
  private int m, n;

  public ChainingHashtable(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size given. Must be larger than 1.");

    m = size;
    table = new LinkedList<?, ?>[size];
  }

 /**
   * Determines whether the hashtable is empty or not
   *
   * @return whether the table is empty or not
   */
  public synchronized boolean isEmpty() {
    return n == 0;
  }

  /**
   * Returns the number of entries in the hashtable.
   *
   * @return the number of entries in the hashtable
   */
  public synchronized int size() {
    return n;
  }

  /**
   * Checks the key to make sure it isn't {@code null} or blank
   * 
   * @param key the key to check
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  private synchronized void checkKey(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");
  }

  /**
   * Checks the value to make sure it isn't {@code null} or blank
   * 
   * @param value the value to check
   * 
   * @throws IllegalArgumentException if the value is {@code null} or blank
   */
  private synchronized void checkValue(V value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");
  }

  /**
   * Hashes the key using the multiplication method described in the class
   * documentation.
   * 
   * @param key the key to hash
   * @return the hashed index slot for the table
   */
  private int hash(K key) {
    return (int) Math.floor(m * ((key.hashCode() * 0.5675) % 1)); 
  }

  /**
   * Inserts a new key/value pair into the hashtable.
   * 
   * @param key   the key to insert
   * @param value the value to insert
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    int hash = hash(key);

    if (table[hash] == null)
      table[hash] = new LinkedList<K, V>();

    ((LinkedList<K, V>) table[hash]).insert(key, value);

    n++;
  }

  /**
   * Internal method used by the other methods to lookup entries in the hashtable.
   * Must use the {@link #checkKey()} method in the implementation so that the
   * other methods that use this method don't have to implement it, making it 
   * the single point of failure.
   *
   * @param key the key to lookup
   * @return the index of the element with the specified key or {@code -1} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  private synchronized int search(K key) {
    checkKey(key);
    int hash = hash(key);

    if (table[hash] != null)
      return ((LinkedList<K, V>) table[hash]).search(key) != null ? hash : -1;
    return -1;
  }

  /**
   * Returns a boolean indicating whether the hashtable contains an entry with the
   * specified key.
   *
   * @param key the key to search for
   * @return whether an entry in the table contains the specified key
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public final synchronized boolean hasKey(K key) {
    return search(key) != -1;
  }

  /**
   * Returns the value for the entry with the specified key or {@code null} if not
   * found.
   *
   * @param key the key of the entry value to retrieve
   * @return the value of the entry with the specified key or {@code null} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public synchronized V get(K key) {
    int idx = search(key);

    if (idx == -1)
      return null;
    return ((LinkedList<K, V>) table[idx]).get(key);
  }

  /**
   * Deletes an entry in the hashtable with the specified key. Returns a boolean value
   * indicating whether the operation was successful or not.
   *
   * @param key the key of the entry to delete
   * @return boolean indicating if the entry was deleted or not
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public synchronized boolean delete(K key) {
    int idx = search(key);

    if (idx != -1) {
      table[idx] = null;
      n--;
      return true;
    }

    return false;
  }

  /**
   * Returns a string JSON object representation of the hashtable.
   *
   * @return a string of the hashtable
   */
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder();
    
    sb.append("{\n");

    for (int i=0; i<m; i++) {
      if (table[i] != null)
        sb.append(table[i].toString().strip() + "\n");
    }

    return sb.toString() + "}";
  }
}