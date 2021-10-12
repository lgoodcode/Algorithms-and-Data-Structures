package LinkedLists;

public class DoublyLinkedList<K, V> {
  protected DoublyNode<K, V> head = null;
  protected DoublyNode<K, V> tail = null;
  // Iteration types
  protected int FORWARD = 0;
  protected int REVERSE = 1;

  /**
   * Empty contructor because there is no initialization.
   */
  public DoublyLinkedList() {}

  /**
   * Returns the node at the head of list.
   * 
   * @return the {@code DoublyNode} at the head or {@code null} if none
   */
  public synchronized DoublyNode<K, V> getHead() {
    return head;
  }

  /**
   * Returns the node at the tail of list.
   * 
   * @return the {@code DoublyNode} at the tail or {@code null} if none
   */
  public synchronized DoublyNode<K, V> getTail() {
    return tail;
  }

  public synchronized void insert(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    DoublyNode<K, V> node = new DoublyNode<>(key, value);

    if (head == null)
      head = tail = node;
    else {
      node.next = head;
      head.prev = node;
      head = node;
    }
  }

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the
   * {@code DoublyNode} or {@code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public synchronized DoublyNode<K, V> search(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = head;
    
    while (node != null && !node.getKey().equals(key))
      node = node.next;
    return node;
  }

  /**
   * Iterates through the list from the tail and goes in reverse until the desired
   * node with the corresponding specified key is found or the end is reached,
   * returning the {@code DoublyNode} or {@code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public synchronized DoublyNode<K, V> rSearch(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = tail;

    while (node != null && !node.getKey().equals(key))
      node = node.prev;
    return node;
  }

  /**
   * Retrieves the value of the node with the specified key or {@code null}
   * if not found.
   * 
   * <p>
   * This is an internal method that is used for both normal {@code get()} and
   * the reverse iteration {@code rGet()}. The process is identical except for
   * the type of {@code search()} method used. Simply passing the type allows for
   * reduced boilerplating.
   * </p>
   * 
   * @param key the key of the desired nodes' value to retrieve
   * @return the value or {@code null} if not node not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  protected V _get(int type, K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = type == FORWARD ? search(key) : rSearch(key);

    if (node != null && node.getKey().equals(key))
      return node.getValue();
    return null;
  }

  /**
   * Retrieves the value of the node with the specified key or {@code null}
   * if not found.
   * 
   * @param key the key of the desired nodes' value to retrieve
   * @return the value or {@code null} if not node not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public synchronized V get(K key) {
    return _get(FORWARD, key);
  }

  /**
   * Retrieves the value of the node with the specified key or {@code null}
   * if not found but, starts iterating from the tail and goes in reverse.
   * 
   * @param key the key of the desired nodes' value to retrieve
   * @return the value or {@code null} if not node not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public synchronized V rGet(K key) {
    return _get(REVERSE, key);
  }

  /**
   * Removes a {@code DoublyNode} containing the specified key. When the desired
   * node with the corresponding key is found, we want to simply dereference the
   * node by setting the current node {@code next} pointer to the node that
   * follows the desired node to remove.
   * 
   * <p>
   * This is an internal method that is used for both normal {@code remove()} and
   * the reverse iteration {@code rRemove()}. The process is identical except for
   * the type of {@code search()} method used. Simply passing the type allows for
   * reduced boilerplating.
   * </p>
   * 
   * @param key the key of the desired node to remove
   */
  private void _remove(int type, K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = type == FORWARD ? search(key) : rSearch(key);

    if (node == null)
      return;
    if (node == head && node == tail) {
      head = tail = null;
      return;
    }
    else if (node == head) {
      head = node.next;
      head.prev = null;
    }
    else if (node == tail) {
      tail = node.prev;
      tail.next = null;
    }
    else {
      node.prev.next = node.next;
      node.next.prev = node.prev;
    }
  }

  /**
   * Removes a {@code DoublyNode} containing the specified key. It starts
   * at the head and then continues down the list looking ahead an additional
   * node. This is so when the next node is the desired node with the 
   * corresponding key, we want to simply dereference the node by setting
   * the current node {@code next} pointer to the node that follows the desired
   * node to remove.
   * 
   * @param key the key of the desired node to remove
   */
  public synchronized void remove(K key) {
    _remove(FORWARD, key);
  }

  /**
   * Removes a {@code DoublyNode} containing the specified key. It starts at the
   * tail and then goes in reverse order of the list looking at each previous
   * node. This is so when the next node is the desired node with the
   * corresponding key, we want to simply dereference the node by setting the
   * current node {@code next} pointer to the node that follows the desired node
   * to remove.
   * 
   * @param key the key of the desired node to remove
   */
  public synchronized void rRemove(K key) {
    _remove(REVERSE, key);
  }

  /**
   * Displays the contents of the list in order in a JSON format.
   * 
   * @return the string format of the object
   */
  public synchronized String toString() {
    if (head == null && tail == null)
      return "{}";

    StringBuilder str = new StringBuilder();
    DoublyNode<K, V> node = head;

    str.append("{");

    while (node != null) {
      str.append("\n\"" + node.toString() + "\"");
      node = node.next;
    }
    
    return str.toString() + "\n}";
  }
}
