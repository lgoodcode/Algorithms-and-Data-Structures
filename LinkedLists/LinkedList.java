package LinkedLists;

/**
 * Creates a simple forward-feed LinkedList that performs, at worst case, O(n).
 * This is because all operations are performed in a linear fashion, iterating
 * through the list until either the desired node is found or the end is reached.
 */
public class LinkedList<K, V> {
  private LinkedListNode<K, V> head = null;

  /**
   * Empty constructor because there is no initialization.
   */
  public LinkedList() {}

  /**
   * Inserts a new {@code LinkedListNode} with the specified key and value pair.
   * If there is no {@code head}, then it will simply set the head as the new
   * node. Otherwise, the new node's {@code next} will point to the current head
   * and the new head will be set as the new node.
   * 
   * @param key   the key of the new node
   * @param value the value of the new node
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or
   *                                  empty.
   */
  public void insert(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    LinkedListNode<K, V> node = new LinkedListNode<>(key, value);

    if (head != null)
      node.next = head;
    head = node;
  }

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the
   * {@code LinkedListNode} or @{code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the {@code LinkedListNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public LinkedListNode<K, V> search(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    LinkedListNode<K, V> node = head;

    while (node != null && !node.getKey().equals(key))
      node = node.next;
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

    LinkedListNode<K, V> node = search(key);

    if (node != null && node.getKey().equals(key))
      return node.getValue();
    return null;
  }

  /**
   * Removes a {@code LinkedListNode} containing the specified key. It starts
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
      
    LinkedListNode<K, V> node = head;

    while (node != null && node.next != null && !node.next.getKey().equals(key))
      node = node.next;

    if (node.next.getKey().equals(key))
      node.next = node.next.next;
  }

  /**
   * Displays the contents of the list in order in a JSON format.
   * 
   * @return the string format of the object
   */
  public String toString() {
    StringBuilder str = new StringBuilder();

    if (head == null)
      return "{}";

    LinkedListNode<K, V> node = head;
    str.append("{");

    while (node != null) {
      str.append("\n\"" + node.toString() + "\"");
      node = node.next;
    }
    
    return str.toString() + "\n}";
  }
}
