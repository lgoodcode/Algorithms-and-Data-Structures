package Stacks;

interface IGenStack<T> {
  public abstract void push(T x) throws StackFullException;
  public abstract T pop() throws StackEmptyException;
}

class StackFullException extends Exception {
  int size;

  StackFullException(int s) { size = s; }

  public String toString() {
    return "\nStack is full. Maximum size is " + size;
  }
}

class StackEmptyException extends Exception { 
  public String toString() {
    return "\nStack is empty.";
  }
}

class GenericStack<T> implements IGenStack<T> {
  private T[] stack;
  private int top;

  // Construct an empty stack
  GenericStack(T[] arrRef) {
    this.stack = arrRef;
    this.top = 0;
  }

  // Construct a stack from another stack
  GenericStack(T[] stackArray, GenericStack<T> obj) {
    this.stack = stackArray;
    this.top = obj.top;

    // Verify the new stack can hold all the elements
    try {
      if (this.stack.length < obj.stack.length) {
        throw new StackFullException(this.stack.length);
      }
    } catch (StackFullException err) {
      System.out.println(err);
    }

    // Copy elements
    for (int i=0; i<top; ++i)
      this.stack[i] = obj.stack[i];
  }

  // Construct a stack from initial values
  GenericStack(T[] stackArray, T[] a) {
    this.stack = stackArray;

    for (int i=0; i<a.length; ++i) {
      try {
        this.push(a[i]);
      } catch (StackFullException err) {
        System.out.println(err);
      }
    }
  }

  public void push(T x) throws StackFullException {
    if (this.top == this.stack.length) 
      throw new StackFullException(this.stack.length);

    this.stack[this.top++] = x;
  }

  public T pop() throws StackEmptyException {
    if (this.stack[this.top-1] == null) 
      throw new StackEmptyException();

    return this.stack[--this.top];
  }
}