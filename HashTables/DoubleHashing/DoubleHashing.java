package Data_Structures.HashTables.DoubleHashing;

interface IDoubleHashing<T> {
  public abstract int hash(int k, int i);
  public abstract int insert(int key, T item) throws HashTableFullExpection;
  public abstract int search(int key);
  public abstract T get(int key) throws HashTableItemNotFound;
  public abstract void remove(int key) throws HashTableItemNotFound;
}

class HashTableFullExpection extends Exception {
  public String toString() {
    return "\nHash table is full";
  }
}

class HashTableItemNotFound extends Exception {
  int k;

  HashTableItemNotFound(int key) { k = key; }

  public String toString() {
    return "\nItem not found for key: " + k;
  }
}

public class DoubleHashing<T> implements IDoubleHashing<T> {
  private int m;
  private T[] table;

  DoubleHashing(T[] t, int n) {
    this.m = n;
    this.table = t;
  }

  public int hash(int k, int i) {
    return (k % this.m) + ((i * (1 + (k % (this.m-1)))) % this.m);
  }

  // TODO: fix hash algorithm, the demo throws an error for index 1887
  public int insert(int key, T item) throws HashTableFullExpection {
    for (int i=0, j=this.hash(key, i); i<this.m; i++, j=this.hash(key, i)) { 
      if (this.table[j] == null) {
        this.table[j] = item;
        return j;
      }
    }
    
    throw new HashTableFullExpection();
  }

  public int search(int key) {
    for (int i=0, j=this.hash(key, i); i<this.m; i++, j=this.hash(key, i)) { 
      if (this.table[j] != null) {
        return j;
      }
    }

    return -1;
  }

  public T get(int key) throws HashTableItemNotFound{
    int idx = this.search(key);

    if (idx == -1) 
      throw new HashTableItemNotFound(key);

    return this.table[idx];
  }

  public void remove(int key) throws HashTableItemNotFound {
    int idx = this.search(key);

    if (idx == -1)
      throw new HashTableItemNotFound(key);

    this.table[idx] = null;
  }
}
