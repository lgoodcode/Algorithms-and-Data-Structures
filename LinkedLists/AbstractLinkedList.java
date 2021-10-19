package data_structures.linkedLists;

import java.util.Objects;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * Creates a simple forward-feed LinkedList that performs, at worst case, O(n).
 * This is because all operations are performed in a linear fashion, iterating
 * through the list until either the desired node is found or the end is reached.
 */
public abstract class AbstractLinkedList<K, V> {
  protected LinkedListNode<K, V> head = null;

  /**
   * The counter to track the number of entries total in the linkedlist.
   */
  protected int size = 0;

  /**
   * The number of times this Hashtable has been structurally modified Structural
   * modifications are those that change the number of entries in the Hashtable or
   * otherwise modify its internal structure (e.g., fullRehash, delete).  This field 
   * is used to make iterators on Collection-views of the Hashtable fail-fast. 
   * (See ConcurrentModificationException).
   */
  protected int modCount = 0;

  /**
   * Iteration types
   */
  protected final int KEYS = 0;
  protected final int VALUES = 1;
  protected final int ENTRIES = 2;

  /**
   * Determines whether the list is empty or not
   * 
   * @return boolean indicating if the list is empty
   */
  public final boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the number of elements in the list
   * 
   * @return the number of elements in the list
   */
  public final int size() {
    return size;
  }

  /**
   * Returns the node at the head of list.
   * 
   * @return the node at the head or {@code null} if none
   */
  public LinkedListNode<K, V> getHead() {
    return head;
  }

  /**
   * Checks whether the specified index is valid, meaning it must be greater or
   * equal to {@code 0} and less than the current size.
   * 
   * @param index the specified index to check
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  protected final void checkIndex(int index) {
    if (index < 0 && index >= size)
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
  }

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
   * Inserts a new node with the specified key and value pair. If there is no
   * {@code head}, then it will simply set the head as the new node. Otherwise,
   * the new node's {@code next} will point to the current head and the new head
   * will be set as the new node.
   * 
   * @param key   the key of the new node
   * @param value the value of the new node
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or
   *                                  blank.
   */
  public abstract void insert(K key, V value);

  /**
   * Inserts a new node at the specified index position.
   * 
   * @param index the position to insert the new node
   * @param key   the key of the new node
   * @param value the value of the new node
   * 
   * @throws IllegalArgumentException  if the key or value is {@code null} or
   *                                   empty.
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public abstract void insertAt(int index, K key, V value);

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the node or
   * {@code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public abstract LinkedListNode<K, V> search(K key);

  /**
   * Returns the node at the specified index or {@code null} if there are no items
   * in the list.
   * 
   * @param index the index position of the node to return
   * @return the node at the specified index position or {@code null} if list is
   *         empty
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public abstract LinkedListNode<K, V> searchIndex(int index);

  /**
   * Retrieves the value of the node with the specified key or {@code null}
   * if not found.
   * 
   * @param key the key of the desired nodes' value to retrieve
   * @return the value or {@code null} if not node not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public abstract V get(K key);

  /**
   * Retrieves the value of the node with at the specified index position.
   * 
   * @param index the index of the desired node's value to retrieve
   * @return the value or {@code null} if node not found
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public abstract V getIndex(int index);

  /**
   * Removes the node containing the specified key. It starts at the head and then
   * continues down the list looking ahead an additional node. This is so when the
   * next node is the desired node with the corresponding key, we want to simply
   * dereference the node by setting the current node {@code next} pointer to the
   * node that follows the desired node to remove.
   * 
   * @param key the key of the desired node to remove
   */
  public abstract void remove(K key);

  /**
   * Removes the node at the specified index. Will throw an exception if the index
   * is invalid.
   * 
   * @param index the position of the node to remove
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public abstract void removeIndex(int index);
  
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
   * Returns an iterable of the keys in this linkedlist. Use the {@code Iterator}
   * methods on the returned object to fetch the keys sequentially. If the
   * linkedlist is structurally modified while enumerating over the keys then the
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
   * @return an iterable of the keys in this linkedlist
   */
  public Iterable<K> keys() {
    return getIterable(KEYS);
  }

  /**
   * Returns an iterable of the values in this linkedlist. Use the {@code Iterator}
   * methods on the returned object to fetch the values sequentially. If the
   * linkedlist is structurally modified while enumerating over the values then the
   * results of enumerating are undefined.
   * 
   * @return an iterable of the values in the linkedlist
   */
  public Iterable<V> values() {
    return getIterable(VALUES);
  }

  public <E extends LinkedListNode<K, V>> Iterable<E> entries() {
    return getIterable(ENTRIES);
  }

  public Iterator<K> keysIterator() {
    return getIterator(KEYS);
  }

  public Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public <E extends LinkedListNode<K, V>> Iterator<E> entriesIterator() {
    return getIterator(ENTRIES);
  }

  public Enumeration<K> keysEnumeration() {
    return getEnumeration(KEYS);
  }

  public Enumeration<V> valuesEnumeration() {
    return getEnumeration(VALUES);
  }

  public <E extends LinkedListNode<K, V>> Enumeration<E> entriesEnumeration() {
    return getEnumeration(ENTRIES);
  }

  /**
   * A linkedlist enumerator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   * 
   * @param <T> the type of the object in the table that is being enumerated
   */
  protected abstract class AbstractEnumerator<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    protected LinkedListNode<?, ?>[] list;
    protected LinkedListNode<?, ?> entry, last;
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
    protected int expectedModCount = AbstractLinkedList.this.modCount;

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
      LinkedListNode<?, ?>[] l = list;
      LinkedListNode<?, ?> e = entry;
      int i = index;

      /* Use locals for faster loop iteration */
      while (e == null && i > 0) {
        e = l[--i];
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
      LinkedListNode<?, ?>[] l = list;
      LinkedListNode<?, ?> e = entry;
      int i = index;

      /* Use locals for faster loop iteration */
      while (e == null && i > 0) {
        e = l[--i];
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
      if (AbstractLinkedList.this.modCount != expectedModCount)
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
     * @throws ConcurrentModificationException if a function modified this map 
     *         during computation.
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

      // Synchronized block to lock the linkedlist object while removing entry
      synchronized (AbstractLinkedList.this) {
        AbstractLinkedList.this.remove((K) last.getKey());
        expectedModCount++;
        last = null;
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
   * @since 1.1
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
