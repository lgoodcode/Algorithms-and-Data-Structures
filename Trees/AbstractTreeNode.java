package data_structures.trees;

public abstract class AbstractTreeNode<K, V> {
  protected AbstractTreeNode() {}

  public abstract K getKey();

  public abstract V getValue();

  public abstract  String toString();
}