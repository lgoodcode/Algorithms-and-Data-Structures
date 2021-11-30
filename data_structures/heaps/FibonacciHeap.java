package data_structures.heaps;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import data_structures.EmptyIterator;
import data_structures.linkedLists.LinkedList;
import data_structures.queues.Queue;

/**
 * This implementation of a Minimum Fibonacci Heap that forgoes the use of a
 * {@code CircularDoublyLinkedList} to hold the child lists by using the
 * techniques of that data structure directly. i.e., when removing a node in a
 * list, to simply dereference it by setting the pointers of that nodes
 * {@code next.prev} and {@code prev.next} properties to the following node.
 *
 * Uses the {@code BiFunction} functional interface instead of creating one for
 * the compare function.
 *
 * @see LinkedLists.CircularLinkedList
 */
public final class FibonacciHeap<T> implements java.io.Serializable {
  /**
   * The internal node used for the to hold the attributes and element.
   */
  private static class Node<T> {
    Node<T> parent;
    Node<T> child;
    Node<T> left;
    Node<T> right;
    T item;
    int degree;
    boolean mark;

    Node(T item) {
      if (item == null || item.toString().isBlank())
        throw new IllegalArgumentException("Item cannot be null or blank.");
      this.item = item;
      degree = 0;
      mark = false;
    }
  }

  @java.io.Serial
  private static final long serialVersionUID = 199208284839394830L;


  /**
   * <i>log2(e)</i> used to calculate <i>log2(n)</i> by
   * {@code log(n) * log2(e) = log2(n)}
   */
  private static transient final double LOG2E = 1.4426950408889634;

  /**
   * The function to compare a value to determine whether an element is less than
   * another to keep the minimum {@code Node} the smallest in the heap.
   */
  private transient BiFunction<T, T, Boolean> compare;

  /**
   * The current smallest element in the heap.
   */
  private transient Node<T> min;

  /**
   * The number of elements in the heap.
   */
  private transient int size = 0;

  /**
   * The number of times this FibHeap has been structurally modified Structural
   * modifications are those that change the number of entries in the list or
   * otherwise modify its internal structure (e.g., insert, delete). This field is
   * used to make iterators on Collection-views of the FibHeap fail-fast.
   *
   * @see ConcurrentModificationException
   */
  private transient int modCount = 0;

  /**
   * Constructs an empty, FibonacciHeap, with the specified compare function to
   * compare keys.
   *
   * @param compare the compare function
   */
  public FibonacciHeap(BiFunction<T, T, Boolean> compare) {
    this.compare = compare;
  }

  /**
   * Constructs an empty, FibonacciHeap, with the default compare function that
   * compares the items using the {@code hashCode()} method.
   */
  public FibonacciHeap() {
    this((T x, T y) -> x.hashCode() < y.hashCode());
  }

  /**
   * Compares the keys between two nodes and returns the appropraite boolean value
   * indicating whether the key of node {@code x} is less than {@code y}.
   *
   * @param x node to compare
   * @param y other node to compare
   * @return is the key of node {@code x} less than node {@code y}
   */
  private boolean isLessThan(Node<T> x, Node<T> y) {
    return compare.apply(x.item, y.item);
  }

  /**
   * Returns a boolean indicating if the heap is empty or not.
   *
   * @return is the heap empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns an integer of the number of items in the heap.
   *
   * @return the number of items in the heap
   */
  public int size() {
    return size;
  }

  /**
   * Removes all the entries in the FibHeap by removing the reference to the root.
   */
  public void clear() {
    min = null;
    size = 0;
    modCount++;
  }

  /**
   * Returns the current minimum element in the heap without extracting it.
   *
   * @return the miniumum element in the heap or {@code null} if empty
   */
  public T peek() {
    return isEmpty() ? null : min.item;
  }

  /**
   * Fib-Insert(H, x)  O(1)
   * 1   x.degree = 0
   * 2   x.p = NIL
   * 3   x.child = NIL
   * 4   x.mark = False
   * 5   if H.min == NIL
   * 6       create a root list for H containing just x
   * 7       H.min = x;
   * 8   else insert x into H's root list
   * 9       if x.key < H.min.key
   * 10          H.min = x
   * 11  H.n = H.n + 1
   */

  /**
   * Inserts a new key/value node into the heap by simply adding it to the root
   * list. The key is used to identify the priority of the new node; a smaller key
   * is extracted sooner than one that is larger.
   *
   * @param key   the key
   * @param value the value
   * @see LinkedLists.CircularLinkedList
   *
   * @throws IllegalArgumentException if the specified item is {@code null} or
   *                                  blank
   */
  public synchronized void insert(T item) {
    if (item == null || item.toString().isBlank())
      throw new IllegalArgumentException("Item cannot be null or blank.");

    Node<T> node = new Node<>(item);

    // If heap is empty
    if (min == null) {
      // Set root list containing just x and as new min
      node.left = node.right = node;
      min = node;
    }
    // CircularDoublyLinkedList - insert into root list
    else {
      node.left = min;
      node.right = min.right;
      min.right = node;
      node.right.left = node;

      if (isLessThan(node, min))
        min = node;
    }

    size++;
    modCount++;
  }

  /**
   * Fib-Heap-Extract-Min(H)  O(lg n) - amortized cost
   * 1   z = H.min
   * 2   if z != null
   * 3       for each child x of z
   * 4           add x to the root list of H
   * 5           x.p = NIL
   * 6       remove z from the root list of H
   * 7       if z == z.right
   * 8           H.min = NIL
   * 9       else H.min = z.right
   * 10          Consolidate(H)
   * 11      H.n = H.n - 1
   * 12  return z
   *
   * We start by saving a pointer to z, the minimum node to return at the end. If
   * z is null, then the heap is already empty and are done. Otherwise, delete
   * node z from the heap by making all of z's children roots of H (if there are
   * any children) and removing z from the root list.
   *
   * If z is its own right sibling, then z was the only node on the root list and
   * had no children, so we just make the heap empty before returning z.
   * Otherwise, we set the pointer H.min into the root list to point to a root
   * other than z, then consolidate the root list which will include setting the
   * H.min to the proper minimum element, as well as consolidate the number of
   * trees in the heap.
   */

  /**
   * Returns the smallest value in the heap and re-consolidates the heap
   * afterwards.
   *
   * @return the smallest value in the heap
   *
   * @throws NoSuchElementException if there is no element to return
   */
  public synchronized T extractMin() {
    if (isEmpty())
      throw new NoSuchElementException("FibonacciHeap is empty.");

    Node<T> temp, x, z = min;

    // If z has a child; get child list of z
    if (z.child != null) {
      x = z.child;

      // For each child x of z, add to root list
      do {
        temp = x.right;
        x.right = z;
        x.left = z.left;
        z.left = x;
        x.left.right = x;

        x.parent = null;
        x = temp;
      } while (x != z.child);
    }

    // Remove z from root list of heap
    z.left.right = z.right;
    z.right.left = z.left;

    // If z was only item in the root list; no items left - min is null
    if (z == z.right)
      min = null;
    else {
      // Otherwise, set min to next item and consolidate
      min = z.right;
      consolidate();
    }

    size--;
    modCount++;

    return z.item;
  }

  /**
   * Consolidate(H)  D(n) + t(H) - 1  D(n): the maximum degree bound (lg n)
   *                                  t(H): number of trees in the heap (n)
   * 1   let A[0..D(H.n)] be a new array
   * 2   for i = 0 to D(H.n)
   * 3       A[i] = NIL
   * 4   for each node w in the root list of H
   * 5       x = w
   * 6       d = w.degree
   * 7       while A[d] != NIL
   * 8           y = A[d]    // another node with same degree as x
   * 9           if x.key > y.key
   * 10              exchange x with y
   * 12          Heap-Link(H, y, x)
   * 13          A[d] = NIL
   * 14          d = d + 1
   * 15      A[d] = x
   * 16  H.min = NIL
   * 17  for i = 0 to D(H.n)
   * 18      if A[i] != NIL
   * 19          if H.min == NIL
   * 20              create a root list for H containing just A[i]
   * 21              H.min = A[i]
   * 22          else insert A[i] into H's root list
   * 23              if A[i].key < H.min.key
   * 24                  H.min = A[i]
   */

  /**
   * Reduces the number of trees in the Fibonacci heap, which consists of
   * repeatedly executing the following steps until every root in the root list
   * has a distinct degree value:
   *
   * <p>
   * 1. Find two roots x and y in the root list with the same degree. Without loss
   * of generality, let x.key <= y.key
   * </p>
   *
   * <p>
   * 2. "Link" y to x: remove y from the root list, and make y a child of x by
   * calling the HeapLink procedure. The procedure increments the x.degree
   * attribute and clears the mark on y.
   * </p>
   *
   * <p>
   * Uses an auxiliary array to keep track of roots according to their degrees.
   * and initializes each entry with {@code null}. The do-while loop after
   * initialing the array, processes each root w in the root list. Roots are
   * linked together, w may be linked to some other node and no longer be a root.
   * Nevertheless, w is always in a tree rooted to some node x, which may or may
   * not be w itself. Because we want at most one root with each degree, we look
   * in the array to see whether it contains a root y with the same degree as x.
   * If it does, then we link the roots x and y but guaranteeing that x remains a
   * root after linking. That is, we link y to x after first swapping the pointers
   * to the two roots if y's key is smaller than x's key.fter linking y to x, the
   * degree of x has increased by 1, and continuing this process, linking x and
   * another root whose degree equals x's new degree, until no other root that we
   * have processed has the same degree as x. We then set the appropriate entry of
   * to point to x, so that as we process roots later on, we have recorded that x
   * is the unique root of its degree that we have already processed.
   * </p>
   *
   * <p>
   * When the loop terminates, at most one root of each degree will remain, and
   * the array will point to each remaining root.
   * </p>
   *
   * <p>
   * After all that, all that remains is to clean up. The min attribute is set to
   * null and the root list is then reconstructed and built from the array, which
   * as mentioned earlier, contains all the roots of each distinct degree.
   * </p>
   */
  @SuppressWarnings("unchecked")
  private void consolidate() {
    // Maximum degree D(n) of any node in an n-node Fibonacci heap is O(log2 n)
    int i, d, num = 0, D = (int) Math.ceil(Math.log(size) * LOG2E);
    // Auxiliary array to keep track of roots according to their degrees
    Node<?>[] arr = new Node<?>[D];
    Node<T> y, x, temp;

    /**
     * Modification: Count the number of nodes in root list. This is necessary
     * because of the CircularLinkedList root list, where once we start calling
     * heapLink() to start making nodes a child of another, it will lose track of
     * whether we actually made a full iteration back to the start.
     */
    temp = min;
    do num++;
    while ((temp = temp.right) != min);

    // For each node in the root list
    x = min;
    for (i = 0; i < num; i++, x = x.right) {
      d = x.degree;
      // Repeatedly links the root x of the tree containing node w to another tree
      // whose root has the same degree as x, until no other root has the same degree.
      while (arr[d] != null) {
        // Another node with the same degree as x
        y = (Node<T>) arr[d];
        // If y is less than x, swap so that the smallest key becomes the parent
        if (isLessThan(y, x)) {
          temp = x;
          x = y;
          y = temp;
        }

        heapLink(y, x);
        arr[d++] = null;
      }

      arr[d] = x;
    }

    // Reconstructs the root list from the auxilary array
    min = null;
    for (i = 0; i < D; i++) {
      if (arr[i] != null) {
        if (min == null) {
          min = (Node<T>) arr[i];
          min.right = min.left = min;
        }
        else {
          // CircularDoublyLinkedList insertion to root list
          temp = (Node<T>) arr[i];
          temp.right = min;
          temp.left = min.left;
          min.left = temp;
          temp.left.right = temp;

          // Checks each node as we go to ensure the smallest is the min
          if (isLessThan(temp, min))
            min = temp;
        }
      }
    }
  }

  /**
   * Fib-Heap-Link(H, y, x)
   * 1   remove y from the root list of H
   * 2   make y a child of x, incrementing x.degree
   * 3   y.mark = False
   */

  /**
   * Makes y a child of x
   *
   * @param y node to become a child
   * @param x node to become a parent
   */
  private void heapLink(Node<T> y, Node<T> x) {
    // Remove y from root list
    y.left.right = y.right;
    y.right.left = y.left;

    // Make y a child of x
    y.parent = x;

    // If there is no child, make new child list
    if (x.child == null) {
      x.child = y;
      y.left = y.right = y;
    }
    else {
      // Insert y into x's child list
      y.right = x.child;
      y.left = x.child.left;
      x.child.left = y;
      y.left.right = y;
      x.child = y;
    }

    x.degree++;
    y.mark = false;
  }

 /**
  * Heap-Decrease-Key(H, x, k)  (-)(1) - amortized cost
  * 1   if k > x.key
  * 2       error "new key is greater than current key"
  * 3   x.key = k
  * 4   y = x.p
  * 5   if y != NIL and x.key < y.key
  * 6       Cut(H, x, y)
  * 7       Cascading-Cut(H, y)
  * 8   if x.key < H.min.key
  * 9       H.min = x
  *
  * Lines 1-4 - Ensures that the new key is no greater than the current key of x and then
  *               assigns the new key to x
  * Lines 5-7 - If x is a root or x's new key is greater or equal to its parent y.key,
  *               then min-heap order is good and no changes are needed.
  * Lines 8-10 - If min-heap order has been violated, we have to cut the link between x and its
  *               parent y, making x a root. Then a cascading-cut operation on y.
  * Lines 12-13 - If the new key is less than the minimum of the heap, set it as the new min
  *
  * The 'mark' attributes are used to obtain desired time bounds. They record a little
  * piece of the history of each node.
  */

  /**
   * Decreases the key of the specified {@code FibonacciNode}. This can only be
   * used on the minimum node when retrieved using {@code getMin()}. The supplied
   * key must be a non-{@code null} value and less than the current key of the
   * specified node.
   *
   * @param x   the node whose key is to be decreased
   * @param key the new key to set the node to
   *
   * @throws IllegalArgumentException if the key is {@code null}, blank, or equal
   *                                  to or greater than the current key.
   * TODO: figure out a way to pragmatically impelement this
   */
  @SuppressWarnings("unused")
  private synchronized void decreaseKey(Node<T> x, T item) {
    if (compare.apply(x.item, item))
      throw new IllegalArgumentException("New item must be smaller than current node item.");

    Node<T> y = x.parent;
    x.item = item;

    if (y != null && isLessThan(x, y)) {
      cut(x, y);
      cascadingCut(y);
    }

    if (isLessThan(x, min))
      min = x;
  }

  /**
   * Cut(H, x, y) 
   * 1 remove x from the child list of y 
   * 2 add x to the root list of H 
   * 3 x.p = NIL 
   * 4 x.mark = False
   *
   * Reverse of the link operation: removes node from the child list of parent
   *
   * Suppose the following events have happened to node x:
   *
   * 1. at some time, x was a root
   * 2. then x was linked to (made the child of) another node
   * 3. then two children of x were removed by cuts
   *
   * As soon as the second child has been lost, we cut x from its parent, making
   * it a new root. The x.mark is True if steps 1 and 2 have occured and one child
   * of x has been cut. The Cut procedure therefor clears x.mark in line 4, since
   * it performs step 1. (Fib-Heap-Link clears y.mark because node y is being
   * linked to another node, and so step 2 is being performed. The next time a
   * child of y is cut, y.mark will be set to True.)
   */

  /**
   * Removes x from parent y child
   *
   * @param x child node
   * @param y parent node
   */
  private void cut(Node<T> x, Node<T> y) {
    // If x was an only child, clear it
    if (x == x.right)
      y.child = null;
    // Otherwise, set child to next node
    else
      y.child = x.right;

    // Remove x from child of y
    x.left.right = x.right;
    x.right.left = x.left;

    // Add x to root list
    x.right = this.min;
    x.left = this.min.left;
    this.min.left = x;
    x.left.right = x;

    x.parent = null;
    x.mark = false;
  }

  /**
   * Cascading-Cut(H, y)
   * 1   z = y.p
   * 2   if z != NIL
   * 3       if y.mark == False
   * 4           y.mark = True
   * 5       else Cut(H, y, z)
   * 6           Cascading-Cut(H, z)
   *
   * Performs a cascading cut operation. Cuts node from its parent and then does
   * the same for its parent and so on up the tree.
   *
   * Because x might be the second child cut from its parent y since the time that
   * y was linked to another node, we need to perform a cascading-cut operation on
   * y. If y is a root, then the test in line 2 causes the procedure to just
   * return. If y is unmarked, the procedure marks it, since its first child has
   * just been cut and returns. If y is marked however, it has just lost its
   * second child; y is cut in line 5 and Cascading-Cut calls itself recursively
   * on y's parent z. The Cascading-Cut recurses its way up the tree until it
   * finds either a root or an unmarked node.
   */

  /**
   * Cuts the node from its parent and continues up to every parent of the
   * original node.
   *
   * @param y node to cut from parent
   */
  private void cascadingCut(Node<T> y) {
    Node<T> z = y.parent;

    if (y.child != null)
      y.degree = y.child.degree + 1;
    else 
      y.degree = 0;

    if (z != null) {
      if (!y.mark)
        y.mark = true;
      else {
        cut(y, z);
        cascadingCut(z);
      }
    }
  }

  /**
   * Internal method used for {@code toString()} to traverse the heap recursively.
   * It starts at the minimum in the root list and iterates to every right node
   * then checks if it has a child, if so, traverses to the child and continues
   * all the way down until all nodes have been reached.
   */
  private void walk(Node<T> node, Consumer<Node<T>> callback) {
    Node<T> x = node;

    do {
      callback.accept(x);

      if (x.child != null)
        walk(x.child, callback);

    } while ((x = x.right) != node);
  }

  /**
   * Recursively traverses the heap, iterating through each {@code CircularLinkedList}
   * of each degree, first going into each child before finishing the list.
   *
   * @return the heap string in an array format
   */
  public String toString() {
    if (isEmpty())
      return "[]";

    StringBuilder sb = new StringBuilder("[");

    walk(min, (node) -> sb.append(node.item + ", "));

    return sb.delete(sb.length() - 2, sb.length()) + "]";
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element).
   * 
   * <p>
   * Uses a {@link LinkedList} to hold the elements as it recursively travels the
   * heap and inserts them at the end so when it calls the
   * {@link LinkedList#toArray()} method, it will list it in proper order.
   * </p>
   * 
   * <p>
   * The returned array will be "safe" in that no references to it are maintained
   * by this list. (In other words, this method must allocate a new array). The
   * caller is thus free to modify the returned array.
   * </p>
   *
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   * </p>
   *
   * @return an array containing all of the elements in this list in proper
   *         sequence
   */
  public Object[] toArray() {
    if (isEmpty())
      return new Object[0];
    
    LinkedList<T> list = new LinkedList<>();

    walk(min, (node) -> list.insertLast(node.item));

    return list.toArray();
  }

  /**
   * Saves the state of this {@code LinkedList} instance to a stream (that is,
   * serializes it).
   * 
   * @param stream the {@link java.io.ObjectOutputStream} to write to
   * 
   * @throws java.io.IOException if serialization fails
   *
   * @serialData The size of the list (the number of elements it contains) is
   *             emitted (int), followed by all of its elements (each an Object)
   *             in the proper order.
   */
  @java.io.Serial
  private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
    // Write out any hidden serialization magic
    stream.defaultWriteObject();

    // Write out size
    stream.writeInt(size);

    // Write out all elements. Iteration is required because the lambda block for
    // recursive walk won't delegate the IOException
    for (T item : iterable())
      stream.writeObject(item);
  }

  /**
   * Reconstitutes this {@code FibonacciHeap} instance from a stream (that is,
   * deserializes it).
   */
  @java.io.Serial
  @SuppressWarnings("unchecked")
  private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
    // Read in any hidden serialization magic
    stream.defaultReadObject();

    // Read in size
    int size = stream.readInt();

    // Read in all elements
    for (int i = 0; i < size; i++)
      insert((T) stream.readObject());
  }

  /**
   * Returns an {@link Iterable} of the elements in the FibHeap
   *
   * @return the {@code Iterable}
   */
  public Iterable<T> iterable() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * Returns an {@link Iterator} of the elements in the FibHeap
   *
   * @return the {@code Iterator}
   */
  public Iterator<T> iterator() {
    if (isEmpty())
      return new EmptyIterator<>();
    return new Itr();
  }

  /**
   * A FibHeap iterator class. This class implements the {@link Iterator} and
   * {@link Iterable} interfaces. It uses a {@link Queue} to walk the heap and
   * hold all the elements because of the CircularLinkedList internal structure,
   * it would be difficult to directly iterate through the nodes.
   *
   * <p>
   * Will throw a {@link ConcurrentModificationException} if the FibHeap was
   * modified outside of the iterator.
   * </p>
   */
  private class Itr implements Iterator<T>, Iterable<T> {
    /**
     * The {@link Queue} containing all the elements of the FibHeap.
     */
    Queue<Node<T>> entries;

    /**
     * The last returned {@link Node} of the iterator.
     */
    Node<T> last;

    /**
     * The expected value of modCount when instantiating the iterator. If this
     * expectation is violated, the iterator has detected concurrent modification.
     */
    int expectedModCount = modCount;

    /**
     * Constructs the iterator with the queue and performs a walk of all the nodes
     * in the heap and adds them to the queue.
     */
    public Itr() {
      entries = new Queue<>(size);
      walk(min, (Node<T> node) -> entries.enqueue(node));
    }

    public Iterator<T> iterator() {
      return this;
    }

    public boolean hasNext() {
      return !entries.isEmpty();
    }

    /**
     * Returns the next element, if it has one to provide.
     *
     * @return the next element
     * @throws ConcurrentModificationException if the list was modified during
     *                                         computation.
     * @throws NoSuchElementException          if no more elements exist
     */
    public T next() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!hasNext())
        throw new NoSuchElementException("FibonacciHeap Iterator");
      last = entries.dequeue();
      return last.item;
    }

    /**
     * {@inheritDoc}
     *
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     *
     * @throws IllegalStateException if the {@code next} method has not yet been
     *         called, or the {@code remove} method has already been called after
     *         the last call to the {@code next} method.
     *
     * @throws ConcurrentModificationException if a function modified this map
     *         during computation.
     */
    @Override
    public void remove() {
      if (last == null)
        throw new IllegalStateException("FibonacciHeap Iterator. No last item.");
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();

      // Synchronized block to lock the FibHeap object while removing entry
      synchronized (FibonacciHeap.this) {
        // Remove the last returned node to from the FibHeap
        Node<T> y = last.parent;

        // If the node has a parent, cut it and place in root list
        if (y != null) {
          cut(last, y);
          cascadingCut(y);
        }

        // Set the node as the minimum and extract it to remove it
        min = last;
        extractMin();

        last = null;
      }
    }
  }
}