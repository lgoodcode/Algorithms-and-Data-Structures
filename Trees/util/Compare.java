package Trees.util;

/**
 * A functional interface to compare two objects.
 */
@FunctionalInterface
public interface Compare<T> {
  abstract public boolean isLessThan(T x, T y);
}
