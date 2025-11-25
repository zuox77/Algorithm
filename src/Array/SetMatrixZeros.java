package Array;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/set-matrix-zeroes/description/?envType=study-plan-v2&envId=top-100-liked

Given an m x n integer matrix, if an element is 0, set its entire row and column to 0's.

You must do it in place.

Example 1:
Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
Output: [[1,0,1],[0,0,0],[1,0,1]]

Example 2:
Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]

思路:
1. 为了节省空间，用原数组的第一行和第一列分别表示其余的列和行是否存在0，然后用两个变量保存第一行和第一列是否存在0
2. 遍历第一行和第一列找到是否存在0
3. 遍历其余的行和列，找到是否存在0，并保存到第一行和第一列
4. 通过原数组第一行和第一列对其余行和列设置0
5. 通过两个变量对第一行和第一列设置0
 */

public class SetMatrixZeros {
    public List<Integer> setMatrixZeros1(int[][] matrix) {
        // 声明变量
        List<Integer> result = new ArrayList<>();
        // 遍历
        while (matrix.length > 0) {
            // 把第一层全部加入result
            for (int num : matrix[0]) {
                result.add(num);
            }
            // matrix逆时针旋转90度
            matrix = spinMatrix(matrix);
        }

        return result;
    }

    public int[][] spinMatrix(int[][] matrix) {
        // 定义新的矩阵长和宽
        int row = matrix[0].length;
        int col = matrix.length - 1; // 因为已经把第一层加进result了, 所以不需要了
        // 因为每次旋转90度, 所以这里长和宽要交换一下
        int[][] matrixNew = new int[row][col]; // 不是 int[col][row] => int[有几行][有几列]
        // 将数字一一放到新矩阵里面
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrixNew[i][j] = matrix[j + 1][row - i - 1];
            }
        }
        return matrixNew;
    }

    public void setMatrixZeros2(int[][] matrix) {
        // 用两个变量保存第一行和第一列是否存在0
        boolean firstRowZero = false;
        boolean firstColZero = false;
        // 遍历查找第一行是否为0
        for (int num : matrix[0]) {
            if (num == 0) {
                firstRowZero = true;
                break;
            }
        }
        // 遍历查找第一列是否为0
        for (int[] nums : matrix) {
            if (nums[0] == 0) {
                firstColZero = true;
                break;
            }
        }

        // 用第一行和第一列去记录每一行和每一列是否有0
        // 不用考虑第一行和第一列，因为已经计算过
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        // 先把除了第一行和第一列以外的设置为0
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // 设置第一行
        if (firstRowZero) {
            for (int i = 0; i < matrix[0].length; i++) {
                matrix[0][i] = 0;
            }
        }
        // 设置第一列
        if (firstColZero) {
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }
}
