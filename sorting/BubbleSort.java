package sorting;

import java.util.function.BiFunction;

public interface BubbleSort {
  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    int i, j, end, len = arr.length - 1;
    T temp;

    for (i = 0; i < len; i++) {
      for (j = 0, end = len - i; j < end; j++) {
        if (compare.apply(arr[j + 1], arr[j])) {
          temp = arr[j + 1];
          arr[j + 1] = arr[j];
          arr[j] = temp;
        }
      }
    }
  }

  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

}