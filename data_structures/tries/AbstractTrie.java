package data_structures.tries;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import data_structures.EmptyEnumerator;
import data_structures.queues.Queue;

public abstract class AbstractTrie<T> {
  public static class Node<T> {
    /**
     * Whether the current node is a word or contains a child that is or contains
     * word(s).
     */
    protected boolean hasWord;

    /**
     * The key of the node.
     */
    protected char key;

    /**
     * The value of the node.
     */
    protected T value;

    /**
     * The root key
     */
    protected final char ROOT = '\s';

    /**
     * The array of the child {@code TrieNodes}.
     */
    protected Node<T>[] children;

    /**
     * The parent {@code TrieNode} used for tracing up to the root.
     */
    protected Node<T> parent;

    /**
     * Initializes an empty {@code TrieNode} with the specified key. The key is used
     * to determine what position the node is in and what words it could contain.
     *
     * @param key the character key to set the {@code TrieNode}
     */
    public <TrieNode extends Node<T>> Node(char key, TrieNode parent) {
      this.key = key;
      this.parent = parent;
      hasWord = false;
    }

    /**
     * Default constructor for the root {@code TrieNode}.
     */
    protected Node() {
      key = ROOT;
      hasWord = false;
    }

    /**
     * Internal method to get the numeric value of the character to map to the
     * proper index location based on the letter in the range of {@code [A, Z]}
     * case-insensitive. The numeric index value from the given character is in the
     * range {@code [0, 25]}.
     *
     * @param c the character to get the index of
     * @return the index of the character in the range of {@code [0, 25]}
     */
    protected final int getIndex(char c) {
      int index = Character.getNumericValue(c);

      if (index < 10 || index > 35)
        throw new IllegalArgumentException("Character must be a letter A-Z, case-insensitive.");
      return index - 10;
    }

    /**
     * Determines whether the current word is a word with a value.
     *
     * @return if the node has a word value
     */
    public final boolean isWord() {
      return value != null;
    }

    /**
     * Determines whether the current {@code TrieNode} is the {@code root} of the
     * {@code Trie} by checking if the key is a whitespace character {@code '\s'}.
     *
     * @return if the node is the {@code root} or not
     */
    public final boolean isRoot() {
      return key == ROOT;
    }

    /**
     * Returns the key of the node
     * 
     * @return the character key of the node
     */
    public final char getKey() {
      return key;
    }

    /**
     * Returns the value of the node or {@code null} if none (not a word).
     * 
     * @return the value of the node or {@code null} if none
     */
    public final T getValue() {
      return value;
    }

    /**
     * Returns the child {@code TrieNode} for the given character.
     *
     * @param <TrieNode> {@link Node} or a subclass of
     * @param c the character of the child to get
     * @return the child {@code TrieNode}
     */
    @SuppressWarnings("unchecked")
    protected <TrieNode extends Node<T>> TrieNode getChild(char c) {
      return (TrieNode) children[getIndex(c)];
    }

    /**
     * Sets a new child {@code TrieNode} for the specified character index.
     *
     * @param <TrieNode> {@link Node} or a subclass of
     * @param c     the character index slot to insert the new child
     * @param child the new {@TrieNode} child
     */
    protected <TrieNode extends Node<T>> void setChild(char c, TrieNode child) {
      children[getIndex(c)] = (Node<T>) child;
    }

    /**
     * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
     * with a word or is a word.
     *
     * @param c the character key of the child {@code TrieNode} to remove
     */
    protected void removeChild(char c) {
      children[getIndex(c)] = null;
    }

    /**
     * Gets the array of {@code TrieNode} children. Used for iterating in
     * {@code delete()} of the {@code Trie} whne tracing up to the root, removing
     * any empty nodes, and in {@code Trie.toString()}.
     *
     * @param <TrieNode> {@link Node} or a subclass of
     * @return the {@code TrieNode} children
     */
    @SuppressWarnings("unchecked")
    protected <TrieNode extends Node<T>> TrieNode[] getChildren() {
      return (TrieNode[]) children;
    }

  }

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
  protected final void checkValue(T value) {
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");
  }

  /**
   * Checks the specified {@code TrieNode} that it isn't {@code null} and is of
   * the class that it belongs to.
   * 
   * @param <TrieNode> {@link Node} or a subclass of 
   * @param node the node to verify
   * 
   * @throws NullPointerException if the node is {@code null}
   * @throws IllegalArgumentException if the node is not of the proper class
   */
  protected abstract <TrieNode extends Node<T>> void checkNode(TrieNode node);

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
   * @param <TrieNode> {@link Node} or a subclass of 
   * @return the root {@code TrieNode} of the {@code Trie}
   */
  public final <TrieNode extends Node<T>> TrieNode getRoot() {
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
  public abstract void insert(String word, T value);

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
   * @param <TrieNode> {@link Node} or a subclass of 
   * @param word   the node to look for
   * @return the {@code TrieNode} if exists or {@code null} if not
   */
  public abstract <TrieNode extends Node<T>> TrieNode search(String word);

  /**
   * Returns a boolean indicating whether the {@code Trie} contains an entry with
   * the specified {@code word}. Returns {@code true} only if the resulting node
   * in the search is not {@code null} or the {@code root}.
   *
   * @param <TrieNode> {@link Node} or a subclass of 
   * @param word   the word to search for
   * @return whether a node in the {@code Trie} contains the specified word
   */
  @SuppressWarnings("unchecked")
  public final <TrieNode extends Node<T>> boolean hasWord(String word) {
    TrieNode node = (TrieNode) search(word);
    return node != null && node.isWord();
  }

  /**
   * Retrieves the value for the corresponding {@code TrieNode} of the given word
   * or {@code null}, is not a word which would contain a value, if not found, or
   * is the {@code root}.
   *
   * @param <TrieNode> {@link Node} or a subclass of
   * @param word       the word of the {@code TrieNode}
   * @return the value or {@code null} if not found
   */
  @SuppressWarnings("unchecked")
  public final <TrieNode extends Node<T>> T get(String word) {
    TrieNode node = (TrieNode) search(word);
    if (node == null || node.isRoot() || !node.isWord())
      return null;
    return node.value;
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
   * @param <TrieNode> {@link Node} or a subclass of
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
  public final <TrieNode extends Node<T>> void delete(TrieNode node) {
    checkNode(node);
    delete(getPrefix(node));
  }

  /**
   * Traces up from the specified {@code TrieNode} up to the {@code root},
   * collecting the keys from the initial node, all the way up to the
   * {@code root}, to form the prefix. Throws an exception if the specified node
   * is {@code null}.
   *
   * @param <TrieNode> {@link Node} or a subclass of
   * @param node       the {@code TrieNode} which to find the prefix of
   * @return the prefix string
   *
   * @throws NullPointerException if the specified node is {@code null}
   */
  public <TrieNode extends Node<T>> String getPrefix(TrieNode node) {
    if (node == null)
      throw new NullPointerException("Node cannot be null.");
    if (node.isRoot())
      return "";

    Node<T> currNode = node, parent = currNode.parent;
    String prefix = "";

    while (!currNode.isRoot()) {
      prefix = currNode.key + prefix;
      currNode = parent;
      parent = currNode.parent;
    }

    return prefix;
  }

  /**
   * The internal method that takes the starting {@code TrieNode} and
   * {@code prefix} that must correspond to their placement. Will throw an
   * exception if given the {@code TrieNode} is {@code null} and if it is the
   * {@code root} and not a blank {@code prefix}, and vice versa.
   *
   * <p>
   * Uses the {@link #walk(Node, String, BiConsumer)} internal method
   * to traverse the {@code Trie} with the current {@code prefix} and uses a
   * {@code Queue} to hold all the words found. Once done traversing, it will
   * dequeue all the words into an {@code String[]} which is then returned.
   * </p>
   *
   * @param <TrieNode> {@link Node} or a subclass of
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
  protected final <TrieNode extends Node<T>> String[] findWords(TrieNode start, String prefix) {
    if (start == null)
      throw new NullPointerException("TrieNode cannot be null.");
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

    walk(start, prefix, (TrieNode node, String chars) -> {
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
   * @param <TrieNode> {@link Node} or a subclass of
   * @param start  the {@code TrieNode} to start finding words from
   * @return the {@code String} array of words contained under the specified
   *         {@code TrieNode} or {@code null} if none or if {@code Trie} is empty
   *
   * @throws NullPointerException if the specified starting {@code TrieNode} is
   *                              {@code null}
   */
  public final <TrieNode extends Node<T>> String[] findWords(TrieNode start) {
    if (start == null)
      throw new NullPointerException("TrieNode cannot be null.");
    return findWords(start, getPrefix(start));
  }

  /**
   * Finds all the words under the specified {@code prefix}. Will make a call to
   * {@link #search(String)} to find the corresponding {@code TrieNode}. Will
   * immediately return {@code null} if there doesn't exist a node with the
   * specified {@code prefix}.
   *
   * @param <TrieNode> {@link Node} or a subclass of
   * @param prefix the prefix of the specified starting {@code TrieNode}
   * @return the {@code String} array of words contained under the {@TrieNode} of
   *         the specified {@code prefix} or {@code null} if none or if
   *         {@code Trie} is empty
   */
  @SuppressWarnings("unchecked")
  public final <TrieNode extends Node<T>> String[] findWords(String prefix) {
    TrieNode start = (TrieNode) search(prefix);
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
   * @param <TrieNode>   a subclass of {@link Node}
   * @param node     the starting {@code TrieNode} to traverse from
   * @param callback the {@code Consumer} lambda function
   */
  @SuppressWarnings("unchecked")
  public final <TrieNode extends Node<T>> void walk(TrieNode node, Consumer<TrieNode> callback) {
    TrieNode[] children = (TrieNode[]) node.getChildren();

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
   * @param <TrieNode>   a subclass of {@link Node}
   * @param node     the starting {@code TrieNode} to traverse from
   * @param prefix   the prefix of the current node being traversed
   * @param callback the {@code BiConsumer} lambda function
   */
  @SuppressWarnings("unchecked")
  protected final <TrieNode extends Node<T>> void
    walk(TrieNode node, String prefix, BiConsumer<TrieNode, String> callback) {

    TrieNode[] children = (TrieNode[]) node.getChildren();

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
   * @param <TrieNode>   a subclass of {@link Node}
   * @param callback the {@code Consumer} lambda function
   */
  public final <TrieNode extends Node<T>> void walk(Consumer<TrieNode> callback) {
    if (!isEmpty())
      walk(getRoot(), callback);
  }

  /**
   * Bridge method to be generic for any {@code Trie} implementation to traverse
   * all the {@code TrieNodes} and return the object string. This is required
   * because the {@code toString()} method cannot be generic because of the
   * erasure.
   *
   * @param <TrieNode> {@link Node} or a subclass of
   * @return the object string of the {@code Trie}
   */
  private <TrieNode extends Node<T>> String _toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder("{\n");

    walk(getRoot(), "", (TrieNode node, String prefix) -> {
      if (node.isWord())
        sb.append("\s\s\"" + prefix + " -> " + node.value + "\",\n");
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
   * @param <E>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterable}
   */
  protected final <E> Iterable<E> getIterable(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, true);
  }

  /**
   * Returns an {@link Iterator} of the specified type.
   *
   * @param <E>  Generic type to allow any type to be iterated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Iterator}
   */
  protected final <E> Iterator<E> getIterator(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, true);
  }
  /**
   * Returns an {@link Enumeration} of the specified type.
   *
   * @param <E>  Generic type to allow any type to be enumerated over
   * @param type the type of item to iterate (keys, values, or entries)
   * @return the {@code Enumeration}
   */
  protected final <E> Enumeration<E> getEnumeration(int type) {
    if (isEmpty())
      return new EmptyEnumerator<>();
    return new Enumerator<>(type, false);
  }

  public final Iterable<String> words() {
    return getIterable(WORDS);
  }

  public final Iterable<T> values() {
    return getIterable(VALUES);
  }

  public final <TrieNode extends Node<T>> Iterable<TrieNode> entries() {
    return getIterable(ENTRIES);
  }

  public final Iterator<String> wordsIterator() {
    return getIterator(WORDS);
  }

  public final Iterator<T> valuesIterator() {
    return getIterator(VALUES);
  }

  public final <TrieNode extends Node<T>> Iterator<TrieNode> entriesIterator() {
    return getIterator(ENTRIES);
  }

  public final Enumeration<String> wordsEnumeration() {
    return getEnumeration(WORDS);
  }

  public final Enumeration<T> valuesEnumeration() {
    return getEnumeration(VALUES);
  }

  public final <TrieNode extends Node<T>> Enumeration<TrieNode> entriesEnumeration() {
    return getEnumeration(ENTRIES);
  }

  /**
   * A trie enumerator class. This class implements the Enumeration,
   * Iterator, and Iterable interfaces, but individual instances can be created
   * with the Iterator methods disabled. This is necessary to avoid
   * unintentionally increasing the capabilities granted a user by passing an
   * Enumeration.
   *
   * @param <E> the type of the object that is being enumerated
   */
  protected final class Enumerator<E> implements Enumeration<E>, Iterator<E>, Iterable<E> {
    protected Queue<Node<T>> entries;
    protected Node<T> last;
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

      walk((Node<T> node) -> {
        if (node.isWord())
          entries.enqueue(node);
      });
    }

    // Iterable method
    public Iterator<E> iterator() {
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
    public E nextElement() {
      if (!hasNext())
        throw new NoSuchElementException("Queue enumerator. No items in queue.");
      last = entries.dequeue();
      return type == WORDS ? (E) AbstractTrie.this.getPrefix(last)
        : (type == VALUES ? (E) last.value : (E) last);
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
    public E next() {
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
