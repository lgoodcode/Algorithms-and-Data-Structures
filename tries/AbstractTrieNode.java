package data_structures.tries;

public abstract class AbstractTrieNode<V> {
  /**
   * Whether the current node is a word or contains a child that is or contains
   * word(s).
   */
  protected boolean hasWord;

  /**
   * The key of the node.
   */
  protected char key;

  /**
   * The value of the node.
   */
  private V value;

  /**
   * Internal method to get the numeric value of the character to map to the
   * proper index location based on the letter in the range of {@code [A, Z]}
   * case-insensitive. The numeric index value from the given character is in the
   * range {@code [0, 25]}.
   *
   * @param c the character to get the index of
   * @return the index of the character in the range of {@code [0, 25]}
   */
  protected final int getIndex(char c) {
    int index = Character.getNumericValue(c);

    if (index < 10 || index > 35)
      throw new IllegalArgumentException("Character must be a letter A-Z, case-insensitive.");
    return index - 10;
  }

  /**
   * Determines whether the current word is a word with a value.
   *
   * @return if the node has a word value
   */
  public final boolean isWord() {
    return value != null;
  }

  /**
   * Gets the value for the node
   *
   * @return the value of the node or {@code null} if none
   */
  public final V getValue() {
    return value;
  }

  /**
   * Sets the value for the node
   *
   * @param value the value to set
   */
  public final void setValue(V value) {
    this.value = value;
  }

  /**
   * Returns the child {@code TrieNode} for the given character.
   *
   * @param c the character of the child to get
   * @return the child {@code TrieNode}
   */
  protected abstract <Node extends AbstractTrieNode<V>> Node getChild(char c);

  /**
   * Sets a new child {@code TrieNode} for the specified character index.
   *
   * @param c     the character index slot to insert the new child
   * @param child the new {@TrieNode} child
   */
  protected abstract <Node extends AbstractTrieNode<V>> void setChild(char c, Node child);

  /**
   * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
   * with a word or is a word.
   *
   * @param c the character key of the child {@code TrieNode} to remove
   */
  protected abstract void removeChild(char c);

  /**
   * Gets the array of {@code TrieNode} children. Used for iterating in
   * {@code delete()} of the {@code Trie} whne tracing up to the root, removing
   * any empty nodes, and in {@code Trie.toString()}.
   *
   * @return the {@code TrieNode} children
   */
  protected abstract <Node extends AbstractTrieNode<V>> Node[] getChildren();

}