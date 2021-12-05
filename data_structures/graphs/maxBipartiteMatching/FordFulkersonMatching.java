package data_structures.graphs.maxBipartiteMatching;

import data_structures.queues.Queue;
import data_structures.graphs.flowNetworks.FlowNetwork;
import static java.util.Arrays.fill;

/**
 * <h3>Ford-Fulkerson (Dinic's) Max Bipartite Matching {@code O(VE)}</h3>
 * 
 * <p>
 * This uses Dinic's max flow algorithm of the Ford-Fulkerson method to find a
 * <i>Maximum Matching</i>, where it's a matching of maximum cardinality, that
 * is, a matching set of edges {@code M} such that for any matching {@code M'},
 * we have {@code |M| >= |M'|}. That means maximum matches where each vertex
 * used in the set of matches {@code M} is distinct, or only used once.
 * </p>
 *
 * <p>
 * Bipartite Graphs: Graphs in which the vertex set can be partitioned into V =
 * L U R, where L and R are disjoint and all edges in E go between L and R. We
 * also assume each vertex in V has at least one incident edge, meaning one edge
 * leaving the vertex.
 * </p>
 *
 * <p>
 * By using the Ford-Fulkerson method to represent the bipartite graph as a flow
 * network where each edge has a flow of {@code 1} and the network has a super
 * sink and super source with edges of Infinite capacity to each vertex in the
 * set {@code L} and {@code R} to determine the matches. Once an edge is used,
 * it will have no residual capacity; {@code flow == capacity}, so it can't be
 * used to in accordance with the flow conservation and capacity constaint
 * properties of the flow network.
 * </p>
 *
 * <p>
 * Using Dinic's of the Ford-Fulkerson method because it has the fastest
 * asymptotic time. It is modified to initialize a super sink and super source
 * and an object to hold the matches. Once the augmenting path is found and the
 * maximum residual capacity is found (in this case, just a path is needed as
 * the residual capacity will always be {@code 1}, to count the max
 * cardinality), the flow for each edge is updated and the matches are updated.
 * This repeats until there is no augmenting path that includes an unmatched
 * vertex.
 * </p>
 *
 * <p>
 * Since any matching in a bipartite graph has cardinality at most
 * {@code min(L, R) = O(V)}, the value of the maximum flow in {@code G'} is
 * {@code O(V)}. Therefore, we can find a maximum matching in a bipartite graph
 * in {@code O(VE') = O(VE)}, since {@code |E'| = (-)(E)}.
 * </p>
 */
public final class FordFulkersonMatching extends BipartiteMatchingAlgorithm {
  public FordFulkersonMatching() {
    super();
  }

  /**
   * Finds the maximum cardinality of matches by splitting the sets {@code L} and
   * {@code R} of the flow network {@code G} by a lower and upper range. The set
   * {@code L} will consist of all vertices from {@code 0} to {@code lower} and
   * set {@code R} is {@code lower + 1} to {@code upper}.
   * 
   * @param network the flow network to find the maximum matchings of
   * @param lower   the last vertex for set {@code L}
   * @param upper   the last vertex for set {@code R}
   * @return the cardinality of maximum matchings
   * 
   * @throws IllegalArgumentException if the specified lower or upper vertex range
   *                                  is negative or greater than the network
   *                                  length.
   */
  public static int totalMatches(FlowNetwork network, int lower, int upper) {
    network.checkVertex(lower);
    network.checkVertex(upper);

    if (lower == upper)
      return 0;
    return computeMaxMatches(new FlowNetwork(network, network.getRows() + 2), lower, upper);
  }

  /**
   * Calculates the maximum bipartite matching through Dinic's Ford-Fulkerson
   * method. Simply uses an integer array to track the levels and matches using
   * the index as the vertex.
   * 
   * <p>
   * Initializes the network for matching by creating a super sink and super
   * source as the last two vertices that were added when the network as passed
   * in. Iterates through all vertices of set {@code L} and adds an edge from the
   * super source to the vertex. Does the same for vertices of set {@code R} and
   * adds an edge from the vertex to the super sink. Those added edges have
   * infinite capacity.
   * </p>
   * 
   * @param G the copied flow network, extended with two new vertices for the
   *          super source and sink
   * @param s the last vertex for set {@code L}
   * @param t the last vertex for set {@code R}
   * @return the maximum cardinality of matches
   */
  private static int computeMaxMatches(FlowNetwork G, int s, int t) {
    int n = G.getRows(), x = n - 2, y = n - 1, maxMatches = 0;
    int[] levels = new int[n], matches = new int[n];

    // Initialize matches of each vertex to NIL
    fill(matches, -1);

    // Initialize nodes for super sink and super source
    levels[x] = -1;
    levels[y] = -1;

    for (int u = 0; u <= s; u++) {
      if (G.hasVertex(u)) {
        levels[u] = -1;
        G.addEdge(x, u, Integer.MAX_VALUE);
      }
    }

    for (int u = s + 1; u <= t; u++) {
      if (G.hasVertex(u)) {
        levels[u] = -1;
        G.addEdge(u, y, Integer.MAX_VALUE);
      }
    }

    while (D_BFS(G, levels, matches, x, y))
      maxMatches += sendFlowMatches(G, levels, matches, Integer.MAX_VALUE, x, y);
    return maxMatches;
  }

  private static int sendFlowMatches(FlowNetwork G, int[] L, int[] M, int flow, int u, int t) {
    if (u == t)
      return flow;

    int c, f, v, currentFlow, cfP;
    for (FlowNetwork.Edge edge : G.getEdges(u)) {
      v = edge.getVertices()[1];
      c = edge.getCapacity();
      f = edge.getFlow();

      // Check that the vertex isn't already matched
      if (M[u] == -1 && L[v] == L[u] + 1 && f < c) {
        currentFlow = Math.min(flow, c - f);
        cfP = sendFlowMatches(G, L, M, currentFlow, v, t);

        if (cfP > 0) {
          edge.addFlow(cfP);
          G.getEdge(v, u).subtractFlow(cfP);
          // Add the match
          M[v] = u;

          return cfP;
        }
      }
    }

    return 0;
  }

  /**
   * Modified BFS that uses <i>levels</i> rather than a boolean visited property.
   * By default it has a value of {@code -1} to indicate no possible edge to push
   * flow to. If greater than {@code -1} then the vertex is visited and has a
   * possible edge to push flow to.
   *
   * <p>
   * The level idea is similar to the concept of flow running down a hill, which
   * goes down a level and doesn't skip around.
   * </p>
   *
   * @param G the flow network
   * @param L the nodes with the level property
   * @param s the source vertex
   * @param t the sink vertex
   * @return if the level of vertex {@code t} is greater than {@code -1},
   *         indicating there is a augmenting path available from the source to
   *         the sink.
   */
  private static boolean D_BFS(FlowNetwork G, int[] L, int[] M, int s, int t) {
    Queue<Integer> Q = new Queue<>(G.getRows());
    int u, v;

    // Reset levels
    fill(L, -1);

    L[s] = 0;
    Q.enqueue(s);

    while (!Q.isEmpty()) {
      u = Q.dequeue();
      // Skip vertex if already matched
      if (M[u] == -1) {
        for (FlowNetwork.Edge edge : G.getEdges(u)) {
          v = edge.getVertices()[1];
          // Find edges with no level and has a positive residual capacity
          if (L[v] < 0 && edge.getFlow() < edge.getCapacity()) {
            L[v] = L[u] + 1;
            Q.enqueue(v);
          }
        }
      }
    }

    return L[t] > -1;
  }
}
