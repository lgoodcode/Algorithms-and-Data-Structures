package sorting;

import java.util.function.BiFunction;

/**
 * O(n^2)
 */
public interface SelectionSort {
  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    int i, minIndex;
    T temp;

    for (i = 0; i < arr.length; i++) {
      minIndex = indexOfMinimum(arr, i, compare);
      temp = arr[minIndex];
      arr[minIndex] = arr[i];
      arr[i] = temp;
    }
  }

  private static <T> int indexOfMinimum(T[] arr, int j, BiFunction<T, T, Boolean> compare) {
    T minValue = arr[j];
    int minIndex = j;

    for (int i = minIndex + 1; i < arr.length; i++) {
      if (compare.apply(arr[i], minValue)) {
        minIndex = i;
        minValue = arr[i];
      }
    }
    return minIndex;
  }

  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }
}