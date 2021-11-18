package sorting;

import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * <h3>Insertion Sort {@code O(n^2)}</h3>
 *
 * <p>
 * Performs faster than BubbleSort for smaller arrays.
 * </p>
 */
public final class InsertionSort {
  InsertionSort() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Insertion-sort the specified array with the specified anonymous function that
   * compares the elements to determine the sort order.
   *
   * @param <T>     the type of the elements
   * @param arr     the array to sort
   * @param compare the {@link BiFunction} to compare elements
   */
  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    int i, j, len = arr.length;
    T temp;

    for (i = 1; i < len; i++) {
      for (j = i, temp = arr[i]; j > 0 && compare.apply(temp, arr[j-1]); j--)
        arr[j] = arr[j-1];
      arr[j] = temp;
    }
  }

  /**
   * Insertion-sort the specified array with the default anonymous function that
   * compares the elements by their {@code hashCode()}.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  /**
   * Insertion-sort the specified array of elements that implement the
   * {@link Comparable} interface to determine the sorted order.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T extends Comparable<T>> void sortComparable(T[] arr) {
    int i, j, len = arr.length;
    T temp;

    for (i = 1; i < len; i++) {
      for (j = i, temp = arr[i]; j > 0 && temp.compareTo(arr[j-1]) == -1; j--)
        arr[j] = arr[j-1];
      arr[j] = temp;
    }
  }

  /**
   * Insertion-sort the specified array of elements using a {@link Comparator} to
   * determine the sorted order.
   *
   * @param <T>        the type of the elements
   * @param arr        the array to sort
   * @param comparator the {@link Comparator} to determine sorted order
   */
  public static <T> void sort(T[] arr, Comparator<T> comparator) {
    int i, j, len = arr.length;
    T temp;

    for (i = 1; i < len; i++) {
      for (j = i, temp = arr[i]; j > 0 && comparator.compare(temp, arr[j-1]) == -1; j--)
        arr[j] = arr[j-1];
      arr[j] = temp;
    }
  }

}