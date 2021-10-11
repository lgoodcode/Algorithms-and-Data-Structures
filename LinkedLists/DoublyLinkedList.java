package LinkedLists;

public class DoublyLinkedList<K, V> {
  private DoublyNode<K, V> head = null;
  private DoublyNode<K, V> tail = null;

  /**
   * Empty contructor because there is no initialization.
   */
  public DoublyLinkedList() {}

  /**
   * Returns the node at the head of list.
   * 
   * @return the {@code DoublyNode} at the head or {@code null} if none
   */
  public DoublyNode<K, V> getHead() {
    return head;
  }

  /**
   * Returns the node at the tail of list.
   * 
   * @return the {@code DoublyNode} at the tail or {@code null} if none
   */
  public DoublyNode<K, V> getTail() {
    return tail;
  }

  public void insert(K key, V value) {
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
   * {@code DoublyNode} or @{code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public DoublyNode<K, V> search(K key) {
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
  public DoublyNode<K, V> rSearch(K key) {
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
   * @param key the key of the desired nodes' value to retrieve
   * @return the value or {@code null} if not node not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public V get(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = search(key);

    if (node != null && node.getKey().equals(key))
      return node.getValue();
    return null;
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
  public V rGet(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = rSearch(key);

    if (node != null && node.getKey().equals(key))
      return node.getValue();
    return null;
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
  public void remove(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = search(key);

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
   * Removes a {@code DoublyNode} containing the specified key. It starts at the
   * tail and then goes in reverse order of the list looking at each previous
   * node. This is so when the next node is the desired node with the
   * corresponding key, we want to simply dereference the node by setting the
   * current node {@code next} pointer to the node that follows the desired node
   * to remove.
   * 
   * @param key the key of the desired node to remove
   */
  public void rRemove(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = search(key);

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
   * Displays the contents of the list in order in a JSON format.
   * 
   * @return the string format of the object
   */
  public String toString() {
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
