package data_structures.linkedLists;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.EmptyIterator;

public class LinkedList<T> extends AbstractLinkedList<T> implements java.io.Serializable {
  public static final class Node<E> {
    protected Node<E> next;
    protected Node<E> prev;
    protected E item;

    protected Node(E item) {
      if (item == null || item.toString().isBlank())
        throw new IllegalArgumentException("Item cannot be null or empty.");
      this.item = item;
    }

    public E getItem() {
      return item;
    }

    public Node<E> next() {
      return next;
    }

    public Node<E> prev() {
      return prev;
    }
  }

  protected transient Node<T> head = null;
  protected transient Node<T> tail = null;

  @java.io.Serial
  private static final long serialVersionUID = 199208284839394801L;

  /**
   * Empty constructor
   */
  public LinkedList() {}

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
   * Returns the head of the list or {@code null} if the list is empty.
   *
   * @return the head of the list or {@code null} if none
   */
  public final Node<T> getHead() {
    return head;
  }

  /**
   * Returns the tail of the list or {@code null} if the list is empty.
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
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}.
   */
  public synchronized void insert(T item) {
    checkItem(item);

    Node<T> node = new Node<>(item);

    if (head == null)
      head = tail = node;
    else {
      link(node, head);
      head = node;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item before the
   * specified node.
   *
   * @param item the item to insert into the list
   * @param node the node to insert the item behind
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   * @throws NullPointerException     if the specified node is {@code null}
   */
  public synchronized void insertBefore(T item, Node<T> node) {
    checkItem(item);
    checkNode(node);

    Node<T> newNode = new Node<>(item);

    if (node.prev != null)
      link(node.prev, newNode, node);
    else {
      link(newNode, node);
      head = newNode;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item after the
   * specified node.
   *
   * @param item the item to insert into the list
   * @param node the node to insert the item after
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   * @throws NullPointerException     if the specified node is {@code null}
   */
  public synchronized void insertAfter(T item, Node<T> node) {
    checkItem(item);
    checkNode(node);

    Node<T> newNode = new Node<>(item);

    if (node.next != null)
      link(node, newNode, node.next);
    else {
      link(node, newNode);
      tail = newNode;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item at the end of
   * the list as the new tail.
   *
   * @param item the item to insert into the list
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   */
  public synchronized void insertLast(T item) {
    checkItem(item);

    Node<T> node = new Node<>(item);

    if (head == null)
      head = tail = node;
    else {
      link(tail, node);
      tail = node;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item at the specified
   * index position.
   *
   * @param index the index to insert the new item at
   * @param item  the item to insert into the list
   *
   * @throws IndexOutOfBoundsException if the specified index is not valid
   * @throws IllegalArgumentException  it the specified item is {@code null} or
   *                                   blank
   */
  public synchronized void insertAt(int index, T item) {
    checkPosition(index);
    checkItem(item);

    if (head == null) {
      head = tail = new Node<>(item);
      size++;
      modCount++;
    }
    else if (head == tail) {
      if (index == 0)
        insert(item);
      else
        insertLast(item);
    }
    else if (index == size)
      insertLast(item);
    else
      insertBefore(item, search(index));
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
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int indexOf(T item) {
    checkItem(item);

    Node<T> node = head;
    int index = 0;

    while (node != null && node.item != item) {
      node = node.next;
      index++;
    }

    return node != null ? index : -1;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int lastIndexOf(T item) {
    checkItem(item);

    Node<T> node = tail;
    int index = size - 1;

    while (node != null && node.item != item) {
      node = node.prev;
      index--;
    }

    return node != null ? index : -1;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public T get(int index) {
    Node<T> node = search(index);
    return node != null ? node.item : null;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public synchronized void remove(int index) {
    Node<T> node = search(index);

    if (node != null)
      remove(node);
  }

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
   * {@inheritDoc}
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
   * Saves the state of this {@code LinkedList} instance to a stream (that is,
   * serializes it).
   *
   * @param stream the {@link java.io.ObjectOutputStream} to write to
   *
   * @throws java.io.IOException if serialization fails
   *
   * @serialData The size of the list (the number of elements it contains) is
   *             emitted (int), followed by all of its elements (each an Object)
   *             in the proper order.
   */
  @java.io.Serial
  private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
    // Write out any hidden serialization magic
    stream.defaultWriteObject();

    // Write out size
    stream.writeInt(size);

    if (isEmpty())
      return;

    Node<T> node = head;

    // Write out all elements in the proper order.
    do {
      stream.writeObject(node.item);
      node = node.next;
    } while (node != null && node != head);
  }

  /**
   * Reconstitutes this {@code LinkedList} instance from a stream (that is,
   * deserializes it).
   */
  @java.io.Serial
  @SuppressWarnings("unchecked")
  private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
    // Read in any hidden serialization magic
    stream.defaultReadObject();

    // Read in size
    int size = stream.readInt();

    // Read in all elements in the proper order.
    for (int i = 0; i < size; i++)
      insertLast((T) stream.readObject());
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
   * A linkedlist iterator class. This class implements the {@link Iterator} and
   * {@link Iterable} interfaces. It simply keeps a reference to a single
   * {@link Node} to retain the current position and not have to sequentially have
   * to find the next node from the start or end again.
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
      synchronized (LinkedList.this) {
        // Remove the last returned node to from the linkedlist
        LinkedList.this.remove((Node<T>) last);
        expectedModCount++;
        last = null;
        cursor--;
      }
    }
  }

}