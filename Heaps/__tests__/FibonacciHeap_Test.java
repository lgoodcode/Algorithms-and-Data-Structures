package Heaps.__tests__;

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

import Heaps.FibonacciHeap;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FibonacciHeap_Test {
  FibonacciHeap<Integer, String> heap;
  FibonacciHeap<String, String> heap2;

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
    void insert() {
      heap.insert(1, "one");
      assertEquals("one", heap.extractMin());
    }

    @Test
    void empty_heap_string() {
      assertEquals("{}", heap.toString());
    }

    @Test
    void extract_returns_null() {
      assertNull(heap.extractMin());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String key) {
      heap2 = new FibonacciHeap<>();

      assertThrows(IllegalArgumentException.class, () -> heap2.insert(key, "test"));
    }

  }

  @Nested
  class Multiple_inserts {

    @BeforeEach
    void create_and_insert() {
      heap = new FibonacciHeap<>();

      heap.insert(4, "four");
      heap.insert(2, "two");
      heap.insert(5, "five");
      heap.insert(1, "one");
      heap.insert(3, "three");
    }

    @Test
    void not_empty() {
      assertFalse(heap.isEmpty());
    }

    @Test
    void get_minimum() {
      assertEquals("one", heap.getMin().getValue());
    }

    @Test
    void decreaseKey_throws_on_invalid_key() {
      assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(heap.getMin(), 3));
    }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals("one", heap.extractMin()),
        () -> assertEquals("two", heap.extractMin()),
        () -> assertEquals("three", heap.extractMin()),
        () -> assertEquals("four", heap.extractMin()),
        () -> assertEquals("five", heap.extractMin())
      );
    }

    @Test
    void empties_and_resets() {
      assertAll(
        () -> assertEquals("one", heap.extractMin()),
        () -> assertEquals("two", heap.extractMin()),
        () -> assertEquals("three", heap.extractMin()),
        () -> assertEquals("four", heap.extractMin()),
        () -> assertEquals("five", heap.extractMin())
      );
      
      assertDoesNotThrow(() -> {
        heap.insert(1, "one");
        heap.insert(2, "two");
        heap.insert(3, "three");
        heap.insert(4, "four");
        heap.insert(5, "five");
      });
    }

    @Test
    void many_insertions() {
      heap.insert(10,  "ten");
      heap.insert(8, "eight");
      heap.insert(6, "six");
      heap.insert(7, "seven");
      heap.insert(9, "nine");
      heap.insert(11, "eleven");
      
      assertAll(
        () -> assertEquals("one", heap.extractMin()),
        () -> assertEquals("two", heap.extractMin()),
        () -> assertEquals("three", heap.extractMin()),
        () -> assertEquals("four", heap.extractMin()),
        () -> assertEquals("five", heap.extractMin())
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
          + "\n\"Key: 1, value: one\""  
          + "\n\"Key: 3, value: three\""  
          + "\n\"Key: 5, value: five\""
          + "\n\"Key: 4, value: four\""  
          + "\n\"Key: 2, value: two\""  
          + "\n}", 
        heap.toString()
      );
    }

    @Test
    void to_string_after_extractions() {
      assertAll(() -> {
          heap.extractMin();

          assertEquals("{"
              + "\n\"Key: 2, value: two\""  
              + "\n\"Key: 4, value: four\""  
              + "\n\"Key: 3, value: three\""  
              + "\n\"Key: 5, value: five\""
              + "\n}", 
            heap.toString()
          );
        },
        () -> {
          heap.extractMin();
          
          assertEquals("{"
              + "\n\"Key: 3, value: three\""  
              + "\n\"Key: 5, value: five\""
              + "\n\"Key: 4, value: four\""  
              + "\n}", 
            heap.toString()
          );
        },
        () -> {
          heap.extractMin();
          
          assertEquals("{"
              + "\n\"Key: 4, value: four\""  
              + "\n\"Key: 5, value: five\""
              + "\n}", 
            heap.toString()
          );
        }

      );

    }
  
  }
}