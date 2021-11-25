package data_structures.stacks;

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import data_structures.EmptyIterator;

/**
 * Constructs a simple Stack with a specified capacity.
 */
public final class Stack<T> implements java.io.Serializable {
  @java.io.Serial
  private static final long serialVersionUID = 199208284839394810L;

  /**
   * Array to hold the elements of the stack.
   */
  private transient T[] stack;

  /**
   * Indicates the number of items in the stack as well as the position of the
   * next item.
   */
  private transient int top = 0;

  /**
   * The number of times this Stack has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the Stack fail-fast.
   *
   * @see ConcurrentModificationException
   */
  private transient int modCount = 0;

  /**
   * Constructs an empty Stack with the specified capacity.
   * 
   * @param size the maximum capacity of the stack
   * 
   * @throws IllegalArgumentException if the specified size is less than {@code 1}
   */
  @SuppressWarnings("unchecked")
  public Stack(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size, must be greater than 1.");
    stack = (T[]) new Object[size];
  }

  /**
   * Constructs a Stack with an initial array of values and will have the capacity
   * that is specified.
   * 
   * @param array the array containing the values to initialize the stack with
   * @param size  the specified maximum stack capacity
   * 
   * @throws NullPointerException     if the array is {@code null}
   * @throws IllegalArgumentException if the specified array is empty or the
   *                                  specified capacity is less than {@code 1}
   */
  @SuppressWarnings("unchecked")
  public Stack(T[] array, int size) {
    if (array == null)
      throw new NullPointerException("Array cannot be null.");
    if (array.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");
    if (size < 1)
      throw new IllegalArgumentException("Illegal size, must be greater than 1.");

    stack = (T[]) new Object[array.length];

    for (int i = 0; i < array.length; ++i)
      push(array[i]);
  }

  /**
   * Constructs a Stack with an initial array of values and will have the capacity
   * of the array length.
   * 
   * @param array the array containing the values to initialize the stack with
   * 
   * @throws NullPointerException     if the array is {@code null}
   * @throws IllegalArgumentException if the specified array is empty
   */
  public Stack(T[] array) {
    this(array, array.length);
  }

  /**
   * Constructs a Stack from another Stack instance.
   * 
   * @param stack the stack to make a copy of
   * 
   * @throws NullPointerException if the specified {@code Stack} is {@code null}
   */
  public Stack(Stack<T> stack) {
    if (stack == null)
      throw new NullPointerException("Stack cannot be null.");
    this.stack = stack.stack;
    top = stack.top;
  }

  /**
   * Returns the number of elements in the stack
   * 
   * @return the number of elements in the stack
   */
  public int size() {
    return top;
  }

  /**
   * The maximum number of elements that can be stored in the stack
   * 
   * @return the maximum number of elements that can be stored
   */
  public int capacity() {
    return stack.length;
  }

  /**
   * Determines whether the stack contains any elements or not
   * 
   * @return whether the stack is empty or not
   */
  public boolean isEmpty() {
    return top == 0;
  }

  /**
   * Determines whether the stack is currently full.
   * 
   * @return whether the stack is full
   */
  public boolean isFull() {
    return top == stack.length;
  }

  /**
   * Clears all elements from the stack.
   */
  public void clear() {
    for (int i = 0; i < top; i++)
      stack[i] = null;
    top = 0;
    modCount++;
  }

  /**
   * Returns the element at the top of the stack without removing it.
   * 
   * @return the element at the top of the stack or {@code null} if empty
   */
  public T peek() {
    return stack[top];
  }

  /**
   * Returns the element at the bottom of the stack without removing it.
   * 
   * @return the element at the bottom of the stack or {@code null} if empty
   */
  public T peekLast() {
    return stack[0];
  }

  /**
   * Checks whether the specified element exists in the stack or not.
   * 
   * @param item the item to check
   * @return whether the element exists in the stack or not
   * 
   * @throws IllegalArgumentException if the specified item is {@code null} or blank
   */
  public boolean has(T item) {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or blank.");

    for (int i = 0; i < top; i++)
      if (stack[i] == item)
        return true;
    return false;
  }

  /**
   * Adds the specified item to the top of the stack.
   * 
   * @param item the item to add
   * 
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   * @throws IllegalStateException    if the stack is currently full
   */
  public synchronized void push(T item) {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or blank value.");
    if (isFull()) 
      throw new IllegalStateException("Exceeded Stack capacity.");

    stack[top++] = item;
    modCount++;
  }

  /**
   * Removes and returns the item at the top of the stack.
   * 
   * @return the item at the top of the stack
   * 
   * @throws NoSuchElementException if the stack is empty
   */
  public synchronized T pop() {
    if (isEmpty()) 
      throw new NoSuchElementException("Stack is empty.");
    
    T item = stack[--top];
    stack[top] = null;
    modCount++;

    return item;
  }

  /**
   * Removes the element at the specified index of the stack.
   * 
   * @param index the position of the element to remove
   * 
   * @throws IndexOutOfBoundsException if the index is less than {@code 0} equal
   *                                   to or greater than the stack size
   * @throws NoSuchElementException    if there is no element to remove at the
   *                                   specified index
   */
  public synchronized void removeAt(int index) {
    if (index < 0)
      throw new IndexOutOfBoundsException("Index cannot be less than 0.");
    if (index >= stack.length)
      throw new IndexOutOfBoundsException("Index cannot be greater than stack size.");
    if (stack[index] == null)
      throw new NoSuchElementException("No element at this index to remove.");

    for (int i = index, len = --top; i < len; i++)
      stack[i] = stack[i+1];
    stack[top] = null;

    modCount++;
  }

  /**
   * Returns an array containing all of the elements in this stack in proper
   * sequence (from first to last element).
   *
   * <p>
   * The returned array will be "safe" in that no references to it are maintained
   * by this stack. (In other words, this method must allocate a new array). The
   * caller is thus free to modify the returned array.
   * </p>
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   * </p>
   *
   * @return an array containing all of the elements in this stack in proper
   *         sequence
   */
  public Object[] toArray() {
    Object[] arr = new Object[size()];

    for (int i = top - 1, j = 0; i >= 0; i--, j++)
      arr[j] = stack[i];
    return arr;
  }

  public String toString() {
    if (isEmpty())
      return "[]";

    StringBuilder sb = new StringBuilder("[");

    for (int i = top - 1; i > 0; i--)
      sb.append(stack[i].toString() + ", ");
    sb.append(stack[0].toString());

    return sb.toString() + "]";
  }

  /**
   * Saves the state of this {@code Stack} instance to a stream (that is,
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
    stream.writeInt(top);

    // Write out all elements in the proper order.
    for (int i = top - 1; i >= 0; i--)
      stream.writeObject(stack[i]);
  }

  /**
   * Reconstitutes this {@code Stack} instance from a stream (that is,
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
      push((T) stream.readObject());
  }

  /**
   * Returns an {@link Iterable} of the elements in the stack
   *
   * @return the {@code Iterable}
   */
  public final Iterable<T> iterable() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * Returns an {@link Iterator} of the elements in the stack
   *
   * @return the {@code Iterator}
   */
  public final Iterator<T> iterator() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * A stack iterator class. This class implements the interfaces. It simply
   * keeps a reference to a single {@link Node} to retain the current position and
   * not have to sequentially have to find the next node from the start or end
   * again.
   *
   * <p>
   * Doesn't need to be generic or static because it will be accessing the
   * stack directly, not creating a list of entries
   * </p>
   *
   * <p>
   * Will throw a {@link ConcurrentModificationException} if the stack was
   * modified outside of the iterator.
   * </p>
   */
  private class Itr implements Iterator<T>, Iterable<T> {
    /**
     * Tracks the current node index position.
     */
    int cursor = top - 1;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = modCount;

    /**
     * Tracks whether the iterator has processed an element for there to be a
     * previous element and if that element hasn't been removed.
     */
    boolean last = false;

    public Iterator<T> iterator() {
      return this;
    }

    public boolean hasNext() {
      return cursor != -1 && stack[cursor] != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    public T next() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("Stack Iterator");
      last = true;
      return stack[cursor--];
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
      if (!last)
        throw new IllegalStateException("Stack Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the stack object while removing entry
      synchronized (Stack.this) {
        Stack.this.removeAt(cursor + 1);
        expectedModCount++;
        last = false;
      }
    }
    
  }
}