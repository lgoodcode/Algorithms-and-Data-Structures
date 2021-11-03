package algorithms.greedy;

import java.util.Arrays;

public final class FractionalKnapsack {
  private static int TOTAL = 0;
  private static int ITEMS = 1;

  @SuppressWarnings("unchecked")
  private static <T> T run(int type, int capacity, int[] weights, int[] values) {
    if (weights.length != values.length)
      throw new IllegalArgumentException("Weights and Values array lengths don't match.");

    int[] items = null;
    int len = weights.length;
    int j = 0, load = 0, total = 0;
    double r;

    if (type == ITEMS)
      items = new int[len];

    // Iterate through the items while the current load is less than specified capacity
    for (int i = 0; i < len && load < capacity; i++) {
      // If weight of item doesn't exceed capacity; add it
      if (weights[i] <= capacity - load) {
        load += weights[i];

        if (type == TOTAL)
          total += values[i];
        else
          items[j++] = i;
      }
      // Otherwise, if we don't have enough capacity left for the item
      else {
        // Get the amount of weight for the item we can use and add it
        r =  ((double) capacity - load) / weights[i];
        load +=  r * weights[i];

        if (type == TOTAL)
          total += r * values[i];
        else
          items[j++] = i;
      }
    }

    return type == TOTAL ? (T) Integer.valueOf(total) : (T) Arrays.copyOf(items, j);
  }

  public static int total(int capacity, int[] weights, int[] values) {
    return run(TOTAL, capacity, weights, values);
  }

  public static int[] items(int capacity, int[] weights, int[] values) {
    return run(ITEMS, capacity, weights, values);
  }

}