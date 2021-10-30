package data_structures.linkedLists;

public class CircularLinkedList<T> extends LinkedList<T> {
  /**
   * Empty constructor besides the call to super() because there is no
   * initialization and extends the {@link LinkedList}.
   */
  public CircularLinkedList() {
    super();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public synchronized void insert(T item) {
    checkItem(item);

    LinkedListNode<T> node = new LinkedListNode<>(item);

    if (head == null) {
      head = tail = node;
      link(node, node);
    } else {
      link(node, head);
      link(tail, node);
      head = node;
    }

    size++;
    modCount++;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   * @throws NullPointerException     {@inheritDoc}
   */
  @Override
  public synchronized void insertBefore(T item, LinkedListNode<T> node) {
    checkItem(item);
    checkNode(node);

    LinkedListNode<T> newNode = new LinkedListNode<>(item);

    link(node.prev, newNode, node);

    if (node == head)
      head = newNode;

    size++;
    modCount++;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   * @throws NullPointerException     {@inheritDoc}
   */
  @Override
  public synchronized void insertAfter(T item, LinkedListNode<T> node) {
    checkItem(item);
    checkNode(node);

    LinkedListNode<T> newNode = new LinkedListNode<>(item);

    link(node, newNode, node.next);

    if (node == tail)
      tail = newNode;

    size++;
    modCount++;
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  @Override
  public synchronized void insertLast(T item) {
    checkItem(item);

    LinkedListNode<T> node = new LinkedListNode<>(item);

    if (head == null) {
      head = tail = node;
      link(node, node);
    } else {
      link(tail, node);
      link(node, head);
      tail = node;
    }

    size++;
    modCount++;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   * @throws IllegalArgumentException  {@inheritDoc}
   */
  @Override
  public synchronized void insertAt(int index, T item) {
    checkPosition(index);
    checkItem(item);

    if (head == null) {
      head = tail = new LinkedListNode<>(item);
      size++;
      modCount++;
    }
    else if (head == tail) {
      if (index == 0)
        insert(item);
      else
        insertLast(item);
    }
    else if (index == size)
      insertLast(item);
    else
      insertBefore(item, search(index));
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  @Override
  public int indexOf(T item) {
    checkItem(item);

    if (head == null)
      return -1;
    else if (head.getItem() == item)
      return 0;

    LinkedListNode<T> node = head;
    int index = 0;

    do {
      node = node.next;
      index++;
    } while (node != head && node.getItem() != item);

    return node.getItem() == item ? index : -1;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public int lastIndexOf(T item) {
    checkItem(item);

    if (tail == null)
      return -1;
    else if (tail.getItem() == item)
      return size - 1;

    LinkedListNode<T> node = tail;
    int index = size - 1;

    do {
      node = node.prev;
      index--;
    } while (node.prev != tail && node.getItem() != item);

    return node.getItem() == item ? index : -1;
  }

}