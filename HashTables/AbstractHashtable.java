package Hashtables;

import Hashtables.exceptions.HashtableFullException;

public abstract class AbstractHashtable<K, V> {
  protected Entry<?, ?>[] table;
  protected int m, n;

  /**
   * Initializes an empty, hashtable, with the specified size for the total
   * capacity for the table.
   * 
   * <h4>Tip: Using a large value, preferrably a prime number, will prevent any
   * unwanted errors from occurring.</h4>
   * 
   * @param size the specififed size of the hashtable maximum capacity
   * 
   * @throws IllegalArgumentException if the specified size is less than 1
   */
  protected AbstractHashtable(int size) {
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
  public final synchronized boolean isEmpty() {
    return n == 0;
  }

  /**
   * Returns the number of entries in the hashtable.
   *
   * @return the number of entries in the hashtable
   */
  public final synchronized int size() {
    return n;
  }

  protected abstract int hash(K key, int i);

  /**
   * Inserts the new entry into the hashtable.
   *
   * @param key the key of the entry
   * @param value the value of the entry
   * @return boolean indicating whether the insertion was successful or not
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   * @throws HashtableFullException   if the attempting to insert while the table
   *                                  is full
   */
  public abstract boolean insert(K key, V value) throws HashtableFullException;

  /**
   * Internal method used by the other methods to lookup entries in the hashtable.
   *
   * @param key the key to lookup
   * @return the index of the element with the specified key or {@code -1} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  protected abstract int search(K key);


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
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
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
  public final synchronized V get(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");

    int idx = search(key);

    return idx != -1 ? (V) table[idx].getValue() : null;
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
  public final synchronized boolean delete(K key) {
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
  public final String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    for (int i=0; i<m; i++) {
      if (table[i] != null)
        sb.append("  \"" + table[i].toString() + "\"\n");
    }

    return sb.toString() + "}";
  }
}
