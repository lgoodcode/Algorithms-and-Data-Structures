package algorithms.greedy;

import java.util.Arrays;

/**
 * Greedy top-down recursive algorithm to calculate the most events for the time
 * allowed without overlapping events.
 */
public interface ActivitySelector {
  /**
   * Greedily calculates the the maximum number of events by comparing the start
   * time of the next event with the finish times of the previously chosen event.
   * Starts at the specified index.
   *
   * @param start      the array of start times
   * @param finish     the array of finish times
   * @param startIndex the start event index
   * @return the array of event indices
   *
   * @throws IllegalArgumentException if the start and finish array lengths don't
   *                                  have equal lengths
   */
  public static int[] run(int[] start, int[] finish, int startIndex) {
    if (start.length != finish .length)
      throw new IllegalArgumentException("Start and finish time arrays don't have equal lengths.");

    int[] results = _run(new int[start.length],start, finish, startIndex, 0);
    int i = 0;

    while (i < results.length && results[i] != 0)
      i++;
    return Arrays.copyOf(results, i);
  }

  private static int[] _run(int[] arr, int[] s, int[] f, int k, int j) {
      int m = k + 1;
      int n = s.length;

      while (m < n && s[m] < f[k])  // while the iterator is less than the number of events and the start
        m++;                        //   time of the event we are on is less than the finish of the current event
                                    //   increment to the next event
      if (m < n) {
        arr[j++] = m;
        return _run(arr, s, f, m, j);
      }                    // if the iterator is less than the total number of events
      return arr;
  }

  /**
   * Greedily calculates the the maximum number of events by comparing the start
   * time of the next event with the finish times of the previously chosen event.
   *
   * @param start  the array of start times
   * @param finish the array of finish times
   * @return the array of event indices
   *
   * @throws IllegalArgumentException if the start and finish array lengths don't
   *                                  have equal lengths
   */
  public static int[] run(int start[], int[] finish) {
    return run(start, finish, 0);
  }
}
