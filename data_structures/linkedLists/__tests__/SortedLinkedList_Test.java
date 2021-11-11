package data_structures.linkedLists.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.linkedLists.SortedLinkedList;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SortedLinkedList_Test {
  SortedLinkedList<String> list;
  SortedLinkedList<String> list2;
  SortedLinkedList<String>.Node<String> node;
  SortedLinkedList<String>.Node<String> node2;

  @Nested
  class When_New {

    @BeforeEach
    void create_list() {
      list = new SortedLinkedList<>();
    }

    @Test
    void insertion() {
      list.insert("a");
    }

    @Test
    void getHead_returns_null() {
      assertNull(list.getHead());
    }

    @Test
    void getTail_returns_null() {
      assertNull(list.getTail());
    }

    @Test
    void peek_returns_null() {
      assertNull(list.peek());
    }

    @Test
    void peekLast_returns_null() {
      assertNull(list.peekLast());
    }

    @Test
    void poll_returns_null() {
      assertNull(list.poll());
    }

    @Test
    void pollLast_returns_null() {
      assertNull(list.pollLast());
    }

    @Test
    void search_throws_on_invalid_index() {
      assertThrows(IndexOutOfBoundsException.class, () -> list.search(1));
    }

    @Test
    void get_throws_on_invalid_index() {
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void empty_list_string() {
      assertEquals("{}", list.toString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      assertThrows(IllegalArgumentException.class, () -> list.insert(value));
    }

    @Test
    void values_is_empty() {
      Iterator<String> values = list.valuesIterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }

  }

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      list = new SortedLinkedList<>();

      list.insert("a");
      list.insert("c");
      list.insert("b");
      list.insert("e");
      list.insert("d");
    }

    @Test
    void getHead() {
      assertEquals(list.search(0), list.getHead());
    }

    @Test
    void getTail() {
      assertEquals(list.search(4), list.getTail());
    }

    @Test
    void peek() {
      assertEquals("a", list.peek());
    }

    @Test
    void peekLast() {
      assertEquals("e", list.peekLast());
    }

    @Test
    void poll() {
      assertEquals("a", list.poll());
      assertEquals("b", list.peek());
    }

    @Test
    void pollLast() {
      assertEquals("e", list.pollLast());
      assertEquals("d", list.peekLast());
    }

    @Test
    void insert() {
      assertEquals("a", list.get(0));
      assertEquals("b", list.get(1));
      assertEquals("c", list.get(2));
      assertEquals("d", list.get(3));
      assertEquals("e", list.get(4));

      list.insert("x");
      list.insert("y");
      list.insert("z");
      list.insert("g");

      assertEquals("g", list.get(5));
      assertEquals("x", list.get(6));
      assertEquals("y", list.get(7));

    }

    @Test
    void indexOf() {
      assertEquals(0, list.indexOf("a"));
      assertEquals(1, list.indexOf("b"));
      assertEquals(2, list.indexOf("c"));
      assertEquals(3, list.indexOf("d"));
      assertEquals(4, list.indexOf("e"));
    }

    @Test
    void indexOf_returns_negative_on_not_found() {
      assertEquals(-1, list.indexOf("test"));
    }

    @Test
    void lastIndexOf() {
      assertEquals(0, list.lastIndexOf("a"));
      assertEquals(1, list.lastIndexOf("b"));
      assertEquals(2, list.lastIndexOf("c"));
      assertEquals(3, list.lastIndexOf("d"));
      assertEquals(4, list.lastIndexOf("e"));
    }

    @Test
    void lastIndexOf_returns_negative_on_not_found() {
      assertEquals(-1, list.lastIndexOf("test"));
    }

    @Test
    void contains() {
      assertTrue(list.contains("b"));
      assertFalse(list.contains("test"));
    }

    @Test
    void get() {
      assertAll(
        () -> assertEquals("a", list.get(0)),
        () -> assertEquals("b", list.get(1)),
        () -> assertEquals("c", list.get(2)),
        () -> assertEquals("d", list.get(3)),
        () -> assertEquals("e", list.get(4))
      );
    }

    @Test
    void remove() {
      list.remove(2);

      assertAll(
        () -> assertEquals("a", list.get(0)),
        () -> assertEquals("b", list.get(1)),
        () -> assertEquals("d", list.get(2)),
        () -> assertEquals("e", list.get(3))
      );
    }

    @Test
    void values() {
      Iterator<String> values = list.valuesIterator();
      assertTrue(values.hasNext());
      assertEquals("a", values.next());
      assertEquals("b", values.next());
      assertEquals("c", values.next());
      assertEquals("d", values.next());
      assertEquals("e", values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void to_string() {
      assertEquals("{"
          + "\n\"0 -> a\","
          + "\n\"1 -> b\","
          + "\n\"2 -> c\","
          + "\n\"3 -> d\","
          + "\n\"4 -> e\","
          + "\n}",
        list.toString()
      );
    }
  }
}
