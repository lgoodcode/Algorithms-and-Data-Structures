package LinkedLists;

public class CircularDoublyLinkedList<K, V> extends DoublyLinkedList<K, V> {
  /**
   * Empty constructor besides the call to super().
   */
  public CircularDoublyLinkedList() { super(); }

  @Override
  public void insert(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    DoublyNode<K, V> node = new DoublyNode<>(key, value);

    if (head == null) {
      head = tail = node;
      node.next = node.prev = node;
    }
    else {
      node.next = head;
      node.prev = head.prev;
      head = head.prev = tail.next = node;
    }
  }

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the
   * {@code DoublyNode} or {@code null} if not found. Stops once the next
   * node points to the head, indicating restarting the cycle.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  @Override
  public DoublyNode<K, V> search(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = head;
    
    if (node == null)
      return null;
    
    while (!node.next.equals(head) && !node.getKey().equals(key))
      node = node.next;

    if (node.getKey().equals(key))
      return node;
    return null;
  }

  /**
   * Iterates through the list from the tail and goes in reverse until the desired
   * node with the corresponding specified key is found or the end is reached,
   * returning the {@code DoublyNode} or {@code null} if not found. Stops once
   * the previous node points to the tail, indicating restarting the cycle.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  @Override
  public DoublyNode<K, V> rSearch(K key) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or blank");

    DoublyNode<K, V> node = head;

    if (node == null)
      return null;
    
    while (!node.prev.equals(head) && !node.getKey().equals(key))
      node = node.prev;

    if (node.getKey().equals(key))
      return node;
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * The internal process of retrieving a value from a node is identical to the
   * superclass {@code DoublyLinkedList} definition.
   * </p>
   */
  @Override
  public V get(K key) {
    return super._get(FORWARD, key);
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * The internal process of retrieving a value from a node is identical to the
   * superclass {@code DoublyLinkedList} definition.
   * </p>
   */
  @Override
  public V rGet(K key) {
    return super._get(REVERSE, key);
  }

  /**
   * Removes a {@code DoublyNode} containing the specified key. Due to the
   * circular nature, there is a slight difference between this and the
   * {@code DoublyLinkedList} implementation; there are no {@code null} pointers,
   * so the removed nodes' previous pointer will point to the nodes next and vice
   * versa for the removed nodes next pointer. This is required to retain the
   * circular structure.
   * 
   * <p>
   * This is an internal method that is used for both normal {@code remove()} and
   * the reverse iteration {@code rRemove()}. The process is identical except for
   * the type of {@code search()} method used. Simply passing the type allows for
   * reduced boilerplating.
   * </p>
   * 
   * <p>
   * Because of the circular structure, a different implementation is required
   * that the superclass definition.
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
    if (node == head && node == tail)
      head = tail = null;
    else if (node == head)
      head = node.next;
    else if (node == tail)
      tail = node.prev;

    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  @Override
  public void remove(K key) {
    _remove(FORWARD, key);
  }

  /**
   * Removes a {@code DoublyNode} containing the specified key. It starts at the
   * tail and then continues up the list looking behind an additional node.
   * 
   * @param key the key of the desired node to remove
   */
  @Override
  public void rRemove(K key) {
    _remove(REVERSE, key);
  }

  /**
   * Due to the circular structure, the iteration process is different in that we
   * want to always iterate the {@code toString()} method on the node because
   * there will always be at least one, and then check whether it is the head to
   * determine once we have gone back to the start.
   * 
   * @return the object string in JSON format
   */
  @Override
  public String toString() {
    if (head == null && tail == null)
      return "{}";

    StringBuilder str = new StringBuilder();
    DoublyNode<K, V> node = head;

    str.append("{");

    do {
      str.append("\n\"" + node.toString() + "\"");
      node = node.next;
    } while (node != head);
    
    return str.toString() + "\n}";
  }
}