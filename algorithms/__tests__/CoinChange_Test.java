package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.*;

import algorithms.greedy.CoinChangeG;
import algorithms.dynamic.CoinChangeDP;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CoinChange_Test {
  Integer[] US = { 1, 5, 10, 25 };
  Integer[] greedyChangeFor43 = { 25, 10, 5, 1, 1, 1 };
  Integer[] DPChangeFor43 = { 1, 1, 1, 5, 10, 25 };
  Integer[] DPChangeFor74 = { 1, 1, 1, 1, 10, 10, 25, 25 };

  @Test 
  void CoinChangeG() {
    assertArrayEquals(greedyChangeFor43, CoinChangeG.run(US, 43));
  }

  @Test 
  void CoinChangeDP() {
    assertArrayEquals(DPChangeFor43, CoinChangeDP.run(US, 43));
    assertArrayEquals(DPChangeFor74, CoinChangeDP.run(US, 74));
  }
}
