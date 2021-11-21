package data_structures.graphs.maxBipartiteMatching;

import data_structures.graphs.Graph;
import data_structures.queues.Queue;

/**
 * Hopcroft-Karp(G)
 * 1   M = 0 (empty set)
 * 2   repeat
 * 3       let P = {P1, P2, ..., Pk} be a maximal set of vertex-disjoint
 *             shortest augmenting paths with respect to M
 * 4       M = M + (P1 U P2 U ... U Pk)
 * 5   until P == 0 (empty set)
 * 6   return M
 */

/**
 * <h3>Hopcroft-Karp {@code O(sqrt(V) E)}</h3>
 * 
 * <p>
 * The Hopcroft-Karp algorithm finds a maximum matching in a bipartite graph on
 * {@code O(sqrt(V) E)}.
 * </p>
 *
 * <p>
 * BFS paritions the vertices into layers. The free vertices in {@code U} are
 * used as the starting vertices of this search and form the first layer of the
 * partitioning. At the first level of the search, there are only unmatched
 * edges, since the free vertices of {@code U} are by definition not adjacent to
 * any matched edges. At subsequent levels of the search, the traversed edges
 * are required to alternate between matched and unmatched. That is, when
 * searching for successors from a vertex in {@code U}, only unmatched edges may
 * be traversed, while from a vertex in {@code V} only matched edges may be
 * traversed. The search terminates at the first layer {@code k} where one or
 * more free vertices in {@code V} are reached.
 * </p>
 *
 * <p>
 * A partial matching here is when the {@code pairU} and {@code pairV} tables
 * that contain one vertex to which each vertex of {@code U} and of {@code V} is
 * matched, or {@code NIL} for unmatched vertices.
 * </p>
 * 
 * <p>
 * Using the {@code null} (dummy) vertex connected to all unmatched vertices in
 * {@code U} and {@code V}, so that when we run the BFS from dummy to dummy, we
 * can get the paths of minimal length that connect currently unmatched vertices
 * in {@code U} to currently unmatched vertices in {@code V}.
 * </p>
 * 
 * <p>
 * As the graph is bipartite, these paths always alternate between vertices in
 * {@code U} and vertices in {@code V}, and we require in our BFS that when
 * going from {@code V} to {@code U}, e always select a matched edge. If we
 * reach an unmatched vertex of {@code V}, then we end at {@code vDummy} and the
 * search for paths n the BFS terminate. To summarize, the BFS starts at un
 * atched vertices in {@code U}, goes to all their neighbors in {@code V}, if
 * all are matched then it goes back to the vertices in {@code U} to whic all
 * these vertices are matched (and which were not visited before), hen it goes
 * to all the neighbors of these vertices, etc., until one of the vertices
 * reached in {@code V} is unmatched.
 * </p>
 *
 * <p>
 * If BFS returns true, then we can go ahead and update the pairing for vertices
 * on the minimal-length paths found from {@code U} to {@code V}: we do so using
 * a depth-first search (DFS). Note that each vertex in V on such a path, except
 * for the last one, is currently matched. So we can explore with the DFS,
 * making sure that the paths that we follow correspond to the distances
 * computed in the BFS. We update along every such path by removing from the
 * matching all edges of the path that are currently in the matching, and adding
 * to the matching all edge of the path that are currently not in the matching:
 * as this is an augmenting path (the first and last edges of the path were not
 * part of the matching, and the path alternated between matched and unmatched
 * edges), then this increased the number of edges in the matching. This is same
 * as replacing the current matching by the symmetric difference between the
 * current matching and the entire path.
 * </p>
 *
 * <p>
 * Note that the code ensures that all augmenting paths that we consider are
 * vertex disjoint. Indeed, after doing the symmetric difference for a path,
 * none of its vertices could be considered again in the DFS, just because the
 * {@code Dist[Pair_V[v]]} will not be equal to {@code Dist[u] + 1} (it would be
 * exactly {@code Dist[u]}).
 * </p>
 *
 * <p>
 * Also observe that the DFS does not visit the same vertex multiple times due
 * to the lines: {@code dist[u] = Infinity; return false;}
 * </p>
 */
public final class HopcroftKarp {
  private static final boolean TOTAL = false;
  private static final boolean MATCHES = true;
  // Dummy vertex
  private static int NIL;

  public static class Node {
    private int pairU;
    private int pairV;
    private int distance;

    private Node(int vertex) {
      pairU = NIL;
      pairV = NIL;
      distance = 0;
    }
  }
  
  // Prevent this class from being instantiated
  public HopcroftKarp() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  @SuppressWarnings("unchecked")
  private static <T> T _run(boolean type, Graph G) {
    int n = G.rows;
    // Set the dummy vertex to the next possible vertex that doesn't exist
    NIL = n;
    Node[] VTS = new Node[n+1];
    Integer maxMatches = 0;

    // Initialize all pairs to the dummy vertex
    for (int i = 0; i < n; i++)
      VTS[i] = new Node(i);

    // Initialize dummy vertex
    VTS[n] = new Node(NIL);

    // While there is an augmenting path
    while (HP_BFS(G, VTS)) {
      for (int u : G.getVertices()) {
        if (VTS[u].pairU == NIL && HP_DFS(G, VTS, u))
          maxMatches++;
      }
    }
    return type == TOTAL ? (T) maxMatches : (T) VTS;
  }

  /**
   * Performs a Breadth-first search on the initial layer of vertices, only
   * traversed edges that haven't been matched.
   * 
   * @param G   the graph
   * @param VTS the array of vertices with their pairs and distances
   * @return whether there is an augmenting path available
   */
  private static boolean HP_BFS(Graph G, Node[] VTS) {
    Queue<Integer> Q = new Queue<>(G.rows);
    int[] vertices = G.getVertices();
    int u, v;

    // Left side vertices - first layer (set distance as 0)
    for (int i = 0; i < vertices.length; i++) {
      u = vertices[i];
      // If u is not matched (paired with dummy vertex)
      if (VTS[u].pairU == NIL) {
        // Set distance of u to 0 and add to queue
        VTS[u].distance = 0;
        Q.enqueue(u);
      }
      // Otherwise, set distance as infinite so that the vertex is considered next time
      else 
        VTS[u].distance = Integer.MAX_VALUE; 
    }

    // Initialize distance to dummy vertex as infinite 
    VTS[NIL].distance = Integer.MAX_VALUE;

    while (!Q.isEmpty()) { 
      u = Q.dequeue();
      // If this node is not NIL and can provide a shorter path to dummy vertex
      if (VTS[u].distance < VTS[NIL].distance) {
        // For each adjacent vertex of u
        for (Graph.Edge edge : G.getEdges(u)) {
          v = edge.getVertices()[1];
          // If v is not considered so far; (v, pairV[v]) is not an explored edge
          if (VTS[VTS[v].pairV].distance == Integer.MAX_VALUE) {
            // Consider pair and add to queue
            VTS[VTS[v].pairV].distance = VTS[u].distance + 1;
            Q.enqueue(VTS[v].pairV);
          }
        }
      }
    }
    // If we can come back to NIL using an alternating path of distinct 
    // vertices, then there is an augmenting path
    return VTS[NIL].distance != Integer.MAX_VALUE;
  }

  /**
   * The Depth-first search part of the Hopcroft-Karp algorithm where once we find
   * a vertex that doesn't have a pair in the {@code U} set (pairU[u] == dummy
   * vertex).
   * 
   * <p>
   * The operation first checks each adjacent vertex {@code v} of the edges of the
   * specified vertex {@code u}. It makes a check whether if {@code pairV[v]} has
   * a distance that is equal to the distance of vertex {@code u + 1} AND performs
   * a recursive DFS operation on the vertex {@code pairV[v]}. The DFS operation
   * ultimately checks if the specified vertex is the dummy vertex {@code NIL}
   * which means the initial vertex being checked doesn't have a pair and can be
   * matched, or, if the checked vertex can be matched to a different vertex.
   * Otherwise, the vertex {@code u} cannot be matched or is already matched.
   * </p>
   * 
   * @param G   the graph containing the edges for matches
   * @param VTS the array of all the vertices with their pairs and distances
   * @param u   the vertex being checked for a potential pair
   * @return whether the vertex {@code u} is {@code NIL} and can be paired with
   *         {@code pairV[v]} from a recursive call or the vertex {@code u} can't
   *         be matched
   */
  private static boolean HP_DFS(Graph G, Node[] VTS, int u) {
    // If vertex, u, being checked doesn't have a pair (paired to dummy), found a match
    if (u == NIL)
      return true;

    int v;
    for (Graph.Edge edge : G.getEdges(u)) {
      v = edge.getVertices()[1];  

      // If pairV[v] has a shorter distance than the current pair for u
      // and doesn't already have a pair that is shorter
      if (VTS[VTS[v].pairV].distance == VTS[u].distance + 1 && HP_DFS(G, VTS, VTS[v].pairV)) {
        VTS[v].pairV = u;
        VTS[u].pairU = v;
        return true;
      } 
    }

    // A pair couldn't be made so we set the distance of u to infinity to
    // indicate there is no match
    VTS[u].distance = Integer.MAX_VALUE;
    return false;
  }

  /**
   * Runs the Hopcroft-Karp algorithm on the specified directed graph where the
   * edges are possible pairs and finds the maximum cardinality of possible pairs.
   * 
   * @param graph the directed graph to run the algorithm on
   * @return the maximum cardinality of matches
   * 
   * @throws IllegalArgumentException if the specified graph is not directed
   */
  public static int total(Graph graph) {
    if (!graph.directed)
      throw new IllegalArgumentException("The graph must be directed.");
    return _run(TOTAL, graph);
  }

  /**
   * Runs the Hopcroft-Karp algorithm on the specified directed graph where the
   * edges are possible pairs and finds the maximum cardinality of possible pairs.
   * Will return an array of the matches where the array indices represent the
   * vertices and the value is the matched vertex.
   * 
   * @param graph the directed grpah to run the algorithm on
   * @return the array of matches
   * 
   * @throws IllegalArgumentException if the specified graph is not directed
   */
  public static int[] matches(Graph graph) {
    if (!graph.directed)
      throw new IllegalArgumentException("The graph must be directed.");
    
    int n = graph.rows, NIL = n;
    Node[] VTS = _run(MATCHES, graph);
    int[] matches = new int[n];

    for (int i = 0; i < n; i++)
      matches[i] = VTS[i].pairU != NIL ? VTS[i].pairU : Graph.NIL;
    return matches;
  }

  /**
   * Runs the Hopcroft-Karp algorithm on the specified directed graph where the
   * edges are possible pairs and finds the maximum cardinality of possible pairs.
   * Takes the results of the {@link #matches(Graph)} method and prints the
   * matches.
   * 
   * @param matches the array of matches
   * @return the string of matches
   */
  public static String printMatches(int[] matches) {
    StringBuilder sb = new StringBuilder("{\n");

    for (int i = 0, len = matches.length; i < len; i++) {
      if (matches[i] != Graph.NIL)
        sb.append("\s\s" + i + " -> " + matches[i] + "\n");
    }

    return sb.toString() + "}";
  }

  /**
   * Runs the Hopcroft-Karp algorithm on the specified directed graph where the
   * edges are possible pairs and finds the maximum cardinality of possible pairs.
   * Will return a string representing the pairs.
   * 
   * @param graph the directed graph to run the algorithm on
   * @return the string of matches
   * 
   * @throws IllegalArgumentException if the specified graph is not directed
   */
  public static String printMatches(Graph graph) {
    return printMatches(matches(graph));
  }
}
