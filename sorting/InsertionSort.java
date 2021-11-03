package sorting;

import java.util.function.BiFunction;

/**
 * Performs faster than BubbleSort for smaller arrays
 */
public interface InsertionSort {
  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    int i, j, len = arr.length;
    T temp;

    for (i = 1; i < len; i++) {
      for (j = i, temp = arr[i]; j > 0 && compare.apply(temp, arr[j-1]); j--)
        arr[j] = arr[j-1];
      arr[j] = temp;
    }
  }

  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

}