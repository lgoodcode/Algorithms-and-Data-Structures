package data_structures.trees.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import data_structures.trees.VanEmdeBoasTree;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class VanEmdeBoasTree_Test {
  VanEmdeBoasTree<Integer> tree;
  int size = 8;

  @Test
  void is_instantiated() {
    new VanEmdeBoasTree<>(size);
  }

  @Test
  void throws_on_bad_universe_size() {
    assertThrows(IllegalArgumentException.class, () -> new VanEmdeBoasTree<>(10));
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

    @Test
    void empty_tree_string() {
      assertEquals("{}", tree.toString());
    }

    @Test
    void empty_array() {
      assertArrayEquals(new Object[0], tree.toArray());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 8 })
    void insert_throws_on_bad_keys(int key) {
      assertThrows(IllegalArgumentException.class, () -> tree.insert(key, 1));
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
      Iterator<Integer> values = tree.valuesIterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }

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

    @Test
    void preorderTreeWalk() {
      StringBuilder sb = new StringBuilder("[");

      tree.preorderTreeWalk((el) -> sb.append(el.toString() + ", "));

      assertEquals("[7, 6, 5, 4, 3, 2, 1, 0]", sb.substring(0, sb.length() - 2) + "]");
    }

    @Test
    void preorderTreeWalk_with_keys() {
      StringBuilder sb = new StringBuilder("{\n");

      tree.preorderTreeWalk((k, el) -> sb.append("\s\s" + k + " -> " + el.toString() + ",\n"));

      assertEquals("{\n"
      + "\s\s7 -> 7,\n"
      + "\s\s6 -> 6,\n"
      + "\s\s5 -> 5,\n"
      + "\s\s4 -> 4,\n"
      + "\s\s3 -> 3,\n"
      + "\s\s2 -> 2,\n"
      + "\s\s1 -> 1,\n"
      + "\s\s0 -> 0\n"
      + "}",
      sb.substring(0, sb.length() - 2) + "\n}");
    }

    @Test
    void keys() {
      Iterator<Integer> keys = tree.keysIterator();
      assertTrue(keys.hasNext());
      assertEquals(0, keys.next());
      assertEquals(1, keys.next());
      assertEquals(2, keys.next());
      assertEquals(3, keys.next());
      assertEquals(4, keys.next());
      assertEquals(5, keys.next());
      assertEquals(6, keys.next());
      assertEquals(7, keys.next());
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
    }

    @Test
    void values() {
      Iterator<Integer> values = tree.valuesIterator();
      assertTrue(values.hasNext());
      assertEquals(0, values.next());
      assertEquals(1, values.next());
      assertEquals(2, values.next());
      assertEquals(3, values.next());
      assertEquals(4, values.next());
      assertEquals(5, values.next());
      assertEquals(6, values.next());
      assertEquals(7, values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void iterator_remove() {
      Iterator<Integer> keys = tree.keysIterator();
      assertTrue(keys.hasNext());
      assertThrows(IllegalStateException.class, () -> keys.remove());
      assertEquals(0, keys.next());
      assertEquals(1, keys.next());
      assertEquals(2, keys.next());
      assertDoesNotThrow(() -> keys.remove());
      assertFalse(tree.member(2));
      assertThrows(IllegalStateException.class, () -> keys.remove());
      assertEquals(3, keys.next());
      assertEquals(4, keys.next());
      assertEquals(5, keys.next());
      assertEquals(6, keys.next());
      assertEquals(7, keys.next());
      assertFalse(keys.hasNext());
      assertThrows(NoSuchElementException.class, () -> keys.next());
      assertEquals(7, tree.size());
    }

    @Test
    void toString_full() {
      assertEquals("{\n"
          + "\s\s0 -> 0,\n"
          + "\s\s1 -> 1,\n"
          + "\s\s2 -> 2,\n"
          + "\s\s3 -> 3,\n"
          + "\s\s4 -> 4,\n"
          + "\s\s5 -> 5,\n"
          + "\s\s6 -> 6,\n"
          + "\s\s7 -> 7\n"
          + "}",
        tree.toString());
    }

    @Test
    void toString_partial() {
      tree.delete(2);
      tree.delete(3);

      assertEquals("{\n"
          + "\s\s0 -> 0,\n"
          + "\s\s1 -> 1,\n"
          + "\s\s4 -> 4,\n"
          + "\s\s5 -> 5,\n"
          + "\s\s6 -> 6,\n"
          + "\s\s7 -> 7\n"
          + "}",
        tree.toString());
    }

    @Test
    void toArray() {
      Object[] arr = { 0, 1, 2, 3, 4, 5, 6, 7 };
      assertArrayEquals(arr, tree.toArray());
    }

  }

  @Nested
  class Many_Insertions {

    @Test
    void insertions() {
      VanEmdeBoasTree<String> tree = new VanEmdeBoasTree<>(32);

      tree.insert(1, "one");
      tree.insert(3, "three");
      tree.insert(4, "four");
      tree.insert(5, "five");
      tree.insert(6, "six");;
      tree.insert(9, "nine");
      tree.insert(7, "seven");
      tree.insert(8, "eight");
      tree.insert(10, "ten");
      tree.insert(11, "11");
      tree.insert(12, "12");
      tree.insert(13, "thirteen");
      tree.insert(14, "fourteen");
      tree.insert(15, "fifteen");
      tree.insert(2, "two");
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
      assertEquals("11", tree.get(11));
      assertEquals("12", tree.get(12));
      assertEquals("thirteen", tree.get(13));
      assertEquals("fourteen", tree.get(14));
      assertEquals("fifteen", tree.get(15));
      assertEquals("sixteen", tree.get(16));

      tree.delete(11);
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
      assertEquals("12", tree.get(12));
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
      assertEquals("12", tree.get(12));
      assertEquals("fifteen", tree.get(15));
      assertEquals("sixteen", tree.get(16));
      assertEquals("seventeen", tree.get(17));
      assertEquals("eighteen", tree.get(18));
      assertEquals("nineteen", tree.get(19));

      tree.insert(3, "three");
      tree.insert(4, "four");

      assertEquals("three", tree.get(3));
      assertEquals("four", tree.get(4));
    }
  }
}
