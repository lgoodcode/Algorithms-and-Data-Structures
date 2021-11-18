package sorting;

import java.util.function.BiFunction;
import static java.util.Arrays.copyOfRange;

import java.util.Comparator;

/**
 * <h3>MergeSort {@code O(n log n)}</h3>
 * 
 * <p>
 * Performs a split operation until a base case of array length of {@code 1} is
 * reached which takes {@code O(log n)} operations. Then, on each split array,
 * performs the sorting operation on each element which in total, will result in
 * {@code O(n)} operations. Therefore, it results in an amortized cost of
 * {@code O(n log n)}.
 * </p>
 * 
 */
public final class MergeSort {
  MergeSort() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  public static <T> void sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    if (arr.length == 1)
      return;

    T[] left = copyOfRange(arr, 0, arr.length / 2);
    T[] right = copyOfRange(arr, arr.length / 2, arr.length);

    sort(left, compare);
    sort(right, compare);
    
    merge(arr, left, right, compare);
  }

  private static <T> void merge(T[] sorted, T[] left, T[] right, BiFunction<T, T, Boolean> compare) {
    int sortedIndex = 0, leftIndex = 0, rightIndex = 0;

    while (leftIndex < left.length && rightIndex < right.length) {
      if (compare.apply(right[rightIndex], left[leftIndex]))
        sorted[sortedIndex++] = right[rightIndex++];
      else
        sorted[sortedIndex++] = left[leftIndex++];
    } 

    while (leftIndex != left.length)
      sorted[sortedIndex++] = left[leftIndex++];;
    while (rightIndex != right.length)
      sorted[sortedIndex++] = right[rightIndex++];;
  }

  public static <T> void sort(T[] arr) {
    sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

  /**
   * Mergesort the specified array of elements that implement the
   * {@link Comparable} interface to determine the sorted order.
   * 
   * @param <T> the type of the elements
   * @param arr the array to sort
   */
  public static <T extends Comparable<T>> void sortComparable(T[] arr) {
    if (arr.length == 1)
      return;

    T[] left = copyOfRange(arr, 0, arr.length / 2);
    T[] right = copyOfRange(arr, arr.length / 2, arr.length);

    sortComparable(left);
    sortComparable(right);
    
    merge(arr, left, right);
  }

  private static <T extends Comparable<T>> void merge(T[] sorted, T[] left, T[] right) {
    int sortedIndex = 0, leftIndex = 0, rightIndex = 0;

    while (leftIndex < left.length && rightIndex < right.length) {
      if (right[rightIndex].compareTo(left[leftIndex]) == -1)
        sorted[sortedIndex++] = right[rightIndex++];
      else
        sorted[sortedIndex++] = left[leftIndex++];
    } 

    while (leftIndex != left.length)
      sorted[sortedIndex++] = left[leftIndex++];;
    while (rightIndex != right.length)
      sorted[sortedIndex++] = right[rightIndex++];;
  }

  /**
   * Quicksort the specified array of elements that implement the
   * {@link Comparator} interface to determine the sorted order.
   * 
   * @param <T>        the type of the elements
   * @param arr        the array to sort
   * @param comparator the {@link Comparator} to determine sorted order
   */
  public static <T> void sort(T[] arr, Comparator<T> comparator) {
    if (arr.length == 1)
      return;

    T[] left = copyOfRange(arr, 0, arr.length / 2);
    T[] right = copyOfRange(arr, arr.length / 2, arr.length);

    sort(left, comparator);
    sort(right, comparator);
    
    merge(arr, left, right, comparator);
  }

  private static <T> void merge(T[] sorted, T[] left, T[] right, Comparator<T> comparator) {
    int sortedIndex = 0, leftIndex = 0, rightIndex = 0;

    while (leftIndex < left.length && rightIndex < right.length) {
      if (comparator.compare(right[rightIndex], left[leftIndex]) == -1)
        sorted[sortedIndex++] = right[rightIndex++];
      else
        sorted[sortedIndex++] = left[leftIndex++];
    } 

    while (leftIndex != left.length)
      sorted[sortedIndex++] = left[leftIndex++];;
    while (rightIndex != right.length)
      sorted[sortedIndex++] = right[rightIndex++];;
  }

}