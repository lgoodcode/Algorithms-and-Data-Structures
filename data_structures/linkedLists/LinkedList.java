package data_structures.linkedLists;

public class LinkedList<T> extends AbstractLinkedList<T> implements java.io.Serializable {
  @java.io.Serial
  private static final long serialVersionUID = 199208284839394801L;

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

    Node<T> node = new Node<>(item);

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
  public synchronized void insertBefore(T item, Node<T> node) {
    checkItem(item);
    checkNode(node);

    Node<T> newNode = new Node<>(item);

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
  public synchronized void insertAfter(T item, Node<T> node) {
    checkItem(item);
    checkNode(node);

    Node<T> newNode = new Node<>(item);

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

    Node<T> node = new Node<>(item);

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
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public int indexOf(T item) {
    checkItem(item);

    Node<T> node = head;
    int index = 0;

    while (node != null && node.item != item) {
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

    Node<T> node = tail;
    int index = size - 1;

    while (node != null && node.item != item) {
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
  public T get(int index) {
    Node<T> node = search(index);
    return node != null ? node.item : null;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public synchronized void remove(int index) {
    Node<T> node = search(index);

    if (node != null)
      remove(node);
  }

  /**
   * Saves the state of this {@code LinkedList} instance to a stream (that is,
   * serializes it).
   * 
   * @param stream the {@link java.io.ObjectOutputStream} to write to
   * 
   * @throws java.io.IOException if serialization fails
   *
   * @serialData The size of the list (the number of elements it contains) is
   *             emitted (int), followed by all of its elements (each an Object)
   *             in the proper order.
   */
  @java.io.Serial
  private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
    // Write out any hidden serialization magic
    stream.defaultWriteObject();

    // Write out size
    stream.writeInt(size);

    if (isEmpty())
      return;

    Node<T> node = head;

    // Write out all elements in the proper order.
    do {
      stream.writeObject(node.item);
      node = node.next;
    } while (node != null && node != head);
  }

  /**
   * Reconstitutes this {@code LinkedList} instance from a stream (that is,
   * deserializes it).
   */
  @SuppressWarnings("unchecked")
  @java.io.Serial
  private void readObject(java.io.ObjectInputStream stream) 
throws java.io.IOException, ClassNotFoundException {
    // Read in any hidden serialization magic
    stream.defaultReadObject();

    // Read in size
    int size = stream.readInt();

    // Read in all elements in the proper order.
    for (int i = 0; i < size; i++)
      insertLast((T) stream.readObject());
  }

}