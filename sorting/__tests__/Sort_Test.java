package sorting.__tests__;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import sorting.BubbleSort;
import sorting.InsertionSort;
import sorting.QuickSort;
import sorting.MergeSort;
import sorting.SelectionSort;
import sorting.RadixSort;
import sorting.BucketSort;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Sort_Test {
  Integer[] integers = { 8, 4, 100, -4, 76, -40 };
  Integer[] integers_sorted = { -40, -4, 4, 8, 76, 100 };

  @Test
  void BubbleSort() {
    BubbleSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

  @Test
  void InsertionSort() {
    InsertionSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

  @Test
  void QuickSort() {
    QuickSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

  @Test
  void MergeSort() {
    assertArrayEquals(integers_sorted, MergeSort.sort(integers));
  }

  @Test
  void SelectionSort() {
    SelectionSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }
  
  @Test
  void RadixSort() {
    RadixSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

  @Test
  void BucketSort() {
    BucketSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

}
