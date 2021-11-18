package data_structures.linkedLists;

import java.util.function.BiFunction;

public class SortedLinkedList<T extends Comparable<T>> extends LinkedList<T> {
  @java.io.Serial
  private static final long serialVersionUID = 199208284839394803L;

  /**
   * The function used to compare two keys and returns a boolean value indicating
   * whether the first argument is less than the second argument.
   */
  private BiFunction<T, T, Boolean> compare;

  /**
   * Empty constructor besides the call to super() because there is no
   * initialization and extends the {@link LinkedList}.
   */
  public SortedLinkedList(BiFunction<T, T, Boolean> compare) {
    super();
    this.compare = compare;
  }

  /**
   * The default constructor that uses implements a default comparison function by
   * comparing two keys using their {@code hashCode()} values.
   */
  public SortedLinkedList() {
    this((T x, T y) -> x.hashCode() < y.hashCode());
  }

  private boolean isLessThan(T x, T y) {
    return compare.apply(x, y);
  }

  /**
   * The internal compare method used to determine if the key of a
   * {@code LinkedListNode} is smaller than the other node key.
   *
   * @param x node to compare
   * @param y the other node to compare
   * @return whether the first node key is smaller than the other node key
   *
   * @throws NullPointerException if the either node is {@code null}
   */
  private boolean isLessThan(Node<T> x, Node<T> y) {
    checkNode(x);
    checkNode(y);
    return isLessThan(x.item, y.item);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Iterates through the list, comparing nodes by the comparison function of the
   * list to place the nodes in descending sorted order.
   * </p>
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(T item) {
    checkItem(item);

    Node<T> temp, node = new Node<>(item);

    if (head == null)
      head = tail = node;
    else if (head == tail) {
      if (isLessThan(node, head)) {
        link(node, head);
        head = node;
      }
      else {
        link(head, node);
        tail = node;
      }
    }
    else {
      temp = head;

      while (isLessThan(temp, node) && temp.next != null)
        temp = temp.next;

      if (temp == head) {
        if (isLessThan(temp, node))
          link(temp, node, temp.next);
        else {
          link(node, temp);
          head = node;
        }
      }
      else if (temp == tail) {
        if (isLessThan(node, temp))
          link(temp.prev, node, temp);
        else {
          link(temp, node);
          tail = node;
        }
      }
      else {
        link(temp.prev, node, temp);
      }
    }

    size++;
    modCount++;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * This implementation uses a BinarySearch by retaining the current node being
   * checked and it's index.
   * </p>
   */
  @Override
  public int indexOf(T item) {
    checkItem(item);

    Node<T> node = null;
    boolean started = false;
    int i = 0, mid, low = 0, high = size - 1;

    while (low <= high) {
      mid = Math.floorDiv(low + high, 2);

      if (!started) {
        if (mid < high >> 1) {
          i = 0;
          node = head;
        }
        else {
          i = high;
          node = tail;
        }
      }
      
      if (mid > i) {
        for (; i < mid; i++)
          node = node.next;
      }
      else {
        for (; i > mid; i--)
          node = node.prev;
      }

      if (node.item == item)
        return mid;
      else if (isLessThan(node.item, item)) {
        low = mid + 1;
      }
      else {
        high = mid - 1;
      }

      started = true;
    }

    return -1;
  }

}
