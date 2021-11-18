package sorting.__tests__;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Comparator;

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

  @Nested
  class BubbleSorts {
    @Test
    void bubbleSort() {
      BubbleSort.sort(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void bubbleSort_comparable() {
      BubbleSort.sortComparable(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void bubbleSort_comparator() {
      BubbleSort.sort(integers, Comparator.naturalOrder());
      assertArrayEquals(integers_sorted, integers);
    }
  }

  @Nested
  class InsertionSorts {
    @Test
    void insertionSort() {
      InsertionSort.sort(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void insertionSort_comparable() {
      InsertionSort.sortComparable(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void insertionSort_comparator() {
      InsertionSort.sort(integers, Comparator.naturalOrder());
      assertArrayEquals(integers_sorted, integers);
    }
  }

  @Nested
  class QuickSorts {
    @Test
    void quickSort() {
      QuickSort.sort(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void quickSort_comparable() {
      QuickSort.sortComparable(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void quickSort_comparator() {
      QuickSort.sort(integers, Comparator.naturalOrder());
      assertArrayEquals(integers_sorted, integers);
    }
  }

  @Nested
  class MergeSorts {
    @Test
    void mergeSort() {
      MergeSort.sort(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void mergeSort_comparable() {
      MergeSort.sortComparable(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void mergeSort_comparator() {
      MergeSort.sort(integers, Comparator.naturalOrder());
      assertArrayEquals(integers_sorted, integers);
    }
  }

  @Nested
  class SelectionSorts {
    @Test
    void selectionSort() {
      SelectionSort.sort(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void selectionSort_comparable() {
      SelectionSort.sortComparable(integers);
      assertArrayEquals(integers_sorted, integers);
    }
  
    @Test
    void selectionSort_comparator() {
      SelectionSort.sort(integers, Comparator.naturalOrder());
      assertArrayEquals(integers_sorted, integers);
    }
  }
  
  @Test
  void radixSort() {
    RadixSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

  @Test
  void bucketSort() {
    BucketSort.sort(integers);
    assertArrayEquals(integers_sorted, integers);
  }

}
