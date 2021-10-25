package data_structures.tries;

public class TrieHash<V> extends AbstractTrie<V> {
  /**
   * The root node of the trie that will hold no value.
   */
  private TrieHashNode<V> root;

  /**
   * Creates a new, empty, trie, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet (26).
   */
  public TrieHash() {
    root = new TrieHashNode<V>();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public synchronized void insert(String word, V value) {
    checkWord(word);
    checkValue(value);

    String currentWord = parseWord(word);
    TrieHashNode<V> child, newChild, node = root;
    char currChar;

    count++;

    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      child = node.getChild(currChar);
      node.hasWord = true;

      if (child != null)
        node = child;
      else {
        newChild = new TrieHashNode<V>(currChar, node);
        node.setChild(currChar, newChild);
        node = newChild;
      }

      currentWord = currentWord.substring(1);
    }

    node.setValue(value);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public TrieHashNode<V> search(String word) {
    String currentWord = parseWord(word);
    TrieHashNode<V> node = root;

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
  @Override
  public synchronized void delete(String word) {
    checkWord(word);

    TrieHashNode<V> parent, node = search(word);
    boolean hasWord = false;

    if (node == null)
      return;

    count--;
    node.setValue(null);

    for (parent = node.parent; parent != null; node = parent, parent = node.parent) {
      if (node.children.isEmpty() && !node.isWord())
        parent.removeChild(node.key);

      for (TrieHashNode<V> child : node.children.values()) {
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

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <Node extends AbstractTrieNode<V>> String getPrefix(Node node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
    if (node.isRoot())
      return "";

    TrieHashNode<V> currNode = (TrieHashNode<V>) node, parent = currNode.parent;
    String prefix = "";

    while (!currNode.isRoot()) {
      prefix = currNode.key + prefix;
      currNode = parent;
      parent = currNode.parent;
    }

    return prefix;
  }

}
