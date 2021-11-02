package data_structures.trees.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.trees.BTree;
import data_structures.trees.BTreeNode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BTree_Test {
  BTree<Integer, String> tree;
  BTree<String, String> tree2;
  BTreeNode<Integer, String> node;
  BTreeNode<Integer, String> node2;

  @Nested 
  class When_New {
    
    @BeforeEach
    void create_tree() {
      tree = new BTree<>();
    }

    @Test 
    void is_empty() {
      assertEquals(0, tree.size());
    }

    @Test
    void insertion() {
      tree.insert(1, "one");
      
      assertEquals("one", tree.get(1));
    }

    @Test
    void get_is_null_on_nonexistent_key() {
      assertNull(tree.get(5));
    }

    @Test
    void minimum_is_null() {
      assertNull(tree.minimum());
    }

    @Test
    void maximum_is_null() {
      assertNull(tree.maximum());
    }

    @Test
    void empty_tree_string() {
      assertEquals("{}", tree.toString());
    }

    @Test
    void delete_throws() {
      assertThrows(NoSuchElementException.class, () -> tree.delete(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_keys(String key) {
      tree2 = new BTree<>();

      assertThrows(IllegalArgumentException.class, () -> tree2.insert(key, "test"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      assertThrows(IllegalArgumentException.class, () -> tree.insert(1, value));
    }

    @Test
    void keys_is_empty() {
      Iterator<Integer> keys = tree.keysIterator();
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
      assertThrows(IllegalStateException.class, () -> keys.remove());
    }

    @Test
    void values_is_empty() {
      Iterator<String> values = tree.valuesIterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }

    @Test
    void entries_is_empty() {
      Iterator<BTreeNode<Integer, String>> entries = tree.entriesIterator();
      assertFalse(entries.hasNext());
      assertThrows(NoSuchElementException.class, () -> entries.next());
      assertThrows(IllegalStateException.class, () -> entries.remove());
    }

  }

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      tree = new BTree<>();

      tree.insert(1, "one");
      tree.insert(2, "two");
      tree.insert(3, "three");
      tree.insert(4, "four");
      tree.insert(5, "five");
    }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals("one", tree.get(1)),
        () -> assertEquals("two", tree.get(2)),
        () -> assertEquals("three", tree.get(3)),
        () -> assertEquals("four", tree.get(4)),
        () -> assertEquals("five", tree.get(5))
      );
    }

    @Test
    void hasKey() {
      assertTrue(tree.hasKey(1));
      assertTrue(tree.hasKey(2));
      assertTrue(tree.hasKey(3));
      assertTrue(tree.hasKey(4));
      assertTrue(tree.hasKey(5));
    }

    @Test
    void minimum() {
      assertEquals("one", tree.minimum().getMinValue());
    }

    @Test
    void maximum() {
      assertEquals("five", tree.maximum().getMaxValue());
    }

    @Test 
    void delete() {
      tree.delete(1);
      assertEquals(4, tree.size());
      assertNull(tree.get(1));
      assertEquals("two", tree.get(2));
      assertEquals("three", tree.get(3));
      assertEquals("four", tree.get(4));
      assertEquals("five", tree.get(5));

      tree.delete(4);
      assertEquals(3, tree.size());
      assertNull(tree.get(4));
      assertEquals("two", tree.get(2));
      assertEquals("three", tree.get(3));
      assertEquals("five", tree.get(5));

      tree.delete(5);
      assertEquals(2, tree.size());
      assertNull(tree.get(5));
      assertEquals("two", tree.get(2));
      assertEquals("three", tree.get(3));
      
      tree.delete(2);
      assertEquals(1, tree.size());
      assertNull(tree.get(2));
      assertEquals("three", tree.get(3));
    } 

    @Test
    void predecessor() {
      assertNull(tree.predecessor(1));
      assertEquals(1, tree.predecessor(2));
      assertEquals(2, tree.predecessor(3));
      assertEquals(3, tree.predecessor(4));
      assertEquals(4, tree.predecessor(5));

      tree.delete(1);
      tree.delete(2);
      tree.delete(5);
      assertEquals(3, tree.predecessor(4));
    }

    @Test
    void successor() {
      assertNull(tree.successor(5));
      assertEquals(2, tree.successor(1));
      assertEquals(3, tree.successor(2));
      assertEquals(4, tree.successor(3));
      assertEquals(5, tree.successor(4));

      tree.delete(1);
      tree.delete(2);
      tree.delete(5);
      assertEquals(4, tree.successor(3));
    }

    @Test
    void keys() {
      Iterator<Integer> keys = tree.keysIterator();
      assertTrue(keys.hasNext());
      assertEquals(1, keys.next());
      assertEquals(3, keys.next());
      assertEquals(4, keys.next());
      assertEquals(5, keys.next());
      assertEquals(2, keys.next());
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
    }

    @Test
    void values() {
      Iterator<String> values = tree.valuesIterator();
      assertTrue(values.hasNext());
      assertEquals("one", values.next());
      assertEquals("three", values.next());
      assertEquals("four", values.next());
      assertEquals("five", values.next());
      assertEquals("two", values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void entries() {
      Iterator<BTreeNode<Integer, String>> entries = tree.entriesIterator();
      assertTrue(entries.hasNext());
      assertEquals(BTreeNode.class, entries.next().getClass());
      assertEquals(BTreeNode.class, entries.next().getClass());
      assertEquals(BTreeNode.class, entries.next().getClass());
      assertFalse(entries.hasNext());
      assertThrows(NoSuchElementException.class, () -> entries.next());
    }

    @Test
    void enumeration_remove() {
      Iterator<Integer> keys = tree.keysIterator();
      keys.next();
      keys.remove();
      assertFalse(tree.hasKey(1));
    }

    @Test
    void to_string() {
      assertEquals("{\n"
          + "\s\s\"1 -> one\",\n"
          + "\s\s\"3 -> three\",\n"
          + "\s\s\"4 -> four\",\n"
          + "\s\s\"5 -> five\",\n"
          + "\s\s\"2 -> two\",\n"
          + "}",
        tree.toString());
    }

  }

  @Nested
  class Many_Insertions {

    @Test
    void insertions() {
      tree = new BTree<>();

      tree.insert(1, "one");
      tree.insert(2, "two");
      tree.insert(3, "three");
      tree.insert(4, "four");
      tree.insert(5, "five");
      tree.insert(6, "six");;
      tree.insert(7, "seven");
      tree.insert(8, "eight");
      tree.insert(9, "nine");
      tree.insert(10, "ten");
      tree.insert(110, "110");
      tree.insert(120, "120");
      tree.insert(13, "thirteen");
      tree.insert(14, "fourteen");
      tree.insert(15, "fifteen");
      tree.insert(16, "sixteen");
      tree.insert(17, "seventeen");
      tree.insert(18, "eighteen");
      tree.insert(19, "nineteen");
      tree.insert(20, "twenty");

      assertEquals("one", tree.get(1));
      assertEquals("two", tree.get(2));
      assertEquals("three", tree.get(3));
      assertEquals("four", tree.get(4));
      assertEquals("five", tree.get(5));
      assertEquals("six", tree.get(6));
      assertEquals("seven", tree.get(7));

      tree.delete(3);
      tree.delete(4);

      assertEquals("eight", tree.get(8));
      assertEquals("nine", tree.get(9));
      assertEquals("ten", tree.get(10));
      assertEquals("110", tree.get(110));
      assertEquals("120", tree.get(120));
      assertEquals("thirteen", tree.get(13));
      assertEquals("fourteen", tree.get(14));
      assertEquals("fifteen", tree.get(15));
      assertEquals("sixteen", tree.get(16));

      tree.delete(110);
      tree.delete(14);

      assertEquals("seventeen", tree.get(17));
      assertEquals("eighteen", tree.get(18));
      assertEquals("nineteen", tree.get(19));
      assertEquals("twenty", tree.get(20));

      assertEquals("one", tree.get(1));
      assertEquals("two", tree.get(2));
      assertEquals("five", tree.get(5));
      assertEquals("six", tree.get(6));
      assertEquals("seven", tree.get(7));    
      assertEquals("eight", tree.get(8));
      assertEquals("nine", tree.get(9));
      assertEquals("ten", tree.get(10));
      assertEquals("120", tree.get(120));
      assertEquals("thirteen", tree.get(13));
      assertEquals("fifteen", tree.get(15));
      assertEquals("sixteen", tree.get(16));
      assertEquals("seventeen", tree.get(17));
      assertEquals("eighteen", tree.get(18));
      assertEquals("nineteen", tree.get(19));
      assertEquals("twenty", tree.get(20));

      tree.delete(5);
      tree.delete(1);
      tree.delete(20);
      tree.delete(13);
      tree.delete(9);
      tree.delete(6);

      assertEquals("two", tree.get(2));
      assertEquals("seven", tree.get(7));    
      assertEquals("eight", tree.get(8));
      assertEquals("ten", tree.get(10));
      assertEquals("120", tree.get(120));
      assertEquals("fifteen", tree.get(15));
      assertEquals("sixteen", tree.get(16));
      assertEquals("seventeen", tree.get(17));
      assertEquals("eighteen", tree.get(18));
      assertEquals("nineteen", tree.get(19));
    
    }
  }
}
