package data_structures.stacks.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.stacks.Stack;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Stack_Test {
  Stack<Integer> stack;
  Stack<String> stack2;
  int size = 5;
  
  @Test
  void is_instantiated() {
    stack = new Stack<>(10);
  }

  @ParameterizedTest
  @ValueSource(ints = { -1, 0 })
  void throws_when_size_is_invalid(int size) {
    assertThrows(IllegalArgumentException.class, () -> new Stack<>(size));
  }

  @Test 
  void throws_when_instantiated_with_empty_array() {
    Integer[] arr = {};
    assertThrows(IllegalArgumentException.class, () -> {
      stack = new Stack<>(arr);
    });
  }

  @Test
  void instantiated_with_initial_values() {
    Integer[] values = { 1, 2, 3, 4, 5 };
    stack = new Stack<>(values);

    assertAll(
      () -> assertEquals(5, stack.pop()),
      () -> assertEquals(4, stack.pop()),
      () -> assertEquals(3, stack.pop()),
      () -> assertEquals(2, stack.pop()),
      () -> assertEquals(1, stack.pop())
    );
  }

  @Nested 
  class When_New {

    @BeforeEach
    void create_stack() {
      stack = new Stack<>(size);
    }

    @Test
    void size() {
      assertEquals(0, stack.size());
    }

    @Test 
    void capacity() {
      assertEquals(5, stack.capacity());
    }

    @Test
    void is_empty() {
      assertTrue(stack.isEmpty());
    }

    @Test
    void is_not_full() {
      assertFalse(stack.isFull());
    }

    @Test
    void push() {
      stack.push(1);
      assertEquals(1, stack.pop());
    }

    @Test
    void empty_stack_string() {
      assertEquals("[]", stack.toString());
    }

    @Test
    void pop_throws_when_empty() {
      assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void push_throws_on_bad_values(String key) {
      stack2 = new Stack<>(size);

      assertThrows(IllegalArgumentException.class, () -> stack2.push(key));
    }

    @Test
    void iterator_is_empty() {
      Iterator<Integer> values = stack.iterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }   

  }

  @Nested
  class Multiple_pushes {

    @BeforeEach
    void create_and_push() {
      stack = new Stack<>(size);

      stack.push(1);
      stack.push(2);
      stack.push(3);
      stack.push(4);
      stack.push(5);
    }

    @Test
    void not_empty() {
      assertFalse(stack.isEmpty());
    }

    @Test
    void isFull() {
      assertTrue(stack.isFull());
    }

    @Test
    void clear() {
      stack.clear();
      assertEquals(0, stack.size());
      assertTrue(stack.isEmpty());
    }

    @Test
    void all_pushes_succeed() {
      assertAll(
        () -> assertEquals(5, stack.pop()),
        () -> assertEquals(4, stack.pop()),
        () -> assertEquals(3, stack.pop()),
        () -> assertEquals(2, stack.pop()),
        () -> assertEquals(1, stack.pop())
      );
    }

    @Test
    void push_throws_when_stack_full() {
      assertThrows(IllegalStateException.class, () -> stack.push(6));
    }

    @Test
    void empties_and_resets() {
      assertAll(
        () -> assertEquals(5, stack.pop()),
        () -> assertEquals(4, stack.pop()),
        () -> assertEquals(3, stack.pop()),
        () -> assertEquals(2, stack.pop()),
        () -> assertEquals(1, stack.pop())
      );
      
      assertDoesNotThrow(() -> {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
      });
    }

    @Test
    void stack_constructed_from_stack() {
      Stack<Integer> q = new Stack<>(stack);

      assertAll(
        () -> assertEquals(5, q.pop()),
        () -> assertEquals(4, q.pop()),
        () -> assertEquals(3, q.pop()),
        () -> assertEquals(2, q.pop()),
        () -> assertEquals(1, q.pop())
      );
    }

    @Test
    void iterator() {
      Iterator<Integer> values = stack.iterator();
      assertTrue(values.hasNext());
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(5, values.next());
      assertEquals(4, values.next());
      assertEquals(3, values.next());
      assertEquals(2, values.next());
      assertEquals(1, values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void iterator_remove() {
      Iterator<Integer> values = stack.iterator();
      assertTrue(values.hasNext());
      assertEquals(5, values.next());
      assertEquals(4, values.next());
      assertDoesNotThrow(() -> values.remove());
      assertFalse(stack.has(4));
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(3, values.next());
      assertEquals(2, values.next());
      assertEquals(1, values.next());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertEquals(4, stack.size());
    }

    @Test
    void toArray() {
      Object[] arr = { 5, 4, 3, 2, 1 };
      assertArrayEquals(arr, stack.toArray());
    }

    @Test
    void to_string() {
      assertEquals("[5, 4, 3, 2, 1]", stack.toString());
    }
  
  }
}
