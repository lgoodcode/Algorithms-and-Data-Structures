package algorithms.dynamic;

/**
 * Longest Common Subsequence {@code O(m n) time}
 *
 * <p>
 * Top-down approach with the 0th row/column initialized with zeroes as a dummy
 * row.
 * </p>
 */
public interface LCS {
  /**
   * Recursively computes the LCS of the two given strings.
   *
   * @param str1 the first string
   * @param str2 the second string
   * @return the LCS
   *
   * @throws IllegalArgumentException if either string is {@code null} or blank
   */
  public static String compute(String str1, String str2) {
    if (str1 == null || str1.isBlank() || str2 == null || str2.isBlank())
      throw new IllegalArgumentException("Strings cannot be null or blank.");

    int m = str1.length();
    int n = str2.length();
    int[][] C = new int[m + 1][n + 1];
    computeAux(str1, str2, C);
    return print(str1, str2, C, m, n);
  }

  private static int computeAux(String X, String Y, int[][] C) {
    int m = X.length();
    int n = Y.length();

    if (m == 0 || n == 0)
      return 0;
    if (X.charAt(m - 1) == Y.charAt(n - 1))
      C[m][n] = computeAux(X.substring(0, m-1), Y.substring(0, n-1), C) + 1;
    else if (computeAux(X.substring(0, m-1), Y, C) >= computeAux(X, Y.substring(0, n-1), C))
      C[m][n] = computeAux(X.substring(0, m-1), Y, C);
    else
      C[m][n] = computeAux(X, Y.substring(0, n-1), C);
    return C[m][n];
  }

  /**
   * Iteratively computes the LCS of the two given strings.
   *
   * @param str1 the first string
   * @param str2 the second string
   * @return the LCS
   *
   * @throws IllegalArgumentException if either string is {@code null} or blank
   */
  public static String computeIterative(String str1, String str2) {
    if (str1 == null || str1.isBlank() || str2 == null || str2.isBlank())
      throw new IllegalArgumentException("Strings cannot be null or blank.");

    int m = str1.length();
    int n = str2.length();
    int[][] C = new int[m + 1][n + 1];

    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        if (str1.charAt(i - 1) == str2.charAt(j - 1))
          C[i][j] = C[i-1][j-1] + 1;
        else if (C[i - 1][j] >= C[i][j - 1])
          C[i][j] = C[i - 1][j];
        else
          C[i][j] = C[i][j - 1];
      }
    }

    return print(str1, str2, C, m, n);
  }

  /**
   * Recursively computes the LCS string from the computed table.
   *
   * @param str1 the first string
   * @param str2 the second string
   * @param C    the computed LCS table
   * @param m    the first string length
   * @param n    the second string length
   * @return the LCS string
   */
  private static String print(String str1, String str2, int[][] C, int m, int n) {
    return printAux(str1, str2, C, "", m, n);
  }

  /**
   * The auxiliary recursive function to compute the LCS string from the computed
   * table.
   *
   * @param str1   the first string
   * @param str2   the second string
   * @param C      the computed LCS table
   * @param output the computed LCS string
   * @param m      the first string index
   * @param n      the second string index
   * @return the LCS string
   */
  private static String printAux(String X, String Y, int[][] C, String output, int i, int j) {
    if (i == 0 || j == 0)
      return output;
    if (X.charAt(i-1) == Y.charAt(j-1))
      return printAux(X, Y, C, X.charAt(i - 1) + output, i-1, j-1);
    else if (C[i-1][j] >= C[i][j-1])
      return printAux(X, Y, C, output, i-1, j);
    return printAux(X, Y, C, output, i, j-1);
  }

}
