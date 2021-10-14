package Hashtables.OpenAddressing;

import Hashtables.Entry;
import Hashtables.exceptions.HashtableFullException;

/**
 * This hashtable uses the DoubleHashing implementation of OpenAddressing, where
 * it consists of a single table and all the slots are occupied with an element
 * or {@code null}. It also uses a counter, {@code n}, to determine whether the
 * table has reached it's capacity without having to attempt to compute multiple
 * hashes to discover the table is full.
 * 
 * <h3>WARNING:</h3>
 * <p>
 * The table size must be large enough to support a variety of keys. If the table
 * capacity is 20 and a series of keys from 1 to 20 are used, it will be fine.
 * However, if the keys 1-200 are used for a capacity of 20, there is a high
 * liklihood there will be collisions and the secondary hash function works
 * in multiples of its value. So, if key 100 hashes to slot 12 and it is used,
 * the next hash value could result in a index of greater than 20, the table
 * length, causing an error. So choose the keys used carefully, use a 
 * larger than needed hashtable capacity, or simply use a large prime number
 * for the table capacity.
 * </p>
 * 
 * <p>
 * DoubleHashing offers one of the best methods available for open addressing
 * because the permutations produced have many of the characteristics of
 * randomly chosen permutations. Double hashing uses a hash function of the
 * form:
 * </p>
 *
 * <p>
 * <pre>
 * h(k, i) = h(h1(k) + (i * h2(k) % m))
 * </pre>
 * </p>
 * 
 * where both h1 and h2 are auxiliary hash functions. The initial probe goes to
 * position {@code T[h1(k)]} and successive probe positions are offset from
 * previous positions by the amount {@code h2(k) mod m}. Thus, unlike the case
 * of linear or quadratic probing, the probe sequence here depends in two ways
 * upon the key k, since the initial probe position, the offset, or both, may
 * vary.
 *
 * <p>
 * The value {@code h2(k)} must be relatively prime to the hash-table size m for
 * the entire hash table to be searched.
 * </p>
 * 
 * <p>
 * A convenient way to ensure this condition is to let me be a power of 2 and to
 * design h2 so that it always produces an odd number.
 * </p>
 *
 * <p>
 * Another way is to let m be prime and to design h2 so that it always returns a
 * positive integer less than m.
 * </p>
 */
public class DoubleHashing<K, V> {
  private Entry<?, ?>[] table;
  private int m, n = 0;

  /**
   * Creates a new, empty, hashtable that has a specified maximum capacity.
   * 
   * @param size the capacity of the hashtable
   */
  public DoubleHashing(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size given. Must be larger than 1.");

    m = size;
    table = new Entry<?, ?>[size];
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
   * Returns the initialized hashtable number of elements it can hold.
   * 
   * @return the number of elements that can be stored.
   */
  public int capacity() {
    return table.length;
  }

  /**
   * The hash function that consists of two hash functions. The initial hash
   * function hashes the initial slot, if it is occupied, then it uses the number
   * of times it has collided as a counter to activate the secondary hash function
   * to hash to another slot index. This process continues until an open slot is
   * found.
   * 
   * <p>
   * <pre>
   * h1(k) = k % m 
   *h2(k) = 1 + (k % m^)
   *h(k, i) = h(h1(k) + (i * h2(k) % m))
   * </pre>
   * </p>
   * 
   * where {@code m^} is chosen to be slightly less than m - i.e (m-1).
   * Here, m^ is chosed to be {@code m - 2} because when it is only 
   * subtracted by one, it can result in a hash value that would exceed
   * the table size by 1 to allow it to result in a possible index slot.
   *
   * <p>
   * The probe will first look at position {@code h1(k)} followed by adding
   * successive values of {@code h2(k, i)}.
   * </p>
   * 
   * @param k the key to hash
   * @param i the number of rehashes
   * @return the hashed index
   */
  private int hash(K key, int i) {
    return (key.hashCode() % m) + (i * (1 + (key.hashCode() % (m-2))) % m);
  }

  /**
   * Inserts an entry into the hashtable with the specified key and value.
   * 
   * @param key   the key of the entry
   * @param value the value of the entry
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   * @throws HashtableFullException   if the attempting to insert while the table
   *                                  is full
   */
  public synchronized boolean insert(K key, V value) throws HashtableFullException {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank.");
    if (n == m)
      throw new HashtableFullException(m);

    for (int i=0, j = hash(key, i); i < m && j < m; i++, j = hash(key, i)) { 
      if (table[j] == null) {
        table[j] = new Entry<K, V>(key, value);
        n++;
        return true;
      }
    }

    return false;
  }

  /**
   * Internal method used by the other methods to lookup entries in the hashtable.
   * 
   * @param key the key to lookup
   * @return the index of the element with the specified key or {@code -1} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  private synchronized int search(K key) {
    for (int i=0, j = hash(key, i); i < m && j < m; i++, j = hash(key, i)) { 
      if (table[j] != null)
        return j;
    }

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
  public synchronized boolean hasKey(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");

    int idx = search(key);

    if (idx != -1 && table[idx].getKey().equals(key))
      return true;
    return false;
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
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");

    int idx = search(key);

    if (idx != -1 && table[idx].getKey().equals(key))
      return (V) table[idx].getValue();
    return null;
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
  public boolean delete(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");

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

    StringBuilder str = new StringBuilder();
    str.append("{\n");

    for (int i=0; i<m; i++) {
      if (table[i] != null)
        str.append("  \"" + table[i].toString() + "\"\n");
    }

    return str.toString() + "}";
  }
}
