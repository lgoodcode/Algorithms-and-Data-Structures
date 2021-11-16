package algorithms.dynamic;

import static java.util.Arrays.copyOf;

/**
 * 0-1 Knapsack problem
 * 
 * <p>
 * Runs in {@code O(n W)} time, where {@code n} is the number of items and
 * {@code W} is the capacity
 * </p>
 * 
 * <p>
 * The weights must be sorted from lowest to highest.
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
  private static int[][] run(int capacity, int[] weights, int[] values) {
    if (weights.length != values.length)
      throw new IllegalArgumentException("Weights and Values array lengths don't match.");

    int n = weights.length;
    int[][] K = new int[n+1][capacity+1]; // Initialize the matrix (n+1 X capacity+1)
    int a, b, i, j;

    for (i = 1; i <= n; i++) {
      for (j = 1; j <= capacity; j++) {
        // If current capacity is less than item weight
        if (j < weights[i-1])
          K[i][j] = K[i-1][j];  // Set table position with corresponding item/weight
        else {                  
          a = K[i-1][j];                              // Previous item value
          b = K[i-1][j - weights[i-1]] + values[i-1]; // Current item value plus the previous item value
          K[i][j] = a > b ? a : b;                    // Set the position with the highest value
        }
      }
    }

    return K;
  }

  public static int total(int capacity, int[] weights, int[] values) {
    int[][] K = run(capacity, weights, values);
    return K[weights.length][capacity];
  }

  public static int[] items(int capacity, int[] weights, int[] values) {
    int[][] K = run(capacity, weights, values);
    int[]  items = new int[weights.length];
    int i = weights.length;
    int j = 0;
    int k = capacity;

    while (i > 0 && k > 0) {
      i--;

      if (K[i+1][k] != K[i][k]) {
        items[j++] = i;
        k -= weights[i];
      }
    }

    return copyOf(items, j);  
  }

}