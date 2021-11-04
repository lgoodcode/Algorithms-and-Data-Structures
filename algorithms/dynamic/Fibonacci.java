package algorithms.dynamic;

public interface Fibonacci {
  public static int calc(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;

    return Fibonacci.calc(n - 2) + Fibonacci.calc(n - 1);
  }
}