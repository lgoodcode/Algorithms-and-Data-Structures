package Trees.util;

/**
 * A functional interface to trigger a callback function when traversing the
 * tree.
 */
@FunctionalInterface
public interface Callback<T> {
  void action(T x);
}