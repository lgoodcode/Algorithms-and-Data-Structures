package sorting;

import java.util.function.BiFunction;

public interface BucketSort {
  @SuppressWarnings("unchecked")
  public static <T> void sort(T[] arr, int bucketSize, BiFunction<T, T, Boolean> compare, BiFunction<T, T, Integer> subtract) {
    if (arr.length == 0)
      return;

    T[][] buckets;
    T minValue = arr[0];
    T maxValue = arr[0];
    int i, j, k, bucketCount, bucketIndex;

    for (i = 1; i < arr.length; i++) {
      if (compare.apply(arr[i], minValue))
        minValue = arr[i];
      else if (compare.apply(maxValue, arr[i]))
        maxValue = arr[i];
    }

    // Initialize buckets
    bucketCount = (int) Math.floor(subtract.apply(maxValue, minValue) / bucketSize) + 1;
    buckets = (T[][]) new Object[bucketCount][bucketSize];

    // Distribute input array values into buckets
    for (i = 0, j = 0; i < arr.length; i++, j = 0) {
      bucketIndex = (int) Math.floor(subtract.apply(arr[i], minValue) / bucketSize);

      while (buckets[bucketIndex][j] != null)
        j++;

      buckets[bucketIndex][j] = arr[i];
    }

    // Sort buckets and place back into input array
    for (i = 0, k = 0; i < bucketCount; i++) {
      // Only iterate through bucket it if it has a value
      if (buckets[i][0] != null) {
        // Only sort bucket if it has more than 1 value
        if (buckets[i][1] != null)
          InsertionSort.sort(buckets[i]);

        for (j = 0; j < bucketSize; j++) {
          if (buckets[i][j] != null)
            arr[k++] = buckets[i][j];
        }
      }
    }
  }

  public static <T> void sort(T[] arr) {
    sort(arr, 5, (T x, T y) -> x.hashCode() < y.hashCode(), (T x, T y) -> x.hashCode() - y.hashCode());
  }
}