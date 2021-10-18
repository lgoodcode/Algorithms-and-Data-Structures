package Hashtables;

import java.util.Map;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import Hashtables.exceptions.DuplicateKeyException;

import static Hashtables.HashtableFunctions.isPrime;

/**
 * This class implements a high performance dynamic hashtable where the size is
 * initialized to the current needs and updates its size when needed as it
 * reaches a certain load threshold. Any non-{@code null} object can be used as
 * the key and value. This is accomplished by using the
 * {@code Object.hashCode()} method which creates a determinstic integer value
 * for any object and can be used by the hash function to derive the index slot.
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
 * themselves are of the {@code Entry} private inner class.
 * </p>
 * 
 * <p>
 * The default values are:
 * <ul>
 * <li>{@code p} 1277</li>
 * <li>{@code T} 3</li>
 * <li>{@code m} 1</li>
 * <li>{@code c} 4</li>
 * <li>{@code loadFactor} 0.9f</li>
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
 * @see Enumerator
 * @version 1.4
 */
public final class CuckooHashtable<K, V> extends AbstractHashtable<K, V> {
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
   * <p>
   * The constructor is overloaded and uses {@code this} to reduce redundant code.
   * </p>
   * 
   * @param prime      prime number for subtables hash function
   * @param size       subtable size
   * @param loadFactor maximum percentage of entries before rebuilding subtables.
   *                   Must be greater than or equal to 0.6f and less than or
   *                   equal to 0.95f
   * 
   * @throws IllegalArgumentException when given a prime number that isn't prime,
   *                                  a subtable size smaller than 1, or a load
   *                                  factor less then 0.6f or greater than 0.95f
   */
  public CuckooHashtable(int prime, int size, float loadFactor) {
    if (!isPrime(prime))
      throw new IllegalArgumentException("Illegal prime number: " + prime);
    if (size < 1)
      throw new IllegalArgumentException("Illegal subtable size: " + size);
    if (Float.isNaN(loadFactor) || loadFactor < 0.6f || loadFactor > 0.95f)
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

    p = prime;
    m = size;
    this.loadFactor = loadFactor;

    buildSubtables();
  }

  /**
   * Constructs a new, empty hashtable with all default values: prime (1277),
   * subtable size (1), and load factor (0.9).
   */
  public CuckooHashtable() {
    this(1277, 1, 0.9f);
  }

  /**
   * Constructs a new, empty hashtable with the specified prime number and default
   * subtable size (1) and load factor (0.9).
   * 
   * @param p prime number
   * @throws IllegalArgumentException if the number isn't prime
   */
  public CuckooHashtable(int prime) {
    this(prime, 1, 0.9f);
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
  public CuckooHashtable(int prime, int size) {
    this(prime, size, 0.9f);
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
  public CuckooHashtable(int prime, float loadFactor) {
    this(prime, 1, loadFactor);
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
  public CuckooHashtable(float loadFactor, int size) {
    this(1277, size, loadFactor);
  }

  /**
   * Constructs a new hashtable with the default values and a specified set
   * of entries of a {@code Map}.
   * 
   * @param S the set of entries
   * 
   * @throws DuplicateKeyException if a duplicate key is used
   */
  public CuckooHashtable(Map<K, V> S) {
    this(1277, 1, 0.9f);

    for (Map.Entry<K, V> entry : S.entrySet())
      insert(entry.getKey(), entry.getValue());
  }

  /**
   * Used within insert() and determines whether the incremented size counter is
   * equal to or exceeds the load capacity.
   * 
   * @return if the new entry cause the table size to exceed load capacity
   * @see #fullRehash()
   */
  private boolean capacityReached() {
    return n >= (m * T) * loadFactor;
  }

  /**
   * Rebuilds the subtables by setting the table index to point to the new
   * {@code CuckooHashSubtable} thereby removing the references to the previous
   * tables to allow them to be garbage collected.
   * 
   * @see #fullRehash()
   */
  private void buildSubtables() {
    for (int i = 0; i < T; ++i)
      tables[i] = new CuckooHashSubtable<K, V>(p, m);
  }

  /**
   * Inserts the given value into the table using the hashed value of the key.
   * Neither the key nor the value can be {@code null}. The size counter is
   * incremented before an insertion or after a call to {@link #fullRehash()}
   * because the load capacity upper limit is {@code 95%}. So, it could possibly
   * cause an index-out-of-bounds error since the table will either be at max
   * capacity (i.e., if {@code m = 1} two entries would be at {@code 66%} load,
   * then three would be {@code 100%}). The {@code modCount} is incremented after
   * the successful insertion, since if a full rehash is triggered, that method
   * will increment {@code modCount}
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
   * 
   * @throws IllegalArgumentException if the key or value is {@code null} or blank
   * 
   * @throws DuplicateKeyException    if the specified key already exists in the
   *                                  hashtable
   */
  @SuppressWarnings("unchecked")
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    n++;

    if (capacityReached()) {
      fullRehash(key, value);
    } else {
      CuckooHashSubtable<K, V> Tj;
      Entry<K, V> newEntry = new Entry<K, V>(key, value), prevEntry;
      boolean first = true;
      K newKey;

      for (int i = 0;; i = ++i % 3, newEntry = prevEntry) {
        Tj = (CuckooHashSubtable<K, V>) tables[i];
        newKey = newEntry.getKey();

        // If the position is empty, insert new entry and return.
        if (Tj.isOccupied(newKey) == false) {
          Tj.insert(newEntry);
          modCount++;
          return;
        }

        // Otherwise, a key already exists here, swap it
        prevEntry = (Entry<K, V>) Tj.extract(newKey);
        Tj.insert(newEntry);

        // Check for duplicate key and cycle
        if (prevEntry.getKey().equals(key)) {
          if (first)
            throw new IllegalArgumentException("Key already exists in table: " + key.toString());

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
   * <p>
   * The new subtable size, {@code m}, is calcluated 
   * </p>
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
   */
  @SuppressWarnings("unchecked")
  private synchronized void fullRehash(K key, V value) {
    int maxNumEntries = T * m;
    Entry<?, ?> entries[] = new Entry<?, ?>[maxNumEntries];
    int entryIdx = 0;

    // Place entries from all subtables into a single array
    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      for (Entry<K, V> e : (Entry<K, V>[]) Tj.table) {
        if (e != null)
          entries[entryIdx++] = e;
      }
    }

    // Add the newest entry
    entries[entryIdx++] = new Entry<K,V>(key, value);

    // Calculate new m, reset size counter, increment modified counter
    m = (1 + c) * Math.max(n, 4);
    n = 0;
    modCount++;

    // Re-create the subtables
    buildSubtables();

    // Garbage collect the removed tables
    System.gc();

    // Re-insert the entries
    for (int i=0; i < entryIdx; i++) {
      insert((K) entries[i].getKey(), (V) entries[i].getValue());
    } 
  }

  /**
   * Performs a lookup in each subtable to find the {@code Entry} with the
   * specified key.
   * 
   * @param key the key of the entry to find
   * @return the {@code Entry} object or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public Entry<K, V> find(K key) {
    checkKey(key);

    Entry<K, V> entry = null;

    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables)
      if ((entry = Tj.getEntry(key)) != null)
        return entry;
    return entry;
  }

  /**
   * Determines whether the given key has an entry in the hashtable.
   * 
   * @param key the key to check if exists
   * @return whether the key exists in the hashtable or not
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public boolean hasKey(K key) {
    return find(key) != null;
  }

  /**
   * Determines whether a key in any of the subtable maps into the
   * specified value.
   * 
   * @param value the value to check if exists
   * @return whether the value exists in the hashtable or not
   * 
   * @throws IllegalArgumentException if the value is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public boolean hasValue(V value) {
    checkValue(value);

    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      if (Tj.hasValue(value))
        return true;
    }

    return false;
  }

  /**
   * Retrieves the value of the entry for the given key if it exists or
   * {@code null} if it doesn't.
   * 
   * @param key the key of the entry whose value we want to retrieve
   * @return the value of the specified key entry if it exists or {@code null} if
   *         not
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public V get(K key) {
    Entry<K, V> entry = find(key);
    return entry != null ? entry.getValue() : null;
  }

  /**
   * Deletes the entry for the specified key if it exists and returns a boolean if
   * the operation was successful or not. If successful, it will increment the modCount
   * and decrement the size counter.
   * 
   * @param key the key of the entry to delete
   * @return whether the entry was successfully deleted or not
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public synchronized boolean delete(K key) {
    checkKey(key);

    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables) {
      if (Tj.hasKey(key) && Tj.delete(key)) {
        modCount++;
        n--;
        return true;
      }
    }
    return false;
  }

  /**
   * Overloaded delete method for the {@code Entry} object.
   * 
   * @param entry the entry to delete
   * @return whether the entry was successfully deleted or not
   * 
   * @throws NullPointerException if the entry is {@code null}
   * @see Entry
   * @see #delete(K key)
   */
  public synchronized boolean delete(Entry<K, V> entry) {
    if (entry == null)
      throw new NullPointerException();
    return delete(entry.getKey());
  }

  /**
   * Iterates over the subtables and uses the {@code CuckooHashSubtable.delete()}
   * method to directly remove all entries.
   */
  @SuppressWarnings("unchecked")
  public synchronized void clear() {
    for (CuckooHashSubtable<K, V> Tj : (CuckooHashSubtable<K, V>[]) tables)
      Tj.clear();
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
   * @see CuckooHashtable
   * @see CuckooHashtable#tables
   * @since 1.1
   */
  protected static class CuckooHashSubtable<K, V> extends AbstractHashSubtable<K, V> {
    private Entry<?, ?>[] table;
    private int m, p, a, b;

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
    protected CuckooHashSubtable(int prime, int size) {
      this.p = prime;
      this.m = size;
      a = (int) (Math.random() * p - 2) + 1;
      b = (int) (Math.random() * p) - 1;
      table = new Entry<?, ?>[m];
    }

    /**
     * The deterministic hash function to derive a index slot for a given key
     * {@code k}.
     *  
     * @param k the key to hash
     * @return integer index for this subtable
     */
    private int hash(K key) {
      return ((a * key.hashCode() + b) % p) % m;
    }

    /**
     * Checkes whether the given key when hashed, occupies a slot.
     * 
     * @param key the key to check if occupies a slot
     * @return whether the key hash occupies a slot in the table
     * 
     * @throws IllegalArgumentException if the key is {@code null} or blank
     */
    protected boolean isOccupied(K key) {
      checkKey(key);
      return table[hash(key)] != null;
    }

    /**
     * Performs a lookup in the hashtable to find the index of the entry
     * with the specified key or {@code -1} if not found.
     * 
     * @param key the key of the entry index to find
     * @return the index of the entry or {@code -1} if not found
     * 
     * @throws IllegalArgumentException if the key is {@code null} or blank
     */
    protected int search(K key) {
      checkKey(key);

      int hash = hash(key);

      if (table[hash] != null && table[hash].getKey().equals(key))
        return hash;
      return -1;
    }

    /**
     * Checks whether the given key has an entry in this table by hashing the key
     * and if the slot is occupied, if the key matches.
     * 
     * @param key the key
     * @return if the given key exist in the table
     * 
     * @throws IllegalArgumentException if the key is {@code null} or blank
     */
    public boolean hasKey(K key) {
      return search(key) != -1;
    }

    /**
     * Checks whether a given key in the subtable maps to the specified value.
     * 
     * @param value the value
     * @return whether the given value exists in the table
     * 
     * @throws IllegalArgumentException if the value is {@code null} or blank
     */
    @SuppressWarnings("unchecked")
    protected boolean hasValue(V value) {
      checkValue(value);
    
      for (Entry<K, V> e : (Entry<K, V>[]) table)
        if (e.getValue().equals(value))
          return true;
      return false;    
    }

    /**
     * Inserts a new {@code Entry} into the subtable by getting its' key, deriving
     * the index slot from hashing it, then inserting it.
     * 
     * @param entry the {@code Entry} object to insert
     * 
     * @throws NullPointerException if the entry is {@code null}
     */
    protected synchronized void insert(Entry<K, V> entry) {
      if (entry == null)
        throw new NullPointerException();
      table[hash(entry.getKey())] = entry;
    }

    public synchronized void insert(K key, V value) {
      return;
    }

    /**
     * Retrieves the value for the entry of the given key.
     * 
     * @param key the key to retrieve the corresponding value
     * @return the value if the given key entry exists or {@code null} if not
     * 
     * @throws IllegalArgumentException if the key is {@code null} or blank
     */
    @SuppressWarnings("unchecked")
    public V get(K key) {
      int idx = search(key);
      return idx != -1 ? (V) table[idx].getValue() : null;
    }

    /**
     * Retrieves the {@code Entry} with the given key.
     * 
     * @param key the key of the entry to retrieve
     * @return the {@code Entry} or {@code null} if not found
     */
    @SuppressWarnings("unchecked")
    protected Entry<K, V> getEntry(K key) {
      int idx = search(key);
      return idx != -1 ? (Entry<K, V>) table[idx] : null;
    }

    /**
     * Removes an {@code Entry} from the hashtable, sets that hash slot to null so
     * it can be used again, and returns the removed {@code Entry}.
     * 
     * @param hash the derived hash index slot from a key
     * @return desired {@code Entry} for either normal removal or swapping entries
     *         in the {@code CuckooHashTable.insert()} process.
     * 
     * @throws IllegalArgumentException if the key is {@code null} or blank
     * 
     * @see CuckooHashtable#insert()
     */
    @SuppressWarnings("unchecked")
    protected synchronized Entry<K, V> extract(K key) {
      checkKey(key);

      int hash = hash(key);
      Entry<K, V> entry = (Entry<K, V>) table[hash];

      table[hash] = null;

      return entry;
    }

    /**
     * Deletes an entry for the given key if it exists and the occupied slot key
     * matches the specified key to know it is correct.
     * 
     * @param key the key of the entry to delete
     * @return whether the operation was successful or not
     * 
     * @throws IllegalArgumentException if the key is {@code null} or blank
     */
    public synchronized boolean delete(K key) {
      int idx = search(key);

      if (idx != -1) {
        table[idx] = null;
        return true;
      }

      return false;
    }

    /**
     * Overloaded method that receives an {@code Entry<K, V>} object and gets
     * the key and passes it into the {@code delete(K key)}.
     * 
     * @param entry the entry to delete
     * @return whether the operation was successful or not
     * 
     * @throws NullPointerException if the entry is {@code null}
     */
    protected synchronized boolean delete(Entry<K, V> entry) {
      if (entry == null)
        throw new NullPointerException();
      return delete(entry.getKey());
    }

    /**
     * Clears all {@code Entry} objects from the table. 
     */
    protected synchronized void clear() {
      for (int i=0; i<table.length; i++)
        table[i] = null;
    }
  }

  /**
   * Returns a string representation of this {@code CuckooHashtable} object in the
   * form of a set of entries, seperated by new lines with the key, an equals sign
   * {@code =}, and the associated element, where the {@code toString} method is
   * used to convert the key and element to strings.
   *
   * @return a string representation of this hashtable
   * @since 1.2
   */
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder();
    Iterable<Entry<K, V>> entries = entries();

    sb.append("{\n");

    for (Entry<K, V> e : entries) {
      K key = e.getKey();
      V value = e.getValue();
      sb.append("  \"" + key.toString() + " = " + value.toString() + "\",\n");
    }

    return sb.toString() + "}";
  }

  protected <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterable<>();
    return new Enumerator<>(type, true);
  }

  protected <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return Collections.emptyIterator();
    return new Enumerator<>(type, true);
  }

  protected <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return Collections.emptyEnumeration();
    return new Enumerator<>(type, false);
  }

  protected class Enumerator<T> extends AbstractEnumerator<T> {
    Enumerator(int type, boolean iterator) {
      table = new Entry<?, ?>[T * m];
      this.type = type;
      this.iterator = iterator;
      index = 0;

      // Copy all entries from each subtable into the single array of entries
      for (CuckooHashSubtable<?, ?> Tj : CuckooHashtable.this.tables) {
        System.arraycopy(Tj.table, 0, table, index, Tj.table.length);
        index += Tj.table.length;
      }
    }  
  }
}
