package algorithms.dynamic;

public interface MatrixChainOrder {
  static int MATRICES = 0;
  static int SOLUTION = 1;

  public static int[][] table(int type, int[] matrices) {
    int i, j, k, q, r, n = matrices.length;
    int[][] m = new int[n+1][n+1];
    int[][] s = new int[n+1][n+1];  // Auxiliary table to print solution

    for (i = 2; i < n; i++) {   // i is chain length
      for (j = 1; j < (n - i) + 1; j++) {
        q = (j + i) - 1;
        m[j][q] = Integer.MAX_VALUE;

        for (k = j; k <= q - 1; k++) {
          // r = cost/scalar multiplications
          r = m[j][k] + m[k + 1][q] + ((matrices[j - 1] * matrices[k]) * matrices[q]);

          if (r < m[j][q]) {
            m[j][q] = r;
            s[j][q] = k; // s[j,q] = Second auxiliary table that stores k
          }
        }
      }
    }

    return type == MATRICES ? m : s;
  }

  // returns the number of multiplications for the optimal solution
  public static int compute(int[] matrices) {
    int[][] table = table(MATRICES, matrices);
    return table[1][matrices.length - 1];
  }

  public static void printTableSolution(int[][] table) {
    int n = table.length;
    String s = "";

    for (int i=0; i < n; i++) {
      for (int j=0; j < n; j++)
        s += table[i][j] == 0 ? "null, " : (table[i][j] + ", ");
      s += "\n";
    }

    System.out.println(s);
  }

  public static void printTableSolution(int[] matrices) {
    int[][] table = table(SOLUTION, matrices);
    printTableSolution(table);
  }

}
