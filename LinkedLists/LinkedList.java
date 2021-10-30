package data_structures.linkedLists;

public class LinkedList<T> extends AbstractLinkedList<T> {
  /**
   * Empty contructor
   */
  public LinkedList() {}

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}.
   */
  public synchronized void insert(T item) {
    checkItem(item);

    LinkedListNode<T> node = new LinkedListNode<>(item);

    if (head == null)
      head = tail = node;
    else {
      link(node, head);
      head = node;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item before the
   * specified node.
   *
   * @param item the item to insert into the list
   * @param node the node to insert the item behind
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   * @throws NullPointerException     if the specified node is {@code null}
   */
  public synchronized void insertBefore(T item, LinkedListNode<T> node) {
    checkItem(item);
    checkNode(node);

    LinkedListNode<T> newNode = new LinkedListNode<>(item);

    if (node.prev != null)
      link(node.prev, newNode, node);
    else {
      link(newNode, node);
      head = newNode;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item after the
   * specified node.
   *
   * @param item the item to insert into the list
   * @param node the node to insert the item after
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   * @throws NullPointerException     if the specified node is {@code null}
   */
  public synchronized void insertAfter(T item, LinkedListNode<T> node) {
    checkItem(item);
    checkNode(node);

    LinkedListNode<T> newNode = new LinkedListNode<>(item);

    if (node.next != null)
      link(node, newNode, node.next);
    else {
      link(node, newNode);
      tail = newNode;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item at the end of
   * the list as the new tail.
   *
   * @param item the item to insert into the list
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   */
  public synchronized void insertLast(T item) {
    checkItem(item);

    LinkedListNode<T> node = new LinkedListNode<>(item);

    if (head == null)
      head = tail = node;
    else {
      link(tail, node);
      tail = node;
    }

    size++;
    modCount++;
  }

  /**
   * Inserts a new {@code LinkedListNode} with the specified item at the specified
   * index position.
   *
   * @param index the index to insert the new item at
   * @param item  the item to insert into the list
   *
   * @throws IndexOutOfBoundsException if the specified index is not valid
   * @throws IllegalArgumentException  it the specified item is {@code null} or
   *                                   blank
   */
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
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int indexOf(T item) {
    checkItem(item);

    LinkedListNode<T> node = head;
    int index = 0;

    while (node != null && node.getItem() != item) {
      node = node.next;
      index++;
    }

    return node != null ? index : -1;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int lastIndexOf(T item) {
    checkItem(item);

    LinkedListNode<T> node = tail;
    int index = size - 1;

    while (node != null && node.getItem() != item) {
      node = node.prev;
      index--;
    }

    return node != null ? index : -1;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public LinkedListNode<T> search(int index) {
    checkIndex(index);

    LinkedListNode<T> node = null;

    if (index < (size >> 1)) {
      node = head;

      for (int i=0; i<index; i++)
        node = node.next;
      return node;
    }
    else {
      node = tail;

      for (int i=size-1; i>index; i--)
        node = node.prev;
      return node;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public T get(int index) {
    LinkedListNode<T> node = search(index);
    return node != null ? node.getItem() : null;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public synchronized void remove(int index) {
    LinkedListNode<T> node = search(index);

    if (node != null) {
      unlink(node);
      size--;
      modCount++;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException {@inheritDoc}
   */
  public synchronized void remove(LinkedListNode<T> node) {
    checkNode(node);
    unlink(node);
    size--;
    modCount++;
  }

}