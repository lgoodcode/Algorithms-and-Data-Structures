package data_structures;

import java.util.Objects;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * This class creates an empty Iterator that has no elements and implements the
 * {@code Iterable}, {@code Iterator}, interfaces.
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
public final class EmptyIterator<T> implements Iterator<T>, Iterable<T> {
  static final EmptyIterator<?> EMPTY_ITERATOR = new EmptyIterator<>();

  public boolean hasNext() {
    return false;
  }

  public T next() {
    throw new NoSuchElementException();
  }

  public void remove() {
    throw new IllegalStateException();
  }

  public Iterator<T> iterator() {
    return this;
  }

  @Override
  public void forEachRemaining(Consumer<? super T> action) {
    Objects.requireNonNull(action);
  }
}
