package data_structures.hashtables;

import data_structures.Entry;
import static data_structures.hashtables.HashtableFunctions.isPrime;

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
public final class QuadraticProbing<K, V> extends AbstractStaticHashtable<K, V> {
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
   * Initializes an empty, hashtable, with the specified size for the total
   * capacity for the table.
   *
   * <p>
   * Uses a default prime number of {@code 1277}.
   * </p>
   *
   * @param size the specififed size of the hashtable maximum capacity
   *
   * @throws IllegalArgumentException if the specified size is less than 1
   */
  public QuadraticProbing(int size) {
    this(size, 1277);
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
    checkDuplicate(key);

    for (int i=0, j = hash(key, i); i < m; i++, j = hash(key, i)) {
      if (table[j] == null) {
        table[j] = new Entry<K, V>(key, value);
        n++;
        modCount++;
        return;
      }
    }

    for (int i=0; i < m; i++) {
      if (table[i] == null) {
        table[i] = new Entry<K, V>(key, value);
        n++;
        modCount++;
        return;
      }
    }
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
    for (int i=0; i < m; i++)
      if (table[i] != null && table[i].getKey().equals(key))
        return i;
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
      modCount++;
      return true;
    }
    return false;
  }

}