package Data_Structures.HashTables.PerfectHashing;

import Data_Structures.HashTables.Entry;
import Data_Structures.HashTables.HashTableFunctions;

/**
 * This class implements a high performance dynamic hashtable where the size is
 * initialized to the current needs and updates its size when needed as it
 * reaches a certain load threshold. Any non-{@code null} object can be used as
 * the key and value. This is accomplished by using the {@code Object.hashCode()}
 * method which creates a determinstic integer value for any object and can be used
 * by the hash function to derive the index slot.
 * 
 * <h3>Important</h3>
 * <p>
 * The prime number must be larger than the expected total table size otherwise
 * the hash function will not work properly and may cause the table to break.
 * </p>
 * 
 * <hr/>
 * 
 * <p>
 * An instance of {@code CuckooHashTable} uses a prime number {@code p} within
 * the hash function and will also construct {@code CuckooHashSubtable} for the
 * subtables which is an inner class, to hold the actual entries. The entries
 * themselves are of the {@code Entry} class.
 * </p>
 * 
 * <p>
 * The default values are:
 * <ul>
 * <li>{@code p} 1277</li>
 * <li>{@code T} 3</li>
 * <li>{@code m} 1</li>
 * <li>{@code c} 4</li>
 * <li>{@code loadFactor} 0.9</li>
 * </ul>
 * 
 * <p>
 * *Some concepts are similar to the java.util.Hashtable implementation by
 * <i>Arthur van Hoff, Josh Bloch, and Neal Gafter</i>.
 * </p>
 * 
 * @param <K> type parameter for the keys of the hashtable
 * @param <V> type parameter for the values of the hashtable
 * 
 * @author Lawrence Good
 * @see CuckooHashSubtable
 * @see Entry
 * @since 1.0
 */
public final class CuckooHashTable<K, V> {
  /**
   * The number of subtables. According to a calculation (I can't remember from
   * where but I'm sure the source is a google away) three subtables has a load
   * capacity of 91% versus 50% with two tables.
   */
  private final int T = 3;

  /**
   * The constant used to recalculate the subtable size {@code m}.
   */
  private final int c = 4;

  /**
   * The prime number for the hash function. Must be larger than the total table
   * size.
   */
  private int p;

  /**
   * The subtable size.
   */
  private int m;

  /**
   * The counter to track the number of entries total in the hashtable.
   */
  private int n = 0;

  /**
   * The maximum threshold for the capacity of the hashtable before rebuilding.
   */
  private float loadFactor;

  /**
   * The hashtable subtables that hold the entries.
   */
  private CuckooHashSubtable<?, ?> tables[] = new CuckooHashSubtable<?, ?>[T];

  /**
   * Constructs a new, empty hashtable with the specified inital prime number,
   * subtable size, and load factor.
   * 
   * @param p          prime number for subtables hash function
   * @param m          subtable size
   * @param loadFactor maximum percentage of entries before rebuilding subtables.
   *                   Must be greater than or equal to 0.5f.
   * @throws IllegalArgumentException when given a prime number that isn't prime,
   *                                  a subtable size smaller than 1, or a load
   *                                  factor less then 0.5f.
   */
  public CuckooHashTable(int p, int m, float loadFactor) throws IllegalArgumentException {
    if (HashTableFunctions.isPrime(p) == false)
      throw new IllegalArgumentException("Illegal prime number: " + p);
    else if (m < 1)
      throw new IllegalArgumentException("Illegal subtable size: " + m);
    else if (Float.isNaN(loadFactor) || Float.compare(loadFactor, 0.5f) < 0)
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

    this.p = p;
    this.m = m;
    this.loadFactor = loadFactor;

    buildSubtables();
  }

  /**
   * Constructs a new, empty hashtable with all default values: prime (1277),
   * subtable size (1), and load factor (0.9).
   */
  public CuckooHashTable() {
    this(1277, 1, 0.9f);
  }

  /**
   * Constructs a new, empty hashtable with the specified prime number and default
   * subtable size (1) and load factor (0.9).
   * 
   * @param p prime number
   * @throws IllegalArgumentException if the number isn't prime
   */
  public CuckooHashTable(int p) {
    this(p, 1, 0.9f);
  }

  /**
   * Constructs a new, empty hashtable with the specified prime number and
   * subtable size, and default load factor(0.9).
   * 
   * @param p prime number
   * @param m subtable size
   * @throws IllegalArgumentException if the number isn't prime or the subtable
   *                                  size is less than 1.
   */
  public CuckooHashTable(int p, int m) {
    this(p, m, 0.9f);
  }

  /**
   * Constructs a new, empty hashtable with the specified prime number and load
   * factor, and default subtable size(1).
   * 
   * @param p          prime number
   * @param loadFactor load factor
   * @throws IllegalArgumentException if the number isn't prime or the load factor
   *                                  is less than 0.5.
   */
  public CuckooHashTable(int p, float loadFactor) {
    this(p, 1, loadFactor);
  }

  /**
   * Constructs a new, empty hashtable with the specified load factor and subtable
   * size, and default prime (1277).
   * 
   * @param loadFactor load factor
   * @param m          subtable size
   * @throws IllegalArgumentException if the load factor is less than 0.5 or the
   *                                  subtable size is less than 1.
   */
  public CuckooHashTable(float loadFactor, int m) {
    this(1277, m, loadFactor);
  }

  // TODO: add constructor to initalize a predefined set

  /**
   * Used within insert() and determines whether the incremented size counter is
   * equal to or exceeds the load capacity.
   * 
   * @return if the new entry cause the table size to exceed load capacity
   */
  private boolean capacityReached() {
    return ++n >= (m * T) * loadFactor;
  }

  /**
   * Rebuilds the subtables by setting the table index to point to the new
   * {@code CuckooHashSubtable} thereby removing the references to the previous
   * tables to allow them to be garbage collected.
   */
  private synchronized void buildSubtables() {
    for (int i = 0; i < T; ++i) {
      tables[i] = new CuckooHashSubtable<K, V>(p, m);
    }
  }

  /**
   * Inserts the given value into the table using the hashed value of the key.
   * Neither the key nor the value can be {@code null}.
   * 
   * <p>
   * Supressed type safety check for when the prevEntry is type casted because it
   * can only be inserted as a new Entry<V> so we know when we retrieve an entry,
   * it will always be of type <V>
   * </p>
   * 
   * <hr>
   * 
   * <h3>Internal operations</h3>
   * 
   * <p>
   * Cycles through all the tables until new value is inserted or reach a cycle.
   * {@code i % 3} keeps the counter in the range {@code [0, 2]} for the tables
   * and ommited the conditional statement for infinite looping. The for-loop is
   * chosen over the while-loop due to the fact an index {@code i} is required to
   * continuously loop through the tables.
   * </p>
   * 
   * <p>
   * A boolean value {@code first} is used to check the key from the first swap,
   * if any, is equal to the argument {@code key}. If so, then it is a duplicate
   * key because the hash function is deterministic, in that a given input will
   * output the same value every time. So that means that the keys are the same
   * and hashed to the same slot. This will throw an exception because duplicates
   * will break the hashtable.
   * </p>
   * 
   * @param key   the hashtable key
   * @param value the value
   * @throws NullPointerException     if the key or value is null
   * @throws IllegalArgumentException if the specified key already exists in the
   *                                  hashtable
   */
  @SuppressWarnings("unchecked")
  public synchronized void insert(K key, V value) throws NullPointerException, IllegalArgumentException {
    if (key == null || value == null)
      throw new NullPointerException();

    if (capacityReached()) {
      fullRehash(key, value);
    } else {
      CuckooHashSubtable<K, V> Tj;
      Entry<K, V> prevEntry, newEntry = new Entry<K, V>(key, value);
      int keyHash;
      boolean first = true;

      for (int i = 0;; i = ++i % 3, newEntry = prevEntry) {
        Tj = (CuckooHashSubtable<K, V>) tables[i];
        keyHash = Tj.hash(newEntry.getKey().hashCode());

        // If the position is empty, insert new entry and return.
        if (Tj.hasHash(keyHash) == false) {
          Tj.insert(keyHash, newEntry);
          return;
        }

        // Otherwise, a key already exists here, swap it
        prevEntry = (Entry<K, V>) Tj.remove(keyHash);
        Tj.insert(keyHash, newEntry);

        // If the key we swapped was the argument key, we are in a cycle - rebuild the
        // tables
        if (prevEntry.getKey().equals(key)) {
          if (first)
            throw new IllegalArgumentException("Duplicate key used: " + key);

          fullRehash(key, value);
        }

        first = false;
      }
    }
  }

  /**
   * Rebuilds the hashtable by iterating through all the entries in the subtables
   * and placing them in a single array, recaclulate the new subtable size, reset
   * the size counter, and re-insert all the entries into the hashtable.
   * 
   * <hr/>
   * 
   * Suppresses the type safety warning when re-inserting the entries and casting
   * the type parameters because the entries we added with the type parameter so
   * we know it's safe.
   * 
   * @param key   the key of the last item before capacity was exceeded or a cycle
   *              was reached.
   * @param value the value of the last item
   * @throws IllegalArgumentException if the specified key already exists in the
   *                                  table
   */
  @SuppressWarnings("unchecked")
  private synchronized void fullRehash(K key, V value) throws IllegalArgumentException {
    int maxNumEntries = T * m;
    int entryIdx = 0;
    Entry<?, ?> entries[] = new Entry<?, ?>[maxNumEntries];

    // Place entries from all subtables into a single array
    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      for (Entry<?, ?> e : Tj.getTable()) {
        if (e != null)
          entries[entryIdx++] = e;
      }
    }

    // Calculate the new m and reset the counter
    m = (1 + c) * Math.max(entries.length, 4);
    n = 0;

    // Re-create the subtables
    buildSubtables();

    // Re-insert the entries
    try {
      for (int i = 0; i < entryIdx; ++i)
        insert((K) entries[i].getKey(), (V) entries[i].getValue());

      insert(key, value);
    } catch (IllegalArgumentException err) {
      System.out.println("Duplicate detected in fullRehash(): " + err);
    }
  }

  /**
   * Determines whether the given key has an entry in the hashtable.
   * 
   * @param key the key
   * @return whether the key exists in the hashtable
   */
  @SuppressWarnings("unchecked")
  public synchronized boolean has(K key) {
    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      if (Tj.has(key))
        return true;
    }

    return false;
  }

  /**
   * Retrieves the value of the entry for the given key if it exists or
   * {@code null} if it doesn't.
   * 
   * @param key
   * @return the value of the specified key entry if it exists or {@code null} if
   *         not
   */
  @SuppressWarnings("unchecked")
  public synchronized V get(K key) {
    V value;

    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      value = (V) Tj.get(key);

      if (value != null)
        return value;
    }

    return null;
  }

  /**
   * Deletes the entry for the specified key if it exists and returns a boolean if
   * the operation was successful or not.
   * 
   * @param key the key
   * @return whether the entry exists and was deleted or not
   */
  @SuppressWarnings("unchecked")
  public synchronized boolean delete(K key) {
    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      if (Tj.has(key))
        return Tj.delete(key);
    }

    return false;
  }

}

/**
 * The CuckooHashTable subtable class which contains the table of {@code Entry}
 * objects that hold the actual key/value pair. An instance of
 * {@code CuckooHashSubtable} contains the prime number {@code p} and subtable
 * size {@code m} denoted from the primary {@code CuckooHashTable}. It also
 * contains two constants, {@code a} and {@code b} which are random integers
 * derived within the range of {@code [0, p-1]}.
 * 
 * <p>
 * The class is static so it can be used within the outerclass, primarily when
 * creating the array of this class in {@code CuckooHashTable.tables}
 * </p>
 * 
 * <p>
 * Randomly choosing the constants a and b from a set that differs by one value,
 * gives us {@code p(p-1)} hash functions. Given a universal collection of hash
 * functions, a singlepair of keys collides with probablity at most {@code 1/m},
 * which gives us {@code Pr of h(k) = h(l) ≤ 1/m}.
 * </p>
 * 
 * @param <K> key type parameter derived from the main type parameter
 * @param <V> value type parameter dervied from the main type parameter
 *            {@code CuckooHashTable}
 * @see CuckooHashTable
 * @see CuckooHashTable#tables
 */
final class CuckooHashSubtable<K, V> {
  private int m, p, a, b;
  private Entry<?, ?>[] table;

  /**
   * Constructs a new, empty hashtable given a specified prime number {@code p}
   * and subtable size {@code m}, which is given from the {@code CuckooHashTable}.
   * The sanity checks on the parameters are done in the constructor of the parent
   * hashtable.
   * 
   * <p>
   * An instance of {@code CuckooHashSubtable} contains an array of {@code Entry}
   * objects as the actual hashtable where the key is hashed to derive the index
   * where the {@code Entry} is inserted.
   * </p>
   * 
   * @param p prime number
   * @param m subtable size
   */
  public CuckooHashSubtable(int p, int m) {
    this.p = p;
    this.m = m;
    a = (int) (Math.random() * p - 2) + 1;
    b = (int) (Math.random() * p) - 1;
    table = new Entry<?, ?>[m];
  }

  /**
   * The deterministic hash function to derive a index slot for a given key
   * {@code k}.
   * 
   * @param k the key hash code
   * @return integer index slot to insert an entry for this subtable
   */
  public int hash(int k) {
    return ((a * k + b) % p) % m;
  }

  /**
   * Retrieves the table reference. Used in the
   * {@code CuckooHashTable.fullRehash()} method to iterate through the and pull
   * all entries to rebuild subtables.
   * 
   * @return the table of {@code Entry} objects
   * @see CuckooHashTable#fullRehash()
   * @see Entry
   */
  @SuppressWarnings("unchecked")
  public synchronized Entry<K, V>[] getTable() {
    return (Entry<K, V>[]) table;
  }

  /**
   * Returns a boolean value indicating whether a given hash index slot is
   * occupied or not.
   * 
   * @param hash the hash index slot from a key
   * @return whether the given hash slot is occupied or not for this subtable
   * @see CuckooHashTable#insert()
   */
  public synchronized boolean hasHash(int hash) {
    return table[hash] != null;
  }

  /**
   * Checks whether the given key has an entry in this table by hashing the key
   * and if the slot is occupied, if the key matches.
   * 
   * @param key the key
   * @return does the given key exist in the table
   */
  public synchronized boolean has(K key) {
    final int keyHash = hash(key.hashCode());

    if (table[keyHash] != null) {
      return table[keyHash].getKey().equals(key);
    }

    return false;
  }

  /**
   * Inserts a new {@code Entry} into the subtable. The hash index slot is
   * retrieved from the actual {@code Entry} object.
   * 
   * @param entry the {@code Entry} object
   * @see Entry#getHash()
   */
  public synchronized void insert(int hash, Entry<K, V> entry) {
    table[hash] = entry;
  }

  /**
   * Retrieves the value for the entry of the given key.
   * 
   * @param key the key to retrieve the corresponding value
   * @return the value if the given key entry exists or {@code null} if not
   */
  @SuppressWarnings("unchecked")
  public synchronized V get(K key) {
    final Entry<K, V> entry = (Entry<K, V>) table[hash(key.hashCode())];

    if (entry != null && entry.getKey().equals(key)) {
      return entry.getValue();
    }

    return null;
  }

  /**
   * Removes an {@code Entry} from the hashtable, sets that hash slot to null so
   * it can be used again, and returns the removed {@code Entry}.
   * 
   * @param hash the derived hash index slot from a key
   * @return desired {@code Entry} for either normal removal or swapping entries
   *         in the {@code CuckooHashTable.insert()} process.
   * @see CuckooHashTable#insert()
   */
  @SuppressWarnings("unchecked")
  public synchronized Entry<K, V> remove(int hash) {
    Entry<K, V> entry = (Entry<K, V>) table[hash];

    table[hash] = null;

    return entry;
  }

  /**
   * Deletes an entry for the given key if it exists and the occupied slot key
   * matches the specified key to know it is correct.
   * 
   * @param key the key
   * @return whether the operation was successful or not
   */
  public synchronized boolean delete(K key) {
    final int keyHash = hash(key.hashCode());

    if (table[keyHash] != null && table[keyHash].getKey().equals(key)) {
      table[keyHash] = null;

      return true;
    }

    return false;
  }
}

class HashDemo {
  public static void main(String[] args) {
    CuckooHashTable<Integer, String> test = new CuckooHashTable<>();

    try {
      test.insert(953, "one");
      test.insert(326, "two");
      test.insert(452, "three");
      test.insert(324, "four");
      test.insert(444, "five");
      test.insert(555, "six");
      test.insert(425, "seven");
      test.insert(345, "eight");
      test.insert(466, "nine");
      // test.insert(466, "ten");

      System.out.println("contains key 953: " + test.has(953));
      System.out.println("contains key 495: " + test.has(495));
      System.out.println("contains key 345: " + test.has(345));

      System.out.println("value of key 345: " + test.get(345));

      System.out.println("deleted key 345: " + test.delete(345));
      System.out.println("contains key 345: " + test.has(345));

      System.out.println("value of key 345: " + test.get(345));

      System.out.println("deleted key 345: " + test.delete(345));

    } catch (IllegalArgumentException err) {
      System.out.println(err);
    }

    System.out.println("Done");

  }
}
