package data_structures.hashtables;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import data_structures.linkedLists.LinkedList;

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
public final class ChainingHashtable<K, V> extends AbstractHashtable<K, V> {
  protected LinkedList<?>[] table;
  protected int m;

  public ChainingHashtable(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size given. Must be larger than 1.");

    m = size;
    table = new LinkedList<?>[size];
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
   * Inserts a new key/value pair into the hashtable. Finds the {@code LinkedList}
   * at the hashed index slot and inserts a new entry into the list with the
   * supplied key and value pair.
   * 
   * @param key   the key to insert
   * @param value the value to insert
   * 
   * @throws IllegalArgumentException if the key or value is {@code null}, blank,
   *                                  or already exists in the hashtable
   */
  @SuppressWarnings("unchecked")
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);
    checkDuplicate(key);

    int hash = hash(key);

    if (table[hash] == null)
      table[hash] = new LinkedList<>();

    ((LinkedList<Entry<K, V>>) table[hash]).insert(new Entry<K, V>(key, value));

    n++;
  }

  /**
   * Internal method used by the other methods to lookup entries in the hashtable.
   * Must use the {@link #checkKey()} method in the implementation so that the
   * other methods that use this method don't have to implement it, making it the
   * single point of failure.
   * 
   * <p>
   * Once the hashed index slot is found, checks if it is {@code null} or not. If
   * not, it iterates through the entries in the list until an entry with the
   * specified key is found.
   * </p>
   *
   * @param key the key to lookup
   * @return the index in the table with the specified key or {@code -1} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  private int search(K key) {
    checkKey(key);
    int hash = hash(key);

    if (table[hash] == null)
      return -1;

    LinkedList<Entry<K, V>> list = ((LinkedList<Entry<K, V>>) table[hash]);
    Iterable<Entry<K, V>> entries = list.values();

    for (Entry<K, V> e : entries)
      if (e.getKey() == key)
        return hash;   
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
  public boolean hasKey(K key) {
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
  public V get(K key) {
    int idx = search(key);

    if (idx == -1)
      return null;
    
    LinkedList<Entry<K, V>> list = ((LinkedList<Entry<K, V>>) table[idx]);
    Iterable<Entry<K, V>> entries = list.values();

    for (Entry<K, V> e : entries)
      if (e.getKey() == key)
        return e.getValue();
    return null;
  }

  /**
   * Deletes an entry in the hashtable with the specified key. Returns a boolean
   * value indicating whether the operation was successful or not. Will remove the
   * list from the table if, after the removal, the list that the entry was
   * removed from is now empty.
   *
   * @param key the key of the entry to delete
   * @return boolean indicating if the entry was deleted or not
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public synchronized boolean delete(K key) {
    int idx = search(key);

    if (idx == -1)
      return false;
    
    LinkedList<Entry<K, V>> list = ((LinkedList<Entry<K, V>>) table[idx]);
    Iterable<Entry<K, V>> entries = list.values();

    for (Entry<K, V> e : entries) {
      if (e.getKey() == key) {
        entries.iterator().remove();
        n--;

        if (list.isEmpty())
          table[idx] = null;
        return true;
      }
    }
    return false;
  }

  protected <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterable<>();
    return new Enumerator<>(type, true);
  }

  protected <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return Collections.emptyIterator();
    return new Enumerator<>(type, true);
  }

  protected <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return Collections.emptyEnumeration();
    return new Enumerator<>(type, false);
  }

  @SuppressWarnings("unchecked")
  protected class Enumerator<T> extends AbstractEnumerator<T> {
    Enumerator(int type, boolean iterator) {
      table = new Entry<?, ?>[n];
      this.type = type;
      this.iterator = iterator;
      size = 0;

      for (LinkedList<Entry<K, V>> list : (LinkedList<Entry<K, V>>[]) ChainingHashtable.this.table) {
        if (list != null) 
          list.values().forEach((entry) -> table[size++] = entry);
      }
    } 
  }

}
