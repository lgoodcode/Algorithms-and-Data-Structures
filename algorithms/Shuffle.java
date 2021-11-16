package algorithms;

import java.util.Random;
import static java.util.Arrays.copyOf;

/**
 * Famous Fisher-Yates shuffle algorithm
 *
 * <p>
 * Iterates each position of the array, starting with its last position and
 * swapping the current position with a random position. The random position is
 * lesser than the current position; this way, the algorithm makes sure the
 * positions already shuffled will not be shuffled again (the more we shuffle a
 * deck of cards, the worse is the shuffle).
 * </p>
 */
public final class Shuffle {
  /**
   * Performs an in-place shuffle of the specified array up to the specified
   * index.
   *
   * @param <T>    the type of the array to shuffle
   * @param arr    the array to shuffle
   * @param length the maximum length to begin the shuffle
   */
  public static <T> void apply(T[] arr, int length) {
    Random rand = new Random();
    T temp;
    int i, j;

    for (i = length; i > 0; i--) {
      j = rand.nextInt(i);
      temp = arr[j];
      arr[j] = arr[i-1];
      arr[i-1] = temp;
    }
  }

  /**
   * Performs an in-place shuffle of the entire specified array.
   *
   * @param <T> the type of the array to shuffle
   * @param arr the array to shuffle
   */
  public static <T> void apply(T[] arr) {
    apply(arr, arr.length);
  }

  /**
   * Makes a copy of the specified array, shuffles it, and then returns the copy,
   * retaining the original array state.
   *
   * @param <T> the type of the array to shuffle
   * @param arr the array to shuffle
   * @returns the shuffled copy of the array
   */
  public static <T> T[] copy(T[] arr) {
    T[] newArr = copyOf(arr, arr.length);
    apply(newArr, arr.length);
    return newArr;
  }

  /**
   * Makes a copy of the specified array, shuffles it up to the specified index,
   * and then returns the copy, retaining the original array state.
   *
   * @param <T>    the type of the array to shuffle
   * @param arr    the array to shuffle
   * @param length the maximum index of the array to shuffle up to
   * @returns the shuffled copy of the array
   */
  public static <T> T[] copy(T[] arr, int length) {
    T[] newArr = copyOf(arr, arr.length);
    apply(newArr, length);
    return newArr;
  }

}
