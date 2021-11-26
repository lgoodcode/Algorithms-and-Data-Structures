package data_structures.graphs.flowNetworks;

/**
 * Generic-Push-Relabel(G)
 * 1   Initialize-Preflow(G, s)
 * 2   while there exists an applicable push or relabel operation
 * 3       select an applicable push or relabel operation and perform it
 */

/**
 * <h3>Goldberg's Generic Push-Relabel {@code O(V^2 E)}</h3>
 *
 * Implements the generic push-relabel algorithm documented in
 * {@link PushRelabelAlgorithm}.
 */
public final class Goldberg extends PushRelabelAlgorithm {
  // Prevent this class from being instantiated
  public Goldberg() {
    super();
  }

  /**
   * Runs the Goldberg algorithm to find the maximum flow in the specified flow
   * network from the specified source to the sink.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the maximum flow from the source to the sink or {@code 0} if the
   *         source is the sink
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static int maxFlow(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return 0;
    return computeMaxFlow(new FlowNetwork(network), source, sink);
  }

  private static int computeMaxFlow(FlowNetwork G, int s, int t) {
    Node[] VTS = new Node[G.getRows()];
    int u;

    // Initialize nodes for vertices that exist
    for (int v : G.getVertices())
      VTS[v] = new Node(v);

    initializePreflow(G, VTS, s);
    // While there is an overflowing vertex
    while ((u = overflowingVertex(G, VTS, s, t)) != -1) {
      for (int v : G.getAdjacentVertices(u)) {
        // If the overflowing vertex has a possible push operation
        if (VTS[u].height == VTS[v].height + 1)
          push(G, VTS, u, v);
        // Otherwise, relabel the vertex until we can push the excess flow
        else
          relabel(G, VTS, u);
      }
    }

    return VTS[t].excess;
  }
}
