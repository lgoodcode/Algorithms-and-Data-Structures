package data_structures.tries.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.tries.TrieHash;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TrieHash_Test {
  TrieHash<Integer> trie;
  TrieHash<String> trie2;

  @Nested
  class When_New {

    @BeforeEach
    void create_trie() {
      trie = new TrieHash<>();
    }

    @Test
    void is_empty() {
      assertEquals(0, trie.size());
    }

    @Test
    void insertion() {
      trie.insert("one", 1);

      assertEquals(1, trie.get("one"));
    }

    @Test
    void null_on_nonexistent_word() {
      assertNull(trie.get("test"));
    }

    @Test
    void empty_array() {
      assertArrayEquals(new Object[0], trie.toArray());
    }

    @Test
    void empty_string() {
      assertEquals("{}", trie.toString());
    }

    @Test
    void findWords_is_empty() {
      assertArrayEquals(new String[0], trie.findWords());
      assertArrayEquals(new String[0], trie.findWords("a"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_words(String word) {
      trie2 = new TrieHash<>();

      assertThrows(IllegalArgumentException.class, () -> trie.insert(word, 1));
    }

    @Test
    void words_is_empty() {
      Iterator<String> words = trie.wordsIterator();
      assertFalse(words.hasNext());
      assertThrows(NoSuchElementException.class, () -> words.next());
      assertThrows(IllegalStateException.class, () -> words.remove());
    }

    @Test
    void values_is_empty() {
      Iterator<Integer> values = trie.valuesIterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }

  }

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      trie = new TrieHash<>();

      trie.insert("one", 1);
      trie.insert("two", 2);
      trie.insert("three", 3);
      trie.insert("four", 4);
      trie.insert("five", 5);
      trie.insert("boats", 6);
      trie.insert("boars", 7);
      trie.insert("boar", 8);
    }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals(1, trie.get("one")),
        () -> assertEquals(2, trie.get("two")),
        () -> assertEquals(3, trie.get("three")),
        () -> assertEquals(4, trie.get("four")),
        () -> assertEquals(5, trie.get("five")),
        () -> assertEquals(6, trie.get("boats")),
        () -> assertEquals(7, trie.get("boars")),
        () -> assertEquals(8, trie.get("boar"))
      );
    }

    @Test
    void clear() {
      trie.clear();
      assertEquals(0, trie.size());
      assertTrue(trie.isEmpty());
    }

    @Test
    void delete() {
      trie.delete("one");

      assertEquals(7, trie.size());
      assertNull(trie.get("one"));
      assertFalse(trie.hasWord("one"));
    }

    @Test
    void findWords() {
      String[] arr = { "five", "four" };
      assertArrayEquals(arr, trie.findWords("f"));
    }

    @Test
    void words() {
      Iterator<String> words = trie.wordsIterator();
      assertTrue(words.hasNext());
      assertEquals("one", words.next());
      assertEquals("three", words.next());
      assertEquals("two", words.next());
      assertEquals("boar", words.next());
      assertEquals("boars", words.next());
      assertEquals("boats", words.next());
      assertEquals("five", words.next());
      assertEquals("four", words.next());
      assertFalse(words.hasNext());
      assertThrows(NoSuchElementException.class, () -> words.next());
    }

    @Test
    void values() {
      Iterator<Integer> values = trie.valuesIterator();
      assertTrue(values.hasNext());
      assertEquals(1, values.next());
      assertEquals(3, values.next());
      assertEquals(2, values.next());
      assertEquals(8, values.next());
      assertEquals(7, values.next());
      assertEquals(6, values.next());
      assertEquals(5, values.next());
      assertEquals(4, values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void iterator_remove() {
      Iterator<Integer> values = trie.valuesIterator();
      assertTrue(values.hasNext());
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(1, values.next());
      assertEquals(3, values.next());
      assertDoesNotThrow(() -> values.remove());
      assertFalse(trie.hasWord("three"));
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals(2, values.next());
      assertEquals(8, values.next());
      assertEquals(7, values.next());
      assertEquals(6, values.next());
      assertEquals(5, values.next());
      assertEquals(4, values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertEquals(7, trie.size());
    }

    @Test
    void toArray() {
      Object[] arr = { 1, 3, 2, 8, 7, 6, 5, 4 };
      assertArrayEquals(arr, trie.toArray());
    }
    
    @Test
    void to_string() {
      assertEquals("{\n"
      + "\s\sone -> 1,\n"
      + "\s\sthree -> 3,\n"
      + "\s\stwo -> 2,\n"
      + "\s\sboar -> 8,\n"
      + "\s\sboars -> 7,\n"
      + "\s\sboats -> 6,\n"
      + "\s\sfive -> 5,\n"
      + "\s\sfour -> 4\n"
          + "}",
        trie.toString());
    }
  }

  @Nested
  class Close_Words {

    @BeforeEach
    void prep() {
      trie = new TrieHash<>();

      trie.insert("boa", 1);
      trie.insert("boat", 2);
      trie.insert("boar", 3);
      trie.insert("boats", 4);
    }

    @Test
    void delete_parent() {
      trie.delete("boat");
      assertEquals(4, trie.get("boats"));
    }

    @Test
    void delete_leaf() {
      trie.delete("boats");
      assertEquals(2, trie.get("boat"));
      assertNull(trie.get("boats"));
    }

    @Test
    void findWords() {
      String[] words = { "boa", "boar", "boat", "boats" };
      assertArrayEquals(words, trie.findWords("boa"));
      assertArrayEquals(words, trie.findWords());
    }
  }

  @Nested
  class Many_Insertions {

    @Test
    void insertions() {
      trie = new TrieHash<>();

      trie.insert("one", 1);
      trie.insert("two", 2);
      trie.insert("three", 3);
      trie.insert("four", 4);
      trie.insert("five", 5);
      trie.insert("six", 6);
      trie.insert("seven", 7);
      trie.insert("eight", 8);
      trie.insert("nine", 9);
      trie.insert("ten", 10);
      trie.insert("one-hundred-ten", 110);
      trie.insert("one-hundred-twenty", 120);
      trie.insert("thirteen", 13);
      trie.insert("fourteen", 14);
      trie.insert("fifteen", 15);
      trie.insert("sixteen", 16);
      trie.insert("seventeen", 17);
      trie.insert("eighteen", 18);
      trie.insert("nineteen", 19);
      trie.insert("twenty", 20);

      String[] arr = { "six", "sixteen", "seven", "seventeen" };
      assertArrayEquals(arr, trie.findWords("s"));

      assertArrayEquals(new String[0], trie.findWords("sh"));


      assertNotNull(trie.get("one"));
      assertNotNull(trie.get("two"));
      assertNotNull(trie.get("three"));
      assertNotNull(trie.get("four"));
      assertNotNull(trie.get("five"));
      assertNotNull(trie.get("six"));
      assertNotNull(trie.get("seven"));

      trie.delete("three");
      trie.delete("four");

      assertNotNull(trie.get("eight"));
      assertNotNull(trie.get("nine"));
      assertNotNull(trie.get("ten"));
      assertEquals(110, trie.get("onehundredten"));
      assertNotNull(trie.get("onehundredtwenty"));
      assertNotNull(trie.get("thirteen"));
      assertNotNull(trie.get("fourteen"));
      assertNotNull(trie.get("fifteen"));
      assertNotNull(trie.get("sixteen"));

      trie.delete("onehundredten");
      trie.delete("fourteen");

      assertNotNull(trie.get("seventeen"));
      assertNotNull(trie.get("eighteen"));
      assertNotNull(trie.get("nineteen"));
      assertNotNull(trie.get("twenty"));

      assertNotNull(trie.get("one"));
      assertNotNull(trie.get("two"));
      assertNotNull(trie.get("five"));
      assertNotNull(trie.get("six"));
      assertNotNull(trie.get("seven"));
      assertNotNull(trie.get("eight"));
      assertNotNull(trie.get("nine"));
      assertNotNull(trie.get("ten"));
      assertNotNull(trie.get("onehundredtwenty"));
      assertNotNull(trie.get("thirteen"));
      assertNotNull(trie.get("fifteen"));
      assertNotNull(trie.get("sixteen"));
      assertNotNull(trie.get("eighteen"));
      assertNotNull(trie.get("nineteen"));
      assertNotNull(trie.get("twenty"));
    }
  }
}
