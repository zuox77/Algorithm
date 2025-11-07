package BFS;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/*
刷题次数: 1

https://leetcode.cn/problems/shortest-bridge/description/

You are given an n x n binary matrix grid where 1 represents land and 0 represents water.
An island is a 4-directionally connected group of 1's not connected to any other 1's. There are exactly two islands in grid.
You may change 0's to 1's to connect the two islands to form one island.
Return the smallest number of 0's you must flip to connect the two islands.

Example 1:
Input: grid = [[0,1],[1,0]]
Output: 1

Example 2:
Input: grid = [[0,1,0],[0,0,0],[0,0,1]]
Output: 2

Example 3:
Input: grid = [[1,1,1,1,1],[1,0,0,0,1],[1,0,1,0,1],[1,0,0,0,1],[1,1,1,1,1]]
Output: 1

思路: DFS + BFS

 */
public class ShortestBridge {
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static final Deque<int[]> queue = new ArrayDeque<>();

    public int shortestBridge(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        // Use DFS to find the first Island and mark it as -1
        findFirstIsland(grid, n, m);
        // BFS
        int layer = 0;
        Set<Integer> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            // Track layer by layer
            int curSize = queue.size();
            for (int i = 0; i < curSize; i++) {
                int[] location = queue.pollLast();
                for (int[] direction: DIRECTIONS) {
                    int newX = location[0] + direction[0];
                    int newY = location[1] + direction[1];
                    // Check validation
                    if (!edgeCheck(n, m, newX, newY) || grid[newX][newY] == 2 || visited.contains(newX * m + newY)) continue;
                    // If find second island
                    if (grid[newX][newY] == 1) {
                        // The first to find the second island is always the shortest
                        return layer;
                    }
                    // Otherwise, push to queue
                    queue.offerFirst(new int[]{newX, newY});
                    visited.add(newX * m + newY);
                }
            }
            // Update layer
            layer++;
        }
        return -1;
    }

    private void findFirstIsland(int[][] grid, int n, int m) {
        // Use DFS to find the first Island and mark it as -1
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    dfs(grid, n, m, i, j);
                    return;
                }
            }
        }
    }

    private void dfs(int[][] grid, int n, int m, int x, int y) {
        // Exit
        if (grid[x][y] == 0) {
            return;
        }
        // mark
        if (grid[x][y] == 1) {
            // Add all locations of first island to queue
            queue.offerFirst(new int[]{x, y});
            grid[x][y] = 2;
        }
        // Iterate 4 directions
        for (int[] direction: DIRECTIONS) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (!edgeCheck(n, m, newX, newY) || grid[newX][newY] == 2) continue;
            // move deeper
            dfs(grid, n, m, newX, newY);
        }
    }

    private boolean edgeCheck(int n, int m, int x, int y) {
        if (0 <= x && x < n && 0 <= y && y < m) {
            return true;
        }
        return false;
    }
}
