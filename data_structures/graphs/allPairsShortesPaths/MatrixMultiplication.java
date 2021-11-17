package data_structures.graphs.allPairsShortesPaths;

import static java.util.Arrays.fill;

  /**
   * This isn't used here
   * 
   * Extend-Shortest-Paths(L, W)   (-)(n^3)
   * 1   n = L.rows
   * 2   let L' = l' ij be a new n x n matrix
   * 3   for i = 1 to n
   * 4       for j = 1 to n
   * 5           l' ij = Infinity
   * 6           for k = 1 to n
   * 7               l' ij = min(l' ij, l ik + w kj)
   * 8   return L'
   *
   * Input:
   *   L = the L^(1) matrix, the original of W
   *   W = the original matrix to compute the shortest-path
   */

  /**
   * Computing shortest-paths weights bottom up {@code O(n^3)}
   * 
   * Given matrices {@code L^(m-1)} where {@code m} is the number of edges of
   * {@code W}, the algorithm returns the matrix {@code L^(m)}. It extends the
   * shortest paths computed so far by one more edge.
   *
   * It computes for all {@code i} and {@code j}, using {@code L} for
   * {@code L^(m-1)} and {@code M} for {@code L^(m)}:
   *
   * <i>l ij^(m) = for 1 <= k <= n min {l ik^(m-1) + w kj}</i>
   */


public final class MatrixMultiplication {
  private static void checkIndex(int[] matrices, int index) {
    if (index < 0)
      throw new IllegalArgumentException("Index cannot be less than 0.");
    if (index > matrices.length)
      throw new IllegalArgumentException("Index cannot be greater than number of matrices.");
  }

  private static void checkIndex(int[][] table, int index) {
    if (index < 0)
      throw new IllegalArgumentException("Index cannot be less than 0.");
    if (index > table.length)
      throw new IllegalArgumentException("Index cannot be greater than table length.");
  }

  private static void checkMatrices(int[] matrices) {
    if (matrices == null)
      throw new NullPointerException("Matrices cannot be null.");
    if (matrices.length == 0)
      throw new IllegalArgumentException("Matrices cannot be empty.");
    if (matrices.length == 1)
      throw new IllegalArgumentException("Must be more than 1 matrix.");
  }

  /**
   * <h3>Matrix Chain Order {@code O(n^3)}</h3>
   * 
   * <p>
   * Top-down dynamic programming approach to calculate the least number of
   * multiplications necessary for the specified array of matrices length.
   * </p>
   * 
   * @param matrices the array of matrices lengths
   * @return the least number of matrix multiplications required
   * 
   * @throws NullPointerException     if the matrices array is {@code null}
   * @throws IllegalArgumentException if the matrices array is empty
   */
  public static int matrixChainOrder(int[] matrices) {
    checkMatrices(matrices);
    return _matrixChainOrder(matrices);
  }

  private static int _matrixChainOrder(int[] p) {
    int n = p.length;
    int[][] M = new int[n][n];
    int i, j, k, q, r, s;

    for (r = 1; r < n; r++) {
      for (i = 1, s = n - r; i < s; i++) {
        j = i + r;
        M[i][j] = Integer.MAX_VALUE;

        for (k = i; k < j; k++) {
          q = M[i][k] + M[k + 1][j] + (p[i-1] * p[k] * p[j]);

          if (q < M[i][j])
            M[i][j] = q;
        }
      }
    }

    return M[1][n-1];
  }

  public static int[][] matrixChainOrderSolution(int[] matrices) {
    checkMatrices(matrices);
    return _matrixChainOrderSolution(matrices);
  }

  private static int[][] _matrixChainOrderSolution(int[] p) {
    int n = p.length;
    int[][] M = new int[n][n];
    int[][] S = new int[n][n];
    int i, j, k, q, r, s;

    for (i = 0; i < n; i++)
      fill(S[i], Integer.MAX_VALUE);

    for (r = 1; r < n; r++) {
      for (i = 1, s = n - r; i < s; i++) {
        j = i + r;
        M[i][j] = Integer.MAX_VALUE;

        for (k = i; k < j; k++) {
          q = M[i][k] + M[k + 1][j] + (p[i-1] * p[k] * p[j]);

          if (q < M[i][j]) {
            M[i][j] = q;
            S[i][j] = k;
          }
        }
      }
    }

    return S;
  }

  public static String printOptimalParenthesis(int[] matrices, int i, int j) {
    checkIndex(matrices, i);
    checkIndex(matrices, j);
    int[][] S = matrixChainOrderSolution(matrices);
    return _printOptimalParenthesis(S, i, j, "");
  }

  public static String printOptimalParenthesis(int[][] solutionTable, int i, int j) {
    checkIndex(solutionTable, i);
    checkIndex(solutionTable, j);
    String solution = _printOptimalParenthesis(solutionTable, i, j, "");
    return solution != null ? solution : "No solution exists for " + i + " and " + j;
  }

  private static String _printOptimalParenthesis(int[][] S, int i, int j, String str) {
    String x, y;

    if (i == j)
      return String.valueOf(i);
    else if (S[i][j] == Integer.MAX_VALUE)
      return null;

    x = _printOptimalParenthesis(S, i, S[i][j], str);
    y =_printOptimalParenthesis(S, S[i][j] + 1, j, str);

    if (x == null || y == null)
      return null;
    return "(" + x + ", " + y + ")";
  }
  
}
