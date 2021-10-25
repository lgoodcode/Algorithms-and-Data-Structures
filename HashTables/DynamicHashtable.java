package data_structures.hashtables;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import static data_structures.hashtables.HashtableFunctions.*;

/**
 * This implementation of the Dynamic Perfect Hashtable differs slightly. It
 * uses the FKS scheme (Fredman, Komlos, and Szemeredi), a two-tiered hash table
 * scheme, with universal hashing at each level. Each key is hashed twice - the
 * first hash value maps to a slot in the second-level hash table, which is
 * another hash table but smaller.
 * 
 * <h4>WARNING: for the table hash functions to work properly, the prime number
 * must be larger than the total number of entries in the table. A very large
 * prime should be chosen. If the number of entries exceed the prime value, the
 * hash functions won't work properly.</h4>
 * 
 * <p>
 * The size that is is initialized will result in a total capacity of that value
 * squared: {@code size = m^2}. This is because it creates {@code m} tables of
 * size {@code m}.
 * </p>
 * 
 * <p>
 * This variation also contains a {@code load factor} to determine when the
 * capacity is reached versus the original algorithm to determine the capacity
 * and new size. The {@code load factor} has a lower and upper bounds to ensure
 * efficiency of {@code [0.6, 0.95]}. This is to prevent ineffecienty trigger a
 * full rehash and expanding the table when the table is barely half full and
 * upper bounds of 95% to prevent reaching full capacity, risking an
 * ouf-of-bounds exception during insertion.
 * </p>
 * 
 * <p>
 * The algorithm that is used to determine the new table size on a full rehash
 * is: {@code 2 * Max(n, 4)}, where {@code n} is the total number of entries
 * stored. It simply doubles the table size which then is squared to get the new
 * total capacity.
 * </p>
 * 
 * <p>
 * This variation doesn't set the {@code b} hash constant as the length of the
 * number of entries, rather it sticks with the range of {@code [0, p-1]} to
 * retain the properties of universal hashing. Randomly choosing the constants
 * {@code a} and {@code b} from a set that differs by one value, gives us
 * {@code p(p-1)} hash functions. A universal collection of hash functions, a
 * single pair of keys collides with probablity at most {@code 1 / m}, which
 * gives us {@code Pr h(k) = h(l) ≤ 1/m}.
 * </p>
 */
public final class DynamicHashtable<K, V> extends AbstractDynamicHashtable<K, V> {
  private DynamicSubtable<?, ?>[] tables;
  private float loadFactor = 0.8f;
  private int a, b, m, p; 
  
  /**
   * Creates a new, empty, dynamic hashtable. The {@code size} specified results
   *  in an actual total capacity of the value squared. The {@code prime} i
   *  used for the hash functions and must be larger than the total expect
   * d table capacity, otherwise, the table won't function properly.
   * 
   * @param size   the desired table size squared
   * @param prime the prime used for the hash functions
   * 
   * @throws IllegalArgumentException if the {@code size} is less than {@code 1}
   *         or the {@code prime} is not a valid prime number
   */
  public DynamicHashtable(int size, int prime) {
    if (size < 1)
      throw new IllegalArgumentException("Size must be greater than 0");
    if (!isPrime(prime))
      throw new IllegalArgumentException("Illegal prime number given: " + prime);

    p = prime;
    m = size;
    a = (int) (Math.random() * p - 2) + 1;
    b = (int) (Math.random() * p) - 1;
    buildSubtables();
  }

  /**
   * Constructs a new, empty, dynamic hashtable with default values of
   * {@code size = 4} and {@code prime = 1277}.
   */
  public DynamicHashtable() {
    this(4, 1277);
  }

  /**
   * Sets new tables with the hashtable's specified size {@code m}.
   */
  private void buildSubtables() {
    tables = new DynamicSubtable<?, ?>[m];

    for (int i=0; i < m; i++)
      tables[i] = new DynamicSubtable<K, V>(m, p);
  }

  /**
   * {@inheritDoc}
   */
  protected boolean capacityReached() {
    return n > (m * m) * loadFactor;
  }

  protected int hash(K key) {
    return ((a * key.hashCode() + b) % p) % m;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);
    checkDuplicate(key);

    n++;

    if (capacityReached())
      fullRehash(key, value);
    else {
      DynamicSubtable<K, V> Tj = (DynamicSubtable<K, V>) tables[hash(key)];

      // If subtable Tj has the capcacity to hold another entry
      if (!Tj.capacityReached())
        Tj.insert(key, value);
      // Otherwise, subtable Tj is too small, check whether to rebuild the subtable or do a full rebuild of all tables
      else {
        if (n <= (m * (m - 1)) * loadFactor)
          Tj.fullRehash(key, value);
        else
          fullRehash(key, value);
      }
    }

    modCount++;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  protected synchronized void fullRehash(K key, V value) {
    int maxNumEntries = Math.max(m, 2) * m;
    Entry<?, ?> entries[] = new Entry<?, ?>[maxNumEntries];
    int entryIdx = 0;

    // Place entries from all subtables into a single array
    for (DynamicSubtable<K, V> Tj : (DynamicSubtable<K, V>[]) tables) {
      for (Entry<K, V> e : (Entry<K, V>[]) Tj.table) {
        if (e != null)
          entries[entryIdx++] = e;
      }
    }

    // Add the newest entry
    entries[entryIdx++] = new Entry<K,V>(key, value);

    // Calculate new m, reset size counter, increment modified counter
    m = 2 * Math.max(n, 4);
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
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public synchronized int search(K key) {
    checkKey(key);

    for (int i=0; i < m; i++)
      if (((DynamicSubtable<K, V>) tables[i]).search(key) != -1)
        return i;
    return -1;
  }

  /**
   * Retrieves the entry with the specified key or {@code null} if not found.
   * 
   * @param key the key of the desired entry
   * @return the entry or {@code null} if not found
   * @throws IllegalArgumentException if the specified key is {@code null} or blank
   */
  @SuppressWarnings("unchecked")
  public synchronized Entry<K, V> getEntry(K key) {
    int idx = search(key);

    if (idx != -1)
      return ((DynamicSubtable<K, V>) tables[idx]).getEntry(key);
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized boolean hasKey(K key) {
    return search(key) != -1;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized V get(K key) {
    Entry<K, V> entry = getEntry(key);
    return entry != null ? entry.getValue() : null;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public synchronized boolean delete(K key) {
    int idx = search(key);

    if (idx != -1 && ((DynamicSubtable<K, V>) tables[idx]).delete(key)) {
      n--;
      modCount++;
      return true;
    }
    return false;
  }

  private static class DynamicSubtable<K, V> extends AbstractHashSubtable<K, V> {
    protected int a, b, m, p;
    protected Entry<?, ?>[] table;
  
    protected DynamicSubtable(int size, int prime) {
      if (size < 1)
        throw new IllegalArgumentException("Size must be greater than 0");
      if (!isPrime(prime))
        throw new IllegalArgumentException("Illegal prime number given: " + prime);
  
      p = prime;
      m = size;
      a = (int) (Math.random() * p - 2) + 1;
      b = (int) (Math.random() * p) - 1;
      table = new Entry<?, ?>[m];
    }
  
    /**
     * Determines whether the subtable is full.
     * 
     * @return boolean indicating whether the table is full or not
     */
    protected boolean capacityReached() {
      return n >= m;
    }
  
    protected int hash(K key) {
      return ((a * key.hashCode() + b) % p) % m;
    }
  
    /**
     * {@inheritDoc}
     */
    public synchronized void insert(K key, V value) {
      int hash = hash(key);
      Entry<K, V> entry = new Entry<K, V>(key, value);
  
      n++;
  
      if (table[hash] == null)
        table[hash] = entry;
      else
        rehash(entry);
    }
  
    /**
     * Takes all the entries in the subtable and finds new hash constants that will
     * be injective for the set of entries keys, sets the new constants, and then
     * re-inserts the entries back into the subtable so there are no collisions.
     * 
     * @param entry the newest entry that caused a collision
     */
    @SuppressWarnings("unchecked")
    protected synchronized void rehash(Entry<K, V> entry) {
      Entry<?, ?>[] entries = new Entry<?, ?>[m];
      int[] keys = new int[m];
      int idx = 0;
  
      // Retrieve all entries from table and set to null to reset table
      for (int i=0; i<m; i++) {
        if (table[i] != null) {
          entries[idx] = table[i];
          keys[idx++] = table[i].getKey().hashCode();
          table[i] = null;
        }
      }
  
      entries[idx] = entry;
      keys[idx++] = entry.getKey().hashCode();
  
      // Get new hash constants that will be injective for the entry keys
      int[] constants = injectiveIntegers(keys, idx, m, p);
      a = constants[0];
      b = constants[1];
  
      // Re-insert all entries
      for (int i=0; i<idx; i++) 
        table[hash((K) entries[i].getKey())] = entries[i];
    }
  
    /**
     * Triggered when the capacity load is reached and rebuilds the subtables with a
     * new calculated table size. Iterates through all tables and places the entries
     * in array and once the tables are initialized, re-inserts all the entries back
     * into the hashtable.
     * 
     * @param key   the key of the new entry that reached the capacity load
     * @param value the value of the new entry that reached the capacity load
     */
    @SuppressWarnings("unchecked")
    protected synchronized void fullRehash(K key, V value) {
      Entry<?, ?>[] entries = new Entry<?, ?>[m + 1];
      int idx = 0;
  
      for (int i=0; i<m; i++) {
        if (table[i] != null)
          entries[idx++] = table[i];
      }
  
      entries[idx++] = new Entry<K, V>(key, value);
  
      m = 5 * Math.max(entries.length, 4);
      table = new Entry<?, ?>[m];
  
      // Garbage collect old tables
      System.gc();
  
      for (int i=0; i<idx; i++)
        table[hash((K) entries[i].getKey())] = entries[i];
    }
  
    /**
     * Performs a lookup in the subtable to find an entry with the specified key.
     * 
     * @param key the key of the entry to search for
     * @return the index of the entry or {@code -1} if not found
     */
    public synchronized int search(K key) {
      int hash = hash(key);
  
      if (table[hash] != null && table[hash].getKey().equals(key))
        return hash;
      return -1;
    }
  
    /**
     * {@inheritDoc}
     */
    public synchronized boolean hasKey(K key) {
      return search(key) != -1;
    }
  
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public synchronized V get(K key) {
      int idx = search(key);
      return idx != -1 ? (V) table[idx].getValue() : null;
    }
  
    /**
     * Retrieves the entry in the subtable with the specified key or {@code null} if
     * not found.
     * 
     * @param key the key of the entry to look retrieve
     * @return the {@code Entry} object or {@code null} if not found
     */
    @SuppressWarnings("unchecked")
    public synchronized Entry<K, V> getEntry(K key) {
      int idx = search(key);
      return idx != -1 ? (Entry<K, V>) table[idx] : null;
    }
  
    /**
     * {@inheritDoc}
     */
    public synchronized boolean delete(K key) {
      int idx = search(key);
  
      if (idx != -1) {
        table[idx] = null;
        n--;
        return true;
      }
      return false;
    }
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

  /**
   * Enumerator class that simply requires a constructor for the implementing data
   * structure since the underlying array type and size is typically unique.
   */
  protected class Enumerator<T> extends AbstractEnumerator<T> {
    Enumerator(int type, boolean iterator) {
      table = new Entry<?, ?>[m * m];
      this.type = type;
      this.iterator = iterator;
      size = 0;

      // Copy all entries from each subtable into the single array of entries
      for (DynamicSubtable<?, ?> Tj : DynamicHashtable.this.tables) {
        for (int i=0; i<Tj.table.length; i++) {
          if (Tj.table[i] != null)
            table[size++] = Tj.table[i];
        }
      }
    }  
  }
}