package searching;

import java.util.function.BiFunction;

/**
 * Requires the array to be sorted. O(log2 n)
 */
public final class InterpolationSearch {
  InterpolationSearch() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  public static <T> int search(T[] arr, T value, int low, int high, BiFunction<T, T, Boolean> compare, 
BiFunction<T, T, Integer> subtract) {
    if (arr.length == 0 || arr.length == 1 && arr[0] != value)
      return -1;

      int x, y, position, delta;

       if (low <= high && compare.apply(arr[low], value) && compare.apply(value, arr[high])) {
        x = Math.max(1, subtract.apply(value, arr[low]));
        y = Math.max(1, subtract.apply(arr[high], value));
        delta = x / y;
        position = low + (int) Math.floor((high - low) * delta) % high;

        if (arr[position] == value)
          return position;
        if (compare.apply(arr[position], value))
          return search(arr, value, position + 1, high, compare, subtract);
        return search(arr, value, low, position - 1, compare, subtract);
      }
      return -1;
  }

  public static <T> int search(T[] arr, T val) {
    return search(arr, val, 0, arr.length - 1, 
      (T x, T y) -> x.hashCode() <= y.hashCode(), 
      (T x, T y) -> x.hashCode() - y.hashCode());
  }


}
