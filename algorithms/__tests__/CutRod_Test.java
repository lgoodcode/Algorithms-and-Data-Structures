package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import algorithms.dynamic.CutRod;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CutRod_Test {
  int[] prices =  { 1, 5, 8, 9, 10, 17, 17, 20, 24, 30 };
  int[] sizes = { 1, 2, 3, 4,  5,  6,  7,  8,  9, 10 };
  int[] results = { 0, 1, 5, 8, 10, 13, 17, 18, 22, 25, 30 };


  @Test
  void brute() {
    assertEquals(10, CutRod.brute(prices, 4));
  }

  @Test
  void bottomUp() {
    assertEquals(10, CutRod.bottomUp(prices, 4));
  }

  @Test
  void extendedBottomUp_table() {
    assertArrayEquals(results, CutRod.bottomUpTable(prices));
  }

  @Test
  void bottomUp_recursive() {
    assertEquals(10, CutRod.bottomUpRecursive(prices, 4));
  }

}
