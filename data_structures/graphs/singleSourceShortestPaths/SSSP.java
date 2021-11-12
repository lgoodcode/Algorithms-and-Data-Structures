package data_structures.graphs.singleSourceShortestPaths;

import data_structures.graphs.Graph;

/**
 * Comparing every possible distance from source to destination could be an
 * enormous number of possibilites, most of which are simply not worth
 * considering. For example, a route from Houston to Boston could include a path
 * that passes through Seattle, which is a poor choise.
 *
 * The <i>Shortest-Paths Problem</i>: given a weighted, directed graph
 * {@code G = (V, E)}, with weight function {@code w : E -> R (Real Numbers)}
 * mapping edges to real-valued weights. The <i>weight</i> {@code w(p)} of path
 * {@code p = [v0, v1, ..., vk]} is the sum of the weights of its consituent
 * edges,
 *
 * <p>
 * Edge weights can represent metrics other than distances, such as time, cost,
 * penalties, loss, or any other quantity that accumulates linearly along a path
 * and that we would want to minimize.
 * </p>
 *
 * <h4>Negative-Weight Edges:</h4>
 *
 * <p>
 * If the graph contains no negative-weight cycles reachable from source
 * {@code s}, then for all {@code v} of {@code V}, the shortest-path weight
 * {@code S(s, v)} remains well defined, even if it has a negative value.
 * </p>
 *
 * <p>
 * A shortest path cannot contain a cycle, since removing the cycle from the
 * path produces a path with the same source and destination vertices and a
 * lower path weight. No path from s to a vertex on the cycle can be
 * shortest-path.
 * <p>
 *
 * <h4>Representing Shortest Paths:</h4>
 *
 * <p>
 * Representing shortest paths is similar to Breadth-first search where we
 * maintain for each vertex {@code v} of {@code V} a predecessor {@code v.p}
 * that is either another vertex or NIL. The algorithms set the {@code v.p}
 * attributes so that the chain of predecessors originating at a vertex
 * {@code v} runs backwards along a shortest path from {@code s} to {@code v}.
 * </p>
 *
 * <p>
 * However, executing a shortest-paths algorithm, the {@code p} values might not
 * indicate shortest paths. As in BFS, we shall be interested in the
 * "predecessor subgraph" {@code G* = (V*, E*)} (substituting the pi symbol for
 * the predecessor, using *). The vertex set {@code V*} is the set of vertices
 * of {@code G} with non-NIL predecessors, plus the source {@code s}:
 * </p>
 *
 * <p>
 * <i>V* = { v of V : v.p != NIL } U {s}</i>
 * </p>
 *
 * <p>
 * The directed edge set {@code E*} is the set of edges included by the
 * {@code p} values for vertices in {@code V*}:
 * </p>
 *
 * <i>E* = { (v.p, v) of E : v of V* - {s} }</i>
 *
 * <p>
 * A shortest-paths tree contains shortest paths from the source defined in
 * terms of edge weights instead of numbers of edges. A shortest-paths tree
 * rooted at s is a directed subgraph G' = (V', E'), where V' is a subset of V
 * and E' is a subset of E, such that:
 * </p>
 *
 * <ol>
 * <li>{@code V'} is the set of vertices reachable from {@code s} in
 * {@code G}</li>
 * <li>{@code G'} forms a rooted tree with root {@code s}</li>
 * <li>For all {@code v} of {@code V'}, the unique simple path from {@code s} to
 * {@code v} in {@code G'} is a shortest path from {@code s} to {@code v} in
 * {@code G}.</li>
 * </ol>
 *
 *
 * <h4>Relaxation:</h4>
 *
 * <p>
 * The algorithms use a technique of <i><b>Relaxation</b></i>. For each vertex
 * {@code v} of {@code V}, we maintain an attribute {@code v.d}, which is an
 * upper bound on the weight of a shortest path from source {@code s} to
 * {@code v}. {@code v.d} is a <i><b>Shortest-Path Estimate</b></i>. We
 * initialize the shortest-path estimates and predecessors with the following
 * procedure:
 * </p>
 *
 * <p>
 * The process of <i><b>Relaxing</b></i> an edge {@code (u, v)} consists of
 * testing whether we can improve the shortest path to {@code v} found so far by
 * going through u and if so, updating {@code v.d} and {@code v.p}. A relaxation
 * step may decrease the value of the shortest path estimate {@code v.d} and
 * update {@code v}'s predecessor attribute {@code v.p}. The following code
 * performs a relaxation step on edge {@code (u, v)} in O(1) time:
 * </p>
 */
public class SSSP {
  public static class Node extends Graph.Vertex {
    protected Node(int vertex) {
      super(vertex);
    }
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
  protected static final void checkGraph(Graph graph) {
    if (!graph.directed && !graph.weighted)
      throw new IllegalArgumentException("The algorithm can only run on a directed weighted graph.");
  }

  /**
   * Initialize-Single-Source(G, s) (-)(V)-time
   * 1   for each vertex v of G.V
   * 2       v.d = Infinity
   * 3       v.p = NIL
   * 4   s.d = 0
   */

  /**
   * Initializes the source vertex for the SSSP algorithm by creating the array of
   * the inner class {@code Node} for each vertex in the graph. It will set the
   * source with a distance of {@code 0} to be the root.
   *
   * @param graph        the weighted directed graph to run the SSSP algorithm on
   * @param sourceVertex the root vertex of all the paths
   * @return the {@code Node[]} containing the initialized vertices
   */
  protected static final Node[] initSource(Graph graph, int sourceVertex) {
    int[] V = graph.getVertices();
    Node[] VTS = new Node[V.length];

    for (int i = 0; i < V.length; i++) {
      VTS[i] = new Node(V[i]);

      if (V[i] == sourceVertex)
        VTS[i].distance = 0;
    }

    return VTS;
  }

  /**
   * Initializes the source vertex for the SSSP algorithm by creating the array of
   * the inner class {@code Node} for each vertex in the graph. It will set the
   * source with a distance of {@code 0} to be the root.
   *
   * <p>
   * Retrievs the array of all vertices in the graph, whether they exist in the
   * graph or not to be able to quickly access them in the {@code Node[]} array
   * with their vertex number as the index.
   * </p>
   *
   * @param graph
   * @param sourceVertex
   * @return
   */
  protected static final Node[] initSourceAll(Graph graph, int sourceVertex) {
    int u, V = graph.rows;
    Node[] VTS = new Node[V];

    for (u = 0; u < V; u++) {
      VTS[u] = new Node(u);

      if (u == sourceVertex)
        VTS[u].distance = 0;
    }

    return VTS;
  }

  /**
   * Relax(u, v, w)
   * 1   if (v.d > u.d + w(u, v))
   * 2       v.d = u.d + W(u, v);
   * 3       v.p = u;
   */

  /**
   * The process of <i>Relaxing</i> an edge {@code (u, v)} consists of testing
   * whether we can improve the shortest path to {@code v} found so far by going
   * through u and if so, updating {@code v.d} and {@code v.p}. A relaxation step
   * may decrease the value of the shortest path estimate {@code v.d} and update
   * {@code v}'s predecessor attribute {@code v.p}.
   *
   * @param VTS the {@code Node[]} containing the attributes for the vertices
   * @param u   the x vertex of an edge to relax
   * @param v   the y vertex of an edge to relax
   * @param w   the weight of the edge
   */
  protected static final void relax(Node[] VTS, int u, int v, int w) {
    if (VTS[v].distance > VTS[u].distance + w) {
      VTS[v].distance = VTS[u].distance + w;
      VTS[v].predecessor = u;
    }
  }

  /**
   * Returns the path string for the start and end vertices using the results of
   * the SSSP algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static String printPath(Node[] nodes, int startVertex, int endVertex) {
    return Graph.printPath(nodes, startVertex, endVertex);
  }

  /**
   * Returns the array of path vertices for the start and end vertices using the
   * results of the SSSP algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static int[] arrayPath(Node[] nodes, int startVertex, int endVertex) {
    return Graph.arrayPath(nodes, startVertex, endVertex);
  }
}
