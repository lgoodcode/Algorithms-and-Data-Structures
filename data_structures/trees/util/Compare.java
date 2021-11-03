package data_structures.trees.util;

/**
 * A functional interface to compare two objects.
 */
@FunctionalInterface
public interface Compare<T> {
  boolean compare(T x, T y);
}
