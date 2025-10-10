package BFS;
/*
https://leetcode.cn/problems/number-of-islands/description/
Given an m x n 2D binary grid which represents a map of '1's (land) and '0's (water), return the number of islands.
An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
You may assume all four edges of the grid are all surrounded by water.

Input: grid = [
  ["1","1","0","0","0"],
  ["1","1","0","0","0"],
  ["0","0","1","0","0"],
  ["0","0","0","1","1"]
]
Output: 3

思路1: BFS
1. 思路看BFS.md
2. 需要注意：对于矩阵类的题目，可以使用一个static class，例如Node。这是因为可以改变矩阵的值
 */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class NumberOfIslands {

    static class Node {
        int row;
        int column;

        Node(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    List<Node> directions = Arrays.asList(
            new Node(0, -1),
            new Node(0, 1),
            new Node(1, 0),
            new Node(-1, 0)
    );

    public int numIslands(char[][] grid) {
        int count = 0;
        Queue<Node> queue = new ArrayDeque<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // If not 1 or visited
                if (grid[i][j] != '1') {
                    continue;
                }
                // Add to queue
                Node root = new Node(i, j);
                queue.offer(root);
                // Marked as visited
                grid[i][j] = '2';
                // Loop
                while (!queue.isEmpty()) {
                    // Pop out first
                    Node node = queue.poll();
                    // Check surroundings
                    checkSurroundings(node, grid, queue);
                }
                count += 1;
            }
        }

        return count;
    }

    public void checkSurroundings(Node node, char[][] grid, Queue queue) {
        for (Node direction : directions) {
            int row = node.row + direction.row;
            int column = node.column + direction.column;
            // Check validation
            if (row < 0 || column < 0 || row >= grid.length || column >= grid[0].length || grid[row][column] != '1') {
                continue;
            }
            // Add to queue
            queue.offer(new Node(row, column));
            // Marked as visited
            grid[row][column] = '2';
        }
    }
}
