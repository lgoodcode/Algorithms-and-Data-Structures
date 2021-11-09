package data_structures.heaps.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import data_structures.heaps.MinHeap;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MinHeap_Test {
  MinHeap<Integer> heap;
  MinHeap<String> heap2;

  @Nested
  class Instantiation {

    @Test
    void is_instantiated() {
      heap = new MinHeap<>(10);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0 })
    void throws_when_size_is_invalid(int size) {
      assertThrows(IllegalArgumentException.class, () -> new MinHeap<>(size));
    }

    // @Test 
    // void throws_when_instantiated_with_empty_array() {
    //   Integer[] arr = {};
    //   assertThrows(IllegalArgumentException.class, () -> {
    //     heap = new MinHeap<>(arr);
    //   });
    // }

    // @Test
    // void instantiated_with_initial_values() {
    //   Integer[] values = { 1, 2, 3, 4, 5 };
    //   heap = new MinHeap<>(values);

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
    int size = 5;

    @BeforeEach
    void create_heap() {
      heap = new MinHeap<>(size);
    }

    @Test 
    void size() {
      assertEquals(0, heap.size());
    }

    @Test
    void capacity() {
      assertEquals(5, heap.capacity());
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
    void insert() {
      heap.insert(1);
      assertEquals(1, heap.extractMin());
    }

    @Test
    void empty_heap_string() {
      assertEquals("{}", heap.toString());
    }

    @Test
    void extractMin_throws_when_empty() {
      assertThrows(NoSuchElementException.class, () -> heap.extractMin());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String key) {
      heap2 = new MinHeap<>(size);

      assertThrows(IllegalArgumentException.class, () -> heap2.insert(key));
    }

  }

  @Nested
  class Multiple_inserts {
    int size = 5;

    @BeforeEach
    void create_and_insert() {
      heap = new MinHeap<>(size);

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
    void peek() {
      assertEquals(1, heap.peek());
    }

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
    void insert_throws_when_full() {
      assertThrows(IllegalStateException.class, () -> heap.insert(6));
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
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);
        heap.insert(5);
      });
    }

    @Test
    void heap_constructed_from_heap() {
      MinHeap<Integer> q = new MinHeap<>(heap);

      assertAll(
        () -> assertEquals(1, q.extractMin()),
        () -> assertEquals(2, q.extractMin()),
        () -> assertEquals(3, q.extractMin()),
        () -> assertEquals(4, q.extractMin()),
        () -> assertEquals(5, q.extractMin())
      );
    }

    @Test
    void to_string() {
      assertEquals("{"
          + "\n\"1\""  
          + "\n\"2\""  
          + "\n\"3\""  
          + "\n\"4\""  
          + "\n\"5\""
          + "\n}", 
        heap.toString()
      );
    }
  
  }
}