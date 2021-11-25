package data_structures.heaps.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.heaps.FibonacciHeap;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FibonacciHeap_Test {
  FibonacciHeap<Integer> heap;
  FibonacciHeap<String> heap2;

  @Nested
  class Instantiation {

    @Test
    void is_instantiated() {
      heap = new FibonacciHeap<>();
    }

    // @Test
    // void throws_when_instantiated_with_empty_array() {
    //   Integer[] arr = {};
    //   assertThrows(IllegalArgumentException.class, () -> {
    //     heap = new FibonacciHeap<>(arr);
    //   });
    // }

    // @Test
    // void instantiated_with_initial_values() {
    //   Integer[] values = { 1, 2, 3, 4, 5 };
    //   heap = new FibonacciHeap<>(values);

    //   assertAll(
    //     () -> assertEquals(5, heap.extractMin()),
    //     () -> assertEquals(4, heap.extractMin()),
    //     () -> assertEquals(3, heap.extractMin()),
    //     () -> assertEquals(2, heap.extractMin()),
    //     () -> assertEquals(1, heap.extractMin())
    //   );
    // }
  }

  @Nested
  class When_New {

    @BeforeEach
    void create_heap() {
      heap = new FibonacciHeap<>();
    }

    @Test
    void size() {
      assertEquals(0, heap.size());
    }

    @Test
    void is_empty() {
      assertTrue(heap.isEmpty());
    }

    @Test
    void peek_is_null() {
      assertNull(heap.peek());
    }

    @Test
    void extractMin_throws_when_empty() {
      assertThrows(NoSuchElementException.class, () -> heap.extractMin());
    }

    @Test
    void insert() {
      heap.insert(1);
      assertEquals(1, heap.extractMin());
    }

    @Test
    void empty_heap_string() {
      assertEquals("[]", heap.toString());
    }

    @Test
    void empty_heap_array() {
      assertArrayEquals(new Object[0], heap.toArray());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      heap2 = new FibonacciHeap<>();

      assertThrows(IllegalArgumentException.class, () -> heap2.insert(value));
    }

    @Test
    void iterator_is_empty() {
      Iterator<Integer> values = heap.iterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }

  }

  @Nested
  class Multiple_inserts {

    @BeforeEach
    void create_and_insert() {
      heap = new FibonacciHeap<>();

      heap.insert(4);
      heap.insert(2);
      heap.insert(5);
      heap.insert(1);
      heap.insert(3);
    }

    @Test
    void not_empty() {
      assertFalse(heap.isEmpty());
    }

    @Test
    void get_minimum() {
      assertEquals(1, heap.peek());
    }

    // @Test
    // void decreaseKey_throws_on_invalid_key() {
    //   assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(heap.peek(), 3));
    // }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals(1, heap.extractMin()),
        () -> assertEquals(2, heap.extractMin()),
        () -> assertEquals(3, heap.extractMin()),
        () -> assertEquals(4, heap.extractMin()),
        () -> assertEquals(5, heap.extractMin())
      );
    }

    @Test
    void clear() {
      heap.clear();
      assertNull(heap.peek());
      assertEquals(0, heap.size());
      assertTrue(heap.isEmpty());
    }

    @Test
    void empties_and_resets() {
      assertAll(
        () -> assertEquals(1, heap.extractMin()),
        () -> assertEquals(2, heap.extractMin()),
        () -> assertEquals(3, heap.extractMin()),
        () -> assertEquals(4, heap.extractMin()),
        () -> assertEquals(5, heap.extractMin())
      );

      assertDoesNotThrow(() -> {
        heap.insert(4);
        heap.insert(2);
        heap.insert(5);
        heap.insert(1);
        heap.insert(3);
      });
    }

    @Test
    void many_insertions() {
      heap.insert(7);
      heap.insert(6);
      heap.insert(10);
      heap.insert(11);
      heap.insert(8);
      heap.insert(9);

      assertAll(
        () -> assertEquals(1, heap.extractMin()),
        () -> assertEquals(2, heap.extractMin()),
        () -> assertEquals(3, heap.extractMin()),
        () -> assertEquals(4, heap.extractMin()),
        () -> assertEquals(5, heap.extractMin())
      );

    }

    // @Test
    // void heap_constructed_from_heap() {
    //   FibonacciHeap<Integer> q = new FibonacciHeap<>(heap);

    //   assertAll(
    //     () -> assertEquals(1, q.extractMin()),
    //     () -> assertEquals(2, q.extractMin()),
    //     () -> assertEquals(3, q.extractMin()),
    //     () -> assertEquals(4, q.extractMin()),
    //     () -> assertEquals(5, q.extractMin())
    //   );
    // }

    @Test
    void to_string() {
      assertEquals("[1, 3, 5, 4, 2]", heap.toString());
    }

    @Test
    void to_string_after_extractions() {
      assertAll(() -> {
          heap.extractMin();

          assertEquals("[2, 3, 5, 4]", heap.toString());
        },
        () -> {
          heap.extractMin();

          assertEquals("[3, 5, 4]", heap.toString());
        },
        () -> {
          heap.extractMin();

          assertEquals("[4, 5]", heap.toString());
        }
      );
    }

    @Test
    void toArray() {
      Object[] arr = { 1, 3, 5, 4, 2 };
      assertArrayEquals(arr, heap.toArray());
    }

    @Test
    void iterator() {
      Iterator<Integer> values = heap.iterator();
      assertTrue(values.hasNext());
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(1, values.next());
      assertEquals(3, values.next());
      assertEquals(5, values.next());
      assertEquals(4, values.next());
      assertEquals(2, values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void iterator_remove() {
      Iterator<Integer> values = heap.iterator();
      assertTrue(values.hasNext());
      assertEquals(1, values.next());
      assertDoesNotThrow(() -> values.remove());
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(3, values.next());
      assertDoesNotThrow(() -> values.remove());
      assertEquals(5, values.next());
      assertEquals(4, values.next());
      assertEquals(2, values.next());
      assertThrows(NoSuchElementException.class, () -> values.next());

      assertEquals(3, heap.size());

      assertEquals(2, heap.extractMin());
    }

  }

  @Nested
  class Many_Insertions {

    @Test
    void many_insertions() {
      heap = new FibonacciHeap<>();

      heap.insert(4);
      heap.insert(44);
      heap.insert(2);
      heap.insert(24);
      heap.insert(5);
      heap.insert(50);
      heap.insert(3);
      heap.insert(35);
      heap.insert(17);
      heap.insert(7);
      heap.insert(10);
      heap.insert(45);
      heap.insert(1);

      assertEquals(1, heap.extractMin());
      assertEquals(2, heap.extractMin());
      assertEquals(3, heap.extractMin());
      assertEquals(4, heap.extractMin());

      heap.insert(1);

      assertEquals(1, heap.extractMin());
      assertEquals(5, heap.extractMin());
      assertEquals(7, heap.extractMin());
      assertEquals(10, heap.extractMin());

      heap.insert(36);
      heap.insert(2);

      assertEquals(2, heap.extractMin());
      assertEquals(17, heap.extractMin());
      assertEquals(24, heap.extractMin());
      assertEquals(35, heap.extractMin());
      assertEquals(36, heap.extractMin());
      assertEquals(44, heap.extractMin());
      assertEquals(45, heap.extractMin());
      assertEquals(50, heap.extractMin());

      assertThrows(NoSuchElementException.class, () -> heap.extractMin());
    }
  }
}