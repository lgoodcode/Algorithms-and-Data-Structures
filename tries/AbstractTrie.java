package data_structures.tries;

import java.util.function.Consumer;
import java.util.function.BiConsumer;

public abstract class AbstractTrie<V> {
  /**
   * Counter tracking the number of entries in the {@code Trie}.
   */
  protected int count;

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
   * @param word the word to search for
   * @return whether a node in the {@code Trie} contains the specified word
   */
  public abstract boolean hasWord(String word);

  /**
   * Retrieves the value for the corresponding {@code TrieNode} of the given word
   * or {@code null} if not found, or is the {@code root}.
   *
   * @param word the word of the {@code TrieNode}
   * @return the value or {@code null} if not found
   */
  public abstract V get(String word);

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
   * @param word the word of the {@code TrieNode} to delete
   *
   * @throws IllegalArgumentException if the word or value is {@code null} or
   *                                  blank
   */
  public abstract void delete(String word);

  /**
   * Traces up from the specified {@code TrieNode} up to the {@code root}, collecting the
   * keys from the initial node, all the way up to the {@code root}, to form the prefix.
   * Throws an exception if the specified node is {@code null}.
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
  protected abstract <Node extends AbstractTrieNode<V>> String[] findWords(Node start, String prefix);

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
  public abstract <Node extends AbstractTrieNode<V>> String[] findWords(Node start);

  /**
   * Finds all the words under the specified {@code prefix}. Will make a call to
   * {@link #search(String)} to find the corresponding {@code TrieNode}. Will
   * immediately return {@code null} if there doesn't exist a node with the
   * specified {@code prefix}.
   *
   * @param prefix the prefix of the specified starting {@code TrieNode}
   * @return the {@code String} array of words contained under the {@TrieNode} of
   *         the specified {@code prefix} or {@code null} if none or if
   *         {@code Trie} is empty
   */
  public abstract String[] findWords(String prefix);

  /**
   * Finds all the words contained in the {@code Trie}.
   *
   * @return the {@code String} array of words contained in the {@code Trie} or
   *         {@code null} if the {@code Trie} is empty
   */
  public abstract String[] findWords();

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
  public <Node extends AbstractTrieNode<V>> void walk(Node node, Consumer<Node> callback) {
    Node[] children = (Node[]) node.getChildren();

    for (int i=0; i<children.length; i++) {
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
  protected <Node extends AbstractTrieNode<V>> void
    walk(Node node, String prefix, BiConsumer<Node, String> callback) {

    Node[] children = (Node[]) node.getChildren();

    for (int i=0; i<children.length; i++) {
      if (children[i] != null) {
        callback.accept(children[i], prefix + children[i].key);
        walk(children[i], prefix + children[i].key, callback);
      }
    }
  }

}
