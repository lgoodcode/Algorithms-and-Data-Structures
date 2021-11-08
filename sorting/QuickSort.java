package sorting;

import java.util.function.BiFunction;

public interface QuickSort {
  public static <T> void sort(T[] arr, int p, int r, BiFunction<T, T, Boolean> compare) {
    if (p < r) {
      int q = partition(arr, p, r, compare);
      sort(arr, p, q - 1, compare);
      sort(arr, q + 1, r, compare);
    }
  }

  private static <T> int partition(T[] arr, int p, int r, BiFunction<T, T, Boolean> compare) {
    T temp, x = arr[r];
    int i = p - 1, j;

    for (j = p; j < r; j++) {
      if (compare.apply(arr[j], x)) {
        i++;
        temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
      }
    }
    i++;
    temp = arr[i];
    arr[i] = arr[r];
    arr[r] = temp;

    return i;
  }

  public static <T> void sort(T[] arr) {
    sort(arr, 0, arr.length - 1, (T x, T y) -> x.hashCode() <= y.hashCode());
  }

  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    sort(arr, 0, arr.length - 1, compare);
  }

}