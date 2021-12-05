package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;

public class GraphTheory {
  /**
   * The node used to hold the attributes of the vertices for the algorithms.
   */
  protected static class Node {
    /**
     * Whether the vertex has been visited through the DFS call yet.
     */
    boolean visited;

    /**
     * The time the vertex was discovered (how many vertices it took to reach this
     * one).
     */
    int disc;

    /**
     * The time of the earliest discovered vertex to where any vertices under the
     * subtree of this vertex has a back edge.
     */
    int low;

    /**
     * The parent vertex to reach this vertex.
     */
    int parent;

    /**
     * Whether this vertex has been marked as an Articulation Point or not to
     * prevent duplicates being added in the queue to return an array of distinct
     * vertices.
     */
    boolean AP;

    Node() {
      low = Integer.MAX_VALUE;
      parent = Graph.NIL;
    }
  }

  // Prevent this class from being instantiated
  public GraphTheory() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }
}
