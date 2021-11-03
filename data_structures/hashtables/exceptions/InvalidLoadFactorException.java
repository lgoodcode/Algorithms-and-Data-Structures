package data_structures.hashtables.exceptions;

public final class InvalidLoadFactorException extends Exception {
  float lf;

  public InvalidLoadFactorException(float lf) {
    super();
    this.lf = lf;
  }

  public String toString() {
    return "\nInvalid load factor given: " + lf;
  }
}
