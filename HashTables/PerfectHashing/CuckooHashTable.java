package Data_Structures.HashTables.PerfectHashing;

import Data_Structures.HashTables.Entry;
import Data_Structures.HashTables.DuplicateKeyException;

// TODO: import functions to verify prime numbers
// TODO: add the delete and get methods
final class CuckooHashSubtable<K extends Number, V> {
  private int m, p, a, b;
  private Entry<?, ?>[] table;

  CuckooHashSubtable(int p, int m) {
    this.p = p;
    this.m = m;
    a = (int) (Math.random() * p - 2) + 1;
    b = (int) (Math.random() * p) - 1;
    table = new Entry<?, ?>[m];
  }

  public int hash(int k) {
    return ((this.a * k + this.b) % this.p) % this.m;
  }

  public Entry<?, ?> [] getTable() {
    return table;
  }

  public boolean contains(int hash) {
    return table[hash] != null;
  }

  public void insert(Entry<?, ?> entry) {
    table[entry.getHash()] = entry;
  }

  // public void insert(int hash, K key, V value) {
  //   table[hash] = new Entry<K, V>(hash, key, value);
  // }

  public Entry<?, ?> remove(int hash) {
    Entry<?, ?> entry = table[hash];

    table[hash] = null;

    return entry;
  }  
}

/**
 * CuckooHashTable
 * 
 * @param <K> number type to hold the entry key
 * @param <V> type to hold the value
 * @param T number of subtables
 * @param p prime number for subtables hash function
 * @param m subtable size
 * @param c constant for recalulating {@code m}
 * @param n number of entries
 * @param loadFactor maximum percentage of entries before rebuilding subtables
 * @param tables {@code CuckooHashSubtable<K, V>[T]} to hash and hold entries
 * 
 * @author Lawrence Good
 * 
 * @since 1.0
 */
public final class CuckooHashTable<K extends Number, V> {
  private int T = 3;
  private int p = 1277;
  private int m = 1;
  private int c = 4;
  private int n = 0;
  private float loadFactor = 0.9f;
  private CuckooHashSubtable<?, ?> tables[] = new CuckooHashSubtable<?, ?>[T];

  // Construct an empty table
  CuckooHashTable() {
    initSubtables(p, m);
  }

  // Construct an empty table with a given prime
  CuckooHashTable(int p) {
    this.p = p;

    initSubtables(p, m);
  }

  // Construct an empty table with a given prime and subtable size
  CuckooHashTable(int p, int m) {
    this.p = p;
    this.m = m;

    initSubtables(p, m);
  }
  
  // Construct a table from a given set
  // CuckooHashTable()
  
  private void initSubtables(int p, int m) {
    for (int i=0; i<T; ++i) {
      tables[i] = new CuckooHashSubtable<K, V>(p, m);
    }  
  }

  // Supress type safety check for when the prevEntry is type casted because
  // it can only be inserted as a new Entry<K, V> so we know when we retrieve
  // an entry, it will always be of type <K, V>
  @SuppressWarnings("unchecked")
  public void insert(K key, V value) throws DuplicateKeyException {
    if (++n >= (m * T) * loadFactor) {
      fullRehash(key, value);
    }
    else {
      CuckooHashSubtable<?, ?> Tj;
      Entry<K, V> newEntry, prevEntry;
      K intKey = key;
      V intVal = value;
      boolean first = true;
      int hash;

      // Cycle through all the tables until new value is inserted or reach a cycle
      // i % 3 to keeps the counter {0-2} for the tables and ommited the conditional 
      // statement for looping
      for (int i=0, z = intKey.intValue();; i = ++i % 3, z = intKey.intValue()) { 
        Tj = tables[i];
        hash = Tj.hash(z);
        newEntry = new Entry<K, V>(hash, intKey, intVal);

        // If the position is empty, insert new entry
        if (Tj.contains(hash) == false) {    
          Tj.insert(newEntry);
          // Tj.insert(hjx, intKey, intVal);

          return;
        }

        // Otherwise, a key already exists here, swap it
        prevEntry = (Entry<K, V>) Tj.remove(hash);
        Tj.insert(newEntry);
        
        // Set the key and value from the old entry to use for next subtable
        intKey = prevEntry.getKey();
        intVal = prevEntry.getValue();
        
        // Check if the key we swapped was the original, we are in a cycle - rebuild the tables
        if (intKey.equals(key)) {
          if (first) throw new DuplicateKeyException(key.intValue());

          fullRehash(key, value);
        }

        first = false;
      }
    }
  }

  // Suppress the type safety warning when re-inserting the entries
  // and casting the type parameters because the entries we added
  // with the type parameter so we know it's safe.
  @SuppressWarnings("unchecked")
  private void fullRehash(K key, V value) {
    int maxNumEntries = T * m;
    int entryIdx = 0;
    Entry<?, ?> entries[] = new Entry<?, ?>[maxNumEntries];

    // Place entries from all subtables into a single array
    for (CuckooHashSubtable<?, ?> Tj : tables) {
      for (Entry<?, ?> e : Tj.getTable()) {
        if (e != null)
          entries[entryIdx++] = e;
      }
    }

    // Add the newest item to entries
    entries[entryIdx++] = new Entry<K, V>(-1, key, value);

    // Calculate the new m and reset the counter
    m = (1 + c) * Math.max(entries.length, 4);
    n = 0;

    // Build new subtables
    for (int i=0; i<T; ++i) {
      tables[i] = new CuckooHashSubtable<K, V>(p, m);
    }

    // Re-insert the entries
    try {
      for (Entry<?, ?> e : entries) {
        insert((K) e.getKey(), (V) e.getValue());
      }
    } catch (DuplicateKeyException err) {
      System.out.println("Duplicate detected in fullRehash(): " + err);
    }
  }
}

class HashDemo {
  public static void main(String[] args) {
    CuckooHashTable<Integer, String> test = new CuckooHashTable<>();

    try {
      test.insert(4953, "one");
      test.insert(3245, "two");
      test.insert(452, "three");
      test.insert(324, "four");
      test.insert(444, "five");
      test.insert(555, "six");
      test.insert(425, "seven");
      test.insert(345, "eight");
      test.insert(466, "nine");
      test.insert(466, "ten");
    } catch (DuplicateKeyException err) {
      System.out.println(err);
    }

    System.out.println("Done");

  }
}
