package data_structures.hashtables;

public final class HashtableFunctions {
  /**
   * Given a valid {@code Number} type, determines if it is a prime number.
   * 
   * @param <T> {@code Number}
   * @param p   number to check if prime or not
   * @return is number prime
   */
  public static <T extends Number> boolean isPrime(T p) {
    int x = p.intValue();

    if (x * 0 != 0)
      return false;

    if (x <= 3)
      return x > 1;

    if (x % 2 == 0 || x % 3 == 0)
      return false;

    for (int i = 5; i * i <= x; i += 6) {
      if (x % i == 0 || x % (i + 2) == 0)
        return false;
    }
    return true;
  }

  /**
   * Recursive function that creates values that will be injective for the given
   * set, table size, and prime number.
   * 
   * @param <T> {@code Number}
   * @param keys   set or list of elements to create the hash function for
   * @param m   maximal size of the subtable
   * @param p   prime number of the hash table T
   * @throws InvalidPrimeException when given an invalid prime number
   * @return {@code int[a, b]} constants for table hash function
   */
  public static <T> int[] injectiveIntegers(int[] keys, int num, int m, int p) {
    // Sanity checks on prime number and subtable size
    if (isPrime(p) == false)
      throw new IllegalArgumentException("Invalid prime number given: " + p);
    else if (m < 1)
      throw new IllegalArgumentException("Invalid table size given: " + p);

    int[] used = new int[m];
    int a = (int) (Math.random() * p - 2) + 1;
    int b = (int) (Math.random() * p - 1);
    int used_idx = 0;
    int hash;

    // Iterate through every value in the set
    for (int i=0; i<num; i++) {
      hash = ((a * keys[i] + b) % p % m);

      // Determine whether the hash has already been used
      for (int j=0; j < used_idx; j++) {
        // If used, the constants doesn't give us an injective hash function; try again
        if (hash == used[j])
          return HashtableFunctions.injectiveIntegers(keys, num, m, p);
      }

      // Add hash to used hashes array
      used[used_idx++] = hash;
    }

    int[] valid = { a, b };

    return valid;
  }

  public static <T> int[] injectiveIntegers(int[] keys, int m, int p) {
    return injectiveIntegers(keys, keys.length, m, p);
  }
}