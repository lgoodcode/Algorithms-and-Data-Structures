package data_structures.graphs.allPairsShortesPaths;

import data_structures.graphs.Graph;

/**
 * Transitive-Closure(G)
 * 1   n = |G.V|
 * 2   let T^(0) = (t ij^(0)) be a new n x n matrix
 * 3   for i = 1 to n
 * 4       for j = 1 to n
 * 5           if i == j or (i, j) is a member of G.E
 * 6               t ij^(0) = 1
 * 7           else t ij^(0) = 0
 * 8   for k = 1 to n
 * 9       let T^(k) = (t ij^(k)) be a new n x n matrix
 * 10      for i = 1 to n
 * 11          for j = 1 to n
 * 12              t ij^(k) = t ij^(k-1) || (t ik^(k-1) && t kj^(k-1))
 * 12  return T^(n)
 */

/**
 * Transitive-Closure representation:
 *
 *             { 0 if i != j and (i, j) isn't a member of E
 * t ij^(0) =  { 1 if i == j or  (i, j) is a member of E
 *
 * and for k >= 1,
 *
 * t ij^(k) = t ij^(k-1) || (t ik^(k-1) && t kj^(k-1))
 */

/**
 * Given a directed graph, we might wish to determine whether graph {@code G}
 * contains a path from {@code i} to {@code j} for all vertex pairs
 * {@code (i, j)} of {@code V}.
 *
 * <p>
 * A <i>Transitive Closure</i> of {@code G} as the graph {@code G* = (V, E*)},
 * where:
 * </p>
 *
 * <i> E* = { (i, j) : there is a path from vertex i to vertex j in G } </i>
 *
 * <p>
 * One way to compute the transitive closure of a graph in {@code (-)(n^3)} time
 * is to assign a weight of {@code 1} to each edge of {@code E} and run the
 * Floyd-Warshall algorithm. If there is a path from vertex {@code i} to vertex
 * {@code j} we get {@code d ij < n} otherwise we get {@code d ij = Infinity}.
 * </p>
 *
 * <p>
 * Another way that can save time and space in practice is to substitute the
 * logical operations {@code &&} (AND) and {@code ||} (OR) for arithmetic
 * operations {@code -} and {@code +} in the Floyd-Warshall algorithm. For
 * {@code i, j, k = 1, 2, ..., n}, we define {@code t ij^(k)} to be {@code 1} if
 * there exists a path from {@code i} to {@code j} with all intermediate
 * vertices in the set {@code [1, 2, ..., k]}, and {@code 0} otherwise.
 * </p>
 *
 * <p>
 * Bitwise operators are used instead of logical operaters since they are being
 * operated on integers.
 * </p>
 *
 * <p>
 * We construct the transitive closure {@code G* = (V, E*)} by putting edge
 * {@code (i, j)} into {@code E*} if and only if {@code t ij^(n) = 1}.
 * </p>
 */
public final class TransitiveClosure extends ASPS {
  // Prevent this class from being instantiated
  public TransitiveClosure() {
    super();
  }

  /**
   * Runs the Transitive-Closure algorithm on the specified graph. Simply sets any
   * valid path from vertices {@code i} to {@code j} with a weight of {@code 1} or
   * {@code 0} if there doesn't exist an edge. Computes a table matrix that shows
   * if a path exists from the vertices at all. Once the matrix {@code T} is
   * computed it runs the Floyd-Warshall algorithm to find all possible paths from
   * every vertex in the graph.
   *
   * <p>
   * Customized the last iteration of the {@code k} for-loop to conver the weights
   * from {@code 0} to {@link Graph.NIL} for representations of no path existing
   * to work with the {@link Graph} path methods.
   * </p>
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
    int T[][] = new int[n][n];
    int W[][] = G.getAdjacencyMatrix();
    int i, j, k, last = n - 1;

    // Initialize T^(0)
    for (i = 0; i < n; i++) {
      // If G[i] is null, fill t ij to 0 for all j
      if (W[i] == null) {
        W[i] = new int[n];

        for (j = 0; j < n; j++)
          T[i][j] = 0;
        continue;
      }

      // Initialize t ij = 1 if i == j or ij is a member, 0 otherwise
      for (j = 0; j < n; j++)
        T[i][j] = i == j || W[i][j] != Graph.NIL ? 1 : 0;
    }

    for (k = 1; k < n; k++) {
      for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
          // t ij^(k) = t ij^(k-1) || (t ik^(k-1) && t kj^(k-1))
          T[i][j] = T[i][j] | (T[i][k] & T[k][j]);

          // Change the 0 to NIL for the Graph path methods to work
          if (k == last)
            T[i][j] = T[i][j] == 1 ? 1 : Graph.NIL;
        }
      }
    }

    return T;
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

    int[][] table = run(graph);

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

    int[][] table = run(graph);

    return Graph.arrayPath(table, startVertex, endVertex);
  }

}
