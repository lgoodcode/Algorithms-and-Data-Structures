package data_structures.stacks.__tests__;

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

import java.util.NoSuchElementException;

import data_structures.stacks.Stack;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Stack_Test {
  Stack<Integer> stack;
  Stack<String> stack2;
  
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
    int size = 5;

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
    void push() {
      stack.push(1);
      assertEquals(1, stack.pop());
    }

    @Test
    void empty_stack_string() {
      assertEquals("{}", stack.toString());
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

  }

  @Nested
  class Multiple_pushes {
    int size = 5;

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
    void to_string() {
      assertEquals("{"
          + "\n\"5\""
          + "\n\"4\""  
          + "\n\"3\""  
          + "\n\"2\""  
          + "\n\"1\""  
          + "\n}", 
        stack.toString()
      );
    }
  
  }
}
