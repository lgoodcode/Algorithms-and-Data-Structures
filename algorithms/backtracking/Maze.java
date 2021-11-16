package algorithms.backtracking;

/**
 * Solves an n x m matrix maze, where a valid move position is flagged with a
 * {@code 1} and an invalid position is {@code 0}. It recursively travels a
 * given path until either the bottom right corner is reached to complete the
 * maze, or it can no longer travel down or right. It will return up to the
 * point where it made that decision and try any other possible move until there
 * is no possible move left, in which case the maze cannot be solved.
 */
public final class Maze {
  private static int FORWARD = 0;
  private static int REVERSE = 1;

  /**
   * Recursive function to move along a given path from the specified start
   * position to the specified end position. Returns a boolean value indicating if
   * the given position has a valid move.
   *
   * <p>
   * Marks the current spot so as the function moves along the path, it marks each
   * spot with a {@code 1}. If it runs into a spot where a valid move cannot be
   * made, it will set the position as {@code 0} and backtracks, setting those
   * positions also as {@code 0}, up to the last valid position with a valid move.
   * </p>
   *
   * @param M the maze matrix
   * @param S the maze solution matrix
   * @param m the length of the x-axis of the maze
   * @param n the length of the y-axis of the maze
   * @param x the current x position
   * @param y the current y position
   * @param a the specified end x position
   * @param b the specified end y position
   * @return boolean indicating whether the move is valid or if the maze was
   *         solved or not
   */
  private static boolean backtrack(int[][] M, int[][] S, int m, int n, int x, int y, int a, int b) {
    // If we reached the end (bottom right corner), it is solved
    if (x == a && y == b) {
      S[x][y] = 1;
      return true;
    }

    // Check if the next move is a valid space
    if (x >= 0 && y >= 0 && x < m && y < n && M[x][y] != 0) {
      // This is a valid space, mark the move
      S[x][y] = 1;

      // Check the path moving right one
      if (backtrack(M, S, m, n, x + 1, y, a, b))
        return true;
      // Check the path moving down one
      if (backtrack(M, S, m, n, x, y + 1, a, b))
        return true;

      // There were no valid paths in either direction, this move will not work,
      // set it back to 0 and backtrack to a valid position
      S[x][y] = 0;
      return false;
    }

    // There is no valid move to make, backtrack to a valid position
    return false;
  }

  private static boolean reverseBacktrack(int[][] M, int[][] S, int m, int n, int x, int y, int a, int b) {
    // If we reached the end (bottom right corner), it is solved
    if (x == a && y == b) {
      S[x][y] = 1;
      return true;
    }

    // Check if the next move is a valid space
    if (x >= 0 && y >= 0 && x < m && y < n && M[x][y] != 0) {
      // This is a valid space, mark the move
      S[x][y] = 1;

      // Check the path moving right one
      if (reverseBacktrack(M, S, m, n, x - 1, y, a, b))
        return true;
      // Check the path moving down one
      if (reverseBacktrack(M, S, m, n, x, y - 1, a, b))
        return true;

      // There were no valid paths in either direction, this move will not work,
      // set it back to 0 and backtrack to a valid position
      S[x][y] = 0;
      return false;
    }

    // There is no valid move to make, backtrack to a valid position
    return false;
  }

  /**
   * The bridge method from the public method and the private recursive method
   * that actually solves the maze.
   *
   * @param maze the maze matrix
   * @param m    the length of the x-axis of the maze
   * @param n    the length of the y-axis of the maze
   * @param x    the current x position
   * @param y    the current y position
   * @param a    the specified end x position
   * @param b    the specified end y position
   * @return the solved maze matrix or {@code null} if not solvable
   *
   * @throws IllegalArgumentException if the specifed xStart or xFinish is less than 0 or
   * greater than the maze width, or if the specified yStart or yFinish is less than 0 or
   * greater than the maze heighth
   */
  private static int[][] _run(int type, int[][] maze, int m, int n, int x, int y, int a, int b) {
    int[][] solution = new int[m][n];

    if (x < 0)
      throw new IllegalArgumentException("xStart cannot be less than 0.");
    if (x >= m)
      throw new IllegalArgumentException("xStart cannot be larger then the width of maze.");
    if (y < 0)
      throw new IllegalArgumentException("yStart cannot be less than 0.");
    if (y >= m)
      throw new IllegalArgumentException("yStart cannot be larger then the heighth of maze.");
    if (a < 0)
      throw new IllegalArgumentException("xFinish cannot be less than 0.");
    if (a >= n)
      throw new IllegalArgumentException("xFinish cannot be larger then the width of maze.");
    if (b < 0)
      throw new IllegalArgumentException("yFinish cannot be less than 0.");
    if (b >= n)
      throw new IllegalArgumentException("yFinish cannot be larger then the heighth of maze.");

    if (type == FORWARD && backtrack(maze, solution, m, n, x, y, a, b))
      return solution;
    else if (type == REVERSE && reverseBacktrack(maze, solution, m, n, x, y, a, b))
      return solution;
    return null;
  }

  /**
   * Solves the given maze matrix. Starts from the top left corner (0, 0) and ends
   * at the botom right corner (n, m).
   *
   * @param maze the maze matrix to solve
   * @return the solved maze matrix or {@code} null if not solvable
   */
  public static int[][] solve(int[][] maze) {
    int m = maze.length;
    int n = maze[0].length;
    return _run(FORWARD, maze, m, n, 0, 0, m - 1, n - 1);
  }

  /**
   * Solves the given maze matrix from the specified start position and ends at
   * the bottom right corner (n, m).
   *
   * @param maze   the maze matrix
   * @param xStart the x start position
   * @param ySTart the y start position
   * @return the solved maze matrix or {@code null} if not solvable
   *
   * @throws IllegalArgumentException if the specifed xStart or is less than 0 or
   * greater than the maze width, or if the specified yStart or is less than 0 or
   * greater than the maze heighth
   */
  public static int[][] solveStart(int[][] maze, int xStart, int yStart) {
    int m = maze.length;
    int n = maze[0].length;

    return _run(FORWARD, maze, m, n, xStart, yStart, m - 1, n - 1);
  }

  /**
   * Solves the given maze matrix from the top left corner (0, 0) to the specified
   * end position.
   *
   * @param maze    the maze matrix
   * @param xFinish the x finish position
   * @param yFinish the y finish position
   * @return the solved maze matrix or {@code null} if not solvable
   *
   * @throws IllegalArgumentException if the specifed xFinish or is less than 0 or
   * greater than the maze width, or if the specified yFinish or is less than 0 or
   * greater than the maze heighth
   */
  public static int[][] solveEnd(int[][] maze, int xFinish, int yFinish) {
    int m = maze.length;
    int n = maze[0].length;

    return _run(FORWARD, maze, m, n, 0, 0, xFinish, yFinish);
  }

  /**
   * Solves the given maze matric from the specified start position to the
   * specified end position. If the specified start is greater than the finish,
   * then it will solve from right to left, instead of the default left to right.
   *
   * @param maze    the maze matrix
   * @param xStart  the x start position
   * @param yStart  the y start position
   * @param xFinish the x finish position
   * @param yFinish the y finish position
   * @return the solved matrix or {@code null} if not solvable
   *
   * @throws IllegalArgumentException if the specifed xStart or xFinish is less than 0 or
   * greater than the maze width, or if the specified yStart or yFinish is less than 0 or
   * greater than the maze heighth
   */
  public static int[][] solve(int[][] maze, int xStart, int yStart, int xFinish, int yFinish) {
    int m = maze.length;
    int n = maze[0].length;

    if (xStart <= xFinish && yStart <= yFinish)
      return _run(FORWARD, maze, m, n, xStart, yStart, xFinish, yFinish);
    return _run(REVERSE, maze, m, n, xStart, yStart, xFinish, yFinish);
  }

}
