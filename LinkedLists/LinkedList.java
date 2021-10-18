package LinkedLists;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Creates a simple forward-feed LinkedList that performs, at worst case, O(n).
 * This is because all operations are performed in a linear fashion, iterating
 * through the list until either the desired node is found or the end is reached.
 */
public class LinkedList<K, V> extends AbstractLinkedList<K, V> {

  /**
   * Empty constructor because there is no initialization.
   */
  public LinkedList() {}

  /**
   * Inserts a new node with the specified key and value pair. If there is no
   * {@code head}, then it will simply set the head as the new node. Otherwise,
   * the new node's {@code next} will point to the current head and the new head
   * will be set as the new node.
   * 
   * @param key   the key of the new node
   * @param value the value of the new node
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or
   *                                  blank.
   */
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    LinkedListNode<K, V> node = new LinkedListNode<>(key, value);

    if (head != null)
      node.next = head;
    head = node;

    size++;
  }

  /**
   * Inserts a new node at the specified index position.
   * 
   * @param index the position to insert the new node
   * @param key   the key of the new node
   * @param value the value of the new node
   * 
   * @throws IllegalArgumentException  if the key or value is {@code null} or
   *                                   empty.
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public synchronized void insertAt(int index, K key, V value) {
    checkIndex(index);
    checkKey(key);
    checkValue(value);

    LinkedListNode<K, V> node = new LinkedListNode<>(key, value);
    LinkedListNode<K, V> prev;

    if (index == 0) {
      node.next = head;
      head = node;
    }
    else {
      prev = searchIndex(index - 1);
      node.next = prev.next;
      prev.next = node;
    }

    size++;
  }

  /**
   * Iterates through the list until the desired node with the corresponding
   * specified key is found or the end is reached, returning the node or
   * {@code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the node or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public synchronized LinkedListNode<K, V> search(K key) {
    checkKey(key);

    LinkedListNode<K, V> node = head;

    while (node != null && !node.getKey().equals(key))
      node = node.next;
    return node;
  }

  /**
   * Returns the node at the specified index or {@code null} if there are no items
   * in the list.
   * 
   * @param index the index position of the node to return
   * @return the node at the specified index position or {@code null} if list is
   *         empty
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public LinkedListNode<K, V> searchIndex(int index) {
    checkIndex(index);

    if (head == null)
      return null;

    LinkedListNode<K, V> node = head;

    for (int i=0; i<index; i++)
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
    LinkedListNode<K, V> node = search(key);
    return node != null ? node.getValue() : null;
  }

  /**
   * Retrieves the value of the node with at the specified index position.
   * 
   * @param index the index of the desired node's value to retrieve
   * @return the value or {@code null} if node not found
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public V getIndex(int index) {
    LinkedListNode<K, V> node = searchIndex(index);
    return node != null ? node.getValue() : null;
  }

  /**
   * Removes the node containing the specified key. It starts at the head and then
   * continues down the list looking ahead an additional node. This is so when the
   * next node is the desired node with the corresponding key, we want to simply
   * dereference the node by setting the current node {@code next} pointer to the
   * node that follows the desired node to remove.
   * 
   * @param key the key of the desired node to remove
   */
  public synchronized void remove(K key) {
    LinkedListNode<K, V> node = head;

    while (node != null && node.next != null && !node.next.getKey().equals(key))
      node = node.next;

    node.next = node.next.next;
    size--;
  }

  /**
   * Removes the node at the specified index. Will throw an exception if the index
   * is invalid.
   * 
   * @param index the position of the node to remove
   * @return boolean indicating whether the node was successfully removed or not
   * 
   * @throws IndexOutOfBoundsException if the index is not within the range
   *                                   {@code [0, size-1]}
   */
  public synchronized void removeIndex(int index) {
    if (index == 0) {
      head = null;
      size--;
      return;
    }

    LinkedListNode<K, V> prev = searchIndex(index - 1);
    LinkedListNode<K, V> node;

    if (prev == null)
      return;
    
    node = prev.next;
    prev.next = node.next;
    size--;
  }
  
  /**
   * Displays the contents of the list in order in a JSON format.
   * 
   * @return the string format of the object
   */
  public String toString() {
    if (head == null)
      return "{}";
    
    StringBuilder sb = new StringBuilder();
    Iterable<LinkedListNode<K, V>> entries = entries();
    
    sb.append("{\n");

    entries.forEach((node) -> sb.append(node.toString() + "\n"));
    
    return sb.toString() + "}";
  }

  protected <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterable<>();
    return new Enumerator<>(type, true);
  }

  protected <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return Collections.emptyIterator();
    return new Enumerator<>(type, true);
  }

  protected <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return Collections.emptyEnumeration();
    return new Enumerator<>(type, false);
  }

  protected class Enumerator<T> extends AbstractEnumerator<T> {
    Enumerator(int type, boolean iterator) {
      list = new LinkedListNode<?, ?>[size];
      this.type = type;
      this.iterator = iterator;
      index = 0;

      LinkedListNode<K, V> node = LinkedList.this.getHead();

      do {
        list[index++] = node;
        node = node.next;
      } while (node != null);
    }
  } 
}
