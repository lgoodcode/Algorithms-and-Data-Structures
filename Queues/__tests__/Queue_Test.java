package Queues.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Queues.Queue;
import Queues.exceptions.*;

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
      assertEquals(10, queue.size());
    }

    @Test
    void is_empty() {
      assertTrue(queue.isEmpty());
    }

    @Test
    void enqueue() {
      try {
        queue.enqueue(1);
      } catch (QueueFullException e) {}
      
      try {
        assertEquals(1, queue.dequeue());
      } catch (QueueEmptyException e) {}
    }

    @Test
    void empty_queue_string() {
      assertEquals("{}", queue.toString());
    }

    @Test
    void throws_QueueEmptyException() {
      assertThrows(QueueEmptyException.class, () -> queue.dequeue());
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

      try {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
      } catch (QueueFullException e) {}
    }

    @Test
    void not_empty() {
      assertFalse(queue.isEmpty());
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
    void throws_QueueFullException() {
      assertThrows(QueueFullException.class, () -> queue.enqueue(6));
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
    void to_string() {
      assertEquals("{"
          + "\n\"1\""  
          + "\n\"2\""  
          + "\n\"3\""  
          + "\n\"4\""  
          + "\n\"5\""
          + "\n}", 
        queue.toString()
      );
    }
  
  }
}