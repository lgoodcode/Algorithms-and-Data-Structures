package data_structures.queues.__tests__;

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

import data_structures.queues.Queue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Queue_Test {
  Queue<Integer> queue;
  Queue<String> queue2;
  
  @Test
  void is_instantiated() {
    queue = new Queue<>(10);
  }

  @Test 
  void throws_when_instantiated_with_empty_array() {
    Integer[] arr = {};
    assertThrows(IllegalArgumentException.class, () -> {
      queue = new Queue<>(arr);
    });
  }

  @Test
  void instantiated_with_initial_values() {
    Integer[] values = { 1, 2, 3, 4, 5 };
    queue = new Queue<>(values);

    assertAll(
      () -> assertEquals(1, queue.dequeue()),
      () -> assertEquals(2, queue.dequeue()),
      () -> assertEquals(3, queue.dequeue()),
      () -> assertEquals(4, queue.dequeue()),
      () -> assertEquals(5, queue.dequeue())
    );
  }

  @Nested 
  class When_New {
    int size = 10;
    
    @BeforeEach
    void create_queue() {
      queue = new Queue<>(size);
    }

    @Test 
    void size() {
      assertEquals(0, queue.size());
    }

    @Test
    void capacity() {
      assertEquals(10, queue.capacity());
    }

    @Test
    void is_empty() {
      assertTrue(queue.isEmpty());
    }

    @Test
    void peek_is_null() {
      assertNull(queue.peek());
    }

    @Test
    void peekLast_is_null() {
      assertNull(queue.peekLast());
    }

    @Test
    void enqueue() {
      queue.enqueue(1);
      assertEquals(1, queue.dequeue());
    }

    @Test
    void empty_queue_string() {
      assertEquals("[]", queue.toString());
    }

    @Test
    void throws_NoSuchElementException() {
      assertThrows(NoSuchElementException.class, () -> queue.dequeue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String key) {
      queue2 = new Queue<>(size);

      assertThrows(IllegalArgumentException.class, () -> queue2.enqueue(key));
    }

  }

  @Nested
  class Multiple_Insertions {
    int size = 5;

    @BeforeEach
    void create_and_insert() {
      queue = new Queue<>(size);

      queue.enqueue(1);
      queue.enqueue(2);
      queue.enqueue(3);
      queue.enqueue(4);
      queue.enqueue(5);
    }

    @Test
    void not_empty() {
      assertFalse(queue.isEmpty());
    }

    @Test
    void isFull() {
      assertTrue(queue.isFull());
    }

    @Test
    void clear() {
      queue.clear();
      assertTrue(queue.isEmpty());
      assertEquals(0, queue.size());
    }

    @Test
    void peek() {
      assertEquals(1, queue.peek());
    }

    @Test
    void peekLast() {
      assertEquals(5, queue.peekLast());
    }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals(1, queue.dequeue()),
        () -> assertEquals(2, queue.dequeue()),
        () -> assertEquals(3, queue.dequeue()),
        () -> assertEquals(4, queue.dequeue()),
        () -> assertEquals(5, queue.dequeue())
      );
    }

    @Test
    void queue_throws_when_full() {
      assertThrows(IllegalStateException.class, () -> queue.enqueue(6));
    }

    @Test
    void empties_and_resets() {
      assertAll(
        () -> assertEquals(1, queue.dequeue()),
        () -> assertEquals(2, queue.dequeue()),
        () -> assertEquals(3, queue.dequeue()),
        () -> assertEquals(4, queue.dequeue()),
        () -> assertEquals(5, queue.dequeue())
      );
      
      assertDoesNotThrow(() -> {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
      });
    }

    @Test
    void queue_constructed_from_queue() {
      Queue<Integer> q = new Queue<>(queue);

      assertAll(
        () -> assertEquals(1, q.dequeue()),
        () -> assertEquals(2, q.dequeue()),
        () -> assertEquals(3, q.dequeue()),
        () -> assertEquals(4, q.dequeue()),
        () -> assertEquals(5, q.dequeue())
      );
    }

    @Test
    void iterator() {
      Iterator<Integer> values = queue.iterator();
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
      Iterator<Integer> values = queue.iterator();
      assertTrue(values.hasNext());
      assertEquals(1, values.next());
      assertEquals(2, values.next());
      assertDoesNotThrow(() -> values.remove());
      assertFalse(queue.has(2));
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(3, values.next());
      assertEquals(4, values.next());
      assertEquals(5, values.next());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertEquals(4, queue.size());
    }


    @Test
    void toArray() {
      Object[] arr = { 1, 2, 3, 4, 5 };
      assertArrayEquals(arr, queue.toArray());
    }

    @Test
    void to_string() {
      assertEquals("[1, 2, 3, 4, 5]", queue.toString());
    }
  
  }
}