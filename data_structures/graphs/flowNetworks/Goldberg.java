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

  /**
   * Computes the residual network {@code Gf} as a matrix where the dimensions the
   * {@code u} and {@code v} vertices of the edges in the network. The value is
   * the residual capacity of each edge. After running the Push-Relabel algorithm
   * and now has the maximum flow, initializes the residual network with the
   * residual capacities of each edge.
   *
   * @param network the flow network to compute the residual network
   * @param source  the source vertex
   * @param sink    the sink vertex
   * @return the residual network matrix
   */
  public static int[][] residualGraph(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return null;
    return computeResidualGraph(new FlowNetwork(network), source, sink);
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

  /**
   * Computes the residual network {@code Gf} as a matrix where the dimensions the
   * {@code u} and {@code v} vertices of the edges in the network. The value is
   * the residual capacity of each edge. After running the Edmond-Karp algorithm
   * and now the the residual capacities of each edge and then will use that to
   * compare to the original flow network.
   *
   * @param network the flow network to compute the residual network
   * @param s       the source vertex
   * @param t       the sink vertex
   * @return the residual network matrix
   */
  private static int[][] computeResidualGraph(FlowNetwork network, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    int n = network.getRows();
    Node[] VTS = new Node[n];
    int[][] Gf = new int[n][];
    int[] V = network.getVertices();
    int i, u;

    // Initialize nodes for vertices that exist
    for (int v : V)
      VTS[v] = new Node(v);

    initializePreflow(network, VTS, s);
    // While there is an overflowing vertex
    while ((u = overflowingVertex(network, VTS, s, t)) != -1) {
      for (int v : network.getAdjacentVertices(u)) {
        // If the overflowing vertex has a possible push operation
        if (VTS[u].height == VTS[v].height + 1)
          push(network, VTS, u, v);
        // Otherwise, relabel the vertex until we can push the excess flow
        else
          relabel(network, VTS, u);
      }
    }

    for (i = 0; i < V.length; i++) {
      u = V[i];
      Gf[u] = new int[n];
      for (int v : network.getAdjacentVertices(u))
        Gf[u][v] = G[u][v].getCapacity() - G[u][v].getFlow();
    }

    return Gf;
  }
}
