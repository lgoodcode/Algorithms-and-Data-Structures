package Hashtables.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import Hashtables.OpenAddressing.LinearProbing;
import Hashtables.exceptions.HashtableFullException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LinearProbing_Test {
  LinearProbing<Integer, String> table;
  LinearProbing<String, String> table2;
  // Iterable<Integer> intKeys;
  // Iterable<String> strKeys;

  // @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // void instantiating_overloaded_constructors() {
  //   assertAll("instantiation for overloaded constructors", 
  //     () -> assertDoesNotThrow(() -> new LinearProbing<>(), "default constructor"),
  //     () -> assertDoesNotThrow(() -> new LinearProbing<>(3), "given a prime"),
  //     () -> assertDoesNotThrow(() -> new LinearProbing<>(3, 1), "given a prime and size"),
  //     () -> assertDoesNotThrow(() -> 
  //       new LinearProbing<>(3, 1, 0.9f), "given a prime, size, and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new LinearProbing<>(3, 0.9f), "given prime and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new LinearProbing<>(0.9f, 1), "given loadFactor and size")
  //   );
  // }

  // @ParameterizedTest(name = "{index}: prime = {0}, size = {1}, loadFactor = {2}")
  // @Execution(ExecutionMode.CONCURRENT)
  // @CsvSource({ "4, 1, 0.9", "3, 0, 0.9", "3, 1, 0.3" })
  // void throws_when_instantiated_with_illegal_values(int prime, int size, float lf) {
  //   assertThrows(IllegalArgumentException.class, () -> new LinearProbing<>(prime, size, lf));
  // }

  @Nested
  class When_New {
    int size = 10;

    @BeforeEach
    void create_hashtable() {
      table = new LinearProbing<>(size);
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
      table2 = new LinearProbing<>(size);

      // Throws NullPointerException for null value and IllegalArgumentException
      assertThrows(Exception.class, () -> table2.insert(key, "test"));
    }
  }

  @Nested
  @Tag("inserted")
  class After_Inserting {
    int size = 3;

    @BeforeEach
    void insert_key_value() {
      table = new LinearProbing<>(1);
      assertDoesNotThrow(() -> table.insert(1, "one"));
    }

    @Test
    void is_no_longer_empty() {
      assertFalse(table.isEmpty());
    }

    @Test 
    void size() {
      assertEquals(1, table.size());
    }

    @Test
    void has_get_and_delete_inserted_key() {
      assertAll("has(), get(), and delete() key", 
        () -> assertTrue(table.hasKey(1)),
        () -> {
          String val = table.get(1);
          assertNotNull(val);
          assertEquals("one", val);
        },
        () -> assertTrue(table.delete(1)),
        () -> assertTrue(table.isEmpty())
      );
    }

    @Test
    void insert_throws_table_is_full() {
      try {
        table.insert(2, "two");
        table.insert(3, "three");
      } catch (HashtableFullException e) {}

      assertThrows(HashtableFullException.class, () -> table.insert(4, "four"));
    }

    @Test
    void to_String() {
      assertEquals("{\n  \"Key: 1, value: one\"\n}", table.toString());
    }
  }

  @Nested
  class Many_Insertions {

    @BeforeEach
    void create_table() {
      table = new LinearProbing<>(1117);
    }

    @Test
    void insertions() {
      try {
        assertTrue(table.insert(1, "one"));
        assertTrue(table.insert(2, "two"));
        assertTrue(table.insert(3, "three"));
        assertTrue(table.insert(4, "four"));
        assertTrue(table.insert(5, "five"));
        assertTrue(table.insert(6, "six"));
        assertTrue(table.insert(7, "seven"));
        assertTrue(table.insert(8, "eight"));
        assertTrue(table.insert(9, "nine"));
        assertTrue(table.insert(10, "ten"));
        assertTrue(table.insert(110, "eleven"));
        assertTrue(table.insert(120, "twelve"));
        assertTrue(table.insert(13, "thirteen"));
        assertTrue(table.insert(14, "fourteen"));
        assertTrue(table.insert(15, "fifteen"));
        assertTrue(table.insert(16, "sixteen"));
        assertTrue(table.insert(17, "seventeen"));
        assertTrue(table.insert(18, "eighteen"));
        assertTrue(table.insert(19, "nineteen"));
        assertTrue(table.insert(20, "twenty"));
      } catch (HashtableFullException e) {}

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