package data_structures.hashtables;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.Entry;
import data_structures.EmptyIterator;

public abstract class AbstractHashtable<K, V> {
  /**
   * The array of {@code Entry} objects that hold the key/value pairs.
   */
  protected Entry<?, ?>[] table;

  /**
   * The number of times this Hashtable has been structurally modified Structural
   * modifications are those that change the number of entries in the Hashtable or
   * otherwise modify its internal structure (e.g., fullRehash, delete).  This field
   * is used to make iterators on Collection-views of the Hashtable fail-fast.
   *
   * @see ConcurrentModificationException
   */
  protected int modCount = 0;

  /**
   * The counter to track the number of entries total in the hashtable.
   */
  protected int n;

  // Enumeration/iteration constants
  private final int KEYS = 0;
  private final int VALUES = 1;
  private final int ENTRIES = 2;

 /**
   * Determines whether the hashtable is empty or not
   *
   * @return whether the table is empty or not
   */
  public final boolean isEmpty() {
    return n == 0;
  }

  /**
   * Returns the number of entries in the hashtable.
   *
   * @return the number of entries in the hashtable
   */
  public final int size() {
    return n;
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
   * Checks the key to make sure it doesn't already exist in the hashtable. A
   * duplicate key will always hash to the same value so it will cause the table
   * to not function properly.
   *
   * @param key the key to check
   *
   * @throws IllegalArgumentException if the key already exists in the hashtable
   */
  protected final void checkDuplicate(K key) {
    if (hasKey(key))
      throw new IllegalArgumentException("Key already exists in the hashtable.");
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

  /**
   * Iterates through the entries of the hashtable and prints it out in a string
   * in an object-like format using an arror "->" to distinguish the relationship
   * between the key and value.
   *
   * @return the hashtable string
   */
  public final String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");
    Iterable<Entry<K, V>> entries = entries();

    entries.forEach((entry) -> sb.append("\s\s\"" + entry.toString() + "\",\n"));

    return sb.toString() + "}";
  }

  /**
   * Returns an {@link Iterable} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  protected <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr<>(type);
  }

  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  protected <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr<>(type);
  }

  /**
   * Returns an iterable of the keys in this hashtable. Use the {@code Iterator}
   * methods on the returned object to fetch the keys sequentially. If the
   * hashtable is structurally modified while enumerating over the keys then the
   * results of enumerating are undefined.
   *
   * <p>
   * Since the type is an {@code Iterable} it can be used in the enhanced for-each
   * loop:
   *
   * <pre>
   * for (Integer key : keys) {
   *   System.out.println("The key is " + key);
   *}
   * </pre>
   * </p>
   *
   * @return an iterable of the keys in this hashtable
   */
  public final Iterable<K> keys() {
    return getIterable(KEYS);
  }

  public final Iterable<V> values() {
    return getIterable(VALUES);
  }

  public final Iterable<Entry<K, V>> entries() {
    return getIterable(ENTRIES);
  }

  public final Iterator<K> keysIterator() {
    return getIterator(KEYS);
  }

  public final Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public final Iterator<Entry<K, V>> entriesIterator() {
    return getIterator(ENTRIES);
  }

  /**
   * A hashtable Iterator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   *
   * @param <T> the type of the object in the table that is being enumerated
   */
  protected abstract class AbstractIterator<T> implements Iterator<T>, Iterable<T> {
    Entry<?, ?>[] entries;
    Entry<?, ?> entry, last;
    int type, size, index = 0;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = AbstractHashtable.this.modCount;

    // Iterable method
    public final Iterator<T> iterator() {
      return this;
    }

    public final boolean hasNext() {
      Entry<?, ?>[] t = entries;
      Entry<?, ?> e = entry;
      int i = index, len = size;

      // Use locals for faster loop iteration
      while (e == null && i < len)
        e = t[i++];

      entry = e;
      index = i;

      return e != null;
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     * @throws ConcurrentModificationException if the hashtable was modified during
     *                                         computation
     * @throws NoSuchElementException          if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public final T next() {
      if (AbstractHashtable.this.modCount != expectedModCount)
        throw new ConcurrentModificationException();

      Entry<?, ?>[] t = entries;
      Entry<?, ?> e = entry;
      int i = index, len = entries.length;

      // Use locals for faster loop iteration
      while (e == null && i < len)
        e = t[i++];

      entry = e;
      index = i;

      if (e != null) {
        last = e;
        entry = null;

        return type == KEYS ? (T) e.getKey() : (type == VALUES ? (T) e.getValue() : (T) e);
      }

      throw new NoSuchElementException("Hashtable Iterator");
    }

    /**
     * {@inheritDoc}
     *
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     *
     * @throws IllegalStateException if the {@code next} method has not yet been
     *         called, or the {@code remove} method has already been called after
     *         the last call to the {@code next} method.
     *
     * @throws ConcurrentModificationException if a function such as fullRehash modified
     *         this map during computation.
     */
    @Override
    @SuppressWarnings("unchecked")
    public final void remove() {
      if (last == null)
        throw new IllegalStateException("Hashtable Iterator");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the hashtable object while removing entry
      synchronized (AbstractHashtable.this) {
        AbstractHashtable.this.delete((K) last.getKey());
        expectedModCount++;
        last = null;
      }
    }
  }

  /**
   * Default Iterator used for hashtables. Sets the entries as the the hashtable
   * and the size so it can be iterated.
   */
  private class Itr<T> extends AbstractIterator<T> {
    Itr(int type) {
      this.type = type;
      entries = AbstractHashtable.this.table;
      this.size = entries.length;
    }
  }

}
