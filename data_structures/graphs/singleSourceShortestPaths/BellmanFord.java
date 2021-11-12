package data_structures.graphs.singleSourceShortestPaths;

import data_structures.graphs.Graph;

/**
 * Bellman-Ford(G, w, s)
 * 1   Initialize-Single-Source(G, s)
 * 2   for i = 1 to |G.V| - 1
 * 3       for each edge (u, v) of G.E
 * 4           Relax(u, v, w)
 * 5   for each edge (u, v) of G.E
 * 6       if v.d > u.d + w(u, v)
 * 7           return False
 * 8   return True
 */

/**
 * The Bellman-Ford algorithm solves the single-source shortest-paths (SSSPs)
 * problem in the general case in which edge weights may be negative. Given a
 * weighted, directed graph {@code G} with source {@code s} and weight function
 * {@code w : E -> R}, the algorithm returns a boolean value indicating whether
 * or not there is a negative-weight cycle that is reachable from the source. If
 * there is such a cycle, the algorithm indicates no solution exists, if no
 * cycle, the algorithm produces the shortest path and their weights.
 *
 * The algorithm relaxes edges, progressively decreasing an estimate {@code v.d}
 * until it achieves the actual shortest-path weight {@code S(s, v)}.
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code O(VE)}</h3>
 *
 * The running time of the Bellman-Ford algorithm is {@code O(VE)}, since the
 * initialization takes {@code (-)(V)} time, each of the {@code |V| - 1} passes
 * over the edges in for lines 2-4 takes {@code (-)(E)} time, and lines 5-7
 * takes {@code O(E)} time.
 */
public final class BellmanFord extends SSSP {
  /**
   * Runs the BellmanFord SSSP algorithm on the specified graph for the source
   * vertex of all the paths If a negative-weight cycle is detected, it will
   * return {@code null} to indicate so.
   *
   * @param graph        the weighted directed graph to run the algorithm on
   * @param sourceVertex the single source vertex from which all paths originate
   *                     from
   * @return the {@code Node[]} results of the algorithm or {@code null} if there
   *         is a cycle
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the source vertex
   *                                  is invalid
   */
  public static Node[] run(Graph graph, int sourceVertex) {
    checkGraph(graph);
    graph.checkVertex(sourceVertex);
    return _run(graph, sourceVertex);
  }

  private static Node[] _run(Graph G, int s) {
    Node[] VTS = initSourceAll(G, s);
    int[] V = G.getVertices();
    Graph.Edge[] edges;
    int i, j, k, u, v, w, len;

    for (k = 0, len = V.length - 1; k < len; k++) {
      for (i = 0; i < V.length; i++) {
        u = V[i];
        edges = G.getEdges(u);

        for (j = 0; j < edges.length; j++) {
          v = edges[j].getVertices()[1];
          w = edges[j].getWeight();

          relax(VTS, u, v, w);
        }
      }
    }

    for (i = 0; i < V.length; i++) {
      u = V[i];
      edges = G.getEdges(u);

      for (j = 0; j < edges.length; j++) {
        v = edges[j].getVertices()[1];
        w = edges[j].getWeight();

        if (VTS[v].distance > VTS[u].distance + w)
          return null;
      }
    }

    return VTS;
  }

  /**
   * Runs the algorithm and returns the path string for the start and end
   * vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = run(graph, startVertex);
    return Graph.printPath(results, startVertex, endVertex);
  }

  /**
   * Runs the algorithm and returns the array of path vertices for the start and
   * end vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for the path
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = run(graph, startVertex);
    return Graph.arrayPath(results, startVertex, endVertex);
  }
}
