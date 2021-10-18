package Hashtables.OpenAddressing;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import Hashtables.Entry;
import Hashtables.AbstractHashtable;
import static Hashtables.HashtableFunctions.isPrime;

/**
 * <h4>Tip: Using a large value, preferrably a prime number, for the table size
 * is a must due to the nature of the hash function being quadratic, it will
 * produce larger index values much more quickly. Doing so will prevent any
 * unwanted errors from occurring.</h4>
 * 
 * Quadratic Probing uses a hash function of the form:
 *
 * <p>
 * <pre>
 * h(k, i) = (h^(k) + c1(i) + c2(i^2)) mod m
 * </pre>
 * </p>
 * 
 * <p>
 * where h^ is the auxiliary hash function
 * </p>
 * 
 * <p>
 * The initial probe position is {@code h'(k)} later positions probed are offset
 * by an arbitrary quadratic polynomial until an open slot is found.
 * </p>
 * 
 * <p>
 * If two keys have the same initial probe position, then their probe sequences
 * are the same, since {@code h(k1, 0) = h(k2, 0)} implies
 * {@code h(k1, i) = h(k2, i)}. This property leads to a milder form of
 * clustering, known as "Secondary Clustering".
 * </p>
 *
 * <p>
 * As with linear probing, the initial probe determines the entire sequence, so
 * only {@code m} distinct probe sequences are used.
 * </p>
 * 
 * <p>
 * Using a universal hash function, where the hash function is chosen randomly,
 * independent of the keys, as the auxiliary hash function.
 * </p>
 */
public final class QuadraticProbing<K, V> extends AbstractHashtable<K, V> {
  /**
   * The array of {@code Entry} objects that hold the key/value pairs.
   */
  private Entry<?, ?>[] table;

  /**
   * The table size
   */
  private int m;  

  /**
   * The prime number used for the hash function
   */
  private int p;
  
  /**
   * The constants generated when instantiated for the hash function
   */
  private int a, b;

  /**
   * Initializes an empty, hashtable, with the specified size for the total
   * capacity for the table.
   * 
   * <h4>Tip: Using a large value, preferrably a prime number, will prevent any
   * unwanted errors from occurring.</h4>
   * 
   * @param size the specififed size of the hashtable maximum capacity
   * 
   * @throws IllegalArgumentException if the specified size is less than 1
   */
  public QuadraticProbing(int size, int prime) {
    if (size < 1)
      throw new IllegalArgumentException("Illegal size given. Must be larger than 1.");
    if (!isPrime(prime))
      throw new IllegalArgumentException("Illegal prime number given: " + prime);

      m = size;
      p = prime;
      a = (int) (Math.random() * p - 2) + 1;
      b = (int) (Math.random() * p) - 1;
      table = new Entry<?, ?>[size];
  }

  /**
   * Returns the initialized hashtable number of elements it can hold.
   * 
   * @return the number of elements that can be stored.
   */
  public int capacity() {
    return table.length;
  }

  /**
   * Checks if the table is full before insertions.
   * 
   * @throws IllegalStateException if the table is full
   */
  protected synchronized void checkCapacity() {
    if (n == table.length)
      throw new IllegalStateException("Hashtable is full.");
  }

  /**
   * The hash function that consits of an initial hash function that uses its own
   * constants and an auxiliary hash function that is quadratic of the {@code i}
   * number of slots skipped.
   * 
   * @param k the key to hash
   * @param i the number of rehashes
   * @return the hashed index
   */
  private int hash(K key, int i) {
    int h1 = ((a * key.hashCode() + b) % p) % m;
    return (int) (h1 + (0.5 * i) + (0.5 * Math.pow(i, 2))) % m;
  }

  /**
   * Inserts an entry into the hashtable with the specified key and value.
   * 
   * @param key   the key of the entry
   * @param value the value of the entry
   * @throws IllegalStateException    {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(K key, V value) {
    checkCapacity();
    checkKey(key);
    checkValue(value);

    for (int i=0, j = hash(key, i); i < m; i++, j = hash(key, i)) { 
      if (table[j] == null) {
        table[j] = new Entry<K, V>(key, value);
        break;
      }
    }

    n++;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized int search(K key) {
    checkKey(key);

    for (int i=0, j = hash(key, i); i < m; i++, j = hash(key, i))
      if (table[j] != null && table[j].getKey().equals(key))
        return j;
    return -1; 
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
  @SuppressWarnings("unchecked")
  public synchronized V get(K key) {
    int idx = search(key);
    return idx != -1 ? (V) table[idx].getValue() : null;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
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

  /**
   * Returns a string JSON object representation of the hashtable.
   *
   * @return a string of the hashtable
   */
  public String toString() {
    if (isEmpty())
      return "{}";

    StringBuilder sb = new StringBuilder();

    sb.append("{\n");

    for (int i=0; i<m; i++) {
      if (table[i] != null)
        sb.append("  \"" + table[i].toString() + "\"\n");
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
      this.type = type;
      this.iterator = iterator;
      this.table = QuadraticProbing.this.table;
      this.index = QuadraticProbing.this.table.length;
    }
  }

}