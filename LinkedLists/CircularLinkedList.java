package LinkedLists;

public class CircularLinkedList<K, V> extends DoublyLinkedList<K, V> {
  /**
   * Empty constructor besides the call to super() because there is no
   * initialization and extends the {@link DoublyLinkedList}.
   */
  public CircularLinkedList() { super(); }

  /**
   * Inserts a new node into the linked list but ensures that the head and tail
   * wrap around to retain the circular structure.
   * 
   * @param key   the key of the new node
   * @param value the value of the new node
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);
    
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

    size++;
  }

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the
   * {@code DoublyNode} or {@code null} if not found. Stops once the next node
   * points to the head, indicating restarting the cycle.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public DoublyNode<K, V> search(K key) {
    checkKey(key);
    
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
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public DoublyNode<K, V> rSearch(K key) {
    checkKey(key);

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
  @Override
  public synchronized void remove(DoublyNode<K, V> node) {
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

    size--;
  }

}