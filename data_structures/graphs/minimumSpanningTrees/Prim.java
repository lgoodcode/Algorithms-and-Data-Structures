package data_structures.graphs.minimumSpanningTrees;

import java.util.function.BiFunction;

import data_structures.graphs.Graph;
import data_structures.heaps.FibonacciHeap;

/**
 * MST-Prim(G, w, r)
 * 1   for each u of G.V
 * 2       u.key = Infinity
 * 3       u.p = NIL
 * 4   r.key = 0
 * 5   Q = G.V (Queue contains all vertices)
 * 6   while Q != 0 (empty)
 * 7       u = Extract-Min(Q)
 * 8       for each v of G.Adj[u]
 * 9           if v of Q and w(u, v) < v.key
 * 10              v.p = u
 * 11              v.key = w(u, v)
 */

/**
 * Prim's algorithm forms the tree from an arbitrary root vertex {@code r} and
 * grows until the tree spans all the vertices in {@code V}. Each step adds to
 * the tree {@code E} a light edge that connects {@code E} to an isolated vertex
 * - one on which no edge of {@code E} is incident.
 *
 * <p>
 * This implementation uses the {@link FibonacciHeap} to get improved
 * performance over the {@code Binary Min Heap}. It also includes a
 * <i>visited</i> attribute to prevent reusing vertices.
 * </p>
 *
 * <p>
 * During execution, all vertices that are not in the tree reside in the
 * min-priority queue {@code Q} based on the {@code key} attribute. For each
 * vertex {@code v}, the {@code v.key} is the minimum weight of any edge
 * connecting {@code v} to a vertex in the tree; {@code v.key = Infinity} if
 * there is no such edge. The {@code v.p} attribute names the parent of the
 * {@code v} in the tree.
 * </p>
 *
 * <p>
 * The algorithm implicitly maintains the set {@code A} from Generic-MST as:
 * </p>
 * <p>
 * <i><b>A = { (v, v.p ) : v of V - {r} - Q }</b></i>
 * </p>
 *
 * <p>
 * When the min-priority queue Q is empty; the MST A for G is thus:
 * </p>
 * <p>
 * <i><b>A = { (v, v.p ) : v of V - {r} }</b></i>
 * </p>
 *
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code O(E lg V)}</h3>
 *
 * <p>
 * The running time of Prim's algorithm depends on how the min-priority queue
 * {@code Q} is implemented. If using a binary min-heap, lines 1-5 will take
 * {@code O(V)} time. Since it runs {@code |V|} calls to Extract-Min operation
 * which takes {@code O(lg V)} time, the total time is for the extractMin
 * operations {@code O(V lg V)}.
 * </p>
 *
 */
public final class Prim {
  public static final class Node extends Graph.Vertex {
    private Node(int vertex) {
      super(vertex);
    }
  }

  /**
   * The comparison function used to determine which {@code Node} has a smaller
   * distance, or key, as noted in the algorithm.
   */
  private static BiFunction<Node, Node, Boolean> compare = (x, y) -> x.distance < y.distance;

  // Prevent this class from being instantiated
  public Prim() { 
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Runs Prim's algorithm on the specified graph, which will find
   *
   * @param graph       the graph matrix to run the algorithm on
   * @param startVertex the vertex to start building the tree from
   * @return array of {@code Node} with the attributes that can be used to form
   *         the MST
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted or if the start vertex is invalid
   */
  public static Node[] run(Graph graph, int startVertex) {
    if (!graph.isWeighted())
      throw new IllegalArgumentException("Graph must be weighted.");
    graph.checkVertex(startVertex);
    return _run(graph, startVertex);
  }

  private static Node[] _run(Graph G, int r) {
    int[] V = G.getVertices();
    Node[] VTS = new Node[V.length];
    FibonacciHeap<Node> Q = new FibonacciHeap<>(compare);
    int i, u, v, w;

    for (i = 0; i < V.length; i++) {
      u = V[i];
      VTS[i] = new Node(u);
      Q.insert(VTS[i]);

      if (u == r)
        VTS[i].distance = 0;
    }

    while (!Q.isEmpty()) {
      u = Q.extractMin().getVertex();
      
      for (Graph.Edge edge : G.getEdges(u)) {
        v = edge.getVertices()[1];
        w = edge.getWeight();

        if (w < VTS[v].distance) {
          VTS[v].predecessor = u;
          VTS[v].distance = w;
        }
      }
    }

    return VTS;
  }

}
