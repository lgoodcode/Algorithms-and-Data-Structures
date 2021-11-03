package searching.__tests__;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import sorting.QuickSort;
import searching.BinarySearch;
// import searching.InterpolationSearch;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Search_Test {
  Integer[] integers = { 8, 4, 100, -4, 76, -40 };
  Integer[] integers_sorted = { -40, -4, 4, 8, 76, 100 };

  @Test
  void BinarySearch() {
    QuickSort.sort(integers);
    assertEquals(0, BinarySearch.search(integers, -40));
    assertEquals(1, BinarySearch.search(integers, -4));
    assertEquals(2, BinarySearch.search(integers, 4));
    assertEquals(3, BinarySearch.search(integers, 8));
    assertEquals(4, BinarySearch.search(integers, 76));
    assertEquals(5, BinarySearch.search(integers, 100));
  }

  @Test
  void BinarySearchIterative() {
    QuickSort.sort(integers);
    assertEquals(0, BinarySearch.search(integers, -40));
    assertEquals(1, BinarySearch.search(integers, -4));
    assertEquals(2, BinarySearch.search(integers, 4));
    assertEquals(3, BinarySearch.search(integers, 8));
    assertEquals(4, BinarySearch.search(integers, 76));
    assertEquals(5, BinarySearch.search(integers, 100));
  }

  // @Test
  // void InterpolationSearch() {
  //   QuickSort.sort(integers);
  //   assertEquals(0, InterpolationSearch.search(integers, -40));
  //   assertEquals(1, InterpolationSearch.search(integers, -4));
  //   assertEquals(2, InterpolationSearch.search(integers, 4));
  //   assertEquals(3, InterpolationSearch.search(integers, 8));
  //   assertEquals(4, InterpolationSearch.search(integers, 76));
  //   assertEquals(5, InterpolationSearch.search(integers, 100));
  // }

}
