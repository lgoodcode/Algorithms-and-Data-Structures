package algorithms.dynamic;

import java.util.Arrays;

/**
 * 0-1 Knapsack problem
 * 
 * <p>
 * Runs in {@code O(n W)} time, where {@code n} is the number of items and
 * {@code W} is the capacity
 * </p>
 * 
 * <p>
 * This maximizes the value of items in a fixed capacity with whole items only.
 * If fractional parts of an item are acceptable, the greedy variation is
 * preferred.
 * </p>
 * 
 * <p>
 * The initialized matrix is {@code numItems + 1} by {@code capacity + 1}. The
 * additional row and column is to initialize the {@code 0th} column and row to
 * zeroes to be able to calculate the previous and current item.
 * </p>
 */
public final class Knapsack {
  private static int TOTAL = 0;
  private static int ITEMS = 1;

  @SuppressWarnings("unchecked")
  private static <T> T run(int type, int capacity, int[] weights, int[] values) {
    if (weights.length != values.length)
      throw new IllegalArgumentException("Weights and Values array lengths don't match.");

    int n = weights.length;
    int[][] K = new int[n+1][capacity+1]; // Initialize the matrix (n+1 X capacity+1)
    int[] items;
    int a, b, i, j, k;

    // Initialize 0th row/column to zeros
    for (i = 0; i <= n; i++)
      K[i][0] = 0;
    for (i = 0; i <= capacity; i++)
      K[0][i] = 0;

    for (i = 1; i <= n; i++) {
      for (j = 1; j <= capacity; j++) {
        // If current capacity is less than item weight
        if (j < weights[i - 1])
          K[i][j] = K[i-1][j];  // Set table position with corresponding item/weight
        else {                  
          a = K[i-1][j];                              // Previous item value
          b = K[i-1][j - weights[i-1]] + values[i-1]; // Current item value plus the previous item value
          K[i][j] = a > b ? a : b;                    // Set the position with the highest value
        }
      }
    }

    if (type == TOTAL)
      return (T) Integer.valueOf(K[n][capacity]);

    // Gets the items of the resulting maximum value
    items = new int[n];
    i = n;
    j = 0;
    k = capacity;

    while (i > 0 && k > 0) {
      i--;

      if (K[i+1][k] != K[i][k]) {
        items[j++] = i;
        k -= weights[i];
      }
    }

    return (T) Arrays.copyOf(items, j);
  }

  public static int total(int capacity, int[] weights, int[] values) {
    return run(TOTAL, capacity, weights, values);
  }

  public static int[] items(int capacity, int[] weights, int[] values) {
    return run(ITEMS, capacity, weights, values);
  }

}