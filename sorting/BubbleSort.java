package sorting;

import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * 
 */
public final class BubbleSort {
  BubbleSort() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Bubblesorts the specified array with the specified anonymous function that
   * compares the elements to determine the sort order.
   *
   * @param <T>     the type of the elements
   * @param arr     the array to sort
   * @param compare the {@link BiFunction} to compare elements
   */
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

  /**
   * Bubblesorts the specified array with the default anonymous function that
   * compares the elements by their {@code hashCode()}.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  /**
   * Bubblesorts the specified array of elements that implement the
   * {@link Comparable} interface to determine the sorted order.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T extends Comparable<T>> void sortComparable(T[] arr) {
    int i, j, end, len = arr.length - 1;
    T temp;

    for (i = 0; i < len; i++) {
      for (j = 0, end = len - i; j < end; j++) {
        if (arr[j + 1].compareTo(arr[j]) == -1) {
          temp = arr[j + 1];
          arr[j + 1] = arr[j];
          arr[j] = temp;
        }
      }
    }
  }

  /**
   * Bubblesorts the specified array of elements using a {@link Comparator} to
   * determine the sorted order.
   *
   * @param <T>        the type of the elements
   * @param arr        the array to sort
   * @param comparator the {@link Comparator} to determine sorted order
   */
  public static <T> void sort(T[] arr, Comparator<T> comparator) {
    int i, j, end, len = arr.length - 1;
    T temp;

    for (i = 0; i < len; i++) {
      for (j = 0, end = len - i; j < end; j++) {
        if (comparator.compare(arr[j + 1], arr[j]) == -1) {
          temp = arr[j + 1];
          arr[j + 1] = arr[j];
          arr[j] = temp;
        }
      }
    }
  }

}