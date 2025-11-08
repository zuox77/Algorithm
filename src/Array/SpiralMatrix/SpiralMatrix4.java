package Array.SpiralMatrix;

/*
https://leetcode.cn/problems/spiral-matrix-iv/

You are given two integers m and n, which represent the dimensions of a matrix.
You are also given the head of a linked list of integers.
Generate an m x n matrix that contains the integers in the linked list presented in spiral order (clockwise),
starting from the top-left of the matrix. If there are remaining empty spaces, fill them with -1.
Return the generated matrix.

Example 1:
Input: m = 3, n = 5, head = [3,0,2,6,8,1,7,9,4,2,5,5,0]
Output: [[3,0,2,6,8],[5,0,-1,-1,1],[5,2,4,9,7]]
Explanation: The diagram above shows how the values are printed in the matrix.
Note that the remaining spaces in the matrix are filled with -1.

Example 2:
Input: m = 1, n = 4, head = [0,1,2]
Output: [[0,1,2,-1]]
Explanation: The diagram above shows how the values are printed from left to right in the matrix.
The last space in the matrix is set to -1.

思路:
 */

import LinkedList.ListNode;

import java.util.Arrays;

public class SpiralMatrix4 {
    public int[][] spiralMatrix(int m, int n, ListNode head) {
        // 创建矩阵
        int[][] matrix = new int[m][n];
        // 初始化
        for (int i = 0; i < m; i++) {
            Arrays.fill(matrix[i], -1);
        }
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
        // 遍历链表
        while (head != null) {
            matrix[row][col] = head.val;
            head = head.next;
            // 计算下一个位置
            int newRow = row + directions[dirFlag][0];
            int newCol = col + directions[dirFlag][1];
            // 检查是否出界或者已经填过
            if (0 > newRow
                    || 0 > newCol
                    || newRow >= m
                    || newCol >= n
                    || matrix[newRow][newCol] != -1) {
                dirFlag = (dirFlag + 1) % 4;
            }
            // 重新计算一下一个位置
            row += directions[dirFlag][0];
            col += directions[dirFlag][1];
        }
        return matrix;
    }
}
