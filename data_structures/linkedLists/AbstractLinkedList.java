package data_structures.linkedLists;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.EmptyIterator;

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
  protected class Node<E> {
    Node<E> next;
    Node<E> prev;
    E item;

    Node(E item) {
      if (item == null || item.toString().isBlank())
        throw new IllegalArgumentException("Item cannot be null or empty.");
      this.item = item;
    }

    public E getItem() {
      return item;
    }
  }

  protected transient Node<T> head = null;
  protected transient Node<T> tail = null;

  /**
   * The counter to track the number of entries total in the linkedlist.
   */
  protected transient int size = 0;

  /**
   * The number of times this LinkedList has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete).  This field
   * is used to make iterators on Collection-views of the LinkedList fail-fast.
   *
   * @see ConcurrentModificationException
   */
  protected transient int modCount = 0;

  /**
   * Sole constructor. (For invocation by subclass constructors, typically
   * implicit.)
   */
  protected AbstractLinkedList() {}

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
   * Checks to make sure the {@code Node} isn't {@code null}.
   *
   * @param node the node to check
   *
   * @throws IllegalArgumentException if the {@code Node} is
   *                                  {@code null}
   */
  protected final void checkNode(Node<T> node) {
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
   * Removes all elements in the linkedlist. Ensures the references are removed so
   * it can be garbage collected.
   */
  public synchronized final void clear() {
    if (isEmpty())
      return;

    Node<T> temp, node = head;

    do {
      temp = node.next;
      node.item = null;
      node.next = null;
      node.prev = null;
      node = temp;
    } while (node != null && node != head);

    head = tail = null;
    size = 0;
    modCount++;
  }

  /**
   * Returns the head of the list or {@code} null if the list is empty.
   *
   * @return the head of the list or {@code null} if none
   */
  public final Node<T> getHead() {
    return head;
  }

  /**
   * Returns the tail of the list or {@code} null if the list is empty.
   *
   * @return the tail of the list or {@code null} if none
   */
  public final Node<T> getTail() {
    return tail;
  }

  /**
   * Returns the item at the front of the list or {@code null} if the list is
   * empty.
   *
   * @return the item of the front of the lists or {@code null} if none
   */
  public final T peek() {
    return head != null ? head.item : null;
  }

  /**
   * Returns the item at the tail of the list or {@code null} if the list is
   * empty.
   *
   * @return the item of the tail of the lists or {@code null} if none
   */
  public final T peekLast() {
    return tail != null ? tail.item : null;
  }

  /**
   * Returns the item at the front of the list or {@code null} if the list is
   * empty. If an item is returned, the entry is removed from the list.
   *
   * @return the item of the front of the lists or {@code null} if none
   */
  public synchronized final T poll() {
    if (head == null)
      return null;
    T item = head.item;
    unlink(head);
    return item;
  }

  /**
   * Returns the item at the end of the list or {@code null} if the list is
   * empty. If an item is returned, the entry is removed from the list.
   *
   * @return the item of the end of the lists or {@code null} if none
   */
  public synchronized final T pollLast() {
    if (tail == null)
      return null;
    T item = tail.item;
    unlink(tail);
    return item;
  }

  /**
   * Internal method that links two {@code Nodes} together. The first
   * node argument is linked as the predecessor of the second node argument.
   *
   * @param pred the predecessor of the second node
   * @param node the successor of the first node
   */
  protected final void link(Node<T> pred, Node<T> node) {
    node.prev = pred;
    pred.next = node;
  }

  /**
   * Internal method that links three {@code Nodes} together. The first
   * node argument is linked as the predecessor of the second node argument and
   * the third is linked as the successor of the second node argument.
   *
   * @param pred the predecessor of the second node
   * @param node the node to insert between the first and third
   * @param succ the successor of the second node
   */
  protected final void link(Node<T> pred, Node<T> node, Node<T> succ) {
    node.prev = pred;
    node.next = succ;
    pred.next = node;
    succ.prev = node;
  }

  /**
   * Internal method that unlinks a {@code Node} from its predecessor
   * and successor, adjusting the pointers to the previous of the node to be
   * removed and the next node of the node to be removed.
   *
   * @param node the node to unlink
   */
  protected final void unlink(Node<T> node) {
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
   * Inserts a new {@code Node} with the specified item value.
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
   * @return the {@code Node} or {@code null} if not found
   *
   * @throws IndexOutOfBoundsException if the specified index is invalid
   */
  public final Node<T> search(int index) {
    checkIndex(index);
    return _search(index);
  }

  private Node<T> _search(int index) {
    Node<T> node = null;

    if (index < (size >> 1)) {
      node = head;

      for (int i=0; i<index; i++)
        node = node.next;
      return node;
    }
    else {
      node = tail;

      for (int i=size-1; i>index; i--)
        node = node.prev;
      return node;
    }
  }

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
   * Remove a node by unlinking it and then decrementing the size counter.
   *
   * @param node the node to remove
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public synchronized final void remove(Node<T> node) {
    checkNode(node);
    unlink(node);
    size--;
    modCount++;
  }

  /**
   * Removes nodes in the specified indices range.
   *
   * @param fromIndex the node at the specified index to start
   * @param toIndex   the node at the specified index to end
   *
   * @throws IndexOutOfBoundsException if the index is not within the range of
   *                                   {@code [0, size-1]}
   * @throws IllegalArgumentException  if the specified start index is greater
   *                                   than the specified end index
   */
  public synchronized final void removeRange(int fromIndex, int toIndex) {
    checkIndex(fromIndex);
    checkPosition(toIndex);

    if (fromIndex > toIndex)
      throw new IllegalArgumentException("Start index cannot be greater than end index.");

    if (fromIndex == toIndex) {
      remove(fromIndex);
      return;
    }

    Node<T> node = _search(fromIndex); // use main method to skip redundant check
    int i = fromIndex, j = toIndex;

    while (i < j) {
      remove(node);
      node = node.next;
      i++;
    }

  }

  /**
   * Returns an array containing all of the elements in this list
   * in proper sequence (from first to last element).
   *
   * <p>The returned array will be "safe" in that no references to it are
   * maintained by this list.  (In other words, this method must allocate
   * a new array).  The caller is thus free to modify the returned array.
   *
   * <p>This method acts as bridge between array-based and collection-based
   * APIs.
   *
   * @return an array containing all of the elements in this list
   *         in proper sequence
   */
  public final Object[] toArray() {
    Object[] arr = new Object[size];
    Node<T> node = head;
    int i = 0;

    while (node != null && i != size) {
      arr[i++] = node.item;
      node = node.next;
    }

    return arr;
  }

  /**
   * Displays the contents of the list in order in an array format.
   *
   * @return the string format of the linkedlist
   */
  public final String toString() {
    if (head == null)
      return "[]";

    StringBuilder sb = new StringBuilder("[");
    Node<T> node = head;
    int i = 0, len = size - 1;

    while (node != null && i != len) {
      sb.append(node.item.toString() + ", ");
      node = node.next;
      i++;
    }

    sb.append(node.item.toString() + "]");

    return sb.toString();
  }

  /**
   * Returns an {@link Iterable} of the elements in the linkedlist
   *
   * @return the {@code Iterable}
   */
  public final Iterable<T> iterable() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * Returns an {@link Iterator} of the elements in the linkedlist
   *
   * @return the {@code Iterator}
   */
  public final Iterator<T> iterator() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * A linkedlist iterator class. This class implements the interfaces. It simply
   * keeps a reference to a single {@link Node} to retain the current position and
   * not have to sequentially have to find the next node from the start or end
   * again.
   *
   * <p>
   * Doesn't need to be generic or static because it will be accessing the
   * linkedlist directly, not creating a list of entries
   * </p>
   *
   * <p>
   * Will throw a {@link ConcurrentModificationException} if the linkedlist was
   * modified outside of the iterator.
   * </p>
   */
  private class Itr implements Iterator<T>, Iterable<T> {
    /**
     * The current {@link Node} of the iterator.
     */
    Node<T> entry = head;

    /**
     * The last returned {@code LinkedListNode} so it can be quickly passed to the
     * {@code unlink} method to remove in {@code O(1)} from the {@code LinkedList}.
     */
    Node<T> last;

    /**
     * Tracks the current node index position.
     */
    int cursor = 0;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = modCount;

    public Iterator<T> iterator() {
      return this;
    }

    public boolean hasNext() {
      return entry != null && cursor != size;
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    public T next() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("LinkedList Iterator");
      cursor++;
      T item = entry.item;
      last = entry;
      entry = entry.next;
      return item;
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
     * @throws ConcurrentModificationException if a function modified this map
     *         during computation.
     */
    @Override
    public void remove() {
      if (last == null)
        throw new IllegalStateException("LinkedList Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the linkedlist object while removing entry
      synchronized (AbstractLinkedList.this) {
        // Remove the last returned node to from the linkedlist
        AbstractLinkedList.this.remove((Node<T>) last);
        expectedModCount = modCount;
        last = null;
        cursor--;
      }
    }
  }

}
