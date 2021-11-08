package data_structures.graphs.minimumSpanningTrees;

import java.util.Arrays;
import java.util.function.BiFunction;

import sorting.QuickSort;
import data_structures.sets.DisjointSet;
import data_structures.graphs.Graph;
import static data_structures.graphs.Graph.Edge;
import static data_structures.sets.DisjointSet.findSet;
import static data_structures.sets.DisjointSet.union;

/**
 * MST-Kruskal(G, w)
 * 1   A = 0 (empty set)
 * 2   for each vertex v of G.V
 * 3       Make-Set(v)
 * 4   sort the edges of G.E into nondecreasing order by weight w
 * 5   for each edge (u, v) of G.E, taken in nondecreasing order by weight
 * 6       if FindSet(u) != FindSet(v)
 * 7           A = A U {(u,v)} (union)
 * 8           Union(u, v)
 * 9   return A
 */

/**
 * Kruskal's algorithm finds a safe edge to add to the growing forest by
 * finding, of all the edges that connect any two trees in the forest, an edge
 * {@code (u,v)} of least weight.
 *
 * Let C1 and C2 denote the two trees that are connected by {@code (u,v)}. Since
 * {@code (u,v)} must be a light edge connecting C1 to some other tree,
 * {@code (u,v)} is a safe edge for C1.
 *
 * <p>
 * The algorithm is greedy because at each step it adds to the forest an edge of
 * least possible weight.
 * </p>
 *
 * <p>
 * Uses a disjoint-set data structure to maintain several disjoint sets of
 * elements. Each set contains the vertices in one tree of the current forest.
 * The operation {@code FindSet(u)} returns a representative element from the
 * set that contains {@code u}.
 * </p>
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code O(E lg V)}</h3>
 *
 * The running time of Kruskal's algorithm depends on how the disjoint-set data
 * structure is implemented. Using the disjoint-set-forest implementation with
 * the union-by-rank and path-compression heuristics. Initializing the set takes
 * {@code O(1)} time and the time to sort the edges is {@code O(E lg E)}.
 *
 * The for loop performs {@code O(E)} Find-Set and Union operations on the
 * disjoint-set forest, along with {@code |V|} Make-Set operations, taking a
 * total time of {@code O((V + E) ~ (V))}, where ~(the fish looking symbol) is a
 * very slowly growing function..
 */
public interface Kruskal {
  static BiFunction<Edge, Edge, Boolean> compare = (Edge x, Edge y) -> x.getWeight() < y.getWeight();

  /**
   * Runs the Kruskal algorithm on the given graph.
   * 
   * <p>
   * {@code A} is the set that will hold the edges of the MST. {@code S} is the
   * array to hold the {@code DisjointSets}. The array of edges are sorted by
   * weight. Then the disjoint-sets are initialized for each vertex in the graph,
   * creating |V| trees.
   * </p>
   * <p>
   * The main part of the algorithm is when it iterates through the sorted edges.
   * It uses {@link DisjointSet#findSet()} to get the root disjoint-set of each
   * vertex and checks whether the {@code u} and {@code v} vertices belong to the
   * same tree. If they do, then the edge {@code (u, v)} cannot bed added to the
   * forest without creating a cycle. Otherwise, the two vertices belong to
   * different trees, so we can add the edge to the array {@code A} of edges for
   * the MST. The two vertices is then run through the {@link DisjointSet#union()}
   * function to have them point to the same tree, so either vertex cannot be used
   * again.
   * </p>
   * 
   * @param graph the graph to find the MST using the Kruskal algorithm
   * @return the array of {@link Graph.Edge} for the MST
   */
  @SuppressWarnings("unchecked")
  public static Edge[] run(Graph graph) {
    Edge[] E = graph.getEdges(), A = new Edge[E.length];
    int[] vertices, V = graph.getVertices();
    int i, j, u, v, numVertices = V.length;
    DisjointSet<Integer>[] S = (DisjointSet<Integer>[]) new DisjointSet<?>[numVertices];

    // Sort the edges by weight
    QuickSort.sort(E, compare);

    for (i = 0; i < numVertices; i++)
      S[i] = new DisjointSet<Integer>(i);

    for (i = 0, j = 0; i < E.length; i++) {
      vertices = E[i].getVertices();
      u = vertices[0];
      v = vertices[1];

      if (findSet(S[u]) != findSet(S[v])) {
        A[j++] = E[i];
        union(S[u], S[v]);
      }
    }

    return Arrays.copyOf(A, j);
  }
}
