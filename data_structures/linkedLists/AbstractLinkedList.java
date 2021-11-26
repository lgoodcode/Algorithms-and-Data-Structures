package data_structures.linkedLists;

import java.util.Iterator;

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
  public abstract void clear();

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
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element).
   *
   * <p>
   * The returned array will be "safe" in that no references to it are maintained
   * by this list. (In other words, this method must allocate a new array). The
   * caller is thus free to modify the returned array.
   * </p>
   *
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   * </p>
   *
   * @return an array containing all of the elements in this list in proper
   *         sequence
   */
  public abstract Object[] toArray();

  /**
   * Returns an {@link Iterable} of the elements in the linkedlist
   *
   * @return the {@code Iterable}
   */
  public abstract Iterable<T> iterable();

  /**
   * Returns an {@link Iterator} of the elements in the linkedlist
   *
   * @return the {@code Iterator}
   */
  public abstract Iterator<T> iterator();

}
