package data_structures.tries;

import data_structures.hashtables.DoubleHashing;

public class TrieHashNode<V> extends AbstractTrieNode<V> {
  /**
   * The hashtable containing the child {@code TrieHashNodes}.
   */
  protected DoubleHashing<Integer, TrieHashNode<V>> children;

  /**
   * The parent {@code TrieHashNode} used for tracing up to the root.
   */
  protected TrieHashNode<V> parent;

 /**
   * Initializes an empty node with the specified key. The key is used to determine
   * what position the node is in and what words it could contain.
   * 
   * @param key the to set the node with
   */
  public TrieHashNode(char key, TrieHashNode<V> parent) {
    children = new DoubleHashing<Integer, TrieHashNode<V>>();
    this.key = key;
    this.parent = parent;
    hasWord = false;
  }

  /**
   * Initializes the root node with no key or value.
   */
  protected TrieHashNode() {
    super();
    children = new DoubleHashing<Integer, TrieHashNode<V>>();
  }

  /**
   * Returns the child {@code TrieNode} for the given character.
   * 
   * @param c the character of the child to get
   * @return the child node
   */
  @SuppressWarnings("unchecked")
  protected TrieHashNode<V> getChild(char c) {
    return children.get(getIndex(c));
  }

  /**
   * Sets a new child {@code TrieNode} for the specified character index.
   * 
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param c the character index slot to insert the new child
   * @param child the new {@TrieNode} child
   */
  @SuppressWarnings("unchecked")
  public <Node extends AbstractTrieNode<V>> void setChild(char c, Node child) {
    children.insert(getIndex(c), (TrieHashNode<V>) child);
  }

  /**
   * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
   * with a word or is a word.
   * 
   * @param c the character key of the node to remove
   */
  protected void removeChild(char c) {
    children.delete(getIndex(c));
  }

  /**
   * Gets the array of {@TrieNode} children. Used for iterating for the
   * {@code Trie.toString()}.
   * 
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @return the children
   */
  @SuppressWarnings("unchecked")
  protected <Node extends AbstractTrieNode<V>> Node[] getChildren() {
    Iterable<TrieHashNode<V>> itr = children.values();
    TrieHashNode<V>[] nodes = (TrieHashNode<V>[]) new TrieHashNode<?>[children.size()];
    int idx = 0;

    for (TrieHashNode<V> node : itr)
      nodes[idx++] = node;
    return (Node[]) nodes;
  }

}
