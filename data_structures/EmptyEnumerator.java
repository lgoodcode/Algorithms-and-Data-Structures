package data_structures;

import java.util.Objects;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * This class creates an empty Enumerator that has no elements and implements
 * the {@code Iterable}, {@code Iterator}, {@code Enumeration} interfaces so
 * that it can be used for all types.
 *
 * <ul>
 * <li>{@link Iterator#hasNext} always returns {@code false}.</li>
 * <li>{@link Iterator#next} always throws {@link NoSuchElementException}.</li>
 * </ul>
 *
 * <p>
 * Implementations of this method are permitted, but not required, to return the
 * same object from multiple invocations.
 * </p>
 *
 * @param <T> the class of the objects in the iterable
 */
public final class EmptyEnumerator<T> implements Enumeration<T>, Iterator<T>, Iterable<T> {
  // TODO: need to disable the warning here for unused variable
  // static final EmptyIterable<?> EMPTY_ITERABLE = new EmptyIterable<>();

  public EmptyEnumerator() {}

  // Enumeration methods
  public boolean hasMoreElements() {
    return false;
  }

  public T nextElement() {
    throw new NoSuchElementException();
  }

  // Iterator methods
  public boolean hasNext() {
    return false;
  }

  public T next() {
    throw new NoSuchElementException();
  }

  public void remove() {
    throw new IllegalStateException();
  }

  // Iterable method
  public Iterator<T> iterator() {
    return this;
  }

  @Override
  public void forEachRemaining(Consumer<? super T> action) {
    Objects.requireNonNull(action);
  }
}
