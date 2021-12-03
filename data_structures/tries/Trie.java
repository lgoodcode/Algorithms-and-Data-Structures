package data_structures.tries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import data_structures.EmptyIterator;
import data_structures.linkedLists.LinkedList;
import data_structures.queues.Queue;

import java.util.ConcurrentModificationException;

public class Trie<T> {
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
     * The array of child nodes.
     */
    TrieNode<T>[] children;

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
    @SuppressWarnings("unchecked")
    Node(char key, TrieNode<T> parent) {
      this.key = key;
      this.parent = parent;
      hasWord = false;
      children = (Node<T>[]) new Node<?>[26];
    }

    @SuppressWarnings("unchecked")
    Node() {
      this.key = ROOT;
      children = (Node<T>[]) new Node<?>[26];
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

    /**
     * {@inheritDoc}
     */
    public TrieNode<T> getChild(char c) {
      return children[getIndex(c)];
    }

    /**
     * {@inheritDoc}
     */
    public void setChild(char c, TrieNode<T> child) {
      children[getIndex(c)] = (Node<T>) child;
    }

    /**
     * Removes a child {@code TrieNode}.
     *
     * @param c the character key of the child node to remove
     */
    public void removeChild(char c) {
      children[getIndex(c)] = null;
    }

    /**
     * Gets the array of of children nodes.
     *
     * @return the array of children nodes
     */
    public TrieNode<T>[] getChildren() {
      return children;
    }

  }

  /**
   * The root node of the {@code Trie}. Will hold no value, just child nodes.
   */
  protected TrieNode<T> root;

  /**
   * Counter tracking the number of entries in the {@code Trie}.
   */
  protected int size;

  /**
   * The number of times this Trie has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the Trie fail-fast.
   *
   * @see ConcurrentModificationException
   */
  protected int modCount;

  // Iteration constants
  protected boolean WORDS = false;
  protected boolean VALUES = true;

  /**
   * Creates a new, empty, {@code Trie}, with the root initialized to a {@code TrieNode}
   * that has a slot for each letter of the alphabet {@code (26)}.
   */
  public Trie() {
    root = new Node<T>();
  }

  /**
   * Checks the word to make sure it isn't {@code null} or blank.
   *
   * @param word the word to check
   *
   * @throws IllegalArgumentException if the word is {@code null}, or blank
   */
  protected final void checkWord(String word) {
    if (word == null || word.isBlank())
      throw new IllegalArgumentException("Word cannot be null or blank.");
  }

  /**
   * Checks the value to make sure it isn't {@code null} or blank
   *
   * @param value the value to check
   *
   * @throws IllegalArgumentException if the value is {@code null} or blank
   */
  protected final void checkValue(T value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or blank.");
  }

  /**
   * Checks that the specified {@code TrieNode} that it isn't {@code null}.
   *
   * @param node the node to verify
   *
   * @throws NullPointerException if the node is {@code null}
   */
  protected void checkNode(TrieNode<T> node) {
    if (node == null)
      throw new NullPointerException("TrieNode cannot be null.");
  }

  /**
   * Parses the input word, removing any whitespace {@code \s}, newline
   * {@code \n}, or tab {@code \t} characters as well as dashes {@code -} and
   * underscores {@code _}. If the input is {@code null} it simply returns a blank
   * {@code String}.
   *
   * @param word the word to parse
   * @return the parsed word
   */
  protected final String parseWord(String word) {
    if (word == null)
      return "";
    return word.strip().replaceAll("[\n\s\t-_]", "");
  }

  /**
   * Returns the number of words stored in the {@code Trie}.
   *
   * @return the number of words
   */
  public final int size() {
    return size;
  }

  /**
   * Returns a boolean value indicating whether the {@code Trie} is empty or not.
   *
   * @return whether the {@code Trie} is empty
   */
  public final boolean isEmpty() {
    return size == 0;
  }

  /**
   * Removes all the entries in the trie by removing the root reference, which has
   * all the trie nodes under it.
   */
  public void clear() {
    root = new Node<T>();
    size = 0;
    modCount++;
  }

  /**
   * Inserts a new word with a corresponding value. If inserting a {@code word}
   * that is already used, its value will be overriden.
   *
   * <p>
   * Checks the {@code word} to ensure that it isn't {@code null} or blank and
   * then parses it to remove invalid characters.
   * </p>
   *
   * <p>
   * Starts at the root node and continues downward, iterating through each,
   * character of the specified {@code word}. At each node on the path downward,
   * it sets the {@code hasWord} property to true because we are inserting a word
   * under its children. If there is no child node for the current character on
   * the current node, a new child node is created and set. Otherwise, the current
   * node points to the child node and the next character is set. Once there are
   * no more characters to iterate through, we set the current node to the
   * specified value.
   * </p>
   *
   * @param word  the word of the new node to insert
   * @param value the value of the new node to insert
   *
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank.
   */
  public synchronized void insert(String word, T value) {
    checkWord(word);
    checkValue(value);

    insertRecursive(root, parseWord(word), value);

    size++;
    modCount++;
  }

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

  /**
   * Recurses through each character of the specified word, going through each
   * child node with the specified character key until there are no characters
   * left in the word.
   *
   * <p>
   * It will stop early if the current node has a {@code false} value for the
   * {@code hasWord} property, which indicates that the node does not contain any
   * children nodes that has {@code hasWord = true}.
   * </p>
   *
   * @param word the node to look for
   * @return the {@code TrieNode} if the trie contains the word or {@code null} if
   *         not
   *
   * @throws IllegalArgumentException if the specified word is {@code null} or
   *                                  blank
   */
  protected final TrieNode<T> search(String word) {
    checkWord(word);

    if (isEmpty())
      return null;
    return searchRecursive(root, parseWord(word));
  }

  private TrieNode<T> searchRecursive(TrieNode<T> node, String word) {
    if (node == null || (!node.hasWord() && !node.isWord()))
      return null;
    if (word.length() == 0)
      return node;
    return searchRecursive(node.getChild(word.charAt(0)), word.substring(1));
  }

  // private Node<T> searchIterative(String word) {
  //   String currentWord = parseWord(word);
  //   Node<T> node = root;

  //   while (currentWord.length() > 0) {
  //     node = node.getChild(currentWord.charAt(0));
  //     currentWord = currentWord.substring(1);

  //     if (node == null || (!node.hasWord && !node.isWord()))
  //       return null;
  //   }

  //   return node;
  // }

  /**
   * Returns a boolean indicating whether the {@code Trie} contains an entry with
   * the specified {@code word}. Returns {@code true} only if the resulting node
   * in the search is not {@code null} or the {@code root}.
   *
   * @param word the word to search for
   * @return whether a node in the {@code Trie} contains the specified word
   *
   * @throws IllegalArgumentException if the word is {@code null} or blank
   */
  public final boolean hasWord(String word)  {
    TrieNode<T> node = search(word);
    return node != null && node.isWord();
  }

  /**
   * Retrieves the value for the corresponding {@code TrieNode} of the given word
   * or {@code null}, is not a word which would contain a value, if not found, or
   * is the {@code root}.
   *
   * @param word the word of the {@code TrieNode}
   * @return the value or {@code null} if not found
   *
   * @throws IllegalArgumentException if the word is {@code null} or blank
   */
  public final T get(String word) {
    TrieNode<T> node = search(word);
    return node != null ? node.getValue() : null;
  }

  /**
   * Deletes a {@code word} value from the {@code Trie} if it exists. Will throw
   * an exception if the specified {@code word} is {@code null} or blank, as they
   * cannot be a valid stored value.
   *
   * <p>
   * Once the node with the {@code word} sets the value to {@code null}, it checks
   * if the node is marked to have children that contain a words. If not, it is
   * removed. From the node it traces back up to the {@code root} and it performs
   * a check of the current nodes children checking if it contains any children
   * that contain words or is a word. Once the node that is a word or contains a
   * child that is a node or has words, is reached, it will immediately stop
   * tracing because we cannot remove any parent node if the current node is a
   * word or contains child nodes with words.
   * </p>
   *
   * @param word the word of the {@code TrieNode} to delete
   *
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank
   */
  public final synchronized void delete(String word) {
    TrieNode<T> node = search(word);

    if (node == null)
      return;

    node.setValue(null);

    // If node doesn't contain a child with a word, remove it and start
    // the cleanup from the parent node
    if (!node.hasWord()) {
      node.getParent().removeChild(node.getKey());
      deleteCleanup(node.getParent());
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
  private final synchronized void deleteCleanup(TrieNode<T> node) {
    boolean hasWord = false;

    for (TrieNode<T> child : node.getChildren()) {
      if (child != null && (child.hasWord() || child.isWord())) {
        hasWord = true;
        break;
      }
    }

    if (!hasWord) {
      // If doesn't have children with words but is a word
      if (node.isWord())
        node.setHasWord(false);
      else {
        node.getParent().removeChild(node.getKey());
        deleteCleanup(node.getParent());
      }
    }
  }

  // private synchronized void deleteIterative(String word) {
  //   Node<T> parent, node = search(word);
  //   boolean hasWord = false;

  //   // If word wasn't found, exit
  //   if (node == null)
  //     return;

  //   // Clear the word value from the node
  //   node.value = null;

  //   // If after removing the value and the node doesn't contain children, remove it
  //   if (!node.hasWord()) {
  //     node.parent.removeChild(node.key);
  //     node = node.parent;
  //   }

  //   // Backtrack up from the node to the root, removing any empty child nodes
  //   for (parent = node.parent; parent != null; node = parent, parent = node.parent) {
  //     for (Node<T> child : node.getChildren()) {
  //       if (child != null && (child.hasWord || child.isWord())) {
  //         hasWord = true;
  //         break;
  //       }
  //     }

  //     if (hasWord)
  //       break;
  //     else if (!hasWord && node.isWord()) {
  //       node.hasWord = false;
  //       break;
  //     }
  //     // Node didn't have any children that contains a word, remove it
  //     else
  //       parent.removeChild(node.key);
  //   }

  //   size--;
  //   modCount++;
  // }

  /**
   * Traces up from the specified node up to the {@code root}, collecting the keys
   * from the initial node, all the way up to the {@code root}, to form the prefix
   * for the node.
   *
   * @param node the node to find the prefix of
   * @return the prefix string
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  protected final String getPrefix(TrieNode<T> node) {
    checkNode(node);
    return getPrefixRecursive(node, "");
  }

  protected final String getPrefixRecursive(TrieNode<T> node, String prefix) {
    if (node.isRoot())
      return prefix;
    return getPrefixRecursive(node.getParent(), node.getKey() + prefix);
  }

  /**
   * Recursively traverses all the nodes in the trie in lexicographical order.
   * Performs side-effect operations on each node.
   *
   * @param node     the starting node to traverse from
   * @param callback the {@code Consumer} lambda function
   */
  protected final void walk(TrieNode<T> node, Consumer<TrieNode<T>> callback) {
    for (TrieNode<T> child : node.getChildren()) {
      if (child != null) {
        callback.accept(child);
        walk(child, callback);
      }
    }
  }

  /**
   * Recursively traverses all the nodes in the trie in lexicographical order and
   * maintains the prefix as it recurses to be able to pass the node and the
   * prefix to the callback function.
   *
   * @param node     the starting node to traverse from
   * @param prefix   the prefix of the current node being traversed
   * @param callback the {@code BiConsumer} lambda function
   */
  protected final void walk(TrieNode<T> node, String prefix, BiConsumer<TrieNode<T>, String> callback) {
    for (TrieNode<T> child : node.getChildren()) {
      if (child != null) {
        callback.accept(child, prefix + child.getKey());
        walk(child, prefix + child.getKey(), callback);
      }
    }
  }

  /**
   * Recursively traverses all the nodes in the trie in lexicographical order.
   *
   * @param callback the {@code Consumer} lambda function
   */
  public final void walk(Consumer<TrieNode<T>> callback) {
    if (!isEmpty())
      walk(root, callback);
  }

  /**
   * Recursively traverses all the nodes in the trie in lexicographical order. The
   * callback provides the current node and the prefix of that node.
   *
   * @param callback the {@code BiConsumer} lambda function
   */
  public final void walk(BiConsumer<TrieNode<T>, String> callback) {
    if (!isEmpty())
      walk(root, "", callback);
  }

  /**
   * Recursively traverses the trie getting all the words contained under the
   * starting node and prefix.
   *
   * @param start  the node to start traversing from
   * @param prefix the prefix to start with
   * @return the {@code String} array of words
   */
  protected final String[] findWords(TrieNode<T> start, String prefix) {
    LinkedList<String> list = new LinkedList<>();
    LinkedList.Node<String> n;
    String[] words;
    int i = 0;

    if (start.isWord())
      list.insert(prefix);

    walk(start, prefix, (TrieNode<T> node, String word) -> {
      if (node.isWord())
        list.insertLast(word);
    });

    words = new String[list.size()];
    n = list.getHead();

    while (n != null) {
      words[i++] = n.getItem();
      n = n.next();
    }

    return words;
  }

  /**
   * Finds all the words under the specified {@code prefix}.
   *
   * @param prefix the prefix to find words of
   * @return the {@code String} array of words
   */
  public String[] findWords(String prefix) {
    prefix = parseWord(prefix);

    TrieNode<T> start;

    if (isEmpty() || (start = search(prefix)) == null)
      return new String[0];
    return findWords(start, prefix);
  }

  /**
   * Finds all the words contained in the trie.
   *
   * @return the {@code String} array of words contained in the trie
   */
  public final String[] findWords() {
    return isEmpty() ? new String[0] : findWords(root, "");
  }

  /**
   * Returns an array containing all of the elements in this queue in proper
   * sequence (from first to last element).
   *
   * <p>
   * The returned array will be "safe" in that no references to it are maintained
   * by this queue. (In other words, this method must allocate a new array). The
   * caller is thus free to modify the returned array.
   * </p>
   *
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   * </p>
   *
   * @return an array containing all of the elements in this queue in proper
   *         sequence
   */
  public final Object[] toArray() {
    if (isEmpty())
      return new Object[0];

    Queue<T> Q = new Queue<>(size);

    walk((node) ->  {
      if (node.isWord())
        Q.enqueue(node.getValue());
    });

    return Q.toArray();
  }
  /**
   * Traverses the {@code Trie} and prints out the words by their prefix and
   * associated values in lexicographical order.
   *
   * @return the object string of the {@code Trie}
   */
  public final String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    walk((TrieNode<T> node, String prefix) -> {
      if (node.isWord())
        sb.append("\s\s" + prefix + " -> " + node.getValue() + ",\n");
    });

    return sb.substring(0, sb.length() - 2) + "\n}";
  }
  /**
   * Returns an {@link Iterable} of the specified type.
   *
   * @param <E>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  private <E> Iterable<E> getIterable(boolean type) {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr<>(type);
  }

  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param <E>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  private <E> Iterator<E> getIterator(boolean type) {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr<>(type);
  }

  public final Iterable<String> words() {
    return getIterable(WORDS);
  }

  public final Iterable<T> values() {
    return getIterable(VALUES);
  }

  public final Iterator<String> wordsIterator() {
    return getIterator(WORDS);
  }

  public final Iterator<T> valuesIterator() {
    return getIterator(VALUES);
  }

  /**
   * A trie iterator class. This class implements the {@link Iterable} and
   * {@link Iterator} interfaces. It keeps a {@link Queue} of the nodes in the
   * trie after walking it and inserting all the nodes that are a word with a
   * corresponding value.
   *
   * <p>
   * Will throw a {@link ConcurrentModificationException} if the trie was modified
   * outside of the iterator.
   * </p>
   *
   * @param <E> the type of the object that is being enumerated (words or values)
   */
  private class Itr<E> implements Iterator<E>, Iterable<E> {
    /**
     * Holds all the nodes.
     */
    Queue<TrieNode<T>> entries;

    /**
     * The last returned node.
     */
    TrieNode<T> last;

    /**
     * Whether this iterator is returning the words or values of the nodes.
     */
    boolean type;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = modCount;

    /**
     * Constructs the enumerator that will be used to enumerate the values in the
     * trie.
     *
     * @param type the type of object to enumerate
     */
    Itr(boolean type) {
      this.type = type;
      entries = new Queue<>(size);

      walk((TrieNode<T> node) -> {
        if (node.isWord())
          entries.enqueue(node);
      });
    }

    public Iterator<E> iterator() {
      return this;
    }

    public boolean hasNext() {
      return !entries.isEmpty();
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public E next() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("Trie Iterator. No items left.");
      last = entries.dequeue();
      return type == WORDS ? (E) getPrefix(last) : (E) last.getValue();
    }

    /**
     * {@inheritDoc}
     *
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     *
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * </p>
     *
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     * </p>
     *
     * @throws IllegalStateException if the {@code next} method has not yet been
     *         called, or the {@code remove} method has already been called after
     *         the last call to the {@code next} method.
     *
     * @throws ConcurrentModificationException if a function modified this map
     *         during computation.
     */
    @Override
    public void remove() {
      if (last == null)
        throw new IllegalStateException("Trie Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      synchronized (Trie.this) {
        delete(getPrefix(last));
        expectedModCount++;
        last = null;
      }
    }
  }
}
