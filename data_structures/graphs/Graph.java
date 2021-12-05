package data_structures.graphs;

import java.util.NoSuchElementException;
import static java.util.Arrays.copyOf;

import data_structures.queues.Queue;

/**
 * Graph matrix that supports weighted and directed graphs. Contains a matrix
 * array that is used to determine vertices and edges. Uses a sentinel
 * {@code NIL} value of {@link Integer.MIN_VALUE} to represent no vertices and
 * weights.
 */
public final class Graph {
  /**
   * Whether the graph is directed or not. If not, an edge goes both ways from the
   * respective vertices.
   */
  private final boolean directed;

  /**
   * Whether the graph edges are weighted or not. If so, the edges will contain
   * the weight value.
   */
  private final boolean weighted;

  /**
   * The length and width of the graph.
   */
  private final int rows;

  /**
   * The graph matrix.
   */
  private int[][] G;

  /**
   * The number of vertices contained in the graph.
   */
  private int vertices;

  /**
   * The number of edges contained in the graph.
   */
  private int edges;

  /**
   * The sentinel value used to represent non-vertices and non-edges.
   */
  public static final int NIL = Integer.MAX_VALUE;

  /**
   * Constructs an empty, graph matrix, with the specified number of rows for an n
   * x n graph. The graph can also be set to be directed and/or weighted.
   *
   * @param rows     the number of rows and columns in the graph
   * @param directed whether the graph has directed edges
   * @param weighted whether the graph has weighted edges
   *
   * @throws IllegalArgumentException if the specified number of rows is less than
   *                                  {@code 1}
   */
  public Graph(int rows, boolean directed, boolean weighted) {
    if (rows < 0)
      throw new IllegalArgumentException("Number of rows must be greater than 0.");

    this.directed = directed;
    this.weighted = weighted;
    this.rows = rows;
    vertices = 0;
    edges = 0;
    G = new int[rows][];
  }

  /**
   * Constructs an empty, graph matrix, with the specified number of rows for an n
   * x n graph. The graph can also be set to be directed and is not weighted.
   *
   * @param rows     the number of rows and columns in the graph
   * @param directed whether the graph has directed edges
   *
   * @throws IllegalArgumentException if the specified number of rows is less than
   *                                  {@code 1}
   */
  public Graph(int rows, boolean directed) {
    this(rows, directed, false);
  }

  /**
   * Constructs an empty, graph matrix, with the specified number of rows for an n
   * x n graph. The graph is not directed or weighted.
   *
   * @param rows the number of rows and columns in the graph
   *
   * @throws IllegalArgumentException if the specified number of rows is less than
   *                                  {@code 1}
   */
  public Graph(int rows) {
    this(rows, false, false);
  }

  /**
   * Constructs a copy of the specified {@link Graph}.
   *
   * @param graph the graph to copy
   *
   * @throws NullPointerException if the graph is {@code null}
   */
  public Graph(Graph graph) {
    if (graph == null)
      throw new NullPointerException("Graph cannot be null.");

    rows = graph.rows;
    directed = graph.directed;
    weighted = graph.weighted;
    vertices = graph.vertices;
    edges = graph.edges;

    G = new int[rows][];

    for (int u : graph.getVertices())
      G[u] = copyOf(graph.G[u], rows);
  }

  /**
   * Constructs a copy and extends the length of the specified {@link Graph}.
   *
   * @param graph the graph to copy
   * @param rows  the new graph length
   *
   * @throws NullPointerException     if the graph is {@code null}
   * @throws IllegalArgumentException if the specified new length is less than the
   *                                  original graph
   */
  public Graph(Graph graph, int rows) {
    if (graph == null)
      throw new NullPointerException("Graph cannot be null.");
    if (rows < graph.rows)
      throw new IllegalArgumentException("New graph length can't be less than the original.");

    this.rows = rows;
    directed = graph.directed;
    weighted = graph.weighted;
    vertices = graph.vertices;
    edges = graph.edges;

    G = new int[rows][];

    for (int u : graph.getVertices()) {
      G[u] = copyOf(graph.G[u], rows);
      // Fill in the new vertices positions with NIL values
      for (int j = graph.rows; j < rows; j++)
        G[u][j] = Graph.NIL;
    }
  }

  /**
   * Returns the transpose of the current graph, which is all the edges reversed.
   *
   * @return the transpose of the graph
   */
  public Graph transpose() {
    Graph G = new Graph(rows, directed, weighted);
    int v;

    for (int u : getVertices()) {
      for (Edge edge : getEdges(u)) {
        v = edge.getVertices()[1];

        if (!G.hasEdge(u, v)) {
          if (weighted)
            G.addEdge(v, u, edge.getWeight());
          else
            G.addEdge(v, u);
        }
      }
    }

    return G;
  }

  /**
   * Used to hold attributes of an algorithm for each vertex. Is meant to be
   * extended for algorithm-specific implementations.
   */
  public static class Vertex {
    private int vertex;
    public int distance;
    public int predecessor;

    protected Vertex(int vertex) {
      this.vertex = vertex;
      distance = Integer.MAX_VALUE;
      predecessor = -1;
    }

    public int getVertex() {
      return vertex;
    }
  }

  /**
   * The static class to easily retrieve the edges from the graph and to be able
   * to access the vertices as well as the weight of the edge, if applicable.
   */
  public static final class Edge {
    private boolean weighted;
    private int u;
    private int v;
    private int w;

    private Edge(int u, int v) {
      weighted = false;
      this.u = u;
      this.v = v;
    }

    private Edge(int u, int v, int w) {
      weighted = true;
      this.u = u;
      this.v = v;
      this.w = w;
    }

    /**
     * Returns a two element {@code int} array containing the {@code u} and
     * {@code v} vertices.
     *
     * @return the edge vertices array
     */
    public int[] getVertices() {
      int[] V = { u, v };
      return V;
    }

    /**
     * Retrieves the weight of the edge, if the graph is weighted. Otherwise, it
     * will throw an exception.
     *
     * @return the edge weight
     *
     * @throws IllegalCallerException if the edge is not weighted
     */
    public int getWeight() {
      if (!weighted)
        throw new IllegalCallerException("This edge is not part of a weighted graph.");
      return w;
    }

    /**
     * Converts the edge into an array to be accessed publicly outside the
     * implementing code. The array will consist of the {@code u} and {@code v}
     * vertices as the {@code 0} and {@code 1}, respectively. If the graph is
     * weighted, it will have the weight as the third value of the array, {@code 2}.
     * 
     * @return the edge in an {@code Integer} array.
     */
    public Integer[] toArray() {
      if (!weighted) {
        Integer[] arr = { u, v };
        return arr;
      }

      Integer[] arr = { u, v, w };
      return arr;
    }

    /**
     * Prints the edge in the normal graph format.
     * 
     * @return the edge string
     */
    public String toString() {
      return "(" + u + ", " + v + ")";
    }
  }

  /**
   * Checks that the specified vertex index is valid.
   *
   * @param vertex the vertex index
   *
   * @throws IllegalArgumentException if the vertix is negative or greater than
   *                                  graph length
   */
  public void checkVertex(int vertex) {
    if (vertex < 0)
      throw new IllegalArgumentException("Vertex cannot be negative.");
    if (vertex >= rows)
      throw new IllegalArgumentException("Vertex cannot be greater than graph length.");
  }

  /**
   * Checks that the specified vertex index is valid for the specified length of
   * the graph.
   *
   * @param rows   the length of the graph
   * @param vertex the vertex index
   *
   * @throws IllegalArgumentException if the vertix is negative or greater than
   *                                  graph length
   */
  public static void checkVertex(int rows, int vertex) {
    if (vertex < 0)
      throw new IllegalArgumentException("Vertex cannot be negative.");
    if (vertex >= rows)
      throw new IllegalArgumentException("Vertex cannot be greater than graph length.");
  }

  /**
   * Returns the number of rows in the graph matrix.
   *
   * @return the number of rows in the graph
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns whether this graph is directed or not.
   *
   * @return if the graph is directed
   */
  public boolean isDirected() {
    return directed;
  }

  /**
   * Returns whether this graph is weighted or not.
   *
   * @return if the graph is weighted
   */
  public boolean isWeighted() {
    return weighted;
  }

  /**
   * Returns the matrix adjacency representation.
   *
   * @return the adjacency matrix
   */
  public int[][] getAdjacencyMatrix() {
    return G;
  }

  /**
   * Returns the number of vertices in the graph.
   *
   * @return the number of vertices
   */
  public int getNumVertices() {
    return vertices;
  }

  /**
   * Returns the number of edges in the graph.
   *
   * @return the number of edges
   */
  public int getNumEdges() {
    return edges;
  }

  /**
   * Returns an array with a length of the number of vertices and each element is
   * the vertex.
   *
   * @return the array of vertices indices
   */
  public int[] getVertices() {
    int[] V = new int[rows];
    int i, j = 0;

    for (i = 0; i < V.length; i++)
      if (G[i] != null)
        V[j++] = i;
    return copyOf(V, j);
  }

  /**
   * Returns an array of the adjacent vertices of the specified vertex.
   * 
   * @param u the vertex whose adjacent vertices is needed
   * @return the adjacent vertices
   * 
   * @throws IllegalArgumentException if the vertex doesn't exist in the graph
   */
  public int[] getAdjacentVertices(int u) {
    if (!hasVertex(u))
      throw new IllegalArgumentException("Vertex " + u + " does not exist in graph.");

    int[] V = new int[rows];
    int i, j, len;

    for (i = 0, j = 0, len = rows; i < len; i++)
      if (G[u][i] != Graph.NIL)
        V[j++] = i;
    return copyOf(V, j);
  }

  /**
   * Returns an array of {@link Graph.Edge} of all the edges in the graph. When
   * initializing the edge array, will set length to twice the number of edges
   * contained in the graph if it isn't directed since the edges will point in
   * both directions.
   *
   * @return array of {@code Edges}
   */
  public Edge[] getEdges() {
    if (edges == 0)
      return new Edge[0];

    int numEdges = directed ? edges : edges * 2;
    int j, k = 0, len = rows;
    Edge[] E = new Edge[numEdges];

    // Iterate through each vertex
    for (int i : getVertices()) {
      // Iterate through each edge of the vertex
      for (j = 0; j < len; j++) {
        if (G[i][j] != NIL)
          E[k++] = weighted ? new Edge(i, j, G[i][j]) : new Edge(i, j);
      }
    }

    return E;
  }

  /**
   * Returns the edges for the specified vertex.
   *
   * @param vertex the vertex whose edges is being retrieved
   * @return the {@code Edge} array
   *
   * @throws IllegalArgumentException if the vertex doesn't exist in the graph
   */
  public Edge[] getEdges(int vertex) {
    if (!hasVertex(vertex))
      throw new IllegalArgumentException("Vertex " + vertex + " does not exist in graph.");

    Edge[] E = new Edge[vertices];
    int i = 0, u = vertex, v, len = rows;

    // Iterate through each possible adjacent vertex v
    for (v = 0; v < len; v++) {
      if (G[u][v] != NIL)
        E[i++] = weighted ? new Edge(u, v, G[u][v]) : new Edge(u, v);
    }

    return copyOf(E, i);
  }

  /**
   * Determines whether a given vertex exists in the graph in or not.
   *
   * @param v the vertex index to check
   * @return whether the vertex is in the graph or not
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   */
  public boolean hasVertex(int v) {
    checkVertex(v);
    return G[v] != null;
  }

  /**
   * Determines whether a given edge exists in the graph in or not.
   *
   * @param u the u vertex index of the edge
   * @param v the v vertex index of the edge
   * @return whether the edge exists or not
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public boolean hasEdge(int u, int v) {
    checkVertex(u);
    checkVertex(v);
    return G[u] != null && G[u][v] != NIL;
  }

  /**
   * Returns an {@link Graph.Edge} and the weight if the graph is weighted.
   *
   * @param u the u vertex of the edge
   * @param v the v vertex of the edge
   * @return the {@code Edge}
   *
   * @throws NoSuchElementException if the edge does not exist in the graph
   */
  public Edge getEdge(int u, int v) {
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    if (weighted)
      return new Edge(u, v, G[u][v]);
    return new Edge(u, v);
  }

  /**
   * Retrieves the weight for an edge.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   * @return the edge weight
   *
   * @throws IllegalCallerException   if the graph is not weighted
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   * @throws NoSuchElementException   if the edge doesn't exist in the graph
   */
  public int getEdgeWeight(int u, int v) {
    if (!weighted)
      throw new IllegalCallerException("This graph is not weighted.");
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    return G[u][v];
  }

  /**
   * Adds a specified vertex index to the graph.
   *
   * @param v the vertex index to add
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   */
  public void addVertex(int v) {
    if (!hasVertex(v)) {
      int i, len = rows;
      G[v] = new int[len];
      vertices++;

      // Initalize adjacencies to NIL
      for (i = 0; i < len; i++)
        G[v][i] = NIL;
    }
  }

  /**
   * Adds an edge to the graph with the given x and y vertices as well as the edge
   * weight, if applicable. Will add the reverse edge if the graph is not
   * directed, so the edge points in both directions.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   * @param w the edge weight
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length or if the edge already
   *                                  exists in the graph
   */
  private void _addEdge(int u, int v, int w) {
    if (!hasVertex(u))
      addVertex(u);
    if (!hasVertex(v))
      addVertex(v);
    if (hasEdge(u, v))
      throw new IllegalArgumentException("Edge already exists in the graph.");

    if (weighted)
      G[u][v] = w;
    else
      G[u][v] = 1;

    if (!directed) {
      if (weighted)
        G[v][u] = w;
      else
        G[v][u] = 1;
    }

    edges++;
  }

  /**
   * Adds an edge to the graph with no weight.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length, or if the edge already
   *                                  exists in the graph
   */
  public void addEdge(int u, int v) {
    _addEdge(u, v, 1);
  }

  /**
   * Adds an edge to the graph with the specified weight.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   * @param w the weight
   *
   * @throws IllegalCallerException   if the graph is not weighted
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length, or if the edge already
   *                                  exists in the graph
   */
  public void addEdge(int u, int v, int w) {
    if (!weighted)
      throw new IllegalCallerException("This graph is not weighted.");
    _addEdge(u, v, w);
  }

  /**
   * Update an existing edge weight. If edge doesn't exist, will create it.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   * @param w the weight
   *
   * @throws IllegalCallerException   if the graph is not weighted
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public void setEdge(int u, int v, int w) {
    if (!weighted)
      throw new IllegalCallerException("This graph is not weighted.");
    if (!hasEdge(u, v))
      _addEdge(u, v, w);
    else {
      G[u][v] = w;

      if (!directed)
        G[v][u] = w;
    }
  }

  /**
   * Removes a vertex from the graph along with it's edges.
   *
   * @param u the vertex index to remove
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   * @throws NoSuchElementException   if the vertex doesn't exist in the graph
   */
  public void removeVertex(int u) {
    if (!hasVertex(u))
      throw new NoSuchElementException("Vertex does not exist: " + u);

    for (int i = 0, len = rows; i < len; i++) {
      if (G[u][i] != NIL) {
        G[u][i] = NIL;

        if (!directed)
          G[i][u] = NIL;

        edges--;
      }
    }

    G[u] = null;
    vertices--;
  }

  /**
   * Removes an edge from the graph.
   *
   * @param u the edge u vertex
   * @param v the edge v vertex
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   * @throws NoSuchElementException   if the edge doesn't exist in the graph
   */
  public void removeEdge(int u, int v) {
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge ("+u+", "+v+") does not exist.");

    G[u][v] = NIL;

    if (!directed)
      G[v][u] = NIL;

    edges--;
  }

  /**
   * Traces the results of an algorithm with the {@code Vertex} subclass nodes and
   * with a given start and end vertex, to return a string of the path. Passes the
   * string value so that if it runs into no path found and returns {@code null},
   * if will retrieve that value and cascade return the {@code null}. Otherwise,
   * it simply appends the string and returns it to result in the path string.
   *
   * @param N   the {@code Vertex} subclass containing the data from an algorithm
   *            to build a path
   * @param u   the start vertex
   * @param v   the end vertex
   * @param str the path string
   */
  private static String printPathAux(Vertex[] N, int u, int v, String str) {
    if (u == v)
      return str == null ? null : str + u;
    else if (N[v] == null || N[v].predecessor == -1)
      return null;
    String s = printPathAux(N, u, N[v].predecessor, str);
    return s == null ? null : s + " -> " + v;
  }

  /**
   * Returns the path string for the start and end vertices of an algorithm that
   * extends the {@link Graph.Vertex}.
   *
   * @param nodes       the {@code Vertex} subclass containing the data from an
   *                    algorithm to build a path
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static final String printPath(Vertex[] nodes, int startVertex, int endVertex) {
    String path = printPathAux(nodes, startVertex, endVertex, "");
    return path != null ? path : "No path exists from " + startVertex + " to " + endVertex;
  }

  /**
   * Traces the results of an ASPS (All-Pairs Shortest Paths) algorithm with the
   * predecessor matrix to determine the vertices for a path with the smallest
   * weight for the specified vertex indices {@code i} and {@code j}.
   *
   * @param p   the predecessor matrix results from the algorithm
   * @param i   the start vertex
   * @param j   the end vertex
   * @param str the path string
   */
  private static String printPathAux(int[][] P, int i, int j, String str) {
    if (i == j)
      return str == null ? null : str + i;
    else if (P[i][j] == Graph.NIL)
      return null;
    String s = printPathAux(P, i, P[i][j], str);
    return s == null ? null : s + " -> " + j;
  }

  /**
   * Returns the path string for a given ASPS (All-Pairs Shortest Paths) algorithm
   * with the derived predecessor matrix and the specified start and end vertices.
   *
   * @param table       the predecessor matrix of the algorithm
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static final String printPath(int[][] table, int startVertex, int endVertex) {
    checkVertex(table.length, startVertex);
    checkVertex(table.length, endVertex);
    String path = printPathAux(table, startVertex, endVertex, "");
    return path != null ? path : "No path exists from " + startVertex + " to " + endVertex;
  }

  /**
   * Uses a {@link Queue} to queue the vertices of a path from the specified start
   * and end vertices to build an array of the path of vertices. Will check if the
   * last queued item, if one exists, is not {@code -1}, which means no path
   * exists and to not queue any of the vertices.
   *
   * @param N the {@code Vertex} subclass containing the data from an algorithm to
   *          build a path
   * @param u the starting vertex of the path
   * @param v the end vertex of the path
   * @param Q the queue to hold the vertices of the path
   */
  private static void arrayPathAux(Vertex[] N, int u, int v, Queue<Integer> Q) {
    if (u == v)
      Q.enqueue(u);
    else if (N[v] == null || N[v].predecessor == -1)
      Q.enqueue(-1);
    else {
      arrayPathAux(N, u, N[v].predecessor, Q);

      if (Q.peek() != -1)
        Q.enqueue(v);
    }
  }

  /**
   * Creates an array of the vertices for the path of the specified start and end
   * vertices. If no path exists, it will return an array with a single {@code -1}
   * element.
   *
   * @param nodes       the {@code Vertex} subclass containing the data from an
   *                    algorithm to build a path
   * @param startVertex the start vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for a path, or a single {@code -1} element if
   *         no path exists
   */
  public static final int[] arrayPath(Vertex[] nodes, int startVertex, int endVertex) {
    Queue<Integer> Q = new Queue<>(nodes.length);
    int[] arr = new int[nodes.length];
    int i = 0;

    arrayPathAux(nodes, startVertex, endVertex, Q);

    if (Q.isEmpty())
      return copyOf(arr, 0);

    while (!Q.isEmpty())
      arr[i++] = Q.dequeue();
    return copyOf(arr, i);
  }

  /**
   * Used for ASPS algorithsm that produce a predecessor matrix table. Uses a
   * {@link Queue} to queue the vertices of a path from the specified start and
   * end vertices to build an array of the path of vertices. Will check if the
   * last queued item, if one exists, is not {@code -1}, which means no path
   * exists and to not queue any of the vertices.
   *
   * @param P the predecessor matrix
   * @param i the starting vertex of the path
   * @param j the end vertex of the path
   * @param Q the queue to hold the vertices of the path
   */
  private static void arrayPathAux(int[][] P, int i, int j, Queue<Integer> Q) {
    if (i == j)
      Q.enqueue(i);
    else if (P[i][j] == Graph.NIL)
      Q.enqueue(-1);
    else {
      arrayPathAux(P, i, P[i][j], Q);

    if (Q.peek() != -1)
      Q.enqueue(j);
    }
  }

  /**
   * Used for ASPS algorithms that produce a predecessor matrix table. Creates an
   * array of the vertices for the path of the specified start and end vertices.
   * If no path exists, it will return an array with a single {@code -1} element.
   *
   * @param table       the predcessor matrix
   * @param startVertex the start vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for a path, or a single {@code -1} element if
   *         no path exists
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static final int[] arrayPath(int[][] table, int startVertex, int endVertex) {
    checkVertex(table.length, startVertex);
    checkVertex(table.length, endVertex);

    Queue<Integer> Q = new Queue<>(table.length);
    int[] arr = new int[table.length];
    int i = 0;

    arrayPathAux(table, startVertex, endVertex, Q);

    if (Q.isEmpty())
      return copyOf(arr, 0);

    while (!Q.isEmpty())
      arr[i++] = Q.dequeue();
    return copyOf(arr, i);
  }

}
