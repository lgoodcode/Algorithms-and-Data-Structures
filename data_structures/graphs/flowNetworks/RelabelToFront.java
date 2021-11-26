package data_structures.graphs.flowNetworks;

import data_structures.linkedLists.LinkedList;

/**
 * Relabel-To-Front(G, s, t)
 * 1   Initialize-Preflow(G, s)
 * 2   L = G.V - {s, t}, in any order
 * 3   for each vertex u of G.V - {s, t}
 * 4       u.current = u.N.head
 * 5   u = L.head
 * 6   while u != NIL
 * 7       old-height = u.h
 * 8       Discharge(u)
 * 9       if (u.h > old-height)
 * 10          move u to the front of list L
 * 11      u = u.next
 */

/**
 * <h3>Relabel to Front {@code O(V^3)}</h3>
 *
 * <p>
 * This algorithm maintains a list of the vertices in the network. It scans the
 * list, repeatedly selecting an overflowing vertex {@code u} and then
 * <i><b>Discharging</b></i> it; performs push and relabel operations until
 * {@code u} is no longer overflowing, or has positive excess. Whenever a vertex
 * is relabeled, it is placed at the front of the list and the algorithm starts
 * from the beginning again.
 * </p>
 *
 * <p>
 * The algorithm relies on <i><b>Admissible Edges</b></i> where
 * {@code cf(u, v) > 0} and {@code h(u) == h(v) + 1}, otherwise the edge is
 * <i><b>Inadmissible</b></i>. The <i><b>Admissible Network</b></i> is
 * {@code Gfh = (V, Efh)}, where {@code Efh} is the set of admissible edges.
 * </p>
 *
 * <p>
 * The vertices {@code u} themselves contain a <i><b>Neighbor List</b></i> that
 * consists of all the adjacent vertices {@code v}. When discharging a vertex
 * {@code u}, it will iterate through it's neighbor list of vertices as long as
 * {@code u} is overflowing. If it doesn't have an admissible edge to send flow
 * through, it will continually relabel itself until a Push operation applies.
 * This continues until {@code u} is no longer overflowing. If a vertex is
 * relabeled during the Discharge operation, the vertex in the main list of
 * vertices is placed to the front of the list and the process restarts until it
 * does a full iteration of the list and no vertices are overflowing.
 * </p>
 */
public final class RelabelToFront extends PushRelabelAlgorithm {
  // Prevent this class from being instantiated
  public RelabelToFront() {
    super();
  }

  /**
   * Holds the {@link LinkedList} for the neighbor list vertices and the current
   * neighbor vertice to visit.
   */
  private static class Node extends PushRelabelAlgorithm.Node {
    /**
     * The neighbor list containing all the adjacent vertices.
     */
    LinkedList<Integer> neighbors;

    /**
     * Holds the current node of the neighbor list.
     */
    LinkedList.Node<Integer> current;

    Node(int vertex) {
      super(vertex);
      neighbors = new LinkedList<>();
      current = null;
    }
  }

  /**
   * Runs the Relabel-to-Front algorithm to find the maximum flow in the specified
   * flow network from the specified source to the sink.
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
    LinkedList<Integer> L = new LinkedList<>();
    LinkedList.Node<Integer> U;
    int oldHeight;

    // Initialize the nodes, their neighbor list, and the main linkedlist
    for (int u : G.getVertices()) {
      VTS[u] = new Node(u);

      if (u != s && u != t) {
        // Initialize the neighbor list of u with adjacent vertices v
        for (int v : G.getAdjacentVertices(u))
          VTS[u].neighbors.insert(v);

        VTS[u].current = VTS[u].neighbors.getHead();

        // L = G.V - {s, t}
        L.insert(u);
      }
    }

    initializePreflow(G, VTS, s);

    U = L.getHead();
    while (U != null) {
      int u = U.getItem();
      oldHeight = VTS[u].height;
      discharge(G, VTS, u);
      // If vertex was relabeled, place it at the front of the list and start again
      if (VTS[u].height > oldHeight) {
        L.remove(U);
        L.insert(u);
        U = L.getHead();
      }

      U = U.next();
    }

    return VTS[t].excess;
  }

  /**
   * Discharge(u)
   * 1   while u.e > 0
   * 2       v = u.current
   * 3       if v == NIL
   * 4           Relabel(u)
   * 5           u.current = u.N.head
   * 6       elseif cf(u, v) > 0 and u.h == v.h + 1
   * 7           Push(u, v)
   * 8       else u.current = u.current.next
   */

  /**
   * Discharges excess flow from {@code u} to an adjacent vertex {@code v} that is
   * an admissible edge, where it has a positive residual capacity and {@code u}
   * has a height greater than {@code v} by one.
   *
   * <p>
   * It will continuously loop for the vertex {@code u} iterating through each
   * adjacent vertex in the neighbor list until it either pushes the flow or
   * relabels itself until it can perform a push operation.
   * </p>
   * 
   * <p>
   * This is modified where instead of making the {@link #relabel()} call, it
   * simply increments the height of the vertex {@code u}. This is because there
   * is excess flow that needs to be pushed and it just loops through the vertices
   * in the neighbor list until we reach the end to increment the height by one to
   * be able to perform that push operation.
   * </p>
   *
   * @param network the flow network
   * @param VTS     the Relabel-to-Front nodes
   * @param u       the overflowing vertex
   */
  protected static void discharge(FlowNetwork network, Node[] VTS, int u) {
    LinkedList.Node<Integer> V;
    int v;

    while (VTS[u].excess > 0) {
      V = VTS[u].current;
      v = V == null ? -1 : V.getItem();

      if (V == null) {
        VTS[u].height++; // relabel(network, VTS, u);
        VTS[u].current = VTS[u].neighbors.getHead();
      }
      else if (network.getEdgeCapacity(u, v) > network.getEdgeFlow(u, v)
        && VTS[u].height == VTS[v].height + 1
      )
        push(network, VTS, u, v);
      else
        VTS[u].current = VTS[u].current.next();
    }
  }
}

