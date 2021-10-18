package LinkedLists.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import LinkedLists.SortedLinkedList;
import LinkedLists.DoublyNode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SortedLinkedList_Test {
  SortedLinkedList<Integer, String> list;
  SortedLinkedList<String, String> list2;
  DoublyNode<Integer, String> node;
  DoublyNode<Integer, String> node2;

  @Test
  void is_instantiated() {
    list = new SortedLinkedList<>();
  }

  @Test
  void node_is_instantiated() {
    node = new DoublyNode<>(1, "one");
  }

  @Nested
  class When_New {

    @BeforeEach
    void create_list() {
      list = new SortedLinkedList<>();
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
    void rSearch_null_on_nonexistent_key() {
      assertNull(list.rSearch(1));
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
    void rGet_returns_null() {
      assertNull(list.rGet(1));
    }

    @Test
    void getIndex_returns_null() {
      assertNull(list.getIndex(1));
    }

    @Test
    void empty_list_string() {
      assertEquals("{}", list.toString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_keys(String key) {
      list2 = new SortedLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.insert(key, "test"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void search_throws_on_bad_keys(String key) {
      list2 = new SortedLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.search(key));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void remove_throws_on_bad_keys(String key) {
      list2 = new SortedLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.remove(key));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void rRemove_throws_on_bad_keys(String key) {
      list2 = new SortedLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.rRemove(key));
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
      list = new SortedLinkedList<>();

      list.insert(5, "five");
      list.insert(3, "three");
      list.insert(1, "one");
      list.insert(2, "two");
      list.insert(4, "four");
    }

    @Test 
    void get() {
      assertEquals("four", list.get(4));
    }

    @Test 
    void rGet() {
      assertEquals("four", list.rGet(4));
    }

    @Test
    void search() {
      node = new DoublyNode<>(4, "four");
      node2 = list.search(4);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }

    @Test
    void rSearch() {
      node = new DoublyNode<>(4, "four");
      node2 = list.rSearch(4);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }

    @Test
    void searchIndex() {
      node = new DoublyNode<>(3, "three");
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
    void rRemove() {
      list.rRemove(3);

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
        () -> assertEquals("one", list.getIndex(0)),
        () -> assertEquals("two", list.getIndex(1)),
        () -> assertEquals("three", list.getIndex(2)),
        () -> assertEquals("five", list.getIndex(3))
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
