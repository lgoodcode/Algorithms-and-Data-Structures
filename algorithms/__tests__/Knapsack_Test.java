package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import algorithms.greedy.FractionalKnapsack;
import algorithms.dynamic.Knapsack;
import algorithms.dynamic.KnapsackMultiple;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Knapsack_Test {
  int[] weights = { 2, 3, 4 };
  int[] values = { 3, 4, 5 };

  int[] prices =  { 1, 5, 8, 9, 10, 17, 17, 20, 24, 30 };
  int[] sizes = { 1, 2, 3, 4,  5,  6,  7,  8,  9, 10 };

  @Test
  void fractionalKnapsack_total() {
    assertEquals(7, FractionalKnapsack.total(5, weights, values));
    assertEquals(8, FractionalKnapsack.total(6, weights, values));
    assertEquals(9, FractionalKnapsack.total(7, weights, values));
  }

  @Test
  void fractionalKnapsack_items() {
    int[] items = { 0, 1 };
    assertArrayEquals(items, FractionalKnapsack.items(4, weights, values));
  }

  @Test
  void Knapsack_total() {
    assertEquals(5, Knapsack.total(4, weights, values));
    assertEquals(8, Knapsack.total(6, weights, values));
    assertEquals(9, Knapsack.total(7, weights, values));
    assertEquals(9, Knapsack.total(8, weights, values));
    assertEquals(12, Knapsack.total(9, weights, values));
  }

  @Test
  void Knapsack_total_rods() {
    assertEquals(5, KnapsackMultiple.total(2, sizes, prices));
    assertEquals(8, KnapsackMultiple.total(3, sizes, prices));
    assertEquals(10, KnapsackMultiple.total(4, sizes, prices));
    assertEquals(13, KnapsackMultiple.total(5, sizes, prices));
    assertEquals(17, KnapsackMultiple.total(6, sizes, prices));
    assertEquals(18, KnapsackMultiple.total(7, sizes, prices));
    assertEquals(22, KnapsackMultiple.total(8, sizes, prices));
  }

  @Test
  void Knapsack_items() {
    int[] items = { 2, 1 };
    int[] items2 = { 2, 0 };
    int[] items3 = { 2 };

    assertArrayEquals(items, Knapsack.items(7, weights, values));
    assertArrayEquals(items2, Knapsack.items(6, weights, values));
    assertArrayEquals(items3, Knapsack.items(4, weights, values));
  }
}
