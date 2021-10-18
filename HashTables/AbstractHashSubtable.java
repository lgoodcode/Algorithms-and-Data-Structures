package Hashtables;

public abstract class AbstractHashSubtable<K, V> {

  /**
   * The counter to track the number of entries total in the hashtable.
   */
  protected int n;

  /**
   * Inserts the new entry into the subtable.
   *
   * @param key   the key of the entry
   * @param value the value of the entry
   * @return boolean indicating whether the insertion was successful or not
   */
  public abstract void insert(K key, V value);

  /**
   * Returns a boolean indicating whether the subtable contains an entry with the
   * specified key.
   *
   * @param key the key to search for
   * @return whether an entry in the table contains the specified key
   */
  public abstract boolean hasKey(K key);

  /**
   * Returns the value for the entry with the specified key or {@code null} if not
   * found.
   *
   * @param key the key of the entry value to retrieve
   * @return the value of the entry with the specified key or {@code null} if not
   *         found
   */
  public abstract V get(K key);

  /**
   * Deletes an entry in the hashtable with the specified key. Returns a boolean value
   * indicating whether the operation was successful or not.
   *
   * @param key the key of the entry to delete
   * @return boolean indicating if the entry was deleted or not
   */
  public abstract boolean delete(K key);
}
