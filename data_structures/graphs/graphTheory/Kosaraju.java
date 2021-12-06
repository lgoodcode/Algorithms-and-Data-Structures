package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;
import data_structures.stacks.Stack;

/**
 * 1 call DFS(G) to compute finishing times u.f for each vertex u
 * 2 compute G^T
 * 3 call DFS(G^T), but in the main loop of DFS, consider the vertices in order
 *     of decreasing u.f (as computed in line 1)
 * 4 output the vertices of each tree in the depth-first forest formed in line 3 as
 *     a seperate strongly connected components
 */

/**
 * <h3>Kosaraju SCC {@code (-)(V + E)}</h3>
 * 
 * <p>
 * <i><b>Strongly Connected Component -</b></i>A maximal set of vertices, in a
 * directed graph {@code G = (V, E)} is a {@code C} subset of {@code G.V} such
 * that for every pair of vertices {@code u} and {@code v} in {@code C}, we have
 * both {@code u ~ v} and {@code v ~ u} that is, vertices {@code u} and
 * {@code v} are reachable from each other. Any vertex that is not on a directed
 * cycle forms a strongly connected component all by itself: for example, a
 * vertex whose in-degree or out-degree is 0, or any vertex of an acyclic
 * graph.
 * </p>
 * 
 * <p>
 * A classic application of depth-first search is decomposing a directed graph
 * into its strongly connected components. After decomposing the graph into
 * strongly connected components, many algorithms run seperately on each one and
 * then combine the solutions according to the structure of connections among
 * components.
 * </p>
 *
 * <p>
 * The algorithm for finding strongly connected components of a graph
 * {@code G = (V, E)} uses the <i><b>transpose</b></i>(reverse) of {@code G},
 * which is {@code G^T = (V, E^T)}, where <i>E^T = {(u,v) : (v,u) is a member of
 * E}</i>. That is, {@code E^T} consists of the edges of {@code G} with their
 * directions reversed. Given an adjaceny-list representation of {@code G}, the
 * time to create {@code G^T} is {@code O(V +E)}.
 *
 * <p>
 * {@code G} and {@code G^T} have exactly the same strongly connected
 * components: {@code u} and {@code v} are reachable from each other in
 * {@code G} if and only if they are reachable from each other in {@code G^T}.
 * </p>
 *
 * <p>
 * The algorithm runs in linear-time {@code (-)(V + E)}, computes the strongly
 * computed components of a directed graph {@code G = (V + E)}, using two
 * depth-first searches, one on G and one on G^T.
 * </p>
 */
public final class Kosaraju {
  // Prevent this algorithm from being instantiated
  public Kosaraju() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  public static Object[] compute(Graph graph) {
    boolean[] VTS = new boolean[graph.getRows()];
    LinkedList<Object[]> SCC = new LinkedList<>();
    Stack<Integer> S = new Stack<>(graph.getRows());
    int[] V = graph.getVertices();
    Graph T = graph.transpose();
    LinkedList<Integer> L;

    for (int u : V) {
      if (!VTS[u])
        K_DFS(graph, VTS, S, u);
    }

    // Reset all vertices visited attribute for second DFS on transpose
    VTS = new boolean[graph.getRows()];

    while (!S.isEmpty()) {
      int u = S.pop();
      if (!VTS[u]) {
        L = new LinkedList<>();
        L.insert(u);
        SCC_DFS(T, VTS, L, u);
        SCC.insertLast(L.toArray());
      }
    }

    return SCC.toArray();
  }

  /**
   * Runs the DFS algorithm to simply calculate the finish times and adds them to
   * a stack to use them in reverse order for the transpose graph. By simply
   * pushing them after iterating through each possible neighbor, we know the
   * vertex is finished.
   * 
   * @param G   the directed graph
   * @param VTS the vertices for the visited attribute
   * @param S   the stack to hold the finished vertices
   * @param u   the current vertex being visited
   */
  private static void K_DFS(Graph G, boolean[] VTS, Stack<Integer> S, int u) {
    VTS[u] = true;
    for (int v : G.getAdjacentVertices(u)) {
      if (!VTS[v])
        K_DFS(G, VTS, S, v);
    }
    S.push(u);
  }

  /**
   * Using the DFS algorithm on the transpose graph using the vertices that
   * finished last, first. All vertices that can be visited will compose a
   * strongly connected component, meaning that the vertices {@code u} and
   * {@code v} are reachable from each other in both the regular and transpose
   * directed graphs.
   * 
   * @param T   the transpose of the directed graph
   * @param VTS the vertices for the visited attribute
   * @param L   the linkedlist to hold the SCC vertices
   * @param u   the current vertex being visited
   */
  private static void SCC_DFS(Graph T, boolean[] VTS, LinkedList<Integer> L, int u) {
    VTS[u] = true;
    for (int v : T.getAdjacentVertices(u)) {
      if (!VTS[v]) {
        L.insertLast(v);
        SCC_DFS(T, VTS, L, v);
      }
    }
  }
}
