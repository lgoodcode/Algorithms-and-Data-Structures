package Stacks.exceptions;

public final class StackFullException extends Exception {
  private int size;

  public StackFullException(int size) { 
    super();
    this.size = size;
  }

  public String toString() {
    return "\nStack is full. Maximum size is " + size;
  }
}