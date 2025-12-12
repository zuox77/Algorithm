package BFSAndDFS;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

/*

https://leetcode.cn/problems/minimum-number-of-flips-to-convert-binary-matrix-to-zero-matrix/description/

Given a m x n binary matrix mat. In one step, you can choose one cell and flip it and all the four neighbors of it if they exist (Flip is changing 1 to 0 and 0 to 1). A pair of cells are called neighbors if they share one edge.
Return the minimum number of steps required to convert mat to a zero matrix or -1 if you cannot.
A binary matrix is a matrix with all cells equal to 0 or 1 only.
A zero matrix is a matrix with all cells equal to 0.

Example 1:
Input: mat = [[0,0],[0,1]]
Output: 3
Explanation: One possible solution is to flip (1, 0) then (0, 1) and finally (1, 1) as shown.

Example 2:
Input: mat = [[0]]
Output: 0
Explanation: Given matrix is a zero matrix. We do not need to change it.

Example 3:
Input: mat = [[1,0,0],[1,0,0]]
Output: -1
Explanation: Given matrix cannot be a zero matrix.

 */

public class FlipBoard {
    private int[][] directions =
            new int[][] {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public int solution(int[][] matrix) {
        HashSet<int[]> visited = new HashSet<>();
        Deque<int[]> queue = new LinkedList<>();

        int count = 0;
        int n = matrix.length;
        int m = matrix[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // not 1
                if (matrix[i][j] != 1) {
                    continue;
                }
                queue.addFirst(new int[] {i, j});
                // add click number
                count++;
                while (!queue.isEmpty()) {
                    int[] coordinate = queue.pollLast();
                    // flip
                    matrix[coordinate[0]][coordinate[1]] = 0;
                    // find next 1s
                    for (int[] direction : directions) {
                        int new_i = coordinate[0] + direction[0];
                        int new_j = coordinate[1] + direction[1];
                        if (validator(new_i, new_j, n, m, matrix)) {
                            queue.addFirst(new int[] {new_i, new_j});
                        }
                    }
                }
            }
        }
        return count;
    }

    public boolean validator(int i, int j, int n, int m, int[][] matrix) {
        if (0 <= i && i < n && 0 <= j && j < m && matrix[i][j] == 1) {
            return true;
        }
        return false;
    }
}
