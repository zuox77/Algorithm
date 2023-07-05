package Array;
import java.util.*;
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
 */

public class SpiralMatrix {
    public List<Integer> solution1(int[][] matrix) {
        // 声明变量
        List<Integer> result = new ArrayList<>();
        // 遍历
        while (matrix.length > 0) {
            // 把第一层全部加入result
            for (int num: matrix[0]) {
                result.add(num);
            }
            // matrix逆时针旋转90度
            matrix = spinMatrix(matrix);
        }

        return result;
    }

    public int[][] spinMatrix(int[][] matrix) {
        // 定义新的矩阵长和宽
        int len = matrix[0].length;
        int width = matrix.length - 1; // 因为已经把第一层加进result了, 所以不需要了
        // 因为每次旋转90度, 所以这里长和宽要交换一下
        int[][] matrixNew = new int[len][width]; // 不是 int[width][len] => int[有几行][有几列]
        // 将数字一一放到新矩阵里面
        for(int i = 0; i < len; i++) {
            for (int j = 0; j < width; j++) {
                matrixNew[i][j] = matrix[j + 1][len - i - 1];
            }
        }
        return matrixNew;
    }
}
