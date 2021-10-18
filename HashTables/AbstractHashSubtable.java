package Hashtables;

public abstract class AbstractHashSubtable<K, V> {

  /**
   * The counter to track the number of entries total in the hashtable.
   */
  protected int n;

  /**
   * Checks the key to make sure it isn't {@code null} or blank
   * 
   * @param key the key to check
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  protected final void checkKey(K key) {
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
  protected final void checkValue(V value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");
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
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract void insert(K key, V value);

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
