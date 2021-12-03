package data_structures.tries;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import data_structures.hashtables.DoubleHashing;

public final class TrieHash<T> extends AbstractTrie<T> {
  private static class Node<T> extends AbstractNode<T> {
    /**
     * The hashtable containing the child nodes.
     */
    DoubleHashing<Character, Node<T>> children;

    /**
     * The parent {@code TrieNode}. Used for tracing up to the root.
     */
    Node<T> parent;

    /**
     * Initializes an empty node with the specified key. The key is used to
     * determine what position the node is in and what words it could contain.
     *
     * @param key    the character key
     * @param parent the parent node that this new child node will be placed under.
     */
    Node(char key, Node<T> parent) {
      this.key = key;
      this.parent = parent;
      hasWord = false;
      children = new DoubleHashing<Character, Node<T>>(26);
    }

    /**
     * Default constructor used for the root {@code TrieNode}.
     */
    Node() {
      super();
      children = new DoubleHashing<Character, Node<T>>(26);
    }

    public Node<T> getParent() {
      return parent;
    }

    /**
     * Returns the child node for the given character key.
     *
     * @param c the character key of the child to get
     * @return the child node or {@code null} if none
     */
    public Node<T> getChild(char c) {
      return children.get(c);
    }

    /**
     * Sets a new child node for the specified character key.
     *
     * @param c     the character key to insert the new child
     * @param child the new child node
     */
    public void setChild(char c, TrieNode<T> child) {
      children.insert(c, (Node<T>) child);
    }

    /**
     * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
     * with a word or is a word.
     *
     * @param c the character key of the node to remove
     */
    public void removeChild(char c) {
      children.remove(c);
    }

    /**
     * Gets the array of {@TrieNode} children nodes.
     *
     * @return the children nodes.
     */
    @SuppressWarnings("unchecked")
    public Node<T>[] getChildren() {
      Node<T>[] nodes = (Node<T>[]) new Node<?>[children.size()];
      Iterable<Node<T>> itr = children.values();
      int idx = 0;

      for (Node<T> node : itr)
        nodes[idx++] = node;
      return nodes;
    }
  }

  /**
   * The root node of the {@code Trie}. Will hold no value, just child nodes.
   */
  private Node<T> root;

  /**
   * Creates a new, empty, trie, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet (26).
   */
  public TrieHash() {
    root = new Node<T>();
  }

  /**
   * {@inheritDoc}
   */
  public void clear() {
    root = new Node<T>();
    size = 0;
    modCount++;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(String word, T value) {
    checkWord(word);
    checkValue(value);

    insertRecursive(root, parseWord(word), value);

    size++;
    modCount++;
  }

  private synchronized void insertRecursive(Node<T> node, String word, T value) {
    if (word.length() == 0) {
      node.value = value;
      return;
    }

    char c = word.charAt(0);
    Node<T> child = node.getChild(c);
    node.hasWord = true;

    if (child == null) {
      child = new Node<T>(c, node);
      node.setChild(c, child);
    }

    insertRecursive(child, word.substring(1), value);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected Node<T> search(String word) {
    checkWord(word);

    if (isEmpty())
      return null;
    return searchRecursive(root, parseWord(word));
  }

  private Node<T> searchRecursive(Node<T> node, String word) {
    if (node == null || (!node.hasWord && !node.isWord()))
      return null;
    if (word.length() == 0)
      return node;
    return searchRecursive(node.getChild(word.charAt(0)), word.substring(1));
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void delete(String word) {
    Node<T> node = search(word);

    if (node == null)
      return;

    node.value = null;

    // If node doesn't contain a child with a word, remove it and start
    // the cleanup from the parent node
    if (!node.hasWord) {
      node.parent.removeChild(node.key);
      deleteCleanup(node.parent);
    }
    else
      deleteCleanup(node);

    size--;
    modCount++;
  }

  /**
   * Performs the checks on the node to determine if it contains any child node
   * with a word or if it is a word itself. If it contains a child that contains
   * child nodes with a word, or is a word itself, it stops the process since the
   * node cannot be removed. If it doesn't contain any child nodes but is a word,
   * it will set the {@code hasWord} flag to {@code false} and stop. If the node
   * doesn't contain any child nodes with a word and isn't a word, we remove it
   * and continue the process up the with the parent.
   *
   * @param node the node to perform the cleanup on
   */
  private synchronized void deleteCleanup(Node<T> node) {
    boolean hasWord = false;

    for (Node<T> child : node.getChildren()) {
      if (child != null && (child.hasWord || child.isWord())) {
        hasWord = true;
        break;
      }
    }

    if (!hasWord) {
      if (node.isWord())
        node.hasWord = false;
      else {
        node.parent.removeChild(node.key);
        deleteCleanup(node.parent);
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  protected String getPrefix(TrieNode<T> node) {
    checkNode(node);
    return getPrefixRecursive((Node<T>) node, "");
  }

  protected String getPrefixRecursive(TrieNode<T> node, String prefix) {
    if (node.isRoot())
      return prefix;
    return getPrefixRecursive(node.getParent(), node.getKey() + prefix);
  }

  /**
   * {@inheritDoc}
   */
  public void walk(Consumer<TrieNode<T>> callback) {
    if (!isEmpty())
      walk(root, callback);
  }

  /**
   * {@inheritDoc}
   */
  public void walk(BiConsumer<TrieNode<T>, String> callback) {
    if (!isEmpty())
      walk(root, "", callback);
  }

  /**
   * {@inheritDoc}
   */
  public String[] findWords() {
    return isEmpty() ? new String[0] : findWords(root, "");
  }

}
