package data_structures.tries;

public final class Trie<T> extends AbstractTrie<T> {
  public static final class Node<T> extends AbstractTrie.Node<T> {
    /**
     * Initializes an empty {@code TrieNode} with the specified key. The key is used
     * to determine what position the node is in and what words it could contain.
     *
     * @param key    the character key to set the {@code TrieNode}
     * @param parent the parent {@code TrieNode} that this new child node will be
     *               placed under for tracing up in the {@link Trie#getPrefix()}
     *               method.
     */
    @SuppressWarnings("unchecked")
    protected <TrieNode extends AbstractTrie.Node<T>> Node(char key, TrieNode parent) {
      super(key, parent);
      children = (Node<T>[]) new Node<?>[26];
    }

    /**
     * Default constructor for the root {@code TrieNode}.
     */
    @SuppressWarnings("unchecked")
    protected Node() {
      super();
      children = (Node<T>[]) new Node<?>[26];
    }
  }

  /**
   * The root node of the {@code Trie} that will hold no value.
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
   *
   * @param <TrieNode> {@link Trie.Node}
   * @throws NullPointerException {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected <TrieNode extends AbstractTrie.Node<T>> void checkNode(TrieNode node) {
    if (node == null)
      throw new NullPointerException("TrieNode cannot be null.");
    if (node.getClass() != Node.class)
      throw new IllegalArgumentException("Node must be an instance of Trie.Node");
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(String word, T value) {
    checkWord(word);
    checkValue(value);

    String currentWord = parseWord(word);
    Node<T> child, newChild, node = root;
    char currChar;

    size++;

    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      child = node.getChild(currChar);
      node.hasWord = true;

      if (child != null)
        node = child;
      else {
        newChild = new Node<T>(currChar, node);
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
  public Node<T> search(String word) {
    String currentWord = parseWord(word);
    Node<T> node = root;

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

    Node<T> parent, node = search(word);
    Node<T>[] children;
    boolean hasWord = false;

    if (node == null)
      return;

    size--;
    node.value = null;

    for (parent = (Node<T>) node.parent; parent != null; node = parent, parent = (Node<T>) node.parent) {
      if (!node.hasWord && !node.isWord()) {
        parent.removeChild(node.key);
        continue;
      }

      children = node.getChildren();

      for (int i = 0; i < children.length; i++) {
        if (children[i] != null && (children[i].hasWord || children[i].isWord())) {
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
      else
        parent.removeChild(node.key);
    }
  }

}
