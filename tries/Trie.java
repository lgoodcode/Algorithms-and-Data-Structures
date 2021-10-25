package data_structures.tries;

import java.util.function.Consumer;

import data_structures.queues.Queue;
import data_structures.queues.exceptions.QueueEmptyException;
import data_structures.queues.exceptions.QueueFullException;

public final class Trie<V> extends AbstractTrie<V> {
  /**
   * The root node of the trie that will hold no value.
   */
  private TrieNode<V> root;

  /**
   * Creates a new, empty, trie, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet (26).
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
  public <Node extends AbstractTrieNode<V>> Node search(String word) {
    String currentWord = parseWord(word);
    TrieNode<V> node = root;

    if (currentWord.isBlank())
      return (Node) node;

    while (currentWord.length() > 0) {
      if (node == null || !node.hasWord)
        return null;

      node = node.getChild(currentWord.charAt(0));
      currentWord = currentWord.substring(1);
    }

    return (Node) node;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasWord(String word) {
    TrieNode<V> node = search(word);
    return node != null && node != root;
  }

  /**
   * {@inheritDoc}
   */
  public V get(String word) {
    TrieNode<V> node = search(word);
    if (node == null || node == root)
      return null;
    return node.getValue();
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

    for (parent = node.parent; node != null; node = parent, parent = node.parent) {
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
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
    if (node == root)
      return "";

    // Typecast node because the AbstractTrieNode doesn't have the parent property
    TrieNode<V> currNode = (TrieNode<V>) node, parent = currNode.parent;
    String prefix = "";

    while (currNode != root) {
      prefix += currNode.key;
      currNode = parent;
      parent = currNode.parent;
    }

    return prefix;
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected <Node extends AbstractTrieNode<V>> String[] findWords(Node start, String prefix) {
    if (start == null)
      throw new NullPointerException("Node cannot be null.");
    if (start == root && !prefix.isBlank())
      throw new IllegalArgumentException("Prefix must be blank if starting from the root.");
    if (start != root && prefix.isBlank())
      throw new IllegalArgumentException("Prefix cannot be blank if not starting from root.");
    if (isEmpty()) {
      return null;
    }

    Queue<String> queue = new Queue<>(size());
    String[] words;
    int i = 0, len;

    walk(start, prefix, (Node node, String chars) -> {
      if (node.isWord()) {
        try {
          queue.enqueue(chars);
        } catch (QueueFullException e) {
        }
      }
    });

    if (start.isWord()) {
      len = queue.size() + 1;
      words = new String[len];
      words[i++] = prefix;
    } else {
      len = queue.size();
      words = new String[len];
    }

    for (; i < len; i++) {
      try {
        words[i] = queue.dequeue();
      } catch (QueueEmptyException e) {
      }
    }

    return words;
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  public <Node extends AbstractTrieNode<V>> String[] findWords(Node start) {
    if (start == null)
      throw new NullPointerException("Node cannot be null.");
    return findWords(start, getPrefix(start));
  }

  /**
   * {@inheritDoc}
   */
  public String[] findWords(String prefix) {
    TrieNode<V> start = search(prefix);
    return start != null ? findWords(start, prefix) : null;
  }

  /**
   * {@inheritDoc}
   */
  public String[] findWords() {
    return isEmpty() ? null : findWords(root, "");
  }

  /**
   * Recursive method that traverses all {@code TrieNode} in the {@code Trie},
   * beginning from the {@code root}. Otherwise, if the {@code Trie} is empty, it
   * won't do anything.
   *
   * @param <Node>   a subclass of {@link AbstractTrieNode}
   * @param callback the {@code Consumer} lambda function
   */
  @SuppressWarnings("unchecked")
  public <Node extends AbstractTrieNode<V>> void walk(Consumer<Node> callback) {
    if (!isEmpty())
      walk((Node) root, callback);
  }

  /**
   * Traversed the {@code Trie} and prints out the words by their prefix and
   * associated values in lexicographical order.
   *
   * @return the object string of the {@code Trie}
   */
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    walk(root, "", (TrieNode<V> node, String prefix) -> {
      V value = node.getValue();

      if (value != null)
        sb.append("\s\s\"" + prefix + " -> " + value + "\",\n");
    });

    return sb.toString() + "}";
  }
}
