package BFS;

/*
https://leetcode.cn/problems/rotting-oranges/?envType=study-plan-v2&envId=top-100-liked
You are given an m x n grid where each cell can have one of three values:

0 representing an empty cell,
1 representing a fresh orange, or
2 representing a rotten orange.
Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten.

Return the minimum number of minutes that must elapse until no cell has a fresh orange.
If this is impossible, return -1.

Example 1:
Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
Output: 4
Example 2:

Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
Output: -1
Explanation: The orange in the bottom left corner (row 2, column 0) is never rotten, because rotting only happens 4-directionally.
Example 3:

Input: grid = [[0,2]]
Output: 0
Explanation: Since there are already no fresh oranges at minute 0, the answer is just 0.

思路1: BFS
1. 思路看BFS.md
2. 这道题是变形题，有三个状态：当前橘子正在腐烂且未腐烂完，当前橘子全部腐烂完，当前橘子全部腐烂完但仍有新鲜橘子。为了考虑这个情况，需要：
  1. 在初始状态计算新鲜橘子的数量，在最后的时候，可以通过剩余新鲜橘子的数量判断是否有无法被腐烂的橘子
  2. 在初始状态可以将所有腐烂的橘子放入queue中（记住：BFS不一定非要通过for loop开启第一个点，也可以先遍历把第一层所有的点都放入queue）
  3. 需要注意，只有腐烂的橘子附近的四个橘子会被腐烂，所以需要计算每一次queue的当前长度，用for loop，这样可以保证每次都只遍历初始的腐烂的橘子
  然后只对这些橘子周围四个橘子进行状态改变
 */

import java.util.ArrayDeque;
import java.util.Queue;

public class RottingOranges {
  private static final int[][] DIRECTIONS = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};

  public int orangesRotting(int[][] grid) {
    int time = 0;
    int freshOrange = 0;
    Queue<int[]> queue = new ArrayDeque<>();

    // Count initial fresh oranges and queue rot oranges
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == 1) {
          freshOrange++;
        } else if (grid[i][j] == 2) {
          queue.offer(new int[] {i, j});
        }
      }
    }

    while (freshOrange > 0 && !queue.isEmpty()) {
      time++;
      int currentSize = queue.size();
      // 这里需要通过计算每一层有多少个来限制腐烂橘子的个数，只对当前腐烂橘子周围四个橘子进行处理
      for (int i = 0; i < currentSize; i++) {
        int[] current = queue.poll();
        for (int[] direction : DIRECTIONS) {
          int row = current[0] + direction[0];
          int column = current[1] + direction[1];
          // Filter out invalid index
          if (row < 0
              || column < 0
              || row >= grid.length
              || column >= grid[0].length
              || grid[row][column] != 1) {
            continue;
          }
          freshOrange--;
          grid[row][column] = 2;
          queue.offer(new int[] {row, column});
        }
      }
    }

    return freshOrange > 0 ? -1 : time;
  }
}
