package sorting;

import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * O(n^2)
 */
public final class SelectionSort {
  SelectionSort() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Selection-sort the specified array with the specified anonymous function that
   * compares the elements to determine the sort order.
   *
   * @param <T>     the type of the elements
   * @param arr     the array to sort
   * @param compare the {@link BiFunction} to compare elements
   */
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

  /**
   * Selection-sort the specified array with the default anonymous function that
   * compares the elements by their {@code hashCode()}.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  /**
   * Selection-sort the specified array of elements that implement the
   * {@link Comparable} interface to determine the sorted order.
   *
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T extends Comparable<T>> void sortComparable(T[] arr) {
    int i, minIndex;
    T temp;

    for (i = 0; i < arr.length; i++) {
      minIndex = indexOfMinimum(arr, i);
      temp = arr[minIndex];
      arr[minIndex] = arr[i];
      arr[i] = temp;
    }
  }

  private static <T extends Comparable<T>> int indexOfMinimum(T[] arr, int j) {
    T minValue = arr[j];
    int minIndex = j;

    for (int i = minIndex + 1; i < arr.length; i++) {
      if (arr[i].compareTo(minValue) == -1) {
        minIndex = i;
        minValue = arr[i];
      }
    }
    return minIndex;
  }

  /**
   * Selection-sort the specified array of elements using a {@link Comparator} to
   * determine the sorted order.
   *
   * @param <T>        the type of the elements
   * @param arr        the array to sort
   * @param comparator the {@link Comparator} to determine sorted order
   */
  public static <T> void sort(T[] arr, Comparator<T> comparator) {
    int i, minIndex;
    T temp;

    for (i = 0; i < arr.length; i++) {
      minIndex = indexOfMinimum(arr, i, comparator);
      temp = arr[minIndex];
      arr[minIndex] = arr[i];
      arr[i] = temp;
    }
  }

  private static <T> int indexOfMinimum(T[] arr, int j, Comparator<T> comparator) {
    T minValue = arr[j];
    int minIndex = j;

    for (int i = minIndex + 1; i < arr.length; i++) {
      if (comparator.compare(arr[i], minValue) == -1) {
        minIndex = i;
        minValue = arr[i];
      }
    }
    return minIndex;
  }
}