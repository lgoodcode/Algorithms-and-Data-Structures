package data_structures.tries;

public class TrieNode<V> extends AbstractTrieNode<V> {
  /**
   * The array of the child {@code TrieNodes}.
   */
  private TrieNode<V>[] children;

  /**
   * The parent {@code TrieNode} used for tracing up to the root.
   */
  protected TrieNode<V> parent;

  /**
   * Initializes an empty {@code TrieNode} with the specified key. The key is used
   * to determine what position the node is in and what words it could contain.
   *
   * @param key the character key to set the {@code TrieNode}
   */
  @SuppressWarnings("unchecked")
  public TrieNode(char key, TrieNode<V> parent) {
    children = (TrieNode<V>[]) new TrieNode<?>[26];
    this.key = key;
    this.parent = parent;
    hasWord = false;
  }

  /**
   * Initializes the root node with {@code '\s'} as the key and {@code null}
   * value.
   */
  @SuppressWarnings("unchecked")
  protected TrieNode() {
    children = (TrieNode<V>[]) new TrieNode<?>[26];
    key = '\s';
    hasWord = false;
  }

  /**
   * Returns the child {@code TrieNode} for the given character.
   *
   * @param c the character of the {{@code TrieNode} child to get
   * @return the child {@code TrieNode}
   */
  @SuppressWarnings("unchecked")
  public TrieNode<V> getChild(char c) {
    return children[getIndex(c)];
  }

  /**
   * Sets a new child {@code TrieNode} for the specified character index.
   *
   * @param c     the character index slot to insert the new child
   * @param child the new {@TrieNode} child
   */
  @SuppressWarnings("unchecked")
  public <Node extends AbstractTrieNode<V>> void setChild(char c, Node child) {
    children[getIndex(c)] = (TrieNode<V>) child;
  }

  /**
   * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
   * with a word or is a word.
   *
   * @param c the character key of the {@code TrieNode} to remove
   */
  protected void removeChild(char c) {
    children[getIndex(c)] = null;
  }

  /**
   * Gets the array of {@code TrieNode} children. Used for iterating in
   * {@code delete()} of the {@code Trie} when tracing up to the root, removing
   * any empty nodes, and in {@code Trie.toString()}.
   *
   * @return the children
   */
  @SuppressWarnings("unchecked")
  protected <Node extends AbstractTrieNode<V>> Node[] getChildren() {
    return (Node[]) children;
  }

}