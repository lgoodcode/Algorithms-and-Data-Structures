package data_structures.stacks;

import java.util.NoSuchElementException;

/**
 * Constructs a simple Stack with a specified capacity.
 */
public final class Stack<T> {
  /**
   * Array to holds the stack elements.
   */
  private T[] stack;

  /**
   * Indicates the number of items in the stack as well as the position of the
   * next item.
   */
  private int top = 0;

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
   * Returns the element at the top of the stack without removing it.
   * 
   * @return the element at the top of the stack or {@code null} if empty
   */
  public T peek() {
    return (T) stack[top];
  }

  /**
   * Returns the element at the bottom of the stack without removing it.
   * 
   * @return the element at the bottom of the stack or {@code null} if empty
   */
  public T peekLast() {
    return (T) stack[0];
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
    if (top == stack.length) 
      throw new IllegalStateException("Exceeded Stack capacity.");

    stack[top++] = item;
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

    return item;
  }

  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    for (int i=top-1; i>-1; i--)
      sb.append("\"" + stack[i].toString() + "\"\n");

    return sb.toString() + "}";
  }
}