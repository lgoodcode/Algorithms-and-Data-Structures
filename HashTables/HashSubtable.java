package HashTables;

import HashTables.HashTableExceptions.*;

public class HashSubtable {
  private int m, p, a, b;
  private Entry<?, ?>[] table;

  protected HashSubtable(int p, int m) throws HashTableException {
    // Sanity checks on prime and subtable size
    if (HashTableFunctions.isPrime(p) == false)
      throw new InvalidPrimeException(p);
    else if (m < 1)
      throw new InvalidSubtableSizeException(m);
    
    this.p = p;
    this.m = m;
    a = (int) (Math.random() * p - 2) + 1;
    b = (int) (Math.random() * p) - 1;
    table = new Entry<?, ?>[m];
  }

  protected int hash(int k) {
    return ((this.a * k + this.b) % this.p) % this.m;
  }

  final Entry<?, ?> [] getTable() {
    return table;
  }

  final void setTable(int hash, Entry<?, ?> entry) {
    table[hash] = entry;
  }
}
