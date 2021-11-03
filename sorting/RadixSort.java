package sorting;

import java.util.function.BiFunction;

public interface RadixSort {
  public static <T> void sort(T[] arr, int radix, BiFunction<T, T, Boolean> compare, BiFunction<T, T, Integer> subtract) {
    if (arr.length == 0)
      return;

    T minValue = arr[0];
    T maxValue = arr[0];
    int exp = 1;

    for (int i = 1; i < arr.length; i++) {
      if (compare.apply(arr[i], minValue))
        minValue = arr[i];
      else if (compare.apply(maxValue, arr[i]))
        maxValue = arr[i];
    }

    // Perform counting sort on each digit, starting from the least significant digit
    while(subtract.apply(maxValue, minValue) / exp >= 1) {
      countSortByDigit(arr, radix, exp, minValue, subtract);
      exp *= radix;
    }

  }

  @SuppressWarnings("unchecked")
  private static <T> void countSortByDigit(T[] arr, int radix, int exp, T minValue, BiFunction<T, T, Integer> subtract) {
    T[] output = (T[]) new Object[arr.length];
    int[] buckets = new int[radix];
    int i, bucketIndex;

    // Initialize buckets
    for (i = 0; i < radix; i++)
      buckets[i] = 0;

    // Count frequencies
    for (i = 0; i < arr.length; i++) {
      bucketIndex = (int) Math.floor((subtract.apply(arr[i], minValue) / exp) % radix);
      buckets[bucketIndex]++;
    }

    // Compute cumulates
    for (i = 1; i < radix; i++)
      buckets[i] += buckets[i - 1];

    // Move records
    for (i = arr.length - 1; i >= 0; i--) {
      bucketIndex = (int) Math.floor((subtract.apply(arr[i], minValue) / exp) % radix);
      output[--buckets[bucketIndex]] = arr[i];
    }

    // Copy back
    for (i = 0; i < arr.length; i++)
      arr[i] = output[i];
  }

  public static <T> void sort(T[] arr) {
    sort(arr, 10, (T x, T y) -> x.hashCode() < y.hashCode(), (T x, T y) -> x.hashCode() - y.hashCode());
  }
}