package Data_Structures.HashTables.PerfectHashing;

// TODO: complete this entire thing
class TableTooLargeException extends Exception {
  private int s;
  private int p;

  TableTooLargeException(int size, int prime) {
    this.s = size;
    this.p = prime;
  }

  public String toString() {
    return "\nGiven table size: " + this.s + " is too large for given prime: " + this.p;
  }
}

public final class DynPerfHash<T extends Number> {
  private int p = 1277;
  private int a = (int) (Math.random() * this.p - 2) + 1;
  private int b = (int) Math.random() * this.p - 1;
  private int m;
  private int count = 0;
  private T[][] table;
  
  private int hash(int k) {
    return ((this.a * k + this.b) % this.p) % this.m;
  }

  // Creating an empty table
  DynPerfHash(T[][] t) {
    this.table = t;
    this.m = t.length;
  }

  // Creating a table from a given set of values
  DynPerfHash(T[][] t, T[] a) {
    this.table = t;
    this.m = t.length;

    // int array K will hold the hash value 
    int[][] K = new int[m/2][m/4];
    K[4][1] = 100;
  }

}
