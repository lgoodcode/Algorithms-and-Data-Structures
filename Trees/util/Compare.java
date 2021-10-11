package Trees.util;

/**
 * A functional interface to compare two objects.
 */
@FunctionalInterface
public interface Compare<T> {
  abstract public boolean compare(T x, T y);
}
