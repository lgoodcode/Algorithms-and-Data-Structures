package algorithms.backtracking;

/**
 * Solves an 9x9 sudoku. Returns a boolean value indicating whether
 * the sudoku is solved. The sudoku solves the matrix given in-place,
 * so if the boolean result is true, the given sudoku will now be solved.
 */
public final class Sudoku {
  private static int UNASSIGNED = 0;

  /**
   * Checks if the specified number is already used in the specified row
   *
   * @param S   the sudoku matrix
   * @param row the row to check the number
   * @param num the number to check
   * @return whether the number is already used in the row or not
   */
  private static boolean usedInRow(int[][] S, int row, int num) {
    for (int col = 0; col < S.length; col++)
      if (S[row][col] == num)
        return true;
    return false;
  }

  /**
   * Checks if the specified number is already used in the specified column
   *
   * @param S   the sudoku matrix
   * @param col the column to check the number
   * @param num the number to check
   * @return whether the number is already used in the column or not
   */
  private static boolean usedInCol(int[][] S, int col, int num) {
    for (int row = 0; row < S.length; row++)
      if (S[row][col] == num)
        return true;
    return false;
  }

  /**
   * Checks if the specified number is already used in the specified 3x3 box
   *
   * @param S           the sudoku matrix
   * @param boxStartRow the start row number of the box
   * @param boxStartCol the start column number of the box
   * @param num         the number to check
   * @return whether the number is already used in the box or not
   */
  private static boolean usedInBox(int[][] S, int boxStartRow, int boxStartCol, int num) {
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        if (S[row + boxStartRow][col + boxStartCol] == num)
          return true;
      }
    }
    return false;
  }

  /**
   * Checks if the specified number is safe to use for the specified row, column,
   * and 3x3 box.
   *
   * @param S   the sudoku matrix
   * @param row the row to check if the number is okay to use on
   * @param col the column to check if the number is okay to use on
   * @param num the number to check if it is okay to use
   * @return whether the number is safe to use for the given row and column
   */
  private static boolean isSafe(int[][] S, int row, int col, int num) {
    return
      !usedInRow(S, row, num) &&
      !usedInCol(S, col, num) &&
      !usedInBox(S, row - (row % 3), col - (col % 3), num);
  }

  /**
   * Performs the recursive operations to solve the sudoku. Backtracks once a
   * number cannot be used is reached back to a point where it can attempt to try
   * another number.
   *
   * @param S the sudoku to solve
   * @return whether the sudoku was solved or not
   */
  private static boolean _solve(int[][] S) {
    int m = S.length;
    int n = S[0].length;
    int num, row = 0, col = 0;
    boolean blankSpaces = false;

    for (row = 0; row < m; row++) {
      for (col = 0; col < n; col++) {
        if (S[row][col] == UNASSIGNED) {
          blankSpaces = true;
          break;
        }
      }

      if (blankSpaces == true)
        break;
    }

    if (blankSpaces == false)
      return true;

    for (num = 1; num <= 9; num++) {
      if (isSafe(S, row, col, num)) {
        S[row][col] = num;

        if (_solve(S))
          return true;

        S[row][col] = UNASSIGNED;
      }
    }

    return false;
  }

  /**
   * Recursively solves the specified sudoku via backtracking. Returns a boolean
   * value indicating whether the sudoku was solved or not. It solves the sudoku
   * matrix in-place, not using additional memory for a copy.
   *
   * @param sudoku the sudoku matrix to solve
   * @return whether the sudoku is solvable or not
   *
   * @throws IllegalArgumentException if the sudoku isn't a 9x9 matrix
   */
  public static boolean solve(int[][] sudoku) {
    if (sudoku.length != 9 || sudoku[0].length != 9 )
      throw new IllegalArgumentException("Invalid sudoku given. Must be a 9x9 matrix.");
    return _solve(sudoku);
  }
}
