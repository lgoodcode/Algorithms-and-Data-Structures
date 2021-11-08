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
   * Runs the BellmanFord SSSP algorithm on the specified graph for the source ve
   * tex of all the paths If a negative-weight cycle is detected, it will return
   * {@code null} to indicate so.
   * 
   * @param graph        the weighted directed graph to run the algorithm on
   * @param sourceVertex the single source vertex from which all paths originate
   *                     from
   * @return the {@code Node[]} results of the algorithm or {@code null} if there
   *         is a cycle
   */
  public static Node[] run(Graph graph, int sourceVertex) {
    return _run(graph, sourceVertex);
  }

  private static Node[] _run(Graph G, int s) {
    Node[] VTS = initSource(G, s);
    int[] V = G.getVertices();
    int i, j, k, u, v, len;
  
    for (k = 0, len = V.length - 1; k < len; k++) {
      for (i = 0; i < V.length; i++) {
        u = V[i];

        for (j = 0; j < V.length; j++) {
          v = V[j];

          if (G.hasEdge(u, v))
            relax(VTS, u, v, G.getEdgeWeight(u, v));
        }
      }
    }

    for (i = 0; i < V.length; i++) {
      u = V[i];
      
      for (j = 0; j < V.length; j++) {
        v = V[j];
        
        if (G.hasEdge(u, v) && VTS[v].distance > VTS[u].distance + G.getEdgeWeight(u, v))
          return null;
      }
    }

    return VTS;
  }
}
