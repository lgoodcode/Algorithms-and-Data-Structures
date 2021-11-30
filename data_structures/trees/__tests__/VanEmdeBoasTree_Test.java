package data_structures.trees.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.*;

import data_structures.trees.VanEmdeBoasTree;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class VanEmdeBoasTree_Test {
  VanEmdeBoasTree<Integer> tree;
  int size = 8;

  @Test
  void is_instantiated() {
    tree  = new VanEmdeBoasTree<>(size);
  }

  @Nested 
  class When_New {
    
    @BeforeEach
    void create_tree() {
      tree = new VanEmdeBoasTree<>(size);
    }

    @Test 
    void isEmpty() {
      assertTrue(tree.isEmpty());
    }

    @Test
    void size_is_zero() {
      assertEquals(0, tree.size());
    }

    @Test
    void universe() {
      assertEquals(size, tree.universe());
    }

    @Test
    void single_insertion() {
      tree.insert(1, 1);
      
      assertEquals(1, tree.minimum());
      assertEquals(1, tree.maximum());
      assertEquals(1, tree.get(1));
      assertNull(tree.get(5));
    }

    @Test
    void throws_on_duplicate_key() {
      tree.insert(1, 1);
      assertThrows(IllegalArgumentException.class, () -> tree.insert(1, 2));
    }

    @Test
    void member_is_false() {
      assertFalse(tree.member(5));
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
    void successor_throws() {
      assertThrows(NoSuchElementException.class, () -> tree.successor(2));
    }

    // @Test
    // void empty_tree_string() {
    //   assertEquals("{}", tree.toString());
    // }

    // @Test
    // void delete_throws() {
    //   assertThrows(IllegalArgumentException.class, () -> tree.delete(null));
    // }

    // @ParameterizedTest
    // @NullAndEmptySource
    // @ValueSource(strings = { " ", "  ", "\t", "\n" })
    // void insert_throws_on_bad_keys(String key) {
    //   tree2 = new AVLTree<>();

    //   assertThrows(IllegalArgumentException.class, () -> tree2.insert(key, "test"));
    // }

    // @ParameterizedTest
    // @NullAndEmptySource
    // @ValueSource(strings = { " ", "  ", "\t", "\n" })
    // void insert_throws_on_bad_values(String value) {
    //   assertThrows(IllegalArgumentException.class, () -> tree.insert(1, value));
    // }

    // @Test
    // void keys_is_empty() {
    //   Iterator<Integer> keys = tree.keysIterator();
    //   assertFalse(keys.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> keys.next());
    //   assertThrows(IllegalStateException.class, () -> keys.remove());
    // }

    // @Test
    // void values_is_empty() {
    //   Iterator<String> values = tree.valuesIterator();
    //   assertFalse(values.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> values.next());
    //   assertThrows(IllegalStateException.class, () -> values.remove());
    // }

    // @Test
    // void entries_is_empty() {
    //   Iterator<AVLTree.Node<Integer, String>> entries = tree.entriesIterator();
    //   assertFalse(entries.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> entries.next());
    //   assertThrows(IllegalStateException.class, () -> entries.remove());
    // }

  }

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      tree = new VanEmdeBoasTree<>(size);

      tree.insert(0, 0);
      tree.insert(7, 7);
      tree.insert(2, 2);
      tree.insert(6, 6);
      tree.insert(4, 4);
      tree.insert(3, 3);
      tree.insert(1, 1);
      tree.insert(5, 5);
    }

    @Test
    void all_inserts_succeed() {
      assertTrue(tree.member(0));
      assertTrue(tree.member(1));
      assertTrue(tree.member(2));
      assertTrue(tree.member(3));
      assertTrue(tree.member(4));
      assertTrue(tree.member(5));
      assertTrue(tree.member(6));
      assertTrue(tree.member(7));
    }

    @Test
    void clear() {
      tree.clear();
      assertEquals(0, tree.size());
      assertTrue(tree.isEmpty());
    }

    @Test
    void get() {
      assertEquals(0, tree.get(0));
      assertEquals(1, tree.get(1));
      assertEquals(2, tree.get(2));
      assertEquals(3, tree.get(3));
      assertEquals(4, tree.get(4));
      assertEquals(5, tree.get(5));
      assertEquals(6, tree.get(6));
      assertEquals(7, tree.get(7));    
    }

    @Test
    void minimum() {
      assertEquals(0, tree.minimum());
    }

    @Test
    void maximum() {
      assertEquals(7, tree.maximum());
    }
    
    @Test
    void successor() {
      assertEquals(1, tree.successor(0));
      assertEquals(2, tree.successor(1));
      assertEquals(3, tree.successor(2));
      assertEquals(4, tree.successor(3));
      assertEquals(5, tree.successor(4));
      assertEquals(6, tree.successor(5));
      assertEquals(7, tree.successor(6));
      assertEquals(-1, tree.successor(7));
    }

    @Test
    void predecessor() {
      assertEquals(-1, tree.predecessor(0));
      assertEquals(0, tree.predecessor(1));
      assertEquals(1, tree.predecessor(2));
      assertEquals(2, tree.predecessor(3));
      assertEquals(3, tree.predecessor(4));
      assertEquals(4, tree.predecessor(5));
      assertEquals(5, tree.predecessor(6));
      assertEquals(6, tree.predecessor(7));
    }

    @Test 
    void delete() {
      tree.delete(0);
      tree.delete(1);
      tree.delete(3);
      tree.delete(7);
      tree.delete(2);
      tree.delete(6);
      tree.delete(5);
      tree.delete(4);
    } 

    @Test 
    void delete_minimums() {
      tree.delete(0);
      tree.delete(1);
      tree.delete(2);
      tree.delete(3);
      tree.delete(4);
      tree.delete(5);
      tree.delete(6);
      tree.delete(7);
    } 

    @Test 
    void delete_maximums() {
      tree.delete(7);
      tree.delete(6);
      tree.delete(5);
      tree.delete(4);
      tree.delete(3);
      tree.delete(2);
      tree.delete(1);
      tree.delete(0);
    } 

    // @Test
    // void postorderTreeWalk() {
    //   StringBuilder str = new StringBuilder();
    //   tree.postorderTreeWalk((node) -> 
    //     str.append(node.toString() + "\n"));
      
    //   assertEquals(
    //         "1 -> one\n"
    //       + "3 -> three\n"
    //       + "5 -> five\n"
    //       + "4 -> four\n"
    //       + "2 -> two\n",
    //     str.toString());
    // }

    // @Test
    // void keys() {
    //   Iterator<Integer> keys = tree.keysIterator();
    //   assertTrue(keys.hasNext());
    //   assertEquals(1, keys.next());
    //   assertEquals(2, keys.next());
    //   assertEquals(3, keys.next());
    //   assertEquals(4, keys.next());
    //   assertEquals(5, keys.next());
    //   assertFalse(keys.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> keys.next());
    // }

    // @Test
    // void values() {
    //   Iterator<String> values = tree.valuesIterator();
    //   assertTrue(values.hasNext());
    //   assertEquals("one", values.next());
    //   assertEquals("two", values.next());
    //   assertEquals("three", values.next());
    //   assertEquals("four", values.next());
    //   assertEquals("five", values.next());
    //   assertFalse(values.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> values.next());
    // }

    // @Test
    // void entries() {
    //   Iterator<AVLTree.Node<Integer, String>> entries = tree.entriesIterator();
    //   assertTrue(entries.hasNext());
    //   assertEquals("one", entries.next().getValue());
    //   assertEquals("two", entries.next().getValue());
    //   assertEquals("three", entries.next().getValue());
    //   assertEquals("four", entries.next().getValue());
    //   assertEquals("five", entries.next().getValue());
    //   assertFalse(entries.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> entries.next());
    // }

    // @Test
    // void iterator_remove() {
    //   Iterator<Integer> keys = tree.keysIterator();
    //   assertTrue(keys.hasNext());
    //   assertThrows(IllegalStateException.class, () -> keys.remove());
    //   assertEquals(1, keys.next());
    //   assertEquals(2, keys.next());
    //   assertDoesNotThrow(() -> keys.remove());
    //   assertFalse(tree.hasKey(2));
    //   assertThrows(IllegalStateException.class, () -> keys.remove());
    //   assertEquals(3, keys.next());
    //   assertEquals(4, keys.next());
    //   assertEquals(5, keys.next());
    //   assertFalse(keys.hasNext());
    //   assertThrows(NoSuchElementException.class, () -> keys.next());
    //   assertEquals(4, tree.size());
    // }

    // @Test
    // void to_string() {
    //   assertEquals("{\n"
    //       + "\s\s\"1 -> one\",\n"
    //       + "\s\s\"2 -> two\",\n"
    //       + "\s\s\"3 -> three\",\n"
    //       + "\s\s\"4 -> four\",\n"
    //       + "\s\s\"5 -> five\",\n"
    //       + "}",
    //     tree.toString());
    // }

  }

  @Nested
  class Many_Insertions {

    // @Test
    // void insertions() {
    //   tree = new AVLTree<>();

    //   tree.insert(1, "one");
    //   tree.insert(2, "two");
    //   tree.insert(3, "three");
    //   tree.insert(4, "four");
    //   tree.insert(5, "five");
    //   tree.insert(6, "six");
    //   tree.insert(7, "seven");
    //   tree.insert(8, "eight");
    //   tree.insert(9, "nine");
    //   tree.insert(10, "ten");
    //   tree.insert(110, "eleven");
    //   tree.insert(120, "twelve");
    //   tree.insert(13, "thirteen");
    //   tree.insert(14, "fourteen");
    //   tree.insert(15, "fifteen");
    //   tree.insert(16, "sixteen");
    //   tree.insert(17, "seventeen");
    //   tree.insert(18, "eighteen");
    //   tree.insert(19, "nineteen");
    //   tree.insert(20, "twenty");

    //   assertNotNull(tree.get(1));
    //   assertNotNull(tree.get(2));
    //   assertNotNull(tree.get(3));
    //   assertNotNull(tree.get(4));
    //   assertNotNull(tree.get(5));
    //   assertNotNull(tree.get(6));
    //   assertNotNull(tree.get(7));

    //   tree.delete(3);
    //   tree.delete(4);

    //   assertNotNull(tree.get(8));
    //   assertNotNull(tree.get(9));
    //   assertNotNull(tree.get(10));
    //   assertNotNull(tree.get(110));
    //   assertNotNull(tree.get(120));
    //   assertNotNull(tree.get(13));
    //   assertNotNull(tree.get(14));
    //   assertNotNull(tree.get(15));
    //   assertNotNull(tree.get(16));

    //   tree.delete(110);
    //   tree.delete(14);

    //   assertNotNull(tree.get(17));
    //   assertNotNull(tree.get(18));
    //   assertNotNull(tree.get(19));
    //   assertNotNull(tree.get(20));

    //   assertNotNull(tree.get(1));
    //   assertNotNull(tree.get(2));
    //   assertNotNull(tree.get(5));
    //   assertNotNull(tree.get(6));
    //   assertNotNull(tree.get(7));    
    //   assertNotNull(tree.get(8));
    //   assertNotNull(tree.get(9));
    //   assertNotNull(tree.get(10));
    //   assertNotNull(tree.get(120));
    //   assertNotNull(tree.get(13));
    //   assertNotNull(tree.get(15));
    //   assertNotNull(tree.get(16));
    //   assertNotNull(tree.get(17));
    //   assertNotNull(tree.get(18));
    //   assertNotNull(tree.get(19));
    //   assertNotNull(tree.get(20));
    // }
  }
}
