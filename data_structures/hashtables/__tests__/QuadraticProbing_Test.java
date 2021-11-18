package data_structures.hashtables.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNull;

import data_structures.Entry;
import data_structures.hashtables.QuadraticProbing;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class QuadraticProbing_Test {
  QuadraticProbing<Integer, String> table;
  QuadraticProbing<String, String> table2;
  int size = 5;

  // @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // void instantiating_overloaded_constructors() {
  //   assertAll("instantiation for overloaded constructors",
  //     () -> assertDoesNotThrow(() -> new QuadraticProbing<>(), "default constructor"),
  //     () -> assertDoesNotThrow(() -> new QuadraticProbing<>(3), "given a prime"),
  //     () -> assertDoesNotThrow(() -> new QuadraticProbing<>(3, 1), "given a prime and size"),
  //     () -> assertDoesNotThrow(() ->
  //       new QuadraticProbing<>(3, 1, 0.9f), "given a prime, size, and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new QuadraticProbing<>(3, 0.9f), "given prime and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new QuadraticProbing<>(0.9f, 1), "given loadFactor and size")
  //   );
  // }

  // @ParameterizedTest(name = "{index}: prime = {0}, size = {1}, loadFactor = {2}")
  // @Execution(ExecutionMode.CONCURRENT)
  // @CsvSource({ "4, 1, 0.9", "3, 0, 0.9", "3, 1, 0.3" })
  // void throws_when_instantiated_with_illegal_values(int prime, int size, float lf) {
  //   assertThrows(IllegalArgumentException.class, () -> new QuadraticProbing<>(prime, size, lf));
  // }

  @Nested
  class When_New {
    @BeforeEach
    void create_hashtable() {
      table = new QuadraticProbing<>(size);
    }

    @Test
    void isEmpty() {
      assertTrue(table.isEmpty());
    }

    @Test
    void size_is_zero() {
      assertEquals(0, table.size());
    }

    @Test
    void capacity() {
      assertEquals(size, table.capacity());
    }

    @Test
    void toString_is_empty_object() {
      assertEquals("{}", table.toString());
    }

    @ParameterizedTest(name = "key {index} => {0}")
    @ValueSource(ints = {123, 2, 5})
    void get_returns_null_for_any_key(int key) {
      assertNull(table.get(key));
    }

    @ParameterizedTest(name = "key {index} => {0}")
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void throws_NullPointerException_for_null_and_empty_keys(String key) {
      table2 = new QuadraticProbing<>(size);
      assertThrows(Exception.class, () -> table2.insert(key, "test"));
    }

    @Test
    void keys_is_empty() {
      Iterator<Integer> keys = table.keysIterator();
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
      assertThrows(IllegalStateException.class, () -> keys.remove());
    }

    @Test
    void values_is_empty() {
      Iterator<String> values = table.valuesIterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }

    @Test
    void entries_is_empty() {
      Iterator<Entry<Integer, String>> entries = table.entriesIterator();
      assertFalse(entries.hasNext());
      assertThrows(NoSuchElementException.class, () -> entries.next());
      assertThrows(IllegalStateException.class, () -> entries.remove());
    }

  }

  @Nested
  class After_Inserting {

    @BeforeEach
    void create_and_insert() {
      table = new QuadraticProbing<>(size);

      table.insert(1, "one");
      table.insert(2, "two");
      table.insert(3, "three");
      table.insert(4, "four");
      table.insert(5, "five");
    }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals("one", table.get(1)),
        () -> assertEquals("two", table.get(2)),
        () -> assertEquals("three", table.get(3)),
        () -> assertEquals("four", table.get(4)),
        () -> assertEquals("five", table.get(5))
      );
    }

    @Test
    void is_no_longer_empty() {
      assertFalse(table.isEmpty());
    }

    @Test
    void size() {
      assertEquals(5, table.size());
    }

    @Test
    void capacity() {
      assertEquals(size, table.capacity());
    }

    @Test
    void insert_throws_table_is_full() {
      assertThrows(IllegalStateException.class, () -> table.insert(4, "four"));
    }

    @Test
    void hasKey() {
      assertTrue(table.containsKey(1));
      assertTrue(table.containsKey(2));
      assertTrue(table.containsKey(3));
      assertTrue(table.containsKey(4));
      assertTrue(table.containsKey(5));
    }

    @Test
    void get() {
      assertEquals("one", table.get(1));
      assertEquals("two", table.get(2));
      assertEquals("three", table.get(3));
      assertEquals("four", table.get(4));
      assertEquals("five", table.get(5));
    }

    @Test
    void remove() {
      table.remove(2);
      assertNull(table.get(2));
    }

    @Test
    void keys() {
      Iterator<Integer> keys = table.keysIterator();
      assertTrue(keys.hasNext());
      assertNotNull(keys.next());
      assertNotNull(keys.next());
      assertNotNull(keys.next());
      assertNotNull(keys.next());
      assertNotNull(keys.next());
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
    }

    @Test
    void values() {
      Iterator<String> values = table.valuesIterator();
      assertTrue(values.hasNext());
      assertNotNull(values.next());
      assertNotNull(values.next());
      assertNotNull(values.next());
      assertNotNull(values.next());
      assertNotNull(values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void entries() {
      Iterator<Entry<Integer, String>> entries = table.entriesIterator();
      assertTrue(entries.hasNext());
      assertNotNull(entries.next());
      assertNotNull(entries.next());
      assertNotNull(entries.next());
      assertNotNull(entries.next());
      assertNotNull(entries.next());
      assertFalse(entries.hasNext());
      assertThrows(NoSuchElementException.class, () -> entries.next());
    }
  }

  @Nested
  class Many_Insertions {

    @BeforeEach
    void create_table() {
      table = new QuadraticProbing<>(29);
    }

    @Test
    void insertions() {
      table.insert(1, "one");
      table.insert(2, "two");
      table.insert(3, "three");
      table.insert(4, "four");
      table.insert(5, "five");
      table.insert(6, "six");
      table.insert(7, "seven");
      table.insert(8, "eight");
      table.insert(9, "nine");
      table.insert(10, "ten");
      table.insert(110, "eleven");
      table.insert(120, "twelve");
      table.insert(13, "thirteen");
      table.insert(14, "fourteen");
      table.insert(15, "fifteen");
      table.insert(16, "sixteen");
      table.insert(17, "seventeen");
      table.insert(18, "eighteen");
      table.insert(19, "nineteen");
      table.insert(20, "twenty");

      assertTrue(table.containsKey(1));
      assertTrue(table.containsKey(2));
      assertTrue(table.containsKey(3));
      assertTrue(table.containsKey(4));
      assertTrue(table.containsKey(5));
      assertTrue(table.containsKey(6));
      assertTrue(table.containsKey(7));
      assertTrue(table.containsKey(8));
      assertTrue(table.containsKey(9));
      assertTrue(table.containsKey(10));
      assertTrue(table.containsKey(110));
      assertTrue(table.containsKey(120));
      assertTrue(table.containsKey(13));
      assertTrue(table.containsKey(14));
      assertTrue(table.containsKey(15));
      assertTrue(table.containsKey(16));
      assertTrue(table.containsKey(17));
      assertTrue(table.containsKey(18));
      assertTrue(table.containsKey(19));
      assertTrue(table.containsKey(20));
    }

  }

}
