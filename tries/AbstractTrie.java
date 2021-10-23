package data_structures.tries;

import java.util.function.Consumer;

public abstract class AbstractTrie<V> {
  /**
   * Counter tracking the number of entries in the tree.
   */
  protected int count;

 /**
   * Checks the word to make sure it isn't {@code null} or blank.
   * 
   * @param word the word to check
   * 
   * @throws IllegalArgumentException if the word is {@code null}, or blank
   */
  protected final void checkWord(String word) {
    if (word == null || word.isBlank())
      throw new IllegalArgumentException("Word cannot be null or blank.");
  }

  /**
   * Checks the value to make sure it isn't {@code null} or blank
   * 
   * @param value the value to check
   * 
   * @throws IllegalArgumentException if the value is {@code null} or blank
   */
  protected final void checkValue(V value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
  }

  /**
   * Receives the input word and removes any whitespace {@code \s}, newline
   * {@code \n}, or tab {@code \t} characters as well as dashes {@code -} and
   * underscores {@code _}.
   * 
   * <p>
   * It makes a call to {@link #checkWord(String)} after parsing the word 
   * because any word input will always be parsed so we check that value
   * instead of the initial input.
   * </p>
   * 
   * @param word the word to parse
   * @returns the parsed word
   */
  protected final String parseWord(String word) {
    String parsed = word.strip().replaceAll("[\n\s\t-_]", "");
    checkWord(parsed);
    return parsed;
  }

  /**
   * Returns the number of words stored in the tree.
   * 
   * @return the number of nodes
   */
  public final int size() {
    return count;
  }

  /**
   * Returns a boolean value indicating whether the tree is empty or not.
   * 
   * @return whether the tree is empty
   */
  public final boolean isEmpty() {
    return count == 0;
  }

  /**
   * Inserts a new tree node into the tree with the given word and value.
   * 
   * @param word  the word of the new node to insert
   * @param value the value of the new node to insert
   * 
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank, or if the word already exists in the
   *                                  tree
   */ 
  public abstract void insert(String word, V value);

  /**
   * Returns a boolean indicating whether the tree contains an entry with the
   * specified word.
   *
   * @param word the word to search for
   * @return whether a node in the tree contains the specified word
   *
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   */
  public abstract boolean hasWord(String word);  

  /**
   * Retrieves the value for the corresponding tree node of the given word or
   * {@code null} if not found.
   * 
   * @param word the word of the tree node
   * @return the value or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank
   */
  public abstract V get(String word);

  /**
   * Deletes a word value from the trie if it exists.
   * 
   * @param word the word of the tree node to delete
   * 
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank
   */
  public abstract void delete(String word);

  @SuppressWarnings("unchecked")
  protected final <Node extends TrieNode<V>> void walk(Node node, Consumer<Node> callback) {
    Node[] children = (Node[]) node.getChildren();

    for (int i=0; i<children.length; i++) {
      if (children[i] != null)
        walk(node, callback);
    }
  }
}
