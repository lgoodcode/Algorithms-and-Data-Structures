package Data_Structures.HashTables.PerfectHashing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import java.util.logging.Logger;

@TestInstance(Lifecycle.PER_CLASS)
interface TestLifecycleLogger {
  static final Logger logger = Logger.getLogger(TestLifecycleLogger.class.getName());

  @BeforeAll
  default void beforeAllTests() {
    logger.info("Beginning tests...");
  }

  @AfterAll
  default void afterAllTests() {
    logger.info("Tests completed.");
  }

  @BeforeEach
  default void beforeEachTest(TestInfo testInfo) {
    logger.info(() -> String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  default void afterEachTest(TestInfo testInfo) {
    logger.info(() -> String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }
}

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CuckooHashTableTest implements TestLifecycleLogger {
  CuckooHashtable<Integer, String> table;
  CuckooHashtable<String, String> table2;
  Iterable<Integer> intKeys;
  Iterable<String> strKeys;

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  void instantiating_overloaded_constructors() {
    assertAll("instantiation for overloaded constructors", 
      () -> assertDoesNotThrow(() -> new CuckooHashtable<>(), "default constructor"),
      () -> assertDoesNotThrow(() -> new CuckooHashtable<>(3), "given a prime"),
      () -> assertDoesNotThrow(() -> new CuckooHashtable<>(3, 1), "given a prime and size"),
      () -> assertDoesNotThrow(() -> 
        new CuckooHashtable<>(3, 1, 0.9f), "given a prime, size, and loadFactor"),
      () -> assertDoesNotThrow(() -> new CuckooHashtable<>(3, 0.9f), "given prime and loadFactor"),
      () -> assertDoesNotThrow(() -> new CuckooHashtable<>(0.9f, 1), "given loadFactor and size")
    );
  }

  @ParameterizedTest(name = "{index}: prime = {0}, size = {1}, loadFactor = {2}")
  @Execution(ExecutionMode.CONCURRENT)
  @CsvSource({ "4, 1, 0.9", "3, 0, 0.9", "3, 1, 0.3" })
  void throws_when_instantiated_with_illegal_values(int prime, int size, float lf) {
    assertThrows(IllegalArgumentException.class, () -> new CuckooHashtable<>(prime, size, lf));
  }

  @Nested
  class When_New {

    @BeforeEach
    void create_hashtable() {
      table = new CuckooHashtable<>();
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
      table2 = new CuckooHashtable<>();

      assertThrows(NullPointerException.class, () -> table2.insert(key, "test"));
    }

    @Nested
    @Tag("inserted")
    class After_Inserting {
      int key = 123;
      String value = "test";

      @BeforeEach
      void insert_key_value() {
        table.insert(key, value);
      }

      @Test
      void is_no_longer_empty() {
        assertFalse(table.isEmpty());
      }

      @Test
      void has_get_and_delete_inserted_key() {
        assertAll("has(), get(), and delete() key", 
          () -> assertTrue(table.hasKey(key)),
          () -> {
            String val = table.get(key);
            assertNotNull(val);
            assertEquals(value, val);
          },
          () -> assertTrue(table.delete(key)),
          () -> assertTrue(table.isEmpty())
        );
      }
    }

  }

}
