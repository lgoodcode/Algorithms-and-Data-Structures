package data_structures.trees.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import data_structures.trees.TreeNode;
import data_structures.trees.BinaryTree.BinarySearchTree;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BinarySearchTree_Test {
  BinarySearchTree<Integer, String> tree;
  BinarySearchTree<String, String> tree2;
  TreeNode<Integer, String> node;
  TreeNode<Integer, String> node2;

  @Test
  void is_instantiated() {
    tree  = new BinarySearchTree<>();
  }

  @Test
  void tree_node_instantiates() {
    node = new TreeNode<>(1, "one");
  }

  @Nested 
  class When_New {
    
    @BeforeEach
    void create_tree() {
      tree = new BinarySearchTree<>();
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
      tree2 = new BinarySearchTree<>();

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
      tree = new BinarySearchTree<>();

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
      node = new TreeNode<>(4, "four");
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
      tree.postorderTreeWalk((TreeNode<Integer, String> node) -> 
        str.append(node.toString() + "\n"));
      
      assertEquals("Key: 5, value: five\n"
          + "Key: 4, value: four\n"
          + "Key: 3, value: three\n"
          + "Key: 2, value: two\n"
          + "Key: 1, value: one\n", 
        str.toString());
    }

    @Test
    void to_string() {
      assertEquals("{"
          + "\n\"Key: 1, value: one\""  
          + "\n\"Key: 2, value: two\""  
          + "\n\"Key: 3, value: three\""  
          + "\n\"Key: 4, value: four\""  
          + "\n\"Key: 5, value: five\""
          + "\n}", 
        tree.toString()
      );
    }

  }
}
