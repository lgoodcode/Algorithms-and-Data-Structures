package data_structures.tries;

public class Trie<V> extends AbstractTrie<V> {
  /**
   * The root node of the {@code Trie} that will hold no value.
   */
  private TrieNode<V> root;

  /**
   * Creates a new, empty, {@code Trie}, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet {@code (26)}.
   */
  public Trie() {
    root = new TrieNode<V>();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(String word, V value) {
    checkWord(word);
    checkValue(value);

    String currentWord = parseWord(word);
    TrieNode<V> child, newChild, node = root;
    char currChar;

    count++;

    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      child = node.getChild(currChar);
      node.hasWord = true;

      if (child != null)
        node = child;
      else {
        newChild = new TrieNode<V>(currChar, node);
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
  public TrieNode<V> search(String word) {
    String currentWord = parseWord(word);
    TrieNode<V> node = root;

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

    TrieNode<V> parent, node = search(word);
    TrieNode<V>[] children;
    boolean hasWord = false;

    if (node == null)
      return;

    count--;
    node.setValue(null);

    for (parent = node.parent; parent != null; node = parent, parent = node.parent) {
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
      } else
        parent.removeChild(node.key);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <Node extends AbstractTrieNode<V>> String getPrefix(Node node) {
    checkNode(node);

    if (node.isRoot())
      return "";

    TrieNode<V> currNode = (TrieNode<V>) node, parent = currNode.parent;
    String prefix = "";

    while (!currNode.isRoot()) {
      prefix = currNode.key + prefix;
      currNode = parent;
      parent = currNode.parent;
    }

    return prefix;
  }

}
