package Hashtables.OpenAddressing;

import Hashtables.Entry;
import Hashtables.AbstractHashtable;

/**
 * Linear Probing uses a hash function of the form:
 *
 * <p>
 * <pre>
 * h(k, i) = (h^(k) + i) mod m
 * </pre>
 * </p>
 * 
 * <p>
 * {@code for i = 0 to m-1} where {@code h^} is the auxiliary hash function
 * </p>
 * 
 * <p>
 * Given key k, we first probe {@code T[h'(k)]} - the slot given by the
 * auxiliary hash function. We next probe slot {@code T[h'(k) + 1]}, and so on
 * up to slot {@code T[m-1]}. Then we wrap around to slots
 * {@code T[0], T[1], ...} until we finally probe slot {@code T[h'(k) - 1]}.
 * Because the initial probe determines the entire probe sequence, there are
 * only m distinct probe sequences.
 * </p>
 *
 * <p>
 * Linear probing is easy to implement, but it suffers from a known problem
 * called "Primary Clustering". Longs runs of occupied slots build up,
 * increasing the average search time. Clusters arise because an empty slot
 * preceded by {@code i} full slots gets filled next with probablility
 * {@code (i + 1) / m}. Long runs of occupied slots tend to get longer, and the
 * average search time increases.
 * </p>
 * 
 * <h3>WARNING:</h3>
 * <p>
 * The table size must be large enough to support a variety of keys. If the
 * table capacity is 20 and a series of keys from 1 to 20 are used, it will be
 * fine. However, if the keys 1-200 are used for a capacity of 20, there is a
 * high liklihood there will be collisions and the secondary hash function works
 * in multiples of its value. So, if key 100 hashes to slot 12 and it is used,
 * the next hash value could result in a index of greater than 20, the table
 * length, causing an error. So choose the keys used carefully, use a larger
 * than needed hashtable capacity, or simply use a large prime number for the
 * table capacity.
 * </p>
 */
public final class LinearProbing<K, V> extends AbstractHashtable<K, V> {

  /**
   * Initializes an empty, hashtable, with the specified size for the total
   * capacity for the table.
   * 
   * <h4>Tip: Using a large value, preferrably a prime number, will prevent any
   * unwanted errors from occurring.</h4>
   * 
   * @param size the specififed size of the hashtable maximum capacity
   * 
   * @throws IllegalArgumentException if the specified size is less than {@code 1}
   */
  public LinearProbing(int size) {
    super(size);
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
   * The hash function
   * 
   * @param key the key to hash
   * @param i   the number of index slots skipped
   * @return the hash value
   */
  protected int hash(K key, int i) {
    return (int) (Math.floor(m * ((key.hashCode() * 0.5675) % 1)) % m) + i;
  }

  /**
   * Inserts the new entry into the hashtable. Starts at {@code T[h'(k)]}, the
   * slot given by the auxiliary hash function. If not available, we proceed to
   * {@code T[h'(k) + 1]} and so on up to slot {@code T[m-1]}. If no available
   * position was found using the hash function, we wrap around and start at
   * {@code 0} up to {@code h'(k) - 1}.
   * 
   * @param key   the key of the entry
   * @param value the value of the entry
   * @return boolean indicating whether the insertion was successful or not
   * 
   * @throws IllegaStateException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(K key, V value) {
    checkCapacity();
    checkKey(key);
    checkValue(value);

    Entry<K, V> entry = new Entry<K, V>(key, value);
    int i, j;

    for (i=0, j = hash(key, i); j < m; i++, j = hash(key, i)) {
      if (table[j] == null) {
        table[j] = entry;
        n++;
        return;
      }
    }

    for (i=0, j = hash(key, 0); i < j; i++) {
      if (table[i] == null) {
        table[i] = entry;
        n++;
        return;
      }
    }
  } 

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected synchronized int search(K key) {
    checkKey(key);

    int i, j;

    for (i=0, j = hash(key, i); j < m; i++, j = hash(key, i)) {
      if (table[j] != null && table[j].getKey().equals(key))
        return j;
    }

    for (i=0, j = hash(key, 0); i < j; i++) {
      if (table[i] != null && table[i].getKey().equals(key))
        return i;
    }

    return -1; 
  }

}
