package data_structures.linkedLists;

import java.util.function.BiFunction;

public class SortedLinkedList<K, V> extends DoublyLinkedList<K, V> {
  private BiFunction<K, K, Boolean> compareFn;
  
  /**
   * Empty contructor because there is no initialization.
   */
  public SortedLinkedList(BiFunction<K, K, Boolean> compareFn) { 
    super(); 
    
    this.compareFn = compareFn;
  }

  public SortedLinkedList() {
    this((K x, K y) -> x.hashCode() < y.hashCode());
  }

  private boolean isLessThan(DoublyNode<K, V> x, DoublyNode<K, V> y) {
    return compareFn.apply(x.getKey(), y.getKey());
  }

  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

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

    size++;
  }

  /**
   * This iterates in the order that the list is because it is sorted. Using
   * the {@code entries()} method will result in the string being in reverse
   * order, which is not what we want for this structure because it should
   * output the contents sorted.
   * 
   * @return the list string
   */
  @Override
  public String toString() {
    if (head == null)
      return "{}";
    
    StringBuilder sb = new StringBuilder();
    DoublyNode<K, V> node = getHead();
    
    sb.append("{\n");

    do {
      sb.append(node.toString() + "\n");
      node = node.next;
    } while (node != null);

    
    return sb.toString() + "}";
  }
}
