package data_structures.stacks.exceptions;

public final class StackEmptyException extends Exception { 
  public String toString() {
    return "\nStack is empty.";
  }
}