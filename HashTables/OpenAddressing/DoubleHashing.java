package Hashtables.OpenAddressing;

import Hashtables.Entry;
import Hashtables.AbstractHashtable;
import Hashtables.exceptions.HashtableFullException;

/**
 * This hashtable uses the DoubleHashing implementation of OpenAddressing, where
 * it consists of a single table and all the slots are occupied with an element
 * or {@code null}. It also uses a counter, {@code n}, to determine whether the
 * table has reached it's capacity without having to attempt to compute multiple
 * hashes to discover the table is full.
 * 
 * <h3>WARNING:</h3>
 * <p>
 * The table size must be large enough to support a variety of keys. If the table
 * capacity is 20 and a series of keys from 1 to 20 are used, it will be fine.
 * However, if the keys 1-200 are used for a capacity of 20, there is a high
 * liklihood there will be collisions and the secondary hash function works
 * in multiples of its value. So, if key 100 hashes to slot 12 and it is used,
 * the next hash value could result in a index of greater than 20, the table
 * length, causing an error. So choose the keys used carefully, use a 
 * larger than needed hashtable capacity, or simply use a large prime number
 * for the table capacity.
 * </p>
 * 
 * <p>
 * DoubleHashing offers one of the best methods available for open addressing
 * because the permutations produced have many of the characteristics of
 * randomly chosen permutations. Double hashing uses a hash function of the
 * form:
 * </p>
 *
 * <p>
 * <pre>
 * h(k, i) = h(h1(k) + (i * h2(k) % m))
 * </pre>
 * </p>
 * 
 * where both h1 and h2 are auxiliary hash functions. The initial probe goes to
 * position {@code T[h1(k)]} and successive probe positions are offset from
 * previous positions by the amount {@code h2(k) mod m}. Thus, unlike the case
 * of linear or quadratic probing, the probe sequence here depends in two ways
 * upon the key k, since the initial probe position, the offset, or both, may
 * vary.
 *
 * <p>
 * The value {@code h2(k)} must be relatively prime to the hash-table size m for
 * the entire hash table to be searched.
 * </p>
 * 
 * <p>
 * A convenient way to ensure this condition is to let me be a power of 2 and to
 * design h2 so that it always produces an odd number.
 * </p>
 *
 * <p>
 * Another way is to let m be prime and to design h2 so that it always returns a
 * positive integer less than m.
 * </p>
 */
public final class DoubleHashing<K, V> extends AbstractHashtable<K, V> {

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
  public DoubleHashing(int size) {
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
   * The hash function that consists of two hash functions. The initial hash
   * function hashes the initial slot, if it is occupied, then it uses the number
   * of times it has collided as a counter to activate the secondary hash function
   * to hash to another slot index. This process continues until an open slot is
   * found.
   * 
   * <p>
   * <pre>
   * h1(k) = k % m 
   *h2(k) = 1 + (k % m^)
   *h(k, i) = h(h1(k) + (i * h2(k) % m))
   * </pre>
   * </p>
   * 
   * where {@code m^} is chosen to be slightly less than m - i.e (m-1).
   * Here, m^ is chosed to be {@code m - 2} because when it is only 
   * subtracted by one, it can result in a hash value that would exceed
   * the table size by 1 to allow it to result in a possible index slot.
   *
   * <p>
   * The probe will first look at position {@code h1(k)} followed by adding
   * successive values of {@code h2(k, i)}.
   * </p>
   * 
   * @param k the key to hash
   * @param i the number of rehashes
   * @return the hashed index
   */
  protected int hash(K key, int i) {
    return (key.hashCode() % m) + (i * (1 + (key.hashCode() % (m-2))) % m);
  }

  /**
   * Inserts an entry into the hashtable with the specified key and value.
   * 
   * @param key   the key of the entry
   * @param value the value of the entry
   * @throws HashtableFullException   {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized boolean insert(K key, V value) throws HashtableFullException {
    if (n == m)
      throw new HashtableFullException(m);
    checkKey(key);
    checkValue(value);

    for (int i=0, j = hash(key, i); i < m && j < m; i++, j = hash(key, i)) { 
      if (table[j] == null) {
        table[j] = new Entry<K, V>(key, value);
        n++;
        return true;
      }
    }

    return false;
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
    
    for (int i=0, j = hash(key, i); i < m && j < m; i++, j = hash(key, i)) { 
      if (table[j] != null && table[j].getKey().equals(key))
        return j;
    }

    return -1; 
  }

}