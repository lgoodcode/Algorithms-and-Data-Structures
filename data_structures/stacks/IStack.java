package data_structures.stacks;

public interface IStack<T> {
  /**
   * Returns the number of elements in the stack
   * 
   * @return the number of elements in the stack
   */
  public int size();

  /**
   * The maximum number of elements that can be stored in the stack
   * 
   * @return the maximum number of elements that can be stored
   */
  public int capacity();

  /**
   * Determines whether the stack contains any elements or not
   * 
   * @return whether the stack is empty or not
   */
  public boolean isEmpty();

  /**
   * Determines whether the stack is currently full.
   * 
   * @return whether the stack is full
   */
  public boolean isFull();

  /**
   * Clears all elements from the stack.
   */
  public void clear();

  /**
   * Returns the element at the top of the stack without removing it.
   * 
   * @return the element at the top of the stack or {@code null} if empty
   */
  public T peek();

  /**
   * Returns the element at the bottom of the stack without removing it.
   * 
   * @return the element at the bottom of the stack or {@code null} if empty
   */
  public T peekLast();

  /**
   * Checks whether the specified element exists in the stack or not.
   * 
   * @param item the item to check
   * @return whether the element exists in the stack or not
   * 
   * @throws IllegalArgumentException if the specified item is {@code null} or blank
   */
  public boolean has(T item);

  /**
   * Adds the specified item to the top of the stack.
   * 
   * @param item the item to add
   * 
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   * @throws IllegalStateException    if the stack is currently full
   */
  void push(T item);

  /**
   * Removes and returns the item at the top of the stack.
   * 
   * @return the item at the top of the stack
   * 
   * @throws NoSuchElementException if the stack is empty
   */
  T pop();

  /**
   * Returns an array containing all of the elements in this stack
   * in proper sequence (from first to last element).
   *
   * <p>The returned array will be "safe" in that no references to it are
   * maintained by this stack.  (In other words, this method must allocate
   * a new array).  The caller is thus free to modify the returned array.
   *
   * <p>This method acts as bridge between array-based and collection-based
   * APIs.
   *
   * @return an array containing all of the elements in this stack
   *         in proper sequence
   */
  Object[] toArray();

  String toString();
}
