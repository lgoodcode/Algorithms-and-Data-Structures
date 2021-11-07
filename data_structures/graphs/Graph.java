package data_structures.graphs;

import java.util.Arrays;
import java.util.NoSuchElementException;

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
  public final boolean directed;

  /**
   * Whether the graph edges are weighted or not. If so, the edges will contain
   * the weight value.
   */
  public final boolean weighted;

  /**
   * The length and width of the graph.
   */
  public final int rows;

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
  public static final int NIL = Integer.MIN_VALUE;

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
   * Checks that the specified vertex index is valid.
   *
   * @param vertex the vertex index
   *
   * @throws IllegalArgumentException if the vertix is negative or greater than
   *                                  graph length
   */
  private void checkVertex(int vertex) {
    if (vertex < 0)
      throw new IllegalArgumentException("Vertex cannot be negative.");
    if (vertex >= rows)
      throw new IllegalArgumentException("Vertex cannot be greater than graph length.");
  }

  /**
   * Checks that the specified weight is not negative.
   *
   * @param weight the weight
   *
   * @throws IllegalArgumentException if the weight is negative
   */
  private void checkWeight(int weight) {
    if (weight < 0)
      throw new IllegalArgumentException("Weight cannot be negative.");
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
   * the index of each vertex.
   *
   * @return the array of vertices indices
   */
  public int[] getVertices() {
    int[] V = new int[rows];
    int i, j = 0;

    for (i = 0; i < V.length; i++)
      if (G[i] != null)
        V[j++] = i;
    return Arrays.copyOf(V, j);
  }

  /**
   * Returns a matrix of the graph edges. Each vertex index array will be the
   * length of the number of edges that vertex has with the respective vertices.
   *
   * <p>
   * If the graph is weighted, it will simply return the graph matrix.
   * </p>
   *
   * @return the edge or graph matrix, if weighted
   */
  public int[][] getEdges() {
    if (weighted)
      return G;

    int[][] E = new int[rows][];
    int i, j, k = 0;

    // Iterate through each vertex
    for (i = 0; i < E.length; i++) {
      if (G[i] != null) {
        E[i] = new int[rows];

        // Iterate through each edge of the vertex
        for (j = 0; j < E.length; j++) {
          if (G[i][j] != NIL)
            E[i][k++] = j;
        }

        // Set edge array of vertex i to be the length of the number of edges
        E[i] = Arrays.copyOf(E[i], k);
      }
    }

    return E;
  }

  /**
   * Retrieves the weight for an edge.
   *
   * @param u the x vertex index
   * @param v the y vertex index
   * @return the edge weight
   *
   * @throws IllegalCallerException   if the graph is not weighted
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public int getEdgeWeight(int u, int v) {
    if (!weighted)
      throw new IllegalCallerException("This graph is not weighted.");
    checkVertex(u);
    checkVertex(v);
    return G[u] != null && G[u][v] != NIL ? G[u][v] : NIL;
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
   * @param u the x vertex index of the edge
   * @param v the y vertex index of the edge
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
   * Adds a specified vertex index to the graph.
   *
   * @param v the vertex index to add
   * @return whether the vertex is in the graph or not
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
   * weight, if applicable.
   *
   * @param u the x vertex index
   * @param v the y vertex index
   * @param w the edge weight
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  private void _addEdge(int u, int v, int w) {
    if (!hasVertex(u))
      addVertex(u);
    if (!hasVertex(v))
      addVertex(v);

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
   * Adds an edge to the graph.
   *
   * @param u the x vertex index
   * @param v the y vertex index
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length, or the edge already exists
   *                                  in the graph
   */
  public void addEdge(int u, int v) {
    checkVertex(u);
    checkVertex(v);

    if (G[u] != null && G[u][v] != NIL)
      throw new IllegalArgumentException("Edge already exists in graph.");

    _addEdge(u, v, 1);
  }

  /**
   * Adds an edge to the graph with the specified weight.
   *
   * @param u the x vertex index
   * @param v the y vertex index
   * @param w the weight
   *
   * @throws IllegalCallerException   if the graph is not weighted
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length, or the edge already exists
   *                                  in the graph, or the weight is negative
   */
  public void addEdge(int u, int v, int w) {
    if (!weighted)
      throw new IllegalCallerException("This graph is not weighted.");
    if (G[u] != null && G[u][v] != NIL)
      throw new IllegalArgumentException("Edge already exists in graph.");

    checkVertex(u);
    checkVertex(v);
    checkWeight(w);

    _addEdge(u, v, w);
  }

  /**
   * Update an existing edge weight. If edge doesn't exist, will set it.
   *
   * @param u the x vertex index
   * @param v the y vertex index
   * @param w the weight
   *
   * @throws IllegalCallerException   if the graph is not weighted
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length, or the edge already exists
   *                                  in the graph, or the weight is negative
   */
  public void setEdge(int u, int v, int w) {
    if (!weighted)
      throw new IllegalCallerException("This graph is not weighted.");

    checkVertex(u);
    checkVertex(v);
    checkWeight(w);

    if (G[u] == null || G[u][v] == NIL)
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
   * @param v the vertex index to remove
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   * @throws NoSuchElementException   if the vertex doesn't exist in the graph
   */
  public void removeVertex(int v) {
    checkVertex(v);

    if (!hasVertex(v))
      throw new NoSuchElementException("Vertex does not exist: " + v);

    for (int i = 0, len = rows; i < len; i++) {
      if (G[v][i] != NIL) {
        if (!directed)
          G[i][v] = NIL;
        edges--;
      }
    }

    G[v] = null;
    vertices--;
  }

  /**
   * Removes an edge from the graph.
   *
   * @param u the edge x vertex
   * @param v the edge y vertex
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   * @throws NoSuchElementException   if the edge doesn't exist in the graph
   */
  public void removeEdge(int u, int v) {
    checkVertex(u);
    checkVertex(v);

    if (G[u] == null || G[u][v] == NIL)
      throw new NoSuchElementException("Edge does not exist: ("+u+", "+v+")");

    G[u][v] = NIL;

    if (!directed)
      G[v][u] = NIL;

    edges--;
  }
}
