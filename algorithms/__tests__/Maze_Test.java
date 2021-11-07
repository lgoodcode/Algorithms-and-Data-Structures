package algorithms.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.*;

import algorithms.backtracking.Maze;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Maze_Test {
  int[][] maze = {
    { 1, 0, 1, 0 },
    { 1, 1, 1, 1 },
    { 0, 0, 1, 0 },
    { 0, 0, 1, 1 }
  };

  int[][] solution = {
    { 1, 0, 0, 0 },
    { 1, 1, 1, 0 },
    { 0, 0, 1, 0 },
    { 0, 0, 1, 1 }
  };

  int[][] maze2 = {
    { 1, 0, 1, 1, 0 },
    { 1, 1, 0, 1, 1 },
    { 1, 0, 0, 0, 0 },
    { 1, 1, 1, 1, 1 }
  };

  int[][] solution2 = {
    { 1, 0, 0, 0, 0 },
    { 1, 0, 0, 0, 0 },
    { 1, 0, 0, 0, 0 },
    { 1, 1, 1, 1, 1 }
  };

  @Test
  void solution() {
    assertArrayEquals(solution, Maze.solve(maze));
  }

  @Test
  void n_m_solution() {
    assertArrayEquals(solution2, Maze.solve(maze2));
  }

  @Test
  void reverse() {
    assertArrayEquals(solution, Maze.solve(maze, 3, 3, 0, 0));
  }

  @Test
  void invalid_positions() {
    assertThrows(IllegalArgumentException.class, () -> Maze.solve(maze, 1, 4, 1, 1));
  }
}
