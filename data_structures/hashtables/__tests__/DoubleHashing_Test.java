package data_structures.hashtables.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNull;

import data_structures.Entry;
import data_structures.hashtables.DoubleHashing;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DoubleHashing_Test {
  DoubleHashing<Integer, String> table;
  DoubleHashing<String, String> table2;
  int size = 5;

  // @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // void instantiating_overloaded_constructors() {
  //   assertAll("instantiation for overloaded constructors",
  //     () -> assertDoesNotThrow(() -> new DoubleHashing<>(), "default constructor"),
  //     () -> assertDoesNotThrow(() -> new DoubleHashing<>(3), "given a prime"),
  //     () -> assertDoesNotThrow(() -> new DoubleHashing<>(3, 1), "given a prime and size"),
  //     () -> assertDoesNotThrow(() ->
  //       new DoubleHashing<>(3, 1, 0.9f), "given a prime, size, and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new DoubleHashing<>(3, 0.9f), "given prime and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new DoubleHashing<>(0.9f, 1), "given loadFactor and size")
  //   );
  // }

  // @ParameterizedTest(name = "{index}: prime = {0}, size = {1}, loadFactor = {2}")
  // @Execution(ExecutionMode.CONCURRENT)
  // @CsvSource({ "4, 1, 0.9", "3, 0, 0.9", "3, 1, 0.3" })
  // void throws_when_instantiated_with_illegal_values(int prime, int size, float lf) {
  //   assertThrows(IllegalArgumentException.class, () -> new DoubleHashing<>(prime, size, lf));
  // }

  @Nested
  class When_New {

    @BeforeEach
    void create_hashtable() {
      table = new DoubleHashing<>(size);
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
      table2 = new DoubleHashing<>(size);
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
      table = new DoubleHashing<>(size);

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
      assertTrue(table.hasKey(1));
      assertTrue(table.hasKey(2));
      assertTrue(table.hasKey(3));
      assertTrue(table.hasKey(4));
      assertTrue(table.hasKey(5));
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
    void delete() {
      table.delete(2);
      assertNull(table.get(2));
    }

    @Test
    void keys() {
      Iterator<Integer> keys = table.keysIterator();
      assertTrue(keys.hasNext());
      assertEquals(5, keys.next());
      assertEquals(1, keys.next());
      assertEquals(2, keys.next());
      assertEquals(3, keys.next());
      assertEquals(4, keys.next());
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
    }

    @Test
    void values() {
      Iterator<String> values = table.valuesIterator();
      assertTrue(values.hasNext());
      assertEquals("five", values.next());
      assertEquals("one", values.next());
      assertEquals("two", values.next());
      assertEquals("three", values.next());
      assertEquals("four", values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void entries() {
      Iterator<Entry<Integer, String>> entries = table.entriesIterator();
      assertTrue(entries.hasNext());
      assertEquals("five", entries.next().getValue());
      assertEquals("one", entries.next().getValue());
      assertEquals("two", entries.next().getValue());
      assertEquals("three", entries.next().getValue());
      assertEquals("four", entries.next().getValue());
      assertFalse(entries.hasNext());
      assertThrows(NoSuchElementException.class, () -> entries.next());
    }

    @Test
    void to_String() {
      assertEquals("{\n"
          + "\s\s\"5 -> five\",\n"
          + "\s\s\"1 -> one\",\n"
          + "\s\s\"2 -> two\",\n"
          + "\s\s\"3 -> three\",\n"
          + "\s\s\"4 -> four\",\n"
          + "}",
        table.toString());
    }
  }

  @Nested
  class Many_Insertions {

    @BeforeEach
    void create_table() {
      table = new DoubleHashing<>(1117);
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

      assertTrue(table.hasKey(1));
      assertTrue(table.hasKey(2));
      assertTrue(table.hasKey(3));
      assertTrue(table.hasKey(4));
      assertTrue(table.hasKey(5));
      assertTrue(table.hasKey(6));
      assertTrue(table.hasKey(7));
      assertTrue(table.hasKey(8));
      assertTrue(table.hasKey(9));
      assertTrue(table.hasKey(10));
      assertTrue(table.hasKey(110));
      assertTrue(table.hasKey(120));
      assertTrue(table.hasKey(13));
      assertTrue(table.hasKey(14));
      assertTrue(table.hasKey(15));
      assertTrue(table.hasKey(16));
      assertTrue(table.hasKey(17));
      assertTrue(table.hasKey(18));
      assertTrue(table.hasKey(19));
      assertTrue(table.hasKey(20));
    }

  }

}
