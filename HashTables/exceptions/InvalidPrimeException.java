package HashTables.exceptions;

public final class InvalidPrimeException extends HashTableException {
  int num;

  public <T extends Number>InvalidPrimeException(T p) { 
    super();
    num = p.intValue(); 
  }

  public String toString() {
    return "\nNumber: " + num + " is not a valid prime number";
  }
}