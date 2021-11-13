package data_structures.graphs.allPairsShortesPaths;

import data_structures.graphs.Graph;

/**
 * For representation, the input is an n x n matrix W representing the edge 
 * weights of an n-vertex directed graph G. W = (w ij), where 
 *  
 *           { 0                                   if i = j
 *   w ij =  { the weight of directed edge (i, j)  if i != j and (i, j) is a member of E
 *           { Infinity                            if i != j and (i, j) is not a member of E
 */

/**
 * The All-Pairs Shortest-Paths problem invloves finding the shortest path from
 * {@code u} to {@code v} for every pair of vertices {@code u,v of V}. We can
 * solve the problem by running a single-source shortest-paths (SSSPs) algorithm
 * {@code |V|} times, once for each vertex as the source. If all weights are
 * nonnegative, we can use Dijkstra's algorithm.
 * 
 * <p>
 * If a graph has negative-weight edges, we cannot use Dijkstra's algorithm.
 * Instead we must run the slower Bellman-Ford algorithm once from each vertex.
 * The resulting running time is {@code O(V^2 E)}, which on a dense graph
 * {@code (|E| is close to |V|^2)} is {@code O(V^4)}.
 * </p>
 * 
 * <p>
 * There are algorithms that can perform better. These algorithms also use
 * adjancency-matrix representation, except for Johnson's algorithm for sparse
 * graphs.
 * </p>
 * 
 * <p>
 * We allow negative-weight edges but assume for the time being that the graph
 * contains no negative-weight cycles.
 * </p>
 * 
 * <p>
 * The output will be in tabular form of all-pairs shortest-paths algorithms,
 * presented in an {@code n x n matrix D = (d ij)}, where {@code d ij }contains
 * the weight of a shortest path from vertex {@code i} to vertex {@code j}.
 * {@code d ij = S(i, j)}.
 * </p>
 * 
 * <p>
 * To solve the ASPSs, we need to compute the shortest-path weight AND a
 * predecessor matrix {@code P = (p ij)}, where {@code p ij} is {@code NIL} if
 * either {@code i = j} or there is no path from {@code i} to {@code j}, and
 * otherwise {@code p ij} is the predecessor of {@code j} on some shortest path
 * from {@code i} to {@code j}. Just as the predecessor subgraph {@code G}.
 * </p>
 */
public class ASPS {
  /**
   * Verifies that the supplied {@link Graph} is valid, being directed and
   * weighted.
   *
   * @param graph the graph to check
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  protected static final void checkGraph(Graph graph) {
    if (!graph.directed && !graph.weighted)
      throw new IllegalArgumentException("The algorithm can only run on a directed weighted graph.");
  }

  /**
   * Returns the path string for the start and end vertices using the results of
   * the ASPS algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static final String printPath(int[][] table, int startVertex, int endVertex) {
    return Graph.printPath(table, startVertex, endVertex);
  }

  /**
   * Returns the array of path vertices for the start and end vertices using the
   * results of the ASPS algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static final int[] arrayPath(int[][] table, int startVertex, int endVertex) {
    return Graph.arrayPath(table, startVertex, endVertex);
  }
  
}
