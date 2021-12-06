package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;
import data_structures.sets.DisjointSet;

/**
 * <i><b>Connected Component - </b></i>A set of vertices that are all connected;
 * it is possible to reach every vertex {@code u} from any other vertex
 * {@code v} in the set.
 *
 * <p>
 * It simply runs the DFS algorithm so that it can visit every adjacent vertex
 * until it has visited every possible vertex that is reachable from the initial
 * vertex in the main DFS call. This is to ensure that all the vertices visited
 * form a connected component, where they are all reachable from each other.
 * </p>
 */
public final class ConnectedComponent {
  // Prevent this algorithm from being instantiated
  public ConnectedComponent() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  public static Object[] compute(Graph graph) {
    // Holds the connected components
    LinkedList<Object[]> CC = new LinkedList<>();
    // Holds the vertices found for the new connected component
    LinkedList<Integer> C = new LinkedList<>();
    // Auto-initialize to false to represent the vertex as not visited
    boolean[] V = new boolean[graph.getRows()];

    for (int u : graph.getVertices()) {
      if (!V[u]) {
        C.insert(u);
        CC_DFS(graph, V, C, u);
        CC.insertLast(C.toArray());
        // Clear linkedlist for next CC
        C.clear();
      }
    }

    return CC.toArray();
  }

  private static void CC_DFS(Graph G, boolean[] V, LinkedList<Integer> C, int u) {
    V[u] = true;
    for (int v : G.getAdjacentVertices(u)) {
      if (!V[v]) {
        C.insertLast(v);
        CC_DFS(G, V, C, v);
      }
    }
  }

  /**
   * Creates Disjoint-sets of connected components in the graph, where each vertex
   * is a disjoint-set. Simply iterates through each vertex and its adjacent
   * neighbors until it cannot reach any more to form a connected component,
   * linking the sets of each vertex found that doesn't already belong to the same
   * set.
   *
   * @param graph the graph to find the disjoint-set connected components
   * @return the array of disjoint-sets
   */
  @SuppressWarnings("unchecked")
  public static DisjointSet<Integer>[] computeDisjointSets(Graph graph) {
    DisjointSet<Integer>[] S = (DisjointSet<Integer>[]) new DisjointSet<?>[graph.getRows()];
    int[] V = graph.getVertices();

    for (int u : V)
      S[u] = new DisjointSet<>(u);

    for (int u : V) {
      for (int v : graph.getAdjacentVertices(u)) {
        if (DisjointSet.findSet(S[u]) != DisjointSet.findSet(S[v]))
          DisjointSet.link(S[u], S[v]);
      }
    }

    return S;
  }

  /**
   * Used to determine whether a disjoint-set vertex belongs to the same connected
   * component as the other specified disjoint-set vertex. It simply uses the
   * {@code findSet()} method which recursively travels up the parent tree for
   * each set to find the root parent for each set. If they both have the same
   * parent, then they are in the same set and belong to the same component.
   *
   * @param u the {@code DisjointSet} vertex
   * @param v the other {@code DisjointSet} vertex
   * @return whether the vertices belong to the same connected component
   */
  public static boolean sameComponent(DisjointSet<Integer> u, DisjointSet<Integer> v) {
    return DisjointSet.findSet(u) == DisjointSet.findSet(v);
  }
}
