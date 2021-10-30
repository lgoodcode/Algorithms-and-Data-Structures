package data_structures.linkedLists;

public final class LinkedListNode<T> {
  protected LinkedListNode<T> next;
  protected LinkedListNode<T> prev;
  private T item;

  public LinkedListNode(T item) {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or empty.");
    this.item = item;
  }

  public T getItem() {
    return item;
  }

  public String toString() {
    return item.toString();
  }
}
