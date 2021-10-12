package Stacks.exceptions;

public final class StackEmptyException extends Exception { 
  public String toString() {
    return "\nStack is empty.";
  }
}