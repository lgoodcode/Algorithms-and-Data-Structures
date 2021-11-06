package algorithms.backtracking;

public interface Maze {
  private static boolean backtrack(int[][] M, int[][] S, int m, int n, int x, int y) {
    if (x == m - 1 && y == n - 1) {
      S[x][y] = 1;
      return true;
    }

    // Check if the next move is a valid space
    if (x >= 0 && y >= 0 && x < m && y < n && M[x][y] != 0) {
      S[x][y] = 1;

      if (backtrack(M, S, m, n, x + 1, y))
        return true;
      if (backtrack(M, S, m, n, x, y + 1))
        return true;

      S[x][y] = 0;
      return false;
    }

    return false;
  }

  private static int[][] _run(int[][] maze, int m, int n) {
    int[][] solution = new int[m][n];

    if (backtrack(maze, solution, m, n, 0, 0))
      return solution;
    return null;
  }

  public static int[][] solve(int[][] maze) {
    return _run(maze, maze.length, maze[0].length);
  }

}
