package LinkedLists;

/**
 * A node for {@code DoublyLinkedList} which extends the {@code LinkedListNode}
 * and is the same except for the addition of the {@code prev} member which is
 * pointer to the previous item in the list of the current node.
 * 
 * @see LinkedListNode
 * @see DoublyLinkedList
 */
public class DoublyNode<K, V> extends LinkedListNode<K, V> {
  DoublyNode<K, V> next = null;
  DoublyNode<K, V> prev = null;

  public DoublyNode(K key, V value) {
    super(key, value);
  }
}
