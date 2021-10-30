package data_structures.linkedLists;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.EmptyEnumerator;

/**
 * Creates a DoublyLinkedList implementation because, the simple forward only
 * implementation is inefficient. It performs, at worst case, {@code O(n)}. This
 * is because all operations are performed in a linear fashion, iterating
 * through the list until either the desired node is found or the end is
 * reached. It includes an optimized index search that starts iterating from
 * either the start or end of the list depending on whether the index is closer
 * to the head or tail, making the search perform {@code O(n / 2)}.
 */
public abstract class AbstractLinkedList<T> {
  protected LinkedListNode<T> head = null;
  protected LinkedListNode<T> tail = null;

  /**
   * The counter to track the number of entries total in the linkedlist.
   */
  protected int size = 0;

  /**
   * The number of times this LinkedList has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete).  This field
   * is used to make iterators on Collection-views of the LinkedList fail-fast.
   * 
   * @see ConcurrentModificationException
   */
  protected int modCount = 0;

  /**
   * Checks whether the specified index is within the range of [0, size-1]. This
   * is to prevent an error occuring.
   *
   * @param index the specified index to check
   *
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  protected final void checkIndex(int index) {
    if (index < 0 || index >= size)
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
  }

  /**
   * Checks whether the specified index is valid, meaning it must be greater or
   * equal to {@code 0} and less than or equal to the current size. Used in the
   * {@link #insertAt()} method, where the index can be equal to the size meaning
   * that the item is to be inserted at the end of the list.
   *
   * @param index the specified index to check
   *
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size]}
   */
  protected final void checkPosition(int index) {
    if (index < 0 || index > size)
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
  }

  /**
   * Checks the value to make sure it isn't {@code null} or blank
   *
   * @param value the value to check
   *
   * @throws IllegalArgumentException if the value is {@code null} or blank
   */
  protected final void checkItem(T item) {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");
  }

  /**
   * Checks to make sure the {@code LinkedListNode} isn't {@code null}.
   *
   * @param node the node to check
   *
   * @throws IllegalArgumentException if the {@code LinkedListNode} is
   *                                  {@code null}
   */
  protected final void checkNode(LinkedListNode<T> node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
  }

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
   * Returns the head of the list or {@code} null if the list is empty.
   *
   * @return the head of the list or {@code null} if none
   */
  public final LinkedListNode<T> getHead() {
    return head;
  }

  /**
   * Returns the tail of the list or {@code} null if the list is empty.
   *
   * @return the tail of the list or {@code null} if none
   */
  public final LinkedListNode<T> getTail() {
    return tail;
  }

  /**
   * Returns the item at the front of the list or {@code null} if the list is
   * empty.
   *
   * @return the item of the front of the lists or {@code null} if none
   */
  public final T peek() {
    return head != null ? head.getItem() : null;
  }

  /**
   * Returns the item at the tail of the list or {@code null} if the list is
   * empty.
   *
   * @return the item of the tail of the lists or {@code null} if none
   */
  public final T peekLast() {
    return tail != null ? tail.getItem() : null;
  }

  /**
   * Returns the item at the front of the list or {@code null} if the list is
   * empty. If an item is returned, the entry is removed from the list.
   *
   * @return the item of the front of the lists or {@code null} if none
   */
  public final T poll() {
    if (head == null)
      return null;
    T item = head.getItem();
    unlink(head);
    return item;
  }

  /**
   * Returns the item at the end of the list or {@code null} if the list is
   * empty. If an item is returned, the entry is removed from the list.
   *
   * @return the item of the end of the lists or {@code null} if none
   */
  public final T pollLast() {
    if (tail == null)
      return null;
    T item = tail.getItem();
    unlink(tail);
    return item;
  }

  /**
   * Internal method that links two {@code LinkedListNodes} together. The first
   * node argument is linked as the predecessor of the second node argument.
   *
   * @param pred the predecessor of the second node
   * @param node the successor of the first node
   */
  protected final void link(LinkedListNode<T> pred, LinkedListNode<T> node) {
    node.prev = pred;
    pred.next = node;
  }

  /**
   * Internal method that links three {@code LinkedListNodes} together. The first
   * node argument is linked as the predecessor of the second node argument and
   * the third is linked as the successor of the second node argument.
   *
   * @param pred the predecessor of the second node
   * @param node the node to insert between the first and third
   * @param succ the successor of the second node
   */
  protected final void link(LinkedListNode<T> pred, LinkedListNode<T> node, LinkedListNode<T> succ) {
    node.prev = pred;
    node.next = succ;
    pred.next = node;
    succ.prev = node;
  }

  /**
   * Internal method that unlinks a {@code LinkedListNode} from its predecessor
   * and successor, adjusting the pointers to the previous of the node to be
   * removed and the next node of the node to be removed.
   *
   * @param node the node to unlink
   */
  protected final void unlink(LinkedListNode<T> node) {
    if (node == head && node == tail) {
      head = tail = null;
    }
    else if (node == head) {
      head.next.prev = head.prev;
      head = node.next;
    }
    else if (node == tail) {
      tail.prev.next = tail.next;
      tail = node.prev;
    }
    else {
      node.next.prev = node.prev;
      node.prev.next = node.next;
    }
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item value.
   *
   * @param item the item to insert into the list
   *
   * @throws IllegalArgumentException if the item is {@code null} or blank.
   */
  public abstract void insert(T item);

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the node index or
   * {@code -1} if not found.
   *
   * @param item the item index to search for
   * @return the node index or {@code -1} if not found
   *
   * @throws IllegalArgumentException if the item is {@code null} or blank
   */
  public abstract int indexOf(T item);

  /**
   * Iterates through the list, starting from the tail, until the desired node
   * with the corresponding specified key is found or the front is reached,
   * returning the node index or {@code -1} if not found.
   *
   * @param item the item index to search for
   * @return the node index or {@code -1} if not found
   *
   * @throws IllegalArgumentException if the item is {@code null} or blank
   */
  public abstract int lastIndexOf(T item);

  /**
   * Determines whether the item exists in the list or not.
   *
   * @param item the item to search the list for
   * @return whether the item exists in the list or not
   *
   * @throws IllegalArgumentException if the item is {@code null} or blank
   */
  public final boolean contains(T item) {
    checkItem(item);
    return indexOf(item) != -1;
  }

  /**
   * Searches and returns the node at the specified index or {@code null} if the
   * specified index is invalid or not within the range.
   *
   * @param index the index of the specified node to retrieve
   * @return the {@code LinkedListNode} or {@code null} if not found
   *
   * @throws IndexOutOfBoundsException if the specified index is invalid
   */
  public abstract LinkedListNode<T> search(int index);

  /**
   * Retrieves the item of the node with the specified index or {@code null} if
   * not found.
   *
   * @param index the index of the item to retrieve
   * @return the item or {@code null} if a node doesn't exist at the specified
   *         index
   *
   * @throws IndexOutOfBoundsException if the specified index is invalid
   */
  public abstract T get(int index);

  /**
   * Removes the node at the specified index.
   *
   * @param index the position of the node to remove
   *
   * @throws IndexOutOfBoundsException if the specified index is invalid
   */
  public abstract void remove(int index);

  /**
   * Removes the specified {@code LinkedListNode} from the list by adjusting
   * the pointers of the surrounding nodes of the specified node.
   *
   * @param index the node to remove
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public abstract void remove(LinkedListNode<T> node);

  /**
   * Displays the contents of the list in order in a JSON format. Overrides due to
   * the node implemented being different.
   *
   * @return the string format of the object
   */
  public final String toString() {
    if (head == null)
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");
    Iterable<T> values = (Iterable<T>) values();
    int i = 0;

    for (T val : values)
      sb.append("\"" + i++ + " -> " + val.toString() + "\",\n");
    return sb.toString() + "}";
  }

  /**
   * Returns an {@link Iterable} of the specified type.
   *
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  protected final Iterable<T> getIterable() {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(true);
  }
  
  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  protected final Iterator<T> getIterator() {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(true);
  }

  /**
   * Returns an {@link Enumeration} of the specified type.
   *
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Enumeration}
   */
  protected final Enumeration<T> getEnumeration() {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(false);
  }

  /**
   * Returns an iterable of the values in this linkedlist. Use the {@code Iterator}
   * methods on the returned object to fetch the values sequentially. If the
   * linkedlist is structurally modified while enumerating over the values then the
   * results of enumerating are undefined.
   *
   * @return an iterable of the values in the linkedlist
   */
  public final Iterable<T> values() {
    return getIterable();
  }

  public final Iterator<T> valuesIterator() {
    return getIterator();
  }

  public final Enumeration<T> valuesEnumeration() {
    return getEnumeration();
  }

  /**
   * A linkedlist enumerator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   *
   * @param <T> the type of the object that is being enumerated
   */
  protected final class Enumerator<E> implements Enumeration<E>, Iterator<E>, Iterable<E> {
    protected LinkedListNode<?>[] list;
    protected LinkedListNode<?> entry, last;
    protected int size, index = 0;

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

    /**
     * Constructs the enumerator that will be used to enumerate the values in the
     * list.
     *
     * @param iterator whether this will serve as an {@code Enumeration} or
     *                 {@code Iterator}
     */
    protected Enumerator(boolean iterator) {
      this.size = AbstractLinkedList.this.size;
      this.iterator = iterator;
      list = new LinkedListNode<?>[size];
      int i = 0;

      LinkedListNode<T> node = getHead();

      do {
        list[i++] = node;
        node = node.next;
      } while (node != null && node != head);
    }

    // Iterable method
    public Iterator<E> iterator() {
      return iterator ? this : this.asIterator();
    }

    /**
     * Checks whether there are more elments to return.
     *
     * @return if this object has one or more items to provide or not
     */
    public boolean hasMoreElements() {
      if (index >= size)
        return false;
      return list[index] != null;
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     *
     * @throws NoSuchElementException if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public E nextElement() {
      if (index >= size)
        throw new NoSuchElementException("LinkedList Enumerator");
      last = list[index];
      return (E) list[index++].getItem();
    }

    /**
     * The Iterator method; the same as Enumeration.
     */
    public boolean hasNext() {
      return hasMoreElements();
    }

    /**
     * Iterator method. Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     */
    public E next() {
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
    public void remove() {
      if (!iterator)
        throw new UnsupportedOperationException();
      if (last == null)
        throw new IllegalStateException("LinkedList Enumerator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the linkedlist object while removing entry
      synchronized (AbstractLinkedList.this) {
        // Pass the current index to remove the last item
        AbstractLinkedList.this.remove((LinkedListNode<T>) last);
        expectedModCount++;
        last = null;
      }
    }
  }

}
