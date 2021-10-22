package data_structures.tries;

public class TrieNode<V> {
  private TrieNode<V>[] children;
  private String key;
  private V value;

  /**
   * Initializes a child node
   * 
   * @param key
   */
  @SuppressWarnings("unchecked")
  public TrieNode(String key) {
    if (key == null || key.isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank.");

    children = (TrieNode<V>[]) new TrieNode<?>[26];
    this.key = key;
    value = null;
  }

  /**
   * Initializes the root node
   */
  @SuppressWarnings("unchecked")
  protected TrieNode() {
    children = (TrieNode<V>[]) new TrieNode<?>[26];
  }

  private int getIndex(char c) {
    int index = Character.getNumericValue(c);
    
    if (index < 10 || index > 35)
      throw new IllegalArgumentException("Character must be a letter A-Z, case-insensitive.");
    return index - 10;
  }

  public TrieNode<V> getChild(char c) {
    return children[getIndex(c)];
  }

  public void setChild(char c, TrieNode<V> child) {
    children[getIndex(c)] = child;
  }

  protected TrieNode<V>[] getChildren() {
    return children;
  }

  public String getKey() {
    return key;
  }

  public void setValue(V value) {
    this.value = value;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
    return key + " -> " + value;
  }

}