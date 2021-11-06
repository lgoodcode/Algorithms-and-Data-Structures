package algorithms.dynamic;

public interface CutRod {
  /**
   * Brute-force method. Checks every possible combination, resulting in an cost
   * time of {@code O(2^n)}.
   *
   * @param prices the array of prices where the length is the index
   * @param length the length to find the optimal price of
   * @return the optimal price
   */
  public static int brute(int[] prices, int length) {
    if (length == 0)
      return 0;

    int q = Integer.MIN_VALUE;
    // i = 1 and i <= n  so that n-i can reach 0 for base case
    for (int i = 1; i <= length; i++)
      q = Math.max(q, prices[i-1] + brute(prices, length - i));
    return q;
  }

  /**
   * Bottom up approach with a cost of {@code O(n^2)}. Calculates the base values
   * and builds up from there. Iterates through each item twice to get the optimal
   * value.
   *
   * @param prices the array of prices where the length is the index
   * @param length the length to find the optimal price of
   * @return the optimal price
   */
  public static int bottomUp(int[] prices, int length) {
    int len = prices.length;
    int[] r = new int[len + 1];
    int i, j, q;

    for (j = 0; j < len; j++) {
      q = Integer.MIN_VALUE;

      for (i = 0; i <= j; i++)
        q = Math.max(q, prices[i] + r[j-i]);
      // j+1 lets us keep r[0] = 0 when n == 0
      r[j+1] = q;
    }

    return r[length];
  }

  /**
   * Bottom up approach with a cost of {@code O(n^2)}. Calculates the array of
   * optimal solutions for all indices of prices and their lengths.
   *
   * @param prices the array of prices where the length is the index
   * @return the optimal price array
   */
  public static int[] bottomUpTable(int[] prices) {
    int len = prices.length;
    int[] r = new int[len + 1];
    int[] s = new int[len + 1];
    int i, j, q;

    for (j = 0; j < len; j++) {
      q = Integer.MIN_VALUE;

      for (i = 0; i <= j; i++) {
        if (q < prices[i] + r[j-i]) {
          q = Math.max(q, prices[i] + r[j-i]);
          s[j] = i + 1;
        }
      }

      r[j+1] = q;
    }

    return r;
  }

  /**
   * Bottom up approach with recursion with a cost of {@code O(n^2)}. Calculates
   * the base values and builds up from there. Iterates through each item twice to
   * get the optimal value.
   *
   * @param prices the array of prices where the length is the index
   * @param length the length to find the optimal price of
   * @return the optimal price
   */
  public static int bottomUpRecursive(int[] prices, int length) {
    int[] r = new int[length + 1];
    return bottomUpRecursiveAux(r, prices, length);
  }

  // Normally is >= 0, but Java initializes arrays with zeroes
  // and n == 0 would be a seperate 'if' statement that would
  // set r[n] = 0 and return r[n]
  private static int bottomUpRecursiveAux(int[] r, int[] p, int n) {
    if (n == 0 || r[n] > 0)
      return r[n];

    int i, q = Integer.MIN_VALUE;
    for (i = 1; i <= n; i++)
      q = Math.max(q, p[i-1] + bottomUpRecursiveAux(r, p, n-i));
    return (r[n] = q);
  }

}
