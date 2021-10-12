package LinkedLists;

/**
 * A functional interface to compare two objects.
 */
@FunctionalInterface
interface Compare<T> {
  abstract public boolean compare(T x, T y);
}


public class SortedDoublyLinkedList<K, V> extends DoublyLinkedList<K, V> {
  private Compare<K> compareFn;
  
  /**
   * Empty contructor because there is no initialization.
   */
  public SortedDoublyLinkedList(Compare<K> compareFn) { 
    super(); 
    
    this.compareFn = compareFn;
  }

  public SortedDoublyLinkedList() {
    this((K x, K y) -> x.hashCode() < y.hashCode());
  }

  private boolean isLessThan(DoublyNode<K, V> x, DoublyNode<K, V> y) {
    return compareFn.compare(x.getKey(), y.getKey());
  }

  public synchronized void insert(K key, V value) {
    if (key == null || key.toString().isBlank())
      throw new IllegalArgumentException("Key cannot be null or empty.");
    if (value == null || value.toString().isBlank())
      throw new IllegalArgumentException("Value cannot be null or empty.");

    DoublyNode<K, V> node = new DoublyNode<>(key, value);
    DoublyNode<K, V> temp;

    if (head == null)
      head = tail = node;
    else if (head == tail) {
      if (isLessThan(node, head)) {
        node.next = head;
        head.prev = node;
        head = node;
      }
      else {
        head.next = node;
        node.prev = head;
        tail = node;
      }
    }
    else {
      temp = head;

      while (isLessThan(temp, node) && temp.next != null)
        temp = temp.next;

      if (temp == head) {
        node.next = temp;
        temp.prev = node;
        head = node;
      }
      else if (temp == tail) {
        node.prev = temp.prev;
        node.prev.next = node;
        node.next = temp;
        temp.prev = node;
      }
      else {
        temp.prev.next = node;
        node.prev = temp.prev;
        temp.prev = node;
        node.next = temp;
      }
    }
  }
}
