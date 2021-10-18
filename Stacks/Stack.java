package Stacks;

import Stacks.exceptions.*;

public class Stack<T> {
  private Entry<?>[] stack;
  private int top = 0;

  public Stack(int size) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size, must be greater than 1.");
    stack = new Entry<?>[size];
  }

  // Construct a stack from initial values
  public Stack(T[] arr) {
    if (arr.length == 0)
      throw new IllegalArgumentException("Initial array of values cannot be empty.");

    stack = new Entry<?>[arr.length];

    for (int i=0; i<arr.length; ++i) {
      try {
        push(arr[i]);
      } catch (StackFullException e) {}
    }
  }

  public Stack(Stack<T> s) {
    stack = s.stack;
    top = s.top;
  }

  public int size() {
    return stack.length;
  }

  public boolean isEmpty() {
    return top == 0;
  }

  public synchronized void push(T item) throws StackFullException {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or blank value.");
    if (top == stack.length) 
      throw new StackFullException(stack.length);

    stack[top++] = new Entry<T>(item);
  }

  @SuppressWarnings("unchecked")
  public synchronized T pop() throws StackEmptyException {
    if (isEmpty()) 
      throw new StackEmptyException();
    
    Entry<T> e = (Entry<T>) stack[--top];
    stack[top] = null;

    return e.getValue();
  }

  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    for (int i=top-1; i>-1; i--)
      sb.append("\"" + stack[i].toString() + "\"\n");

    return sb.toString() + "}";
  }

  /**
   * A nested inner class that will be used to hold values for the {@code Queue}.
   */
  static class Entry<T> {
    private T value;
  
    public Entry(T value) {
      this.value = value;
    }
  
    public T getValue() {
      return value;
    }
  
    public String toString() {
      return value.toString();
    }
  }
}