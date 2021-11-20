package data_structures.graphs.allPairsShortesPaths;

import static java.util.Arrays.fill;

import data_structures.graphs.Graph;

/**
 * TODO: test the results with matrices from book
 */
public final class MatrixMultiplication {
  // Prevent this class from being instantiated
  public MatrixMultiplication() { 
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Verifies that the supplied {@link Graph} is valid, being directed and
   * weighted.
   *
   * @param graph the graph to check
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  private static final void checkGraph(Graph graph) {
    if (!graph.directed && !graph.weighted)
      throw new IllegalArgumentException("The algorithm can only run on a directed weighted graph.");
  }

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
   * Computing shortest-paths weights bottom up
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

  /**
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
   * Given matrices {@code L^(m-1)} where {@code m} is the number of edges and
   * {@code W}, the algorithm returns the matrix {@code L^(m)}. It extends the
   * shortest paths computed so far by one more edge.
   *
   * <p>
   * It computes for all {@code i} and {@code j}, using {@code L} for
   * {@code L^(m-1)} and {@code M} for {@code L^(m)}:
   * </p>
   *
   * <i>L ij^(m) = for 1 <= k <= n min {L ik^(m-1) + w kj}</i>
   * 
   * <p>
   * Modified: instead of the {@code w kj} on line 7 of the algorithm, we use the copied
   * matrix {@code l kj}, because for space efficiency, the matrix {@code L} is holding the most
   * recent calculations and is passed in every call instead of computing an array
   * of matrices for each call.
   * </p>
   * 
   * <p>
   * This is essentially the Floyd-Warshall algorithm.
   * </p>
   * 
   * @param matrix the matrix to find the shortest paths of
   * @return a matrix table of the shortest paths values
   */
  private static int[][] extendsShortestPaths(int[][] matrix) {
    int i, j, k, n = matrix.length;
    int[][] L = matrix;

    for (i = 0; i < n; i++) {
      for (j = 0; j < n; j++) {
        // Removed this line because we are passing the same matrix to reduce
        // memory usage of holding a copy of the same matrix from #allPaths
        // L[i][j] = Integer.MAX_VALUE;

        for (k = 0; k < n; k++) {
          // Prevent the addition overflow
          if (L[i][k] != Graph.NIL && L[k][j] != Graph.NIL)
            L[i][j] = Math.min(L[i][j], L[i][k] + L[k][j]);
        }
      }
    }
    return L;
  }

  private static int[][] extendsShortestPathsPredMatrix(int[][] matrix, int[][] P) {
    int i, j, k, n = matrix.length;
    int[][] L = matrix;

    for (i = 0; i < n; i++) {
      for (j = 0; j < n; j++) {
        for (k = 0; k < n; k++) {
          // Prevent the addition overflow
          if (L[i][k] != Graph.NIL && L[k][j] != Graph.NIL && L[i][j] > L[i][k] + L[k][j]) {
            L[i][j] = Math.min(L[i][j], L[i][k] + L[k][j]);
            P[i][j] = P[k][j];
          }
        }
      }
    }
    return L;
  }

  /**
   * Faster-All-Pairs-Shortest-Paths(W)  (-)(n^3 lg n)
   * 1   n = W.rows
   * 2   L^(1) = W
   * 3   m = 1
   * 4   while m < n - 1
   * 5       let L^(2m) be a new n x n matrix
   * 6       L^(2m) = Extend-Shortest-Paths(L^(m), L^(m))
   * 7       m = 2m
   * 8   return L^(m)
   */

  /**
   * <h3>Matrix-Multiplication Find All Paths {@code O(n^3 lg n)}</h3>
   * 
   * Because matrix multiplication is associative, we can compute {@code L^(n-1)}
   * with only {@code ceil[ lg (n-1) ]} matrix products by computing with a
   * technique called <i>Repeated Squaring</i>.
   *
   * <p>
   * In each iteration of the while loop, we compute {@code L^(2m) = (L^(m))^2},
   * starting with {@code m = 1}. At the end of each iteration we double the value
   * of {@code m}. The final iteration computes {@code L^(n-1)} by actually
   * computing {@code L^(2m)} for some {@code n - 1 <= 2m
   * < 2n - 2} such that {@code L^(2m) = L^(n-1)}.
   * </p>
   *
   * <p>
   * Because each of the {@code ceil[ lg (n-1) ]} matrix products takes
   * {@code (-)(n^3)} time, the procedure therefore runs in {@code (-)(n^3 lg n)}
   * time.
   * </p>
   *
   * <p>
   * With the {@code ceil[ lg (n-1) ]} matrices, each with {@code n^2} elements,
   * the total space requirements is {@code (-)(n^2 lg n)}, which can be reduced
   * down to {@code (-)(n^2)} space by using only two {@code n x n} matrices,
   * which is done here.
   * </p>
   * 
   * <p>
   * {@code L} is a copy of the original matrix passed in and then runs the matrix
   * products for the {@code ceil[lg(n-1)]} iterations, passing the same matrix in
   * so it doesn't use extra memory to store the previous calculations since we
   * only want the final, optimal, results.
   * </p>
   * 
   * @param matrix the matrix of the weighted paths to find the shortest paths of
   * @return the matrix table, M, of shortest paths from i to j, M[i][j]
   */
  public static int[][] allPaths(int[][] matrix) {
    int m = 1, n = matrix.length;
    int[][] L = new int[n][n];

    for (int i = 0; i < n; i++) {
      if (matrix[i] == null)
        fill(L[i], Integer.MAX_VALUE);
      else {
        for (int j = 0; j < n; j++) 
          L[i][j] = matrix[i][j];
      }
    }

    while (m < n - 1) {
      L = extendsShortestPaths(L);
      m *= 2;
    }

    return L;
  }

  public static int[][] allPathsPredMatrix(int[][] matrix) {
    int m = 1, n = matrix.length;
    int[][] L = new int[n][n];
    int[][] P = new int[n][n];

    for (int i = 0; i < n; i++) {
      if (matrix[i] == null) {
        fill(L[i], Graph.NIL);
        fill(P[i], Graph.NIL);
      }
      else {
        for (int j = 0; j < n; j++) {
          L[i][j] = matrix[i][j];

          if (i != j && matrix[i][j] != Graph.NIL)
            P[i][j] = i;
          else
            P[i][j] = Graph.NIL;
        }
      }
    }

    while (m < n - 1) {
      L = extendsShortestPathsPredMatrix(L, P);
      m *= 2;
    }

    return P;
  }

  public static int[][] allPathsWeights(Graph graph) {
    checkGraph(graph);
    return allPaths(graph.getAdjacencyMatrix());
  }

  public static int[][] allPathsPredMatrix(Graph graph) {
    checkGraph(graph);
    return allPathsPredMatrix(graph.getAdjacencyMatrix());
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

  /**
   * Runs the algorithm and returns the path string for the start and end
   * vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or no path exists message string
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    checkGraph(graph);
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);

    int[][] table = allPathsPredMatrix(graph.getAdjacencyMatrix());

    return Graph.printPath(table, startVertex, endVertex);
  }

  /**
   * Runs the algorithm and returns the array of path vertices for the start and
   * end vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for the path or an array containing just
   *         {@code -1} if no path exists
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    checkGraph(graph);
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);

    int[][] table = allPathsPredMatrix(graph.getAdjacencyMatrix());

    return Graph.arrayPath(table, startVertex, endVertex);
  }

}
