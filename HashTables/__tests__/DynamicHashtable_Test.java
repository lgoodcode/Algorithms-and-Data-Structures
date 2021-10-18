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

import Hashtables.PerfectHashing.DynamicHashtable;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DynamicHashtable_Test {
  DynamicHashtable<Integer, String> table;
  DynamicHashtable<String, String> table2;
  // Iterable<Integer> intKeys;
  // Iterable<String> strKeys;
  int size = 5;
  int prime = 1277;

  // @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // void instantiating_overloaded_constructors() {
  //   assertAll("instantiation for overloaded constructors", 
  //     () -> assertDoesNotThrow(() -> new DynamicHashtable<>(), "default constructor"),
  //     () -> assertDoesNotThrow(() -> new DynamicHashtable<>(3), "given a prime"),
  //     () -> assertDoesNotThrow(() -> new DynamicHashtable<>(3, 1), "given a prime and size"),
  //     () -> assertDoesNotThrow(() -> 
  //       new DynamicHashtable<>(3, 1, 0.9f), "given a prime, size, and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new DynamicHashtable<>(3, 0.9f), "given prime and loadFactor"),
  //     () -> assertDoesNotThrow(() -> new DynamicHashtable<>(0.9f, 1), "given loadFactor and size")
  //   );
  // }

  // @ParameterizedTest(name = "{index}: prime = {0}, size = {1}, loadFactor = {2}")
  // @Execution(ExecutionMode.CONCURRENT)
  // @CsvSource({ "4, 1, 0.9", "3, 0, 0.9", "3, 1, 0.3" })
  // void throws_when_instantiated_with_illegal_values(int prime, int size, float lf) {
  //   assertThrows(IllegalArgumentException.class, () -> new DynamicHashtable<>(prime, size, lf));
  // }

  @Nested
  class When_New {

    @BeforeEach
    void create_hashtable() {
      table = new DynamicHashtable<>(size, prime);
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
      table2 = new DynamicHashtable<>(size, prime);

      // Throws NullPointerException for null value and IllegalArgumentException
      assertThrows(Exception.class, () -> table2.insert(key, "test"));
    }
  }

  @Nested
  @Tag("inserted")
  class After_Inserting {

    @BeforeEach
    void insert_key_value() {
      table = new DynamicHashtable<>(size, prime);
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
    void to_String() {
      assertEquals("{\n\s\s\"1 -> one\",\n}", table.toString());
    }
  }

  @Nested
  class Many_Insertions {

    @BeforeEach
    void create_table() {
      table = new DynamicHashtable<>(1, prime);
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
