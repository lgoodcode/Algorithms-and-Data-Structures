package HashTables.exceptions;

public final class InvalidLoadFactorException extends HashTableException {
  float lf;

  public InvalidLoadFactorException(float lf) {
    super();
    this.lf = lf;
  }

  public String toString() {
    return "\nInvalid load factor given: " + lf;
  }
}
