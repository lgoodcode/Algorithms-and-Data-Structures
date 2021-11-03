package algorithms.greedy;

import java.util.Arrays;

public interface CoinChangeG {
  public static Integer[] run(Integer[] coins, Integer amount) {
    Integer[] change = new Integer[30];
    int i, j, coin, total = 0;

    for (i = coins.length - 1, j = 0; i >= 0; i--) {
      coin = coins[i];

      while (total + coin <= amount) {
        change[j++] = coin;
        total += coin;
      }
    }

    return Arrays.copyOf(change, j);
  }
}
