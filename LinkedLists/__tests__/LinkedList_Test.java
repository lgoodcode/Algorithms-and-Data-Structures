package LinkedLists.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import LinkedLists.LinkedList;
import LinkedLists.LinkedListNode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LinkedList_Test {
  LinkedList<Integer, String> list;
  LinkedList<String, String> list2;
  LinkedListNode<Integer, String> node;
  LinkedListNode<Integer, String> node2;

  @Test
  void is_instantiated() {
    list = new LinkedList<>();
  }

  @Test
  void node_is_instantiated() {
    node = new LinkedListNode<>(1, "one");
  }

  @Nested
  class When_New {

    @BeforeEach
    void create_list() {
      list = new LinkedList<>();
      list2 = new LinkedList<>();
    }

    @Test
    void insertion() {
      list.insert(1, "one");
    }

    @Test 
    void search_null_on_nonexistent_key() {
      assertNull(list.search(1));
    }

    @Test 
    void searchIndex_null_on_nonexistent_key() {
      assertNull(list.search(4));
    }

    @Test
    void get_returns_null() {
      assertNull(list.get(1));
    }

    @Test 
    void getIndex_returns_null() {
      assertNull(list2.getIndex(3));
    }

    @Test
    void empty_list_string() {
      assertEquals("{}", list.toString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_keys(String key) {
      list2 = new LinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.insert(key, "test"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void search_throws_on_bad_keys(String key) {
      list2 = new LinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.search(key));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      assertThrows(IllegalArgumentException.class, () -> list.insert(1, value));
    }
  } 

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      list = new LinkedList<>();

      list.insert(1, "one");
      list.insert(2, "two");
      list.insert(3, "three");
      list.insert(4, "four");
      list.insert(5, "five");
    }

    @Test
    void insertAt() {
      list.insertAt(3, 6, "six");

      assertEquals("six", list.getIndex(3));
      assertEquals("two", list.getIndex(4));
      assertEquals("three", list.getIndex(2));
      assertEquals("one", list.getIndex(5));
    }

    @Test
    void get() {
      assertAll(
        () -> assertEquals("one", list.get(1)),
        () -> assertEquals("two", list.get(2)),
        () -> assertEquals("three", list.get(3)),
        () -> assertEquals("four", list.get(4)),
        () -> assertEquals("five", list.get(5))
      );
    }

    @Test
    void getIndex() {
      assertAll(
        () -> assertEquals("one", list.getIndex(4)),
        () -> assertEquals("two", list.getIndex(3)),
        () -> assertEquals("three", list.getIndex(2)),
        () -> assertEquals("four", list.getIndex(1)),
        () -> assertEquals("five", list.getIndex(0))
      );
    }

    @Test
    void search() {
      node = new LinkedListNode<>(4, "four");
      node2 = list.search(4);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }
    
    @Test
    void searchIndex() {
      node = new LinkedListNode<>(3, "three");
      node2 = list.searchIndex(2);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }

    @Test
    void remove() {
      list.remove(3);

      assertAll(
        () -> assertEquals("one", list.get(1)),
        () -> assertEquals("two", list.get(2)),
        () -> assertEquals("four", list.get(4)),
        () -> assertEquals("five", list.get(5))
      );
    }

    @Test
    void removeIndex() {
      list.removeIndex(3);

      assertAll(
        () -> assertEquals("one", list.getIndex(3)),
        () -> assertEquals("three", list.getIndex(2)),
        () -> assertEquals("four", list.getIndex(1)),
        () -> assertEquals("five", list.getIndex(0))
      );
    }

    @Test
    void to_string() {
      assertEquals("{"
          + "\n\"1 -> one\""  
          + "\n\"2 -> two\""  
          + "\n\"3 -> three\""  
          + "\n\"4 -> four\""  
          + "\n\"5 -> five\""
          + "\n}", 
        list.toString()
      );
    }
  }
}
