package data_structures.tries;

import data_structures.hashtables.DoubleHashing;

public final class TrieHash<T> extends AbstractTrie<T> {
  public static class HashNode<T> extends Node<T> {
    /**
     * The hashtable containing the child {@code TrieHashNodes}.
     */
    DoubleHashing<Character, HashNode<T>> children;

    /**
     * The parent {@code TrieHashNode} used for tracing up to the root.
     */
    HashNode<T> parent;

    /**
     * Initializes an empty node with the specified key. The key is used for the
     * hashtable to map the node and identify what words it could contain.
     *
     * @param key    the to set the node with
     * @param parent the parent {@code HashNode} that this new child node will be
     *               placed under for tracing up in the {@link Trie#getPrefix()}
     *               method.
     */
    HashNode(char key, HashNode<T> parent) {
      super(key, parent);     
      children = new DoubleHashing<Character, HashNode<T>>();
    }

    /**
     * Initializes the root node with no key or value.
     */
    HashNode() {
      super();
      children = new DoubleHashing<Character, HashNode<T>>();
    }

    /**
     * Returns the child {@code TrieNode} for the given character.
     *
     * @param c the character of the child to get
     * @return the child node
     */
    @SuppressWarnings("unchecked")
    protected HashNode<T> getChild(char c) {
      return children.get(c);
    }

    /**
     * Sets a new child {@code TrieNode} for the specified character index.
     *
     * @param <TrieNode> a subclass of {@link AbstractTrieNode}
     * @param c the character index slot to insert the new child
     * @param child the new {@TrieNode} child
     */
    @SuppressWarnings("unchecked")
    public <TrieNode extends Node<T>> void setChild(char c, TrieNode child) {
      children.insert(c, (HashNode<T>) child);
    }

    /**
     * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
     * with a word or is a word.
     *
     * @param c the character key of the node to remove
     */
    protected void removeChild(char c) {
      children.remove(c);
    }

    /**
     * Gets the array of {@TrieNode} children. Used for iterating for the
     * {@code Trie.toString()}.
     *
     * @param <TrieNode> a subclass of {@link AbstractTrieNode}
     * @return the children
     */
    @SuppressWarnings("unchecked")
    protected <TrieNode extends Node<T>> TrieNode[] getChildren() {
      Iterable<HashNode<T>> itr = children.values();
      HashNode<T>[] nodes = (HashNode<T>[]) new HashNode<?>[children.size()];
      int idx = 0;

      for (HashNode<T> node : itr)
        nodes[idx++] = node;
      return (TrieNode[]) nodes;
    }
  }

  /**
   * The root node of the trie that will hold no value.
   */
  private HashNode<T> root;

  /**
   * Creates a new, empty, trie, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet (26).
   */
  public TrieHash() {
    root = new HashNode<T>();
  }

  /**
   * {@inheritDoc}
   *
   * @param <TrieNode> {@link Abstract.Node} subclass
   * @throws NullPointerException {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected <TrieNode extends AbstractTrie.Node<T>> void checkNode(TrieNode node) {
    if (node == null)
      throw new NullPointerException("TrieNode cannot be null.");
    if (node.getClass() != HashNode.class)
      throw new IllegalArgumentException("Node must be an instance of Trie.HashNode");
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public synchronized void insert(String word, T value) {
    checkWord(word);
    checkValue(value);

    String currentWord = parseWord(word);
    HashNode<T> child, newChild, node = root;
    char currChar;

    size++;

    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      child = node.getChild(currChar);
      node.hasWord = true;

      if (child != null)
        node = child;
      else {
        newChild = new HashNode<T>(currChar, node);
        node.setChild(currChar, newChild);
        node = newChild;
      }

      currentWord = currentWord.substring(1);
    }

    node.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public HashNode<T> search(String word) {
    String currentWord = parseWord(word);
    HashNode<T> node = root;

    if (currentWord.isBlank())
      return node;

    while (currentWord.length() > 0) {
      if (node == null || !node.hasWord)
        return null;

      node = node.getChild(currentWord.charAt(0));
      currentWord = currentWord.substring(1);
    }

    return node;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void delete(String word) {
    checkWord(word);

    HashNode<T> parent, node = search(word);
    boolean hasWord = false;

    if (node == null)
      return;

    size--;
    node.value = null;

    for (parent = node.parent; parent != null; node = parent, parent = node.parent) {
      if (node.children.isEmpty() && !node.isWord())
        parent.removeChild(node.key);

      for (HashNode<T> child : node.children.values()) {
        if (child.hasWord || child.isWord()) {
          hasWord = true;
          break;
        }

        if (hasWord)
          break;
        else if (!hasWord && node.isWord()) {
          node.hasWord = false;
          break;
        }
        else
          parent.removeChild(node.key);
      }
    }
  }

}
