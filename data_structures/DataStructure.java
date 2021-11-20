package data_structures;

public interface DataStructure<T> {
  /**
   * Returns the number of elements in the collection
   *
   * @return the number of elements in the collection
   */
  public int size();

  /**
   * Determines whether the collection contains any elements or not
   *
   * @return whether the collection is empty or not
   */
  public boolean isEmpty();

  /**
   * Returns {@code true} if this collection contains the specified element.
   * More formally, returns {@code true} if and only if this collection
   * contains at least one element {@code e} such that
   * {@code Objects.equals(o, e)}.
   *
   * @param item element whose presence in this collection is to be tested
   * @return {@code true} if this collection contains the specified
   *         element
   * @throws ClassCastException if the type of the specified element
   *         is incompatible with this collection
   * @throws NullPointerException if the specified element is null and this
   *         collection does not permit null elements
   */
  boolean contains(T item);

  /**
   * Clears all elements from the collection.
   */
  public void clear();

  /**
   * Returns an array containing all of the elements in this collection in proper
   * sequence (from first to last element).
   *
   * <p>
   * The returned array will be "safe" in that no references to it are maintained
   * by this collection. (In other words, this method must allocate a new array). The
   * caller is thus free to modify the returned array.
   *
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   *
   * @return an array containing all of the elements in this collection in proper
   *         sequence
   */
  Object[] toArray();

  String toString();
}
