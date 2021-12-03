package data_structures.tries;

import data_structures.hashtables.DoubleHashing;

/**
 * The TrieHash is the same implementation as the normal Trie. However, the only
 * difference is the inner class Node used because of the children property. It
 * implements an inner Node class that implements the {@link TrieNode} interface
 * to ensure it works with the methods and overrides only two methods:
 * {@link #clear()} and {@link #insertRecursive(TrieNode, String, Object)}. The
 * two methods are the only ones that instantiate the TrieHash Node objects so
 * they need to override the default Trie Node.
 */
public final class TrieHash<T> extends Trie<T> {
  private static class Node<T> implements TrieNode<T> {
    /**
     * Whether the current node contains a child that is or contains word(s).
     */
    boolean hasWord;

    /**
     * The character key of the node.
     */
    char key;

    /**
     * The value of the node.
     */
    T value;

    /**
     * The hashtable of child nodes
     */
    DoubleHashing<Character, TrieNode<T>> children;

    /**
     * The parent {@code TrieNode}. Used for tracing up to the root.
     */
    TrieNode<T> parent;

    /**
     * Initializes an empty node with the specified key. The key is used to
     * determine what position the node is in and what words it could contain.
     *
     * @param key    the character key
     * @param parent the parent node that this new child node will be placed under.
     */
    Node(char key, TrieNode<T> parent) {
      this.key = key;
      this.parent = parent;
      children = new DoubleHashing<Character, TrieNode<T>>(26);
    }

    Node() {
      this.key = ROOT;
      children = new DoubleHashing<Character, TrieNode<T>>(26);
    }

    public char getKey() {
      return key;
    }

    public T getValue() {
      return value;
    }

    public void setValue(T value) {
      this.value = value;
    }

    public TrieNode<T> getParent() {
      return parent;
    }

    public boolean hasWord() {
      return hasWord;
    }

    public void setHasWord(boolean hasWord) {
      this.hasWord = hasWord;
    }

    public boolean isWord() {
      return value != null;
    }

    public boolean isRoot() {
      return key == ROOT;
    }

    /**
     * Returns the child node for the given character key.
     *
     * @param c the character key of the child to get
     * @return the child node or {@code null} if none
     */
    public TrieNode<T> getChild(char c) {
      return children.get(c);
    }

    /**
     * Sets a new child node for the specified character key.
     *
     * @param c     the character key to insert the new child
     * @param child the new child node
     */
    public void setChild(char c, TrieNode<T> child) {
      children.insert(c, child);
    }

    /**
     * Removes a child node if it doesn't contain any node with a word or is a word.
     *
     * @param c the character key of the node to remove
     */
    public void removeChild(char c) {
      children.remove(c);
    }

    /**
     * Gets the array of children nodes.
     *
     * @return the children nodes.
     */
    @SuppressWarnings("unchecked")
    public TrieNode<T>[] getChildren() {
      TrieNode<T>[] nodes = (TrieNode<T>[]) new TrieNode<?>[children.size()];
      Iterable<TrieNode<T>> itr = children.values();
      int idx = 0;

      for (TrieNode<T> node : itr)
        nodes[idx++] = node;
      return nodes;
    }
  }

  /**
   * Creates a new, empty, TrieHash, with the root initialized to a {@code TrieNode}
   * that uses a hashtable to store the children.
   */
  public TrieHash() {
    root = new Node<T>();
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Overriden to initialize the root with {@code TrieHash.Node} instance.
   * </p>
   */
  @Override
  public final void clear() {
    root = new Node<T>();
    size = 0;
    modCount++;
  }

  // Overriden to be able to insert the TrieHash.Node instance
  @Override
  protected synchronized void insertRecursive(TrieNode<T> node, String word, T value) {
    if (word.length() == 0) {
      node.setValue(value);
      return;
    }

    char c = word.charAt(0);
    TrieNode<T> child = node.getChild(c);
    node.setHasWord(true);

    if (child == null) {
      child = new Node<T>(c, node);
      node.setChild(c, child);
    }

    insertRecursive(child, word.substring(1), value);
  }

}
