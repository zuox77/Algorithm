package Array.SpiralMatrix;

/*
https://leetcode.cn/problems/spiral-matrix-ii/description/

Given a positive integer n, generate an n x n matrix filled with elements from 1 to n2 in spiral order.

Example 1:
Input: n = 3
Output: [[1,2,3],[8,9,4],[7,6,5]]

Example 2:
Input: n = 1
Output: [[1]]

思路:
 */

public class SpiralMatrix2 {
    public int[][] spiralMatrix(int n) {
        // 创建矩阵
        int[][] matrix = new int[n][n];
        // 创建方向矩阵
        // directions[0]：向右
        // directions[1]：向下
        // directions[2]：向左
        // directions[3]：向上
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        // 双指针记录坐标
        int row = 0;
        int col = 0;
        // 一个变量记录方向
        int dirFlag = 0;
        // 从1到n^2开始填数
        for (int i = 1; i <= n * n; i++) {
            // 填数字
            matrix[row][col] = i;
            // 计算下一个坐标
            int newRow = row + directions[dirFlag][0];
            int newCol = col + directions[dirFlag][1];
            // 检查是否要换方向
            // 是否出边界 + 是否已经填入数字（默认是0，所以如果不是0，就是已经填入数字）
            if (newRow < 0 || newCol < 0 || newRow >= n || newCol >= n || matrix[newRow][newCol] != 0) {
                dirFlag = (dirFlag + 1) % 4;
            }
            // 重新计算下一个坐标
            row += directions[dirFlag][0];
            col += directions[dirFlag][1];
        }
        return matrix;
    }
}
