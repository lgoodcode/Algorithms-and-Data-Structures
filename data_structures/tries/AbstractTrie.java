package data_structures.tries;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.EmptyEnumerator;
import data_structures.queues.Queue;

public abstract class AbstractTrie<V> {
  /**
   * Counter tracking the number of entries in the {@code Trie}.
   */
  protected int count;

  /**
   * The number of times this Trie has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the Trie fail-fast.
   *
   * @see ConcurrentModificationException
   */
  protected int modCount;

  // Enumeration/iteration constants
  private int WORDS = 0;
  private int VALUES = 1;
  private int ENTRIES = 2;

  /**
   * Checks the word to make sure it isn't {@code null} or blank. It is only used
   * in the {@link #insert()} and {@link #delete()} methods because blank words
   * cannot be stored since it is the {@code root}.
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
  protected final void checkValue(V value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
  }

  protected final <Node extends AbstractTrieNode<V>> void checkNode(Node node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
  }

  /**
   * Receives the input word and removes any whitespace {@code \s}, newline
   * {@code \n}, or tab {@code \t} characters as well as dashes {@code -} and
   * underscores {@code _}. If the input is {@code null} it simply returns a blank
   * {@code String}.
   *
   * @param word the word to parse
   * @returns the parsed word
   */
  protected final String parseWord(String word) {
    if (word == null)
      return "";
    return word.strip().replaceAll("[\n\s\t-_]", "");
  }

  /**
   * Returns the number of words stored in the {@code Trie}.
   *
   * @return the number of nodes
   */
  public final int size() {
    return count;
  }

  /**
   * Returns a boolean value indicating whether the {@code Trie} is empty or not.
   *
   * @return whether the {@code Trie} is empty
   */
  public final boolean isEmpty() {
    return count == 0;
  }

  /**
   * Returns the {@code root} of the {@code Trie}.
   *
   * <p>
   * This helps get around having to set a {@code root} property in the
   * {@link AbstractTrie} where, if the default method is implemented in the
   * abstract class, it will always returns the {@code AbstractTrieNode}, which
   * will cause the methods to not function. This makes a call to
   * {@link #search(String)} with a blank string {@code ""} so that it will
   * immediately return the {@code root} node of {@code Trie}. This allows more
   * methods to be default implementations, such as {@link #walk(Consumer)}, which
   * traverses the entire {@code Trie} from the {@code root} and the
   * {@link #_toString()} bridge method.
   * </p>
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @return the root {@code TrieNode} of the {@code Trie}
   */
  public final <Node extends AbstractTrieNode<V>> Node getRoot() {
    return search("");
  }

  /**
   * Inserts a new {@code TrieNode} into the {@code Trie} with the specified
   * {@code word} and value. If inserting a {@code word} that is already used, its
   * value will be overriden.
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
   * the current node, a new child node is created and set on the current node.
   * Otherwise, the current node points to the child node and the next character
   * is set. Once there are no more characters to iterate through, we set the
   * current node to the specified value.
   * </p>
   *
   * @param word  the word of the new node to insert
   * @param value the value of the new node to insert
   *
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank.
   */
  public abstract void insert(String word, V value);

  /**
   * Iterates through each character of the specified {@code word} and returns the
   * resulting node if it exists or {@code null} if the node doesn't exist or
   * there was no children leading to it. It will stop early if the current node
   * has a false value for the {@code hasWord} property, which indicates that the
   * node is a leaf and has no children to search through.
   *
   * <p>
   * Will accept a {@code null} or blank string because it will be parsed through
   * {@link #parseWord(String)} to a blank string which will result in returning
   * the {@code root}.
   * </p>
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param word   the node to look for
   * @return the {@code TrieNode} if exists or {@code null} if not
   */
  public abstract <Node extends AbstractTrieNode<V>> Node search(String word);

  /**
   * Returns a boolean indicating whether the {@code Trie} contains an entry with
   * the specified {@code word}. Returns {@code true} only if the resulting node
   * in the search is not {@code null} or the {@code root}.
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param word   the word to search for
   * @return whether a node in the {@code Trie} contains the specified word
   */
  @SuppressWarnings("unchecked")
  public final <Node extends AbstractTrieNode<V>> boolean hasWord(String word) {
    Node node = (Node) search(word);
    return node != null && node.isWord();
  }

  /**
   * Retrieves the value for the corresponding {@code TrieNode} of the given word
   * or {@code null} if not found, or is the {@code root}.
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param word   the word of the {@code TrieNode}
   * @return the value or {@code null} if not found
   */
  @SuppressWarnings("unchecked")
  public final <Node extends AbstractTrieNode<V>> V get(String word) {
    Node node = (Node) search(word);
    if (node == null || node.isRoot())
      return null;
    return node.getValue();
  }

  /**
   * Deletes a {@code word} value from the {@code Trie} if it exists. Will throw
   * an exception if the specified {@code word} is {@code null} or blank, as they
   * cannot be a valid stored value.
   *
   * <p>
   * Once the node with the {@code word} sets the value to {@code null}, it traces
   * back up to the {@code root} and it performs a check of the current nodes
   * children checking if it contains any children that contain words or is a
   * word. Once the node that is a word or contains a child that is a node or has
   * words, is reached, it will immediately stop tracing because we cannot remove
   * any parent node if the current node is a word or contains nodes with words.
   * </p>
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param word the word of the {@code TrieNode} to delete
   *
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank
   */
  public abstract void delete(String word);

  /**
   * Deletes a {@code TrieNode} from the {@code Trie} with the specified node.
   *
   * @param node the {@code TrieNode} to delete
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public final <Node extends AbstractTrieNode<V>> void delete(Node node) {
    checkNode(node);
    delete(getPrefix(node));
  }

  /**
   * Traces up from the specified {@code TrieNode} up to the {@code root},
   * collecting the keys from the initial node, all the way up to the
   * {@code root}, to form the prefix. Throws an exception if the specified node
   * is {@code null}.
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param node   the {@code TrieNode} which to find the prefix of
   * @return the prefix string
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public abstract <Node extends AbstractTrieNode<V>> String getPrefix(Node node);

  /**
   * The internal method that takes the starting {@code TrieNode} and
   * {@code prefix} that must correspond to their placement. Will throw an
   * exception if given the {@code TrieNode} is {@code null} and if it is the
   * {@code root} and not a blank {@code prefix}, and vice versa.
   *
   * <p>
   * Uses the {@link #walk(AbstractTrieNode, String, BiConsumer)} internal method
   * to traverse the {@code Trie} with the current {@code prefix} and uses a
   * {@code Queue} to hold all the words found. Once done traversing, it will
   * dequeue all the words into an {@code String[]} which is then returned.
   * </p>
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param start  the {@code TrieNode} to start finding words from
   * @param prefix the prefix of the specified {@code TrieNode}
   * @return the {@code String} array of words contained under the specified
   *         {@code TrieNode} or {@code null} if none or if {@code Trie} is empty
   *
   * @throws NullPointerException     if the specified starting {@code TrieNode}
   *                                  is {@code null}
   *
   * @throws IllegalArgumentException if the start {@code TrieNode} is the
   *                                  {@code root} and the {@code prefix} is
   *                                  blank, or the start node is not the
   *                                  {@code root} and the {@code prefix} is blank
   */
  protected final <Node extends AbstractTrieNode<V>> String[] findWords(Node start, String prefix) {
    if (start == null)
      throw new NullPointerException("Node cannot be null.");
    if (start.isRoot() && !prefix.isBlank())
      throw new IllegalArgumentException("Prefix must be blank if starting from the root.");
    if (!start.isRoot() && prefix.isBlank())
      throw new IllegalArgumentException("Prefix cannot be blank if not starting from root.");
    if (isEmpty()) {
      return null;
    }

    // Queue is initialized to the number of words in the trie so the maximum length
    // cannot be exceeded
    Queue<String> queue = new Queue<>(size());
    String[] words;
    int i = 0, len;

    walk(start, prefix, (Node node, String chars) -> {
      if (node.isWord())
        queue.enqueue(chars);
    });

    if (start.isWord()) {
      len = queue.size() + 1;
      words = new String[len];
      words[i++] = prefix;
    }
    else {
      len = queue.size();
      words = new String[len];
    }

    for (; i < len; i++)
      words[i] = queue.dequeue();

    return words;
  }

  /**
   * Finds all the words under the specified {@code TrieNode}. Will make a call to
   * {@link #getPrefix()} to get the {@code prefix} for the specified node to form
   * all the words. Will throw an exception if the specified starting node is
   * {@code null}.
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param start  the {@code TrieNode} to start finding words from
   * @return the {@code String} array of words contained under the specified
   *         {@code TrieNode} or {@code null} if none or if {@code Trie} is empty
   *
   * @throws NullPointerException if the specified starting {@code TrieNode} is
   *                              {@code null}
   */
  public final <Node extends AbstractTrieNode<V>> String[] findWords(Node start) {
    if (start == null)
      throw new NullPointerException("Node cannot be null.");
    return findWords(start, getPrefix(start));
  }

  /**
   * Finds all the words under the specified {@code prefix}. Will make a call to
   * {@link #search(String)} to find the corresponding {@code TrieNode}. Will
   * immediately return {@code null} if there doesn't exist a node with the
   * specified {@code prefix}.
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @param prefix the prefix of the specified starting {@code TrieNode}
   * @return the {@code String} array of words contained under the {@TrieNode} of
   *         the specified {@code prefix} or {@code null} if none or if
   *         {@code Trie} is empty
   */
  @SuppressWarnings("unchecked")
  public final <Node extends AbstractTrieNode<V>> String[] findWords(String prefix) {
    Node start = (Node) search(prefix);
    return start != null ? findWords(start, prefix) : null;
  }

  /**
   * Finds all the words contained in the {@code Trie}.
   *
   * @return the {@code String} array of words contained in the {@code Trie} or
   *         {@code null} if the {@code Trie} is empty
   */
  public final String[] findWords() {
    return isEmpty() ? null : findWords("");
  }

  /**
   * Recursive method that takes a {@code TrieNode} and a
   * {@code Consumer<TrieNode>} callback lambda function to perform side-effect
   * operations each {@code TrieNode} within the {@code Trie} in lexicographical
   * order.
   *
   * @param <Node>   a subclass of {@link AbstractTrieNode}
   * @param node     the starting {@code TrieNode} to traverse from
   * @param callback the {@code Consumer} lambda function
   */
  @SuppressWarnings("unchecked")
  public final <Node extends AbstractTrieNode<V>> void walk(Node node, Consumer<Node> callback) {
    Node[] children = (Node[]) node.getChildren();

    for (int i = 0; i < children.length; i++) {
      if (children[i] != null) {
        callback.accept(children[i]);
        walk(children[i], callback);
      }
    }
  }

  /**
   * Internal recursive method that takes a {@code TrieNode} and a
   * {@code BiConsumer<TrieNode, String>} callback lambda function to perform
   * side-effect operations each {@code TrieNode} within the {@code Trie} in
   * lexicographical order.
   *
   * <p>
   * This is used for the toString() method for subclass implementations.
   * </p>
   *
   * @param <Node>   a subclass of {@link AbstractTrieNode}
   * @param node     the starting {@code TrieNode} to traverse from
   * @param prefix   the prefix of the current node being traversed
   * @param callback the {@code BiConsumer} lambda function
   */
  @SuppressWarnings("unchecked")
  protected final <Node extends AbstractTrieNode<V>> void
    walk(Node node, String prefix, BiConsumer<Node, String> callback) {

    Node[] children = (Node[]) node.getChildren();

    for (int i = 0; i < children.length; i++) {
      if (children[i] != null) {
        callback.accept(children[i], prefix + children[i].key);
        walk(children[i], prefix + children[i].key, callback);
      }
    }
  }

  /**
   * Recursive method that traverses all {@code TrieNode} in the {@code Trie},
   * beginning from the {@code root}. Otherwise, if the {@code Trie} is empty, it
   * won't do anything.
   *
   * @param <Node>   a subclass of {@link AbstractTrieNode}
   * @param callback the {@code Consumer} lambda function
   */
  public final <Node extends AbstractTrieNode<V>> void walk(Consumer<Node> callback) {
    if (!isEmpty())
      walk(getRoot(), callback);
  }

  /**
   * Bridge method to be generic for any {@code Trie} implementation to traverse
   * all the {@code TrieNodes} and return the object string. This is required
   * because the {@code toString()} method cannot be generic because of the
   * erasure.
   *
   * @param <Node> a subclass of {@link AbstractTrieNode}
   * @return the object string of the {@code Trie}
   */
  private <Node extends AbstractTrieNode<V>> String _toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    walk(getRoot(), "", (Node node, String prefix) -> {
      if (node.isWord())
        sb.append("\s\s\"" + prefix + " -> " + node.getValue() + "\",\n");
    });

    return sb.toString() + "}";
  }

  /**
   * Traverses the {@code Trie} and prints out the words by their prefix and
   * associated values in lexicographical order.
   *
   * @return the object string of the {@code Trie}
   */
  @Override
  public final String toString() {
    return _toString();
  }

  /**
   * Returns an {@link Iterable} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  protected final <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, true);
  }

  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  protected final <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, true);
  }
  /**
   * Returns an {@link Enumeration} of the specified type.
   *
   * @param <T>  Generic type to allow any type to be enumerated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Enumeration}
   */
  protected final <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, false);
  }

  public final Iterable<String> words() {
    return getIterable(WORDS);
  }

  public final Iterable<V> values() {
    return getIterable(VALUES);
  }

  public final <Node extends AbstractTrieNode<V>> Iterable<Node> entries() {
    return getIterable(ENTRIES);
  }

  public final Iterator<String> wordsIterator() {
    return getIterator(WORDS);
  }

  public final Iterator<V> valuesIterator() {
    return getIterator(VALUES);
  }

  public final <Node extends AbstractTrieNode<V>> Iterator<Node> entriesIterator() {
    return getIterator(ENTRIES);
  }

  public final Enumeration<String> wordsEnumeration() {
    return getEnumeration(WORDS);
  }

  public final Enumeration<V> valuesEnumeration() {
    return getEnumeration(VALUES);
  }

  public final <Node extends AbstractTrieNode<V>> Enumeration<Node> entriesEnumeration() {
    return getEnumeration(ENTRIES);
  }

  /**
   * A trie enumerator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   *
   * @param <T> the type of the object that is being enumerated
   */
  protected final class Enumerator<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
    protected Queue<AbstractTrieNode<V>> entries;
    protected AbstractTrieNode<V> last;
    protected int type, size, index = 0;

    /**
     * Indicates whether this Enumerator is serving as an Iterator or an
     * Enumeration.
     */
    protected boolean iterator;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    protected int expectedModCount = AbstractTrie.this.modCount;

    /**
     * Constructs the enumerator that will be used to enumerate the values in the
     * trie.
     *
     * @param type     the type of object to enumerate
     * @param iterator whether this will serve as an {@code Enumeration} or
     *                 {@code Iterator}
     */
    protected Enumerator(int type, boolean iterator) {
      this.size = AbstractTrie.this.count;
      this.iterator = iterator;
      this.type = type;
      entries = new Queue<>(size);

      walk((AbstractTrieNode<V> node) -> {
        if (node.isWord())
          entries.enqueue(node);
      });
    }

    // Iterable method
    public Iterator<T> iterator() {
      return iterator ? this : this.asIterator();
    }

    /**
     * Checks whether there are more elments to return.
     *
     * @return if this object has one or more items to provide or not
     */
    public boolean hasMoreElements() {
      return !entries.isEmpty();
    }

    /**
     * Returns the next element if it has one to provide.
     *
     * @return the next element
     *
     * @throws NoSuchElementException if no more elements exist
     */
    @SuppressWarnings("unchecked")
    public T nextElement() {
      if (!hasNext())
        throw new NoSuchElementException("Queue enumerator. No items in queue.");
      last = entries.dequeue();
      return type == WORDS ? (T) AbstractTrie.this.getPrefix(last)
        : (type == VALUES ? (T) last.getValue() : (T) last);
    }

    /**
     * The Iterator method; the same as Enumeration.
     */
    public boolean hasNext() {
      return hasMoreElements();
    }

    /**
     * Iterator method. Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     */
    public T next() {
      if (AbstractTrie.this.modCount != expectedModCount)
        throw new ConcurrentModificationException();
      return nextElement();
    }

    /**
     * {@inheritDoc}
     *
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     *
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this iterator, e.g., if the object is an
     *         {@code Enumeration}.
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
      if (!iterator)
        throw new UnsupportedOperationException();
      if (last == null)
        throw new IllegalStateException("trie Enumerator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the trie object while removing entry
      synchronized (AbstractTrie.this) {
        // Pass the current index to remove the last item
        AbstractTrie.this.delete(AbstractTrie.this.getPrefix(last));
        expectedModCount++;
        last = null;
      }
    }
  }

}
