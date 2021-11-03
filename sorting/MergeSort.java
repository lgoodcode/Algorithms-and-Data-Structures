package sorting;

import java.util.function.BiFunction;

/**
 * Returns the sorted array, does not do in-place sorting
 */
public interface MergeSort {
  @SuppressWarnings("unchecked")
  public static <T> T[] sort(T[] arr, BiFunction<T, T, Boolean> compare) {
    if (arr.length == 1)
      return arr;

    int middle = (int) Math.ceil(arr.length / 2);
    T[] left = (T[]) new Object[middle];
    T[] right = (T[]) new Object[arr.length - middle];

    System.arraycopy(arr, 0, left, 0, middle);
    System.arraycopy(arr, middle, right, 0, arr.length - middle);

    left = sort(left, compare);
    right = sort(right, compare);
    
    return merge(left, right, compare);
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] merge(T[] left, T[] right, BiFunction<T, T, Boolean> compare) {
    T[] sorted = (T[]) new Object[left.length + right.length];
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

    return sorted;
  }

  public static <T> T[] sort(T[] arr) {
    return sort(arr, (T x, T y) -> x.hashCode() < y.hashCode());
  }

}