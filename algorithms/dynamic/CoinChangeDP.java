package algorithms.dynamic;

import static java.util.Arrays.copyOf;

/**
 * Calculates the minimum possible coins for change for a given amount.
 *
 * <p>
 * Top-down approach memoized to save calculated values when finding optimal
 * solution.
 * </p>
 *
 * <p>
 * This version is improved by using static variables to retain computed values.
 * If the amount is less than the current memo values, it can use them,
 * otherwise, it extends the length of the array.
 * </p>
 */
public final class CoinChangeDP {
  private static int[][] memo;

  /**
   * Calculates the the change with the given coin denomination to derive the
   * minimum number of coins possible.
   *
   * @param coins  the denominations of coins
   * @param amount the amount to make change for
   * @return the coins to make the change
   */
  public static int[] run(int[] coins, int amount) {
    if (memo == null) {
      memo = new int[amount + 1][30];
      memo[0] = new int[1];
    }
    else if (amount > memo.length) {
      int oldLen = memo.length, newLen = amount + 1;
      memo = copyOf(memo, newLen);

      for (int i = oldLen; i < newLen; i++)
        memo[i] = new int[30];
    }

    return makeChange(coins, amount);
  }

  private static int[] makeChange(int[] coins, int amount) {
    if (amount == 0)
      return memo[0];
    if (memo[amount][0] != 0)
      return memo[amount];

    int[] min = new int[30], newMin = new int[30];
    int i, j, coin, newAmount;

    for (i = 0, j = 0; i < coins.length; i++, j = 0) {
      coin = coins[i];
      newAmount = amount - coin;

      if (newAmount >= 0) {
        newMin = makeChange(coins, newAmount);

        while (j < newMin.length && newMin[j] != 0)
          j++;

        min = copyOf(newMin, j + 1);
        min[j] = coin;
      }
    }

    return (memo[amount] = min);
  }
}

/**
 * Original
 */
// const coinChange = (coins, amount) => {
//   const memo = [];

//   const makeChange = value => {
//     if (!value)
//       return [];
//     if (memo[value])
//       return memo[value];

//     let min = [];
//     let newMin, newAmount;
//     for (let i=0; i < coins.length; i++) {
//       const coin = coins[i];
//       newAmount = value - coin;
//       if (newAmount >= 0)
//         newMin = makeChange(newAmount);
//       if (newAmount >= 0 && (newMin.length < min.length - 1 || !min.length)) // this is where we use the least amount of coins
//         min = [coin].concat(newMin); // new min amount of coins for amount
//     }
//     return (memo[value] = min);
//   };
//   return makeChange(amount);
// };