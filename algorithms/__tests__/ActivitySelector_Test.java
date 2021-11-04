package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.*;

import algorithms.greedy.ActivitySelector;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ActivitySelector_Test {
  int[] start =  { 1, 3, 0, 5, 3, 5,  6,  8,  8,  2, 12 };
  int[] finish = { 4, 5, 6, 7, 9, 9, 10, 11, 12, 14, 16 };
  int[] expected = { 3, 7, 10 };

  @Test
  void activities() {
    assertArrayEquals(expected, ActivitySelector.run(start, finish, 0));
  }
}
