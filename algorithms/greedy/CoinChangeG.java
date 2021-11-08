package algorithms.greedy;

import static java.util.Arrays.copyOf;;

/**
 * Greedily calculates the minimum number of coins for change for a given
 * amount.
 */
public interface CoinChangeG {
  /**
   * Greedily calculates the the change with the given coin denomination to derive
   * the minimum number of coins possible. Starts by reducing the change amount
   * with the largest coin possible then followed by subsequent coins.
   *
   * <p>
   * This can result in a failed attempt if there is no coin for {@code 1} and the
   * remaining amount is an odd number, i.e., {@code 3} with a smallest coin value
   * of {@code 2}.
   * </p>
   *
   * @param coins  the denominations of coins
   * @param amount the amount to make change for
   * @return the coins to make the change
   */
  public static int[] run(int[] coins, int amount) {
    int[] change = new int[30];
    int i, j, coin, total = 0;

    for (i = coins.length - 1, j = 0; i >= 0; i--) {
      coin = coins[i];

      while (total + coin <= amount) {
        change[j++] = coin;
        total += coin;
      }
    }

    return copyOf(change, j);
  }
}
