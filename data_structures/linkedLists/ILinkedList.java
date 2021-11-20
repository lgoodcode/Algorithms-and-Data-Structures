package data_structures.linkedLists;

public interface ILinkedList<T> {
  /**
   * Determines whether the list is empty or not
   *
   * @return boolean indicating if the list is empty
   */
  boolean isEmpty();

  /**
   * Returns the number of elements in the list
   *
   * @return the number of elements in the list
   */
  int size();

  /**
   * Removes all elements in the linkedlist. Ensures the references are removed so
   * it can be garbage collected.
   */
  void clear();

  /**
   * Returns the item at the front of the list or {@code null} if the list is
   * empty.
   *
   * @return the item of the front of the lists or {@code null} if none
   */
  T peek();

  /**
   * Returns the item at the tail of the list or {@code null} if the list is
   * empty.
   *
   * @return the item of the tail of the lists or {@code null} if none
   */
  T peekLast();

  /**
   * Returns the item at the front of the list or {@code null} if the list is
   * empty. If an item is returned, the entry is removed from the list.
   *
   * @return the item of the front of the lists or {@code null} if none
   */
  T poll();

  /**
   * Returns the item at the end of the list or {@code null} if the list is
   * empty. If an item is returned, the entry is removed from the list.
   *
   * @return the item of the end of the lists or {@code null} if none
   */
  T pollLast();

  /**
   * Inserts a new {@code Node} with the specified item value.
   *
   * @param item the item to insert into the list
   *
   * @throws IllegalArgumentException if the item is {@code null} or blank.
   */
  void insert(T item);

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
  int indexOf(T item);

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
  int lastIndexOf(T item);

  /**
   * Determines whether the item exists in the list or not.
   *
   * @param item the item to search the list for
   * @return whether the item exists in the list or not
   *
   * @throws IllegalArgumentException if the item is {@code null} or blank
   */
  boolean contains(T item);

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
  T get(int index);

  /**
   * Removes the node at the specified index.
   *
   * @param index the position of the node to remove
   *
   * @throws IndexOutOfBoundsException if the specified index is invalid
   */
  void remove(int index);

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
  void removeRange(int fromIndex, int toIndex);

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
  Object[] toArray();

  /**
   * Displays the contents of the list in order in an array format.
   *
   * @return the string format of the linkedlist
   */
  String toString();
}
