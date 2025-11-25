package Array.SpiralMatrix;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/spiral-matrix/description/

Given an m x n matrix, return all elements of the matrix in spiral order.

Input: matrix = [
[ 1, 2, 3, 4],
[ 5, 6, 7, 8],
[ 9,10,11,12]
]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]

思路: 旋转矩阵
1. 这个题没有什么技巧, 就是简单直接的按照顺序输出结果, 但如果我们用各种指针去判断循环的话, 就会很麻烦,
   因为有四个方向, 所以要判断什么时候转向, 转向以后是哪个坐标点改变, 还要判断边界条件, 边界条件也不断在换, 所以比较麻烦
2. 但转化一下思路, 其实可以每次逆时针90度旋转matrix自己, 如果这样的话, 那我们每次都把matrix的第一行加入result即可, 例如
   [ 1, 2, 3, 4]
   [ 5, 6, 7, 8]
   [ 9,10,11,12]

   将第一层加入result, 此时result = [1,2,3,4]
   逆时针90度旋转matrix
   [ 8, 12]
   [ 7, 11]
   [ 6, 10]
   [ 5, 9]

   将第一层加入result, 此时result = [1,2,3,4,8,12]
   逆时针90度旋转matrix
   [11,10, 9]
   [ 7, 6, 5]

   将第一层加入result, 此时result = [1,2,3,4,8,12,11,10,9]
   逆时针90度旋转matrix
   [5]
   [6]
   [7]
   ...
   以此类推
3. 最主要的就是, 如何旋转matrix
   一上面的例子, 第一次的时候, 把第一层加入result, 所以
   现在matrix是
   [ 1, 2, 3, 4]
   [ 5, 6, 7, 8]
   [ 9,10,11,12]
   我们想要的是
   [ 8, 12]
   [ 7, 11]
   [ 6, 10]
   [ 5, 9]
   以新旧矩阵的坐标来一一对比找到规律:
   对于8: 新坐标 ( 0, 0), 原坐标(1, 3),
   对于12: 新坐标 ( 0, 1), 原坐标(2, 3)
   对于7: 新坐标 ( 1, 0), 原坐标(1, 2)
   对于11: 新坐标 ( 1, 1), 原坐标(2, 2)
   对于6: 新坐标 ( 2, 0), 原坐标(1, 1)
   对于10: 新坐标 ( 2, 0), 原坐标(1, 1)
   对于5: 新坐标 ( 3, 0), 原坐标(1, 0)
   对于9: 新坐标 ( 3, 1), 原坐标(2, 0)
   对于任意数: 新坐标 ( i, j), 原坐标(j + 1, matrix[0].length - i - 1)
   公式: newMatrix[i][j] = oldMatrix[j + 1][matrix[0].length - i - 1]
   j + 1: 是因为每次我们都去除了第一行, 所以运行for循环的时候, 要从1开始

思路：通过flag去控制方向
1. 建立一个int[][]代表方向，需要保证该数组是右下左上的顺序
2. 通过一个flag去控制第一步的方向究竟选哪一个
3. 需要先计算出下一次的坐标，通过坐标判断：
    1. 是否出界
    2. 是否visited
    如果出界或者visited，说明需要换方向，所以直接flag = (flag + 1) % 4
4. 更新坐标
5. 如果不想改变matrix来标记visited，那么可以计算每次每个方向的次数，精确控制
    以上面的例子1举例
    1. 第一次往右，需要走四次才换方向，即n次，
    2. 第一次往下，需要两次，即m = 3，m - 1 = 2
    3. 第一次往左，需要三次，即n - 1次
    4. 第一次往上，需要一次（因为第一行和第一列都已经加入过了），即m - 2次
    总结：按照右下左上的顺序，分别需要[n, m - 1, n - 1, m - 2, n - 2, m - 3, ...]次
    所以可以用两个变量记录，然后相互更新并且交换
    比如for (int k = 0; k < currentSteps; k++)来精确控制步数，初始化currentStep = n，nextStep = m - 1
    然后通过
    int tmp = currentStep;
    currentStep = nextStep;
    nextStep = tmp - 1;
    来更新和交替
 */

public class SpiralMatrix {
    public List<Integer> spiralMatrix(int[][] matrix) {
        // 声明变量
        List<Integer> result = new ArrayList<>();
        // 遍历
        while (matrix.length > 0) {
            // 把第一层全部加入result
            for (int num : matrix[0]) {
                result.add(num);
            }
            // matrix逆时针旋转90度
            matrix = helper(matrix);
        }

        return result;
    }

    public int[][] helper(int[][] matrix) {
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

    public List<Integer> spiralMatrix2(int[][] matrix) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int direFlag = 0, m = matrix.length, n = matrix[0].length, count = 0;
        int row = 0, col = 0;
        List<Integer> ans = new ArrayList<>(n * m);
        while (count < n * m) {
            // Add to ans
            ans.add(matrix[row][col]);
            // Mark as visited
            matrix[row][col] = Integer.MAX_VALUE;
            // Add count
            count++;
            // Find next direction
            int newRow = row + directions[direFlag][0];
            int newCol = col + directions[direFlag][1];
            if (newRow < 0
                    || newRow >= m
                    || newCol < 0
                    || newCol >= n
                    || matrix[newRow][newCol] == Integer.MAX_VALUE) {
                direFlag++;
                direFlag %= 4;
            }
            row += directions[direFlag][0];
            col += directions[direFlag][1];
        }
        return ans;
    }

    public List<Integer> spiralMatrix3(int[][] matrix) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int m = matrix.length, n = matrix[0].length, count = 0;
        /*
        col需要从-1开始，因为下面的逻辑，先更新坐标再添加数值，是最好的写法
        否则先添加数值再更新坐标的话，当需要换方向的时候，同一个位置的数值会被添加两次
         */
        int row = 0, col = -1, currentStep = n, nextStep = m - 1;
        List<Integer> ans = new ArrayList<>();
        // 通过一个for循环里面的i，来代表direFlag
        for (int i = 0; count < n * m; i = (i + 1) % 4) {
            for (int k = 0; k < currentStep; k++) {
                row += directions[i][0];
                col += directions[i][1];
                // Add to ans
                ans.add(matrix[row][col]);
                // Add count
                count++;
            }
            // Update nextStep
            int tmp = currentStep;
            currentStep = nextStep;
            nextStep = tmp - 1;
        }
        return ans;
    }
}
