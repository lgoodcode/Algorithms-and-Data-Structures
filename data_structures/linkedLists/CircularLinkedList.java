package data_structures.linkedLists;

public final class CircularLinkedList<T> extends LinkedList<T> {
  @java.io.Serial
  private static final long serialVersionUID = 199208284839394802L;
  
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

    Node<T> node = new Node<>(item);

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
  public synchronized void insertBefore(T item, Node<T> node) {
    checkItem(item);
    checkNode(node);

    Node<T> newNode = new Node<>(item);

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
  public synchronized void insertAfter(T item, Node<T> node) {
    checkItem(item);
    checkNode(node);

    Node<T> newNode = new Node<>(item);

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

    Node<T> node = new Node<>(item);

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
      head = tail = new Node<>(item);
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
    else if (head.item == item)
      return 0;

    Node<T> node = head;
    int index = 0;

    do {
      node = node.next;
      index++;
    } while (node != head && node.item != item);

    return node.item == item ? index : -1;
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
    else if (tail.item == item)
      return size - 1;

    Node<T> node = tail;
    int index = size - 1;

    do {
      node = node.prev;
      index--;
    } while (node.prev != tail && node.item != item);

    return node.item == item ? index : -1;
  }

}