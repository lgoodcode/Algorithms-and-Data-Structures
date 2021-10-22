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
   * character of the specified {@code word}. At each level, if a node with the
   * current character doesn't exist, it is created. Once node is reached with the
   * final character, it will set the value of the current node.
   * </p>
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(String word, V value) {
    checkWord(word);
    checkValue(value);

    TrieNode<V> child, newChild, node = root;
    String currentWord = word;
    char currChar;

    count++;

    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      child = node.getChild(currChar);

      if (child != null) {
        currentWord = currentWord.substring(1);
        node = child;
      }
      else {
        newChild = new TrieNode<V>((node.getKey() != null ? node.getKey() : "") + currChar);

        if (currentWord.length() == 1)
          newChild.setValue(value);

        node.setChild(currChar, newChild);
        node = newChild;
        currentWord = currentWord.substring(1);
      }
    }
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
    checkWord(word);
    
    TrieNode<V> child, node = root;
    String currentWord = word;
    char currChar;

    while (currentWord.length() > 0) {
      currChar = currentWord.charAt(0);
      child = node.getChild(currChar);

      if (child == null)
        return null;
      
      node = child;
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
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void delete(String word) {
    TrieNode<V> node = search(word);

    if (node != null) {
      node.setValue(null);
      count--;
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
