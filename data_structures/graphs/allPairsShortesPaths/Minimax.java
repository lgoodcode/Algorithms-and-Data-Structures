package data_structures.graphs.allPairsShortesPaths;

import data_structures.graphs.Graph;

/**
 * In graph problems involves finding a path between two nodes that minimizes
 * the maximum cost along the path. (Example problems include finding feasible
 * paths to take by car with a limited fuel tank and rest stations at every
 * node). You can also add in an update to a predecessor matrix in order to keep
 * track of the actual path found.
 *
 * <p>
 * Based off of the FloydWarshall algorithm.
 * </p>
 *
 * <p>
 * {@code d} is an distance matrix for {@code n} nodes e.g. {@code d[i][j]} is
 * the direct distance from {@code i} to {@code j}. the distance from a node to
 * itself e.g. {@code d[i][i]} should be initialized to {@code 0}. The resulting
 * table positions {@code d[i][j]} will contain the length of the minimax path
 * from {@code i} to {@code j}.
 * </p>
 */
public final class Minimax extends ASPS {
  // Prevent this class from being instantiated
  public Minimax() {
    super();
  }

  /**
   * Runs the Floyd Warshall MiniMax algorithm on the graph that minimizes the
   * maximum cost along a path. Will return the matrix table of the cost of paths
   * from vertices {@code i} to {@code j} in position {@code D[i][j]}.
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
    int n = G.rows;
    int D[][] = new int[n][n];
    int W[][] = G.getAdjacencyMatrix();
    int i, j, k;

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
        for (j = 0; j < n; j++)
          D[i][j] = Math.min(D[i][j], Math.max(D[i][k], D[k][j]));
      }
    }

    return D;
  }

  /**
   * Runs the Floyd Warshall MiniMax algorithm on the graph that minimizes the
   * maximum cost along a path. Will return the matrix table of the paths to
   * result in the minimum maximum costs along a specified path.
   *
   * @param graph the graph matrix to run the algorithm on
   * @return the predecessor matrix of the algorithm on the graph
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static int[][] table(Graph graph) {
    checkGraph(graph);
    return _table(graph);
  }

  private static int[][] _table(Graph G) {
    int n = G.rows;
    int D[][] = new int[n][n];
    int P[][] = new int[n][n];
    int W[][] = G.getAdjacencyMatrix();
    int i, j, k, x, y;

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
          if (D[i][j] <= Math.max(D[i][k], D[k][j])) {
            x = D[i][j];
            y = P[i][j];
          }
          else if (D[i][k] >= D[k][j]) {
            x = D[i][k];
            y = P[i][k];
          }
          else {
            x = D[k][j];
            y = P[k][j];
          }

          D[i][j] = x;
          P[i][j] = y;
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
   * Runs the algorithm and returns the path string for the start and end
   * vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);
    int[][] table = table(graph);
    return Graph.printPath(table, startVertex, endVertex);
  }

  /**
   * Runs the algorithm and returns the array of path vertices for the start and
   * end vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for a path, or a single {@code -1} element if
   *         no path exists
   * 
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);
    int[][] table = table(graph);
    return Graph.arrayPath(table, startVertex, endVertex);
  }

}
