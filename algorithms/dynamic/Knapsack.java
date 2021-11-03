package algorithms.dynamic;

import java.util.Arrays;

/**
 * The initialized matrix is {@code numItems + 1} by {@code capacity + 1}. The
 * additional row and column is to initialize the {@code 0th} column and row to
 * zeroes to be able to calculate the previous and current item.
 */
public class Knapsack {
  private static int TOTAL = 0;
  private static int ITEMS = 1;

  @SuppressWarnings("unchecked")
  private static <T> T run(int type, int capacity, int[] weights, int[] values) {
    if (weights.length != values.length)
      throw new IllegalArgumentException("Weights and Values array lengths don't match.");

    int n = weights.length;
    int[][] K = new int[n+1][capacity+1]; // Initialize the matrix (n+1 X capacity+1)
    int a, b, i, j, k, W; // W is the current capacity
    int[] items = null;

    if (type == ITEMS)
      items = new int[n];

    for (i = 0; i <= n; i++) {
      for (W = 0; W <= capacity; W++) {
        // Set the first row and column to zero
        if (i == 0 || W == 0)
          K[i][W] = 0;
        // If the items weight is less than the capacity
        else if (weights[i-1] <= W) {
          // a is the current item value plus the value of the previous item
          a = values[i-1] + K[i-1][W - weights[i-1]];
          // b is the previous item value
          b = K[i-1][W];

          K[i][W] = a > b ? a : b; // max(a,b)
        }
        // If the item exceeds the capacity, ignore it and use the previous item
        else
          K[i][W] = K[i-1][W];
      }
    }

    // Gets the items of the resulting maximum value
    i = n;
    j = 0;
    k = capacity;

    if (type == ITEMS) {
      while (i > 0 && k > 0) {
        i--;

        if (K[i+1][k] != K[i][k]) {
          items[j++] = i;
          k -= weights[i];
        }
      }
    }

    return type == TOTAL ? (T) Integer.valueOf(K[n][k]) : (T) Arrays.copyOf(items, j);
  }

  public static int total(int capacity, int[] weights, int[] values) {
    return run(TOTAL, capacity, weights, values);
  }

  public static int[] items(int capacity, int[] weights, int[] values) {
    return run(ITEMS, capacity, weights, values);
  }

}