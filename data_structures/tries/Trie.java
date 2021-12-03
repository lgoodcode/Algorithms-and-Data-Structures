package data_structures.tries;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class Trie<T> extends AbstractTrie<T> {
  private static class Node<T> extends AbstractNode<T> {
    /**
     * The array of child nodes.
     */
    Node<T>[] children;

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
    @SuppressWarnings("unchecked")
    Node(char key, Node<T> parent) {
      this.key = key;
      this.parent = parent;
      hasWord = false;
      children = (Node<T>[]) new Node<?>[26];
    }

    @SuppressWarnings("unchecked")
    Node() {
      super();
      children = (Node<T>[]) new Node<?>[26];
    }

    public final Node<T> getParent() {
      return parent;
    }

    /**
     * Derives the index for the {@code children} array using the
     * {@link Character#getNumericValue(char)} method to map the character key to
     * the proper index location based on the letter in the range of {@code [A, Z]}
     * case-insensitive. The numeric index value from the given character is in the
     * range {@code [0, 25]}.
     *
     * @param c the character to get the index of
     * @return the index of the character in the range of {@code [0, 25]}
     */
    int getIndex(char c) {
      int index = Character.getNumericValue(c);

      if (index < 10 || index > 35)
        throw new IllegalArgumentException("Character must be a letter A-Z, case-insensitive.");
      return index - 10;
    }

    public Node<T> getChild(char c) {
      return children[getIndex(c)];
    }

    public void setChild(char c, TrieNode<T> child) {
      children[getIndex(c)] = (Node<T>) child;
    }

    public void removeChild(char c) {
      children[getIndex(c)] = null;
    }

    public Node<T>[] getChildren() {
      return children;
    }
  }

  /**
   * The root node of the {@code Trie}. Will hold no value, just child nodes.
   */
  private Node<T> root;

  /**
   * Creates a new, empty, {@code Trie}, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet {@code (26)}.
   */
  public Trie() {
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

  protected synchronized void insertRecursive(Node<T> node, String word, T value) {
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

  @SuppressWarnings("unused")
  private synchronized void insertIterative(String word, T value) {
    String currentWord = parseWord(word);
    Node<T> child, node = root;
    char currChar;

    // While there are characters in the word to continue down
    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      // Get the child node for the char key
      child = node.getChild(currChar);
      // The node will now have a descendant with a word
      node.hasWord = true;

      if (child != null)
        node = child;
      else {
        Node<T> newChild = new Node<T>(currChar, node);
        node.setChild(currChar, newChild);
        node = newChild;
      }
      // Continue to the next character of the word
      currentWord = currentWord.substring(1);
    }

    // Set the value of the node for the word
    node.value = value;

    size++;
    modCount++;
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
    if (node == null || (!node.hasWord() && !node.isWord()))
      return null;
    if (word.length() == 0)
      return node;
    return searchRecursive(node.getChild(word.charAt(0)), word.substring(1));
  }

  @SuppressWarnings("unused")
  private Node<T> searchIterative(String word) {
    String currentWord = parseWord(word);
    Node<T> node = root;

    while (currentWord.length() > 0) {
      node = node.getChild(currentWord.charAt(0));
      currentWord = currentWord.substring(1);

      if (node == null || (!node.hasWord && !node.isWord()))
        return null;
    }

    return node;
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
    if (!node.hasWord()) {
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
      if (child != null && (child.hasWord() || child.isWord())) {
        hasWord = true;
        break;
      }
    }

    if (!hasWord) {
      // If doesn't have children with words but is a word
      if (node.isWord())
        node.hasWord = false;
      else {
        node.parent.removeChild(node.key);
        deleteCleanup(node.parent);
      }
    }
  }

  @SuppressWarnings("unused")
  private synchronized void deleteIterative(String word) {
    Node<T> parent, node = search(word);
    boolean hasWord = false;

    // If word wasn't found, exit
    if (node == null)
      return;

    // Clear the word value from the node
    node.value = null;

    // If after removing the value and the node doesn't contain children, remove it
    if (!node.hasWord()) {
      node.parent.removeChild(node.key);
      node = node.parent;
    }

    // Backtrack up from the node to the root, removing any empty child nodes
    for (parent = node.parent; parent != null; node = parent, parent = node.parent) {
      for (Node<T> child : node.getChildren()) {
        if (child != null && (child.hasWord || child.isWord())) {
          hasWord = true;
          break;
        }
      }

      if (hasWord)
        break;
      else if (!hasWord && node.isWord()) {
        node.hasWord = false;
        break;
      }
      // Node didn't have any children that contains a word, remove it
      else
        parent.removeChild(node.key);
    }

    size--;
    modCount++;
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  protected String getPrefix(TrieNode<T> node) {
    checkNode(node);
    return getPrefixRecursive(node, "");
  }

  protected String getPrefixRecursive(TrieNode<T> node, String prefix) {
    if (node.isRoot())
      return prefix;
    return getPrefixRecursive(node.getParent(), node.getKey() + prefix);
  }

  @SuppressWarnings("unused")
  private String getPrefixIterative(Node<T> node) {
    Node<T> parent = node.parent;
    String prefix = "";

    while (!node.isRoot()) {
      prefix = node.key + prefix;
      node = parent;
      parent = node.parent;
    }

    return prefix;
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
