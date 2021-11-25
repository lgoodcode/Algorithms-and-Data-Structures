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
    void is_not_full() {
      assertFalse(heap.isFull());
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
    void removeAt_throws() {
      assertThrows(IndexOutOfBoundsException.class, () -> heap.removeAt(-1));
      assertThrows(IndexOutOfBoundsException.class, () -> heap.removeAt(100));
    }

    @Test
    void empty_heap_string() {
      assertEquals("[]", heap.toString());
    }

    @Test
    void empty_toArray() {
      assertArrayEquals(new Object[0], heap.toArray());
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
    void removeAt() {
      heap.removeAt(2);

      assertAll(
        () -> assertEquals(1, heap.extractMin()),
        () -> assertEquals(2, heap.extractMin()),
        () -> assertEquals(4, heap.extractMin()),
        () -> assertEquals(5, heap.extractMin())
      );    
    }

    @Test
    void insert_throws_when_full() {
      assertThrows(IllegalStateException.class, () -> heap.insert(6));
    }

    @Test
    void is_full() {
      assertTrue(heap.isFull());
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
      assertEquals("[1, 2, 3, 4, 5]", heap.toString());
    }

    @Test
    void toArray() {
      Object[] arr = { 1, 2, 3, 4, 5 };
      assertArrayEquals(arr, heap.toArray());
    }

    @Test
    void iterator() {
      Iterator<Integer> values = heap.iterator();
      assertTrue(values.hasNext());
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(1, values.next());
      assertEquals(2, values.next());
      assertEquals(3, values.next());
      assertEquals(4, values.next());
      assertEquals(5, values.next());
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
      assertEquals(2, values.next());
      assertDoesNotThrow(() -> values.remove());
      assertEquals(3, values.next());
      assertEquals(4, values.next());
      assertEquals(5, values.next());
      assertThrows(NoSuchElementException.class, () -> values.next());

      assertEquals(3, heap.size());

      assertEquals(3, heap.extractMin());
    }
  
  }

  @Nested
  class Many_Insertions {

    @Test
    void many_insertions() {
      heap = new MinHeap<>(30);

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