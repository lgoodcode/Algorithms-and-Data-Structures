package data_structures.hashtables;

import java.util.Objects;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public abstract class AbstractHashtable<K, V> {
  /**
   * The number of times this Hashtable has been structurally modified Structural
   * modifications are those that change the number of entries in the Hashtable or
   * otherwise modify its internal structure (e.g., fullRehash, delete).  This field 
   * is used to make iterators on Collection-views of the Hashtable fail-fast. 
   * (See ConcurrentModificationException).
   */
  protected int modCount = 0;

  /**
   * The counter to track the number of entries total in the hashtable.
   */
  protected int n;

  /**
   * Iteration types
   */
  protected final int KEYS = 0;
  protected final int VALUES = 1;
  protected final int ENTRIES = 2;

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
  protected abstract <T> Iterable<T> getIterable(int type);
  
  /**
   * Returns an {@link Iterator} of the specified type.
   * 
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  protected abstract <T> Iterator<T> getIterator(int type);
  
  /**
   * Returns an {@link Enumeration} of the specified type.
   * 
   * @param <T>  Generic type to allow any type to be enumerated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Enumeration}
   */
  protected abstract <T> Enumeration<T> getEnumeration(int type);

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
   * for (Integer k : keys) {
   *   System.out.println("The key is " + k);
   * }
   * </pre>
   * </p>
   *
   * @return an iterable of the keys in this hashtable
   */
  public Iterable<K> keys() {
    return getIterable(KEYS);
  }

  /**
   * Returns an iterable of the values in this hashtable. Use the {@code Iterator}
   * methods on the returned object to fetch the values sequentially. If the
   * hashtable is structurally modified while enumerating over the values then the
   * results of enumerating are undefined.
   * 
   * @return an iterable of the values in the hashtable
   */
  public Iterable<V> values() {
    return getIterable(VALUES);
  }

  public <E extends Entry<K, V>> Iterable<E> entries() {
    return getIterable(ENTRIES);
  }

  public Iterator<K> keysIterator() {
    return getIterator(KEYS);
  }

  public Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public <E extends Entry<K, V>> Iterator<E> entriesIterator() {
    return getIterator(ENTRIES);
  }

  public Enumeration<K> keysEnumeration() {
    return getEnumeration(KEYS);
  }

  public Enumeration<V> valuesEnumeration() {
    return getEnumeration(VALUES);
  }

  public <E extends Entry<K, V>> Enumeration<E> entriesEnumeration() {
    return getEnumeration(ENTRIES);
  }

  /**
   * A hashtable enumerator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   * 
   * @param <T> the type of the object in the table that is being enumerated
   */
  protected abstract class AbstractEnumerator<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    protected Entry<?, ?>[] table;
    protected Entry<?, ?> entry, last;
    protected int type, index;

    /**
     * Indicates whether this Enumerator is serving as an Iterator or an
     * Enumeration.
     */
    protected boolean iterator;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    protected int expectedModCount = AbstractHashtable.this.modCount;

    // Iterable method
    public final Iterator<T> iterator() {
      return iterator ? this : this.asIterator();
    }

    /**
     * Checks whether there are more elments to return.
     *
     * @return if this object has one or more items to provide or not
     */
    public final boolean hasMoreElements() {
      Entry<?, ?>[] t = table;
      Entry<?, ?> e = entry;
      int i = index;

      /* Use locals for faster loop iteration */
      while (e == null && i > 0) {
        e = t[--i];
      }

      entry = e;
      index = i;

      return e != null;
    }

    /**
     * Returns the next element if it has one to provide.
     * 
     * @return the next element
     * 
     * @throws NoSuchElementException if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public final T nextElement() {
      Entry<?, ?>[] t = table;
      Entry<?, ?> e = entry;
      int i = index;

      /* Use locals for faster loop iteration */
      while (e == null && i > 0) {
        e = t[--i];
      }

      entry = e;
      index = i;

      if (e != null) {
        last = e;
        entry = null;

        return type == KEYS ? (T) e.getKey() : (type == VALUES ? (T) e.getValue() : (T) e);
      }

      throw new NoSuchElementException("Hashtable Enumerator");
    }

    /**
     * The Iterator method; the same as Enumeration.
     */
    public final boolean hasNext() {
      return hasMoreElements();
    }

    /**
     * Iterator method. Returns the next element in the iteration.
     * 
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the fullRehash function modified
     *         this map during computation.
     */
    public final T next() {
      if (AbstractHashtable.this.modCount != expectedModCount)
        throw new ConcurrentModificationException();
      return nextElement();
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
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this iterator, e.g., if the object is an
     *         {@code Enumeration}.
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
      if (!iterator)
        throw new UnsupportedOperationException();
      if (last == null)
        throw new IllegalStateException("Hashtable Enumerator");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the hashtable object while removing entry
      synchronized (AbstractHashtable.this) {
        if (AbstractHashtable.this.delete((K) last.getKey())) {
          expectedModCount++;
          last = null;
          return;
        }
        throw new ConcurrentModificationException();
      }
    }
  }

   /**
   * This class creates an empty {@code Iterable} that has no elements.
   *
   * <ul>
   * <li>{@link Iterator#hasNext} always returns {@code false}.</li>
   * <li>{@link Iterator#next} always throws {@link NoSuchElementException}.</li>
   * </ul>
   *
   * <p>
   * Implementations of this method are permitted, but not required, to return the
   * same object from multiple invocations.
   * </p>
   *
   * @param <T> the class of the objects in the iterable
   */
  protected static final class EmptyIterable<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    /**
     * This is set so the class object will have a single value; a endless cycle of
     * itself, so that the this$0 value pointing to the CuckooHashtable doesn't
     * exist.
     */
    // TODO: need to disable the warning here for unused variable
    // static final EmptyIterable<?> EMPTY_ITERABLE = new EmptyIterable<>();
    public EmptyIterable() {}

    // Enumeration methods
    public boolean hasMoreElements() {
      return false;
    }

    public T nextElement() {
      throw new NoSuchElementException();
    }

    // Iterator methods
    public boolean hasNext() {
      return false;
    }

    public T next() {
      throw new NoSuchElementException();
    }

    public void remove() {
      throw new IllegalStateException();
    }

    // Iterable method
    public Iterator<T> iterator() {
      return this;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      Objects.requireNonNull(action);
    }
  }
}
