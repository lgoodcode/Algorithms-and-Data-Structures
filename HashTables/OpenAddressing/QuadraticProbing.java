package Hashtables.OpenAddressing;

import Hashtables.Entry;
import Hashtables.AbstractHashtable;
import static Hashtables.HashTableFunctions.isPrime;

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
  private int a, b, p;

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
    super(size);

    if (!isPrime(prime))
      throw new IllegalArgumentException("Illegal prime number given: " + prime);

    p = prime;
    a = (int) (Math.random() * p - 2) + 1;
    b = (int) (Math.random() * p) - 1;
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
  private synchronized void checkCapacity() {
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
  protected int hash(K key, int i) {
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
   * Internal method used by the other methods to lookup entries in the hashtable.
   * 
   * @param key the key to lookup
   * @return the index of the element with the specified key or {@code -1} if not
   *         found
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected synchronized int search(K key) {
    checkKey(key);

    for (int i=0, j = hash(key, i); i < m; i++, j = hash(key, i)) { 
      if (table[j] != null && table[j].getKey().equals(key))
        return j;
    }

    return -1; 
  }

}