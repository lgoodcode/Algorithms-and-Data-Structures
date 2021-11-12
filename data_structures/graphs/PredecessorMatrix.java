package data_structures.graphs;

public class PredecessorMatrix {
  public int[][] P;

  public PredecessorMatrix(int rows) {
    this.P = new int[rows][rows];
  }
}
