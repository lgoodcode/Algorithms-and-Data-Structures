package data_structures.hashtables;

import data_structures.Entry;

public abstract class AbstractStaticHashtable<K, V> extends AbstractHashtable<K, V> {
  /**
   * The array of {@code Entry} objects that hold the key/value pairs.
   */
  protected Entry<?, ?>[] table;

  /**
   * The table size
   */
  protected int m;

  /**
   * Returns the initialized hashtable number of elements it can hold.
   * 
   * @return the number of elements that can be stored.
   */
  public final int capacity() {
    return table.length;
  }

  /**
   * Checks if the table is full before insertions.
   * 
   * @throws IllegalStateException if the table is full
   */
  protected final void checkCapacity() {
    if (n == table.length)
      throw new IllegalStateException("Hashtable is full.");
  }

  /**
   * Inserts the new entry into the hashtable.
   *
   * @param key   the key of the entry
   * @param value the value of the entry
   * @return boolean indicating whether the insertion was successful or not
   *
   * @throws IllegalStateException    if attempting to insert while the table is
   *                                  full
   * @throws IllegalArgumentException if the key or value is {@code null}, blank,
   *                                  or already exists in the hashtable
   */
  public abstract void insert(K key, V value);

  /**
   * Performs a lookup in the hashtable and returns the index of the entry with
   * the specified key or {@code -1} if not found.
   * 
   * <p>
   * Must use the {@link #checkKey()} method in the implementation so that the
   * other methods that use this method don't have to implement it, making it a
   * point of failure.
   * </p>
   *
   * @param key the key to lookup
   * @return the index of the element with the specified key or {@code -1} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract int search(K key);


  /**
   * Returns a boolean indicating whether the hashtable contains an entry with the
   * specified key.
   *
   * @param key the key to search for
   * @return whether an entry in the table contains the specified key
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract boolean hasKey(K key);

  /**
   * Returns the value for the entry with the specified key or {@code null} if not
   * found.
   *
   * @param key the key of the entry value to retrieve
   * @return the value of the entry with the specified key or {@code null} if not
   *         found
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract V get(K key);

  /**
   * Deletes an entry in the hashtable with the specified key. Returns a boolean value
   * indicating whether the operation was successful or not.
   *
   * @param key the key of the entry to delete
   * @return boolean indicating if the entry was deleted or not
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract boolean delete(K key);

}
