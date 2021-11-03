package algorithms.dynamic;

import java.util.Arrays;

/**
 * Top-down approach memoized to save calculated values when finding optimal
 * solution
 *
 * This version is improved by using static variables to retain computed values.
 * If the amount is less than the current memo values, it can use them, otherwise,
 * it extends the length of the array.
 */
public final class CoinChangeDP {
  private static Integer[][] memo;

  public static Integer[] run(Integer[] coins, Integer amount) {
    if (memo == null) {
      memo = new Integer[amount + 1][30];
      memo[0] = new Integer[1];
    }
    else if (amount > memo.length) {
      int oldLen = memo.length, newLen = amount + 1;
      memo = Arrays.copyOf(memo, newLen);

      for (int i = oldLen; i < newLen; i++)
        memo[i] = new Integer[30];
    }

    return makeChange(coins, amount);
  }

  private static Integer[] makeChange(Integer[] coins, Integer amount) {
    if (amount == 0)
      return memo[0];
    if (memo[amount][0] != null)
      return memo[amount];

    Integer[] min = new Integer[30], newMin = new Integer[30];
    int i, j, coin, newAmount;

    for (i = 0, j = 0; i < coins.length; i++, j = 0) {
      coin = coins[i];
      newAmount = amount - coin;

      if (newAmount >= 0) {
        newMin = makeChange(coins, newAmount);

        while (j < newMin.length && newMin[j] != null)
          j++;

        min = Arrays.copyOf(newMin, j + 1);
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