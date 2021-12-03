package data_structures.graphs.allPairsShortesPaths;

import data_structures.graphs.Graph;

/**
 * Floyd-Warshall(W)
 * 1   n = W.rows
 * 2   D^(0) = W
 * 3   for k = 1 to n
 * 4       let D^(k) = d ij^(k) be a new n x n matrix
 * 5       for i = 1 to n
 * 6           for j = 1 to n
 * 7               d ij^(k) = min(d ij^(k-1), d ik^(k-1) + d kj^(k-1))
 * 8   return D^(n)
 */

/**
 * For representation, the input is an n x n matrix W representing the edge
 * weights of an n-vertex directed graph G. W = (w ij), where
 *
 *           { 0                                   if i = j
 *   w ij =  { the weight of directed edge (i, j)  if i != j and (i, j) is a member of E
 *           { Infinity                            if i != j and (i, j) is not a member of E
 *
 * We allow negative-weight edges but assume for the time being that the graph contains
 * no negative-weight cycles.
 *
 * The output will be in tabular form of all-pairs shortest-paths algorithms,
 * presented in an n x n matrix D = (d ij), where d ij contains the weight of
 * a shortest path from vertex i to vertex j. d ij = S(i, j).
 */

/**
 * Floyd-Warshall is a dynamic programming formulation to solve to ASPSs problem
 * on a directed graph. It runs in {@code (-)(V^3)} time, negative-weight cycles
 * may be present, but we assume that there are none.
 *
 * <p>
 * Improved version only requires {@code (-)(n^2)} space instead of
 * {@code (-)(n^3)} by instead of having a 3-dimensional matrix
 * {@ccode D[k][i][j]}, it only uses a 2-dimensional matrix and overwrites the
 * positions on each iteration of {@code k}.
 * </p>
 *
 * <p>
 * Added a method to check whether the supplied graph contains a negative weight
 * cycle by simply running the algorithm on the graph and checking if the
 * resulting table contains a negative value in any of the diagonal positions,
 * which should contain {@code 0}.
 * </p>
 *
 * <p>
 * The algorithm considers the intermediate verties of a shortest path, where an
 * intermediate vertex of a simple path {@code p = [v1, v2, ..., vn]} is any
 * vertex of {@code p} other than any vertex that is already included in the
 * path {@code p}.
 * </p>
 *
 * <p>
 * Let {@code d ij^(k)} be the weight of a shortest path from vertex {@code i}
 * to {@code j} for which all intermediate vertices are in the set
 * {@code [1, 2, ..., k]}. When {@code k = 0}, a path from vertex {@code i} to
 * {@code j} with no intermediate vertex numbered higher than 0 has no
 * intermediate vertices at all. Such a path has at most one edge, and hence
 * {@code d ij^(0) = w ij}.
 * </p>
 *
 * <p>
 * <i>d ij^(k) = w ij if k = 0 and if k >= 1 min(d ij^(k-1), d ik^(k-1) + d
 * kj^(k-1))</i>
 * </p>
 *
 * <p>
 * Because for any path, all intermediate vertices are in the set
 * {@code [1, 2, ..., n]}, the matrix {@code D^(n) = d ij^(n)} gives the final
 * answer: {@code d ij^(n) = S(i, j)} for all {@code i, j} of {@code V}.
 * </p>
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code (-)(n^3)}</h3>
 *
 * <p>
 * Using a Adjacency-matrix that starts at {@code 1} for all rows so that
 * {@code D^(0)}, the starting graph will already be set. If positions start at
 * 0, then we would need to add all the if statements to correctly calculate the
 * {@code D^(0)} just for when {@code k == 0}.
 * </p>
 *
 * <p>
 * The algorithm runs {@code k} iterations for each row of the matrix, creating
 * a new matrix and computes the values of {@code d ij^(k)} in order of
 * increasing values of {@code k}, so that it'll return the matrix {@code D^(n)}
 * of shortest-path weights.
 * </p>
 *
 * <p>
 * Because of the triply nested for loops and no elaborate data structures, the
 * algorithm runs in (-)(n^3) time.
 * </p>
 */
public final class FloydWarshall extends ASPS {
  // Prevent this class from being instantiated
  public FloydWarshall() { 
    super(); 
  }

  /**
   * Runs the Floyd Warshall algorithm on the specified graph, which will find all
   * the shortest paths possible for all vertices to all vertices with the least
   * weight. Will produce a table of the weights from vertex to vertex or
   * {@link Graph#NIL} which is {@code Infinity} to represent not possible path
   * since {@code null} cannot be used for primitive values.
   *
   * @param graph the graph matrix to run the algorithm on
   * @return the table of weights for vertex to vertex paths
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static int[][] run(Graph graph) {
    checkGraph(graph);
    return _run(graph);
  }

  private static int[][] _run(Graph G) {
    int n = G.getRows();
    int D[][] = new int[n][n];
    int W[][] = G.getAdjacencyMatrix();
    int a, b, c, i, j, k;

    // Initialize D^(0)
    for (i = 0; i < n; i++) {
      if (W[i] == null) {
        W[i] = new int[n];

        for (j = 0; j < n; j++)
          D[i][j] = i == j ? 0 : Graph.NIL;
        continue;
      }

      for (j = 0; j < n; j++) {
        if (i == j)
          D[i][j] = 0;
        else if (W[i][j] == Graph.NIL)
          D[i][j] = Graph.NIL;
        else
          D[i][j] = W[i][j];
      }
    }

    for (k = 1; k < n; k++) {
      for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
          a = D[i][j];
          b = D[i][k];
          c = D[k][j];
          // Check for Infinity value (NIL) to prevent addition overflow
          D[i][j] = b == Graph.NIL || c == Graph.NIL ? a : Math.min(a, b + c);
        }
      }
    }

    return D;
  }

  /**
   * Runs the Floyd Warshall algorithm on the specified graph to produce a
   * predecessor matrix that contains the vertices to form a path, if one exists.
   * The predecessor matrix is used to print the path.
   *
   * @param graph the graph matrix to run the algorithm on
   * @return the predecessor matrix of the graph
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static int[][] table(Graph graph) {
    checkGraph(graph);
    return _table(graph);
  }

  private static int[][] _table(Graph G) {
    int n = G.getRows();
    int D[][] = new int[n][n];
    int P[][] = new int[n][n];
    final int W[][] = G.getAdjacencyMatrix();
    int a, b, c, i, j, k;

    // Initialize D^(0) and P^(0)
    for (i = 0; i < n; i++) {
      if (W[i] == null) {
        W[i] = new int[n];

        for (j = 0; j < n; j++) {
          D[i][j] = i == j ? 0 : Graph.NIL;
          P[i][j] = Graph.NIL;
        }
        continue;
      }

      for (j = 0; j < n; j++) {
        if (i == j) {
          D[i][j] = 0;
          P[i][j] = Graph.NIL;
        }
        else if (W[i][j] == Graph.NIL) {
          D[i][j] = Graph.NIL;
          P[i][j] = Graph.NIL;
        }
        else {
          D[i][j] = W[i][j];
          P[i][j] = i;
        }
      }
    }

    for (k = 1; k < n; k++) {
      for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
          a = D[i][j];
          b = D[i][k];
          c = D[k][j];

          // Check for Infinity values to prevent addition overflow
          if (b == Graph.NIL || c == Graph.NIL || a <= b + c) {
            D[i][j] = a;
            P[i][j] = P[i][j];
          }
          else {
            D[i][j] = b + c;
            P[i][j] = P[k][j];
          }
        }
      }
    }

    return P;
  }

  /**
   * Detects if the specified graph contains a negative weight cycle by checking
   * if there is a negative value in the diagonal values of the table, which
   * should contain {@code 0}.
   *
   * @param graph the directed weighted graph to check
   * @return whether the graph contains a negative weight cycle or not
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static boolean hasNegativeWeightCycle(Graph graph) {
    int[][] table = run(graph);

    for (int i = 0; i < table.length; i++)
      if (table[i][i] < 0)
        return true;
    return false;
  }

  /**
   * Detects if the specified matrix table results from the algorithm contains a
   * negative weight cycle by checking if there is a negative value in the
   * diagonal values of the table, which should contain {@code 0}.
   *
   * @param table the matrix table results from the algorithm
   * @return whether the table contains a negative weight cycle or not
   */
  public static boolean hasNegativeWeightCycle(int[][] table) {
    for (int i = 0; i < table.length; i++)
      if (table[i][i] < 0)
        return true;
    return false;
  }

  /**
   * Runs the algorithm and returns the path string for the start and end
   * vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return if the graph contains a negative weight cycle, or the string path if
   *         one exists or no path exists message string
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);

    int[][] table = table(graph);

    if (hasNegativeWeightCycle(table))
      return "Graph contains a negative weight cycle.";
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
   *         {@code -1} if the graph contains a negative weight cycle or if no
   *         path exists
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);

    int[][] table = table(graph);
    int[] cycle = { -1 };

    if (hasNegativeWeightCycle(table))
      return cycle;
    return Graph.arrayPath(table, startVertex, endVertex);
  }

}
