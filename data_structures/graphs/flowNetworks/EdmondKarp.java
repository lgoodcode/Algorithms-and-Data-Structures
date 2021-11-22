package data_structures.graphs.flowNetworks;

import data_structures.queues.Queue;

/**
 * Edmond-Karp(G, s, t)
 * 1   for each edge (u, v) of G.E
 * 2       (u, v).f = 0
 * 3   while there exists a path from s to t in the residual network Gf augment flow f along p
 * 4       cf(p) = min {cf(u, v) : (u, v) is in p}
 * 5       for each edge (u, v) in p
 * 6           if (u, v) is a member of G.E
 * 7               (u, v).f = (u, v).f + cf(p)
 * 8           else (v, u).f = (v, u).f - cf(p)
 */

/**
 * <h3>Edmond-Karp Algorithm {@code O(E |f*|)}</h3>
 *
 * <p>
 * Same as the Edmond-Karp algorithm except, it uses the Breadth-first search
 * algorithm to find the augmenting path {@code p} which has a running time of
 * {@code O(V + E) rather than Depth-first search, which is {@code (-)(V + E)}.
 * </p>
 *
 * <p>
 * {@code f*} denotes a maximum flow in the transformed network, then a
 * straightforward implementation of Edmond-Karp executes the while loop at most
 * {@code |f*|} times ({@code |f*|} denotes the maximum flow of a residual
 * network), since the flow value increases by at least one unit in each
 * iteration. The time to find a path in a residual network would be {@code O(V
 * + E') = O(E)}. So, each while loop taking {@code O(E)}, executed {@code |f*|}
 * times, results in a total running time of {@code O(E |f*|}).
 * </p>
 */
public final class EdmondKarp extends MaxFlowAlgorithm {
  // Prevent this class from being instantiated
  public EdmondKarp() {
    super();
  }

  /**
   * Runs the Edmond-Karp algorithm to find the maximum flow in the specified
   * flow network from the specified source to the sink.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the maximum flow from the source to the sink
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static int maxFlow(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);
    return run(network, source, sink);
  }

  private static int run(FlowNetwork network, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    Node[] VTS = new Node[network.getRows()];
    int maxFlow = 0;

    // Initialize nodes
    for (int i = 0; i < G.length; i++)
      VTS[i] = new Node(i);

    // While there is a path p from source to sink in residual network Gf that can
    // be augmented
    while (EK_BFS(network, VTS, s, t)) {
      // Find the minimum residual capacity of all edges from s to t along path p
      maxFlow += residualCapacity(G, VTS, Integer.MAX_VALUE, t);
    }
    return maxFlow;
  }

  /**
   * Modfied BFS to just check if there is a path from source {@code s} to sink
   * {@code t} that has a positive residual capacity to be able to augment a path.
   *
   * @param G   the flow network
   * @param VTS the nodes
   * @param s   the source
   * @param t   the sink
   * @returns whether a path exists from the source to the sink that can be
   *          augmented
   */
  private static boolean EK_BFS(FlowNetwork G, Node[] VTS, int s, int t) {
    Queue<Integer> Q = new Queue<>(G.getRows());
    int[] vertices;
    int u, v;

    // Reset nodes to unvisited
    for (Node node : VTS) {
      node.visited = false;
      node.predecessor = NIL;
    }

    VTS[s].visited = true;
    Q.enqueue(s);

    while (!Q.isEmpty()) {
      u = Q.dequeue();

      for (FlowNetwork.Edge edge : G.getEdges(u)) {
        // Get the vertices to ensure we are setting v to the adjacent vertex
        // because of the reversed edges, which could set v == u
        vertices = edge.getVertices();
        v = vertices[1] == u ? vertices[0] : vertices[1];

        // Find edges with a positive residual capacity: the maximum amount of flow
        // that can be added to each edge in the augmenting path
        // (0 < f < c for all (u, v) in path p)
        if (edge.getCapacity() - edge.getFlow() > 0 && !VTS[v].visited()) {
          VTS[v].visited = true;
          VTS[v].predecessor = u;
          Q.enqueue(v);
        }
      }
    }

    // Find if there was a path from the source to the sink (reached sink from source)
    return VTS[t].visited();
  }

  /**
   * This is an iterative form of the algorithm, which uses a for-loop to backtrack
   * along the path from the sink to the source, finding the residual capacity to
   * augment the edges along the path.
   *
   * This is not as efficient because it involves executing the for-loop twice,
   * once to find the residual capacity, and again to update the flow in the
   * augmenting path.
   */
  @SuppressWarnings("unused")
  private static int runIterative(FlowNetwork network, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    Node[] VTS = new Node[network.getRows()];
    int u, v, cfP, maxFlow = 0;

    // Initialize nodes
    for (u = 0; u < G.length; u++)
      VTS[u] = new Node(u);

    // Augment flow while there is a path from source to sink in residual network Gf
    while (EK_BFS(network, VTS, s, t)) {
      // Find the minimum residual capacity of all edges from s to t along path p
      cfP = Integer.MAX_VALUE;

      // Find the residual capacity to augment edges along the path
      for (v = t; v != s; v = VTS[v].predecessor) {
        u = VTS[v].predecessor;
        // Residual Capacity: cf(p) = min {cf(u, v) : (u, v) is in p}
        // cf(u, v) = c(u, v) - f(u, v)
        cfP = Math.min(cfP, G[u][v].getCapacity() - G[u][v].getFlow());
      }

      // Augment flow and update residual capacities of edges by reversing edges along the path
      for (v = t; v != s; v = VTS[v].predecessor) {
        u = VTS[v].predecessor;
        // For every edge in the augmenting path, a value of minimum residual capacity cf(u, v)
        // in the path is added to the flow of all the edges in the path.
        G[u][v].addFlow(cfP);
        // An flow of equal amount is subtracted to edges in reverse direction for every successive
        // nodes in the augmenting path. - Flow preservation
        G[v][u].subtractFlow(cfP);
      }

      // Residual capacity: the maximum amount we can increase the flow
      maxFlow += cfP;
    }
    return maxFlow;
  }
}
