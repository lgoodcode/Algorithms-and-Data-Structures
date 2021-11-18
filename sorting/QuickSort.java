package sorting;

import java.util.Comparator;
import java.util.function.BiFunction;

public final class QuickSort {
  QuickSort() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  private static <T> void sort(T[] arr, int p, int r, BiFunction<T, T, Boolean> compare) {
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

  /**
   * Quicksort the specified array with the default anonymous function that
   * compares the elements by their {@code hashCode()}.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T> void sort(T[] arr) {
    sort(arr, 0, arr.length - 1, (T x, T y) -> x.hashCode() <= y.hashCode());
  }

  /**
   * Quicksort the specified array with the specified anonymous function that
   * compares the elements to determine the sort order.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   * @param compare the {@link BiFunction} to compare elements
   */
  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    sort(arr, 0, arr.length - 1, compare);
  }

  private static <T extends Comparable<T>> void sortComparable(T[] arr, int p, int r) {
    if (p < r) {
      int q = partitionComparable(arr, p, r);
      sortComparable(arr, p, q - 1);
      sortComparable(arr, q + 1, r);
    }
  }

  private static <T extends Comparable<T>> int partitionComparable(T[] arr, int p, int r) {
    T temp, x = arr[r];
    int i = p - 1, j;

    for (j = p; j < r; j++) {
      if (arr[j].compareTo(x) == -1) {
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

  /**
   * Quicksort the specified array of elements that implement the
   * {@link Comparable} interface to determine the sorted order.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T extends Comparable<T>> void sortComparable(T[] arr) {
    sortComparable(arr, 0, arr.length - 1);
  }

  private static <T> void sort(T[] arr, int p, int r, Comparator<T> comparator) {
    if (p < r) {
      int q = partition(arr, p, r, comparator);
      sort(arr, p, q - 1, comparator);
      sort(arr, q + 1, r, comparator);
    }
  }

  private static <T> int partition(T[] arr, int p, int r, Comparator<T> comparator) {
    T temp, x = arr[r];
    int i = p - 1, j;

    for (j = p; j < r; j++) {
      if (comparator.compare(arr[j], x) == -1) {
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

  /**
   * Quicksort the specified array of elements using a {@link Comparator} to
   * determine the sorted order.
   *
   * @param <T>        the type of the elements
   * @param arr        the array to sort
   * @param comparator the {@link Comparator} to determine sorted order
   */
  public static <T> void sort(T[] arr, Comparator<T> comparator) {
    sort(arr, 0, arr.length - 1, comparator);
  }

}