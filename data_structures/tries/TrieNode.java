package data_structures.tries;

public interface TrieNode<T> {
  /**
   * The key to identify the root node which is the whitespace character,
   * {@code \s}. This is chosen because any parameter is checked and prevents a
   * blank string to ensure that the root cannot be retrieved.
   */
  char ROOT = '\s';

  char getKey();

  T getValue();

  void setValue(T value);

  TrieNode<T> getParent();

  /**
   * Determines whether the current node contains a child node that has a word
   * with a value.
   *
   * @return if the node has a child node with a word
   */
  boolean hasWord();

  void setHasWord(boolean hasWord);

  /**
   * Determines whether the current node is a word with a corresponding value.
   *
   * @return if the node has a word value
   */
  boolean isWord();

  /**
   * Determines whether the current {@code TrieNode} is the {@code root} of the
   * {@code Trie} by checking if the key is a whitespace character {@code '\s'}.
   *
   * @return if the node is the {@code root} or not
   */
  boolean isRoot();

  /**
   * Returns the child {@code TrieNode} for the given character.
   *
   * @param c the character of the child to get
   * @return the child {@code TrieNode}
   */
  TrieNode<T> getChild(char c);

  /**
   * Sets a new child {@code TrieNode} for the specified character index.
   *
   * @param c     the character index slot to insert the new child
   * @param child the new {@TrieNode} child
   */
  void setChild(char c, TrieNode<T> child);

  /**
   * Removes a child {@code TrieNode}.
   *
   * @param c the character key of the child node to remove
   */
  void removeChild(char c);

  /**
   * Gets the array of of children nodes.
   *
   * @return the array of children nodes
   */
  TrieNode<T>[] getChildren();
}
