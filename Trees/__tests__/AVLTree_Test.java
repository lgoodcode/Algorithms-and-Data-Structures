package data_structures.trees.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import data_structures.trees.AVLTree;
import data_structures.trees.AVLTreeNode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AVLTree_Test {
  AVLTree<Integer, String> tree;
  AVLTree<String, String> tree2;
  AVLTreeNode<Integer, String> node;
  AVLTreeNode<Integer, String> node2;

  @Test
  void is_instantiated() {
    tree  = new AVLTree<>();
  }

  @Test
  void tree_node_instantiates() {
    node = new AVLTreeNode<>(1, "one");
  }

  @Nested 
  class When_New {
    
    @BeforeEach
    void create_tree() {
      tree = new AVLTree<>();
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
    void null_on_nonexistent_key() {
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
      assertThrows(IllegalArgumentException.class, () -> tree.delete(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_keys(String key) {
      tree2 = new AVLTree<>();

      assertThrows(IllegalArgumentException.class, () -> tree2.insert(key, "test"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      assertThrows(IllegalArgumentException.class, () -> tree.insert(1, value));
    }

  }

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      tree = new AVLTree<>();

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
    void search() {
      node = new AVLTreeNode<>(4, "four");
      node2 = tree.search(4);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }

    @Test
    void minimum() {
      assertEquals(1, tree.minimum().getKey());
    }

    @Test
    void maximum() {
      assertEquals(5, tree.maximum().getKey());
    }

    @Test 
    void delete() {
      tree.delete(1);

      assertEquals(4, tree.size());
      assertNull(tree.get(1));
    } 

    @Test
    void predecessor() {
      assertEquals(2, tree.predecessor(tree.search(3)).getKey());
    }

    @Test
    void successor() {
      assertEquals(4, tree.successor(tree.search(3)).getKey());
    }

    @Test
    void postorderTreeWalk() {
      StringBuilder str = new StringBuilder();
      tree.postorderTreeWalk((AVLTreeNode<Integer, String> node) -> 
        str.append(node.toString() + "\n"));
      
      assertEquals(
            "1 -> one\n"
          + "3 -> three\n"
          + "5 -> five\n"
          + "4 -> four\n"
          + "2 -> two\n",
        str.toString());
    }

    @Test
    void to_string() {
      assertEquals("{\n"
          + "\s\s\"1 -> one\",\n"
          + "\s\s\"2 -> two\",\n"
          + "\s\s\"3 -> three\",\n"
          + "\s\s\"4 -> four\",\n"
          + "\s\s\"5 -> five\",\n"
          + "}",
        tree.toString());
    }

  }

  @Nested
  class Many_Insertions {

    @Test
    void insertions() {
      tree = new AVLTree<>();

      tree.insert(1, "one");
      tree.insert(2, "two");
      tree.insert(3, "three");
      tree.insert(4, "four");
      tree.insert(5, "five");
      tree.insert(6, "six");
      tree.insert(7, "seven");
      tree.insert(8, "eight");
      tree.insert(9, "nine");
      tree.insert(10, "ten");
      tree.insert(110, "eleven");
      tree.insert(120, "twelve");
      tree.insert(13, "thirteen");
      tree.insert(14, "fourteen");
      tree.insert(15, "fifteen");
      tree.insert(16, "sixteen");
      tree.insert(17, "seventeen");
      tree.insert(18, "eighteen");
      tree.insert(19, "nineteen");
      tree.insert(20, "twenty");

      assertNotNull(tree.get(1));
      assertNotNull(tree.get(2));
      assertNotNull(tree.get(3));
      assertNotNull(tree.get(4));
      assertNotNull(tree.get(5));
      assertNotNull(tree.get(6));
      assertNotNull(tree.get(7));

      tree.delete(3);
      tree.delete(4);

      assertNotNull(tree.get(8));
      assertNotNull(tree.get(9));
      assertNotNull(tree.get(10));
      assertNotNull(tree.get(110));
      assertNotNull(tree.get(120));
      assertNotNull(tree.get(13));
      assertNotNull(tree.get(14));
      assertNotNull(tree.get(15));
      assertNotNull(tree.get(16));

      tree.delete(110);
      tree.delete(14);

      assertNotNull(tree.get(17));
      assertNotNull(tree.get(18));
      assertNotNull(tree.get(19));
      assertNotNull(tree.get(20));

      assertNotNull(tree.get(1));
      assertNotNull(tree.get(2));
      assertNotNull(tree.get(5));
      assertNotNull(tree.get(6));
      assertNotNull(tree.get(7));    
      assertNotNull(tree.get(8));
      assertNotNull(tree.get(9));
      assertNotNull(tree.get(10));
      assertNotNull(tree.get(120));
      assertNotNull(tree.get(13));
      assertNotNull(tree.get(15));
      assertNotNull(tree.get(16));
      assertNotNull(tree.get(17));
      assertNotNull(tree.get(18));
      assertNotNull(tree.get(19));
      assertNotNull(tree.get(20));
    }
  }
}
