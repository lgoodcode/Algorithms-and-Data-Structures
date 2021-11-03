package searching;

import java.util.function.BiFunction;

/**
 * Requires the array to be sorted. O(log2 n)
 */
public interface BinarySearch {
  public static <T> int search(T[] arr, T value, int low, int high, BiFunction<T, T, Boolean> compare) {
    if (arr.length == 0 || arr.length == 1 && arr[0] != value)
      return -1;

    if (low <= high) {
      int mid = (int) Math.floor((low + high) / 2);
      T element = arr[mid];

      if (element == value)
        return mid;
      if (compare.apply(element, value))
        return search(arr, value, mid + 1, high, compare);
      return search(arr, value, low, mid - 1, compare);
    }
    return -1;
  }

  public static <T> int search(T[] arr, T val) {
    return search(arr, val, 0, arr.length - 1, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  public static <T> int searchIterative(T[] arr, T value, int low, int high, BiFunction<T, T, Boolean> compare) {
    if (arr.length == 0 || arr.length == 1 && arr[0] != value)
      return -1;

    while (low <= high) {
      int mid = (int) Math.floor((low + high) / 2);
      T element = arr[mid];

      if (element == value)
        return mid;
      if (compare.apply(element, value))
        low = mid + 1;
      else
        high = mid - 1;
    }
    return -1;
  }

  public static <T> int searchIterative(T[] arr, T val) {
    return searchIterative(arr, val, 0, arr.length - 1, (T x, T y) -> x.hashCode() < y.hashCode());
  }
}
