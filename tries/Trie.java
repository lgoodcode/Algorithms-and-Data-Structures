package data_structures.tries;

public class Trie<V> extends AbstractTrie<V> {
  /**
   * The root node of the trie that will hold no value.
   */
  private TrieNode<V> root;

  /**
   * Creates a new, empty, trie, with the root initialized to a {@code TrieNode} that
   * has a slot for each letter of the alphabet (26).
   */
  public Trie() {
    root = new TrieNode<V>();
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Starts at the root node and continues downward, iterating through each,
   * character of the specified {@code word}. At each node on the path downward,
   * it sets the {@code hasWord} property to true because we are inserting a word
   * under its children. If there is no child node for the current character on
   * the current node, a new child node is created and set on the current node.
   * Otherwise, the current node points to the child node and the next character
   * is set. Once there are no more characters to iterate through, we set the
   * current node to the specified value.
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(String word, V value) {
    String currentWord = parseWord(word);
    checkValue(value);

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
   * Iterates through each character of the specified {@code word} and returns the
   * resulting node if it exists or {@code null} if the node doesn't exist or
   * there was no children leading to it.
   * 
   * @param word the node to look for
   * @return the node if exists or {@code null} if not
   * 
   * @throws IllegalArgumentException if the word is {@code null} or blank
   */
  public TrieNode<V> search(String word) {
    String currentWord = parseWord(word);
    TrieNode<V> node = root;

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
  public boolean hasWord(String word) {
    return search(word) != null;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public V get(String word) {
    TrieNode<V> node = search(word);
    return node != null ? node.getValue() : null;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Once the node with the word sets the value to {@code null}, it performs a
   * check by iterating through the current nodes children checking if it contains
   * any children that contain words or is a word. Once the node that is a word or
   * contains a child that is a node or has words, is reached, it will immediately
   * stop tracing because we cannot remove any parent node if the current node is
   * a word or contains nodes with words.
   * </p>
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void delete(String word) {
    TrieNode<V> parent, node = search(word);
    TrieNode<V>[] children;
    boolean hasWord = false;

    if (node == null)
      return;

    count--;
    node.setValue(null);
    
    // Trace back up to the root to remove any nodes that don't contain any words
    for (parent = node.parent; parent != null; node = parent, parent = node.parent) {
      children = node.getChildren();

      for (int i=0; i<children.length; i++) {
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
        parent.removeChild(node.getKey());
    }
  }

  // TODO: test this once I get a decent computer to debug it...
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    walk(root, (TrieNode<V> node) -> {
      V value = node.getValue();

      if (value != null)
        sb.append("\s\s\"" + node.toString() + "\",\n");
    });

    return sb.toString() + "}";
  }
}
