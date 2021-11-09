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
    void minimum_is_null() {
      assertNull(heap.getMin());
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
      assertEquals("{}", heap.toString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      heap2 = new FibonacciHeap<>();

      assertThrows(IllegalArgumentException.class, () -> heap2.insert(value));
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
      assertEquals(1, heap.getMin().getItem());
    }

    @Test
    void decreaseKey_throws_on_invalid_key() {
      assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(heap.getMin(), 3));
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
      assertEquals("{"
          + "\n\"Item: 1\""  
          + "\n\"Item: 3\""  
          + "\n\"Item: 5\""
          + "\n\"Item: 4\""  
          + "\n\"Item: 2\""  
          + "\n}", 
        heap.toString()
      );
    }

    @Test
    void to_string_after_extractions() {
      assertAll(() -> {
          heap.extractMin();

          assertEquals("{"
              + "\n\"Item: 2\""  
              + "\n\"Item: 4\""  
              + "\n\"Item: 3\""  
              + "\n\"Item: 5\""
              + "\n}", 
            heap.toString()
          );
        },
        () -> {
          heap.extractMin();
          
          assertEquals("{"
              + "\n\"Item: 3\""  
              + "\n\"Item: 5\""
              + "\n\"Item: 4\""  
              + "\n}", 
            heap.toString()
          );
        },
        () -> {
          heap.extractMin();
          
          assertEquals("{"
              + "\n\"Item: 4\""  
              + "\n\"Item: 5\""
              + "\n}", 
            heap.toString()
          );
        }

      );

    }
  
  }
}