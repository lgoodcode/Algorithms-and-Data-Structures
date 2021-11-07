package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import algorithms.Shuffle;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Shuffle_Test {
  Integer[] arr = { 1, 2, 3, 4, 5 };

  @Test 
  void shuffle() {
    Integer[] copy = { 1, 2, 3, 4, 5 };
    Shuffle.apply(arr);
    assertFalse(Arrays.equals(arr, copy));
  }

  @Test 
  void shuffle_copy() {
    assertFalse(Arrays.equals(arr, Shuffle.copy(arr)));
  }

}