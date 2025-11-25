package Array;

/*
刷题次数: 2
第二次: 虽然忘了具体实现, 但思路还记得, 用另一种方式做出来了

https://leetcode.cn/problems/rotate-image/description/

You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).
You have to rotate the image in-place, which means you have to modify the input 2D matrix directly.
DO NOT allocate another 2D matrix and do the rotation.

Input: matrix = [
[1,2,3],
[4,5,6],
[7,8,9]]
Output: [
[7,4,1],
[8,5,2],
[9,6,3]]

思路: 两次交换法
1. 沿着左上右下的对角线, 一一交换, 比如:
   [5,1,9,11]
   [2,4,8,10]
   [13,3,6,7]
   [15,14,12,16]
   交换后为:
   [5,2,13,15]
   [1,4,3,14]
   [9,8,6,12]
   [11,10,7,16]
2. 沿着中轴线一一交换, 最后得到;
   [15,13, 2, 5],
   [14, 3, 4, 1],
   [12, 6, 8, 9],
   [16, 7,10,11]

时间复杂度: O(N*N), N是matrix的长度, 因为需要遍历几乎所有的点
空间复杂度: O(1)
 */
public class RotateImage {
    public void rotateImage(int[][] matrix) {
        int n = matrix.length;
        // 沿着左上右下对角线交换
        for (int i = 0; i < n; i++) {
            // 对角线交换的实现:
            // 1. (1,1), 那么交换(1,0)(0,1)
            // 2. (2,2), 那么交换(2,1)(1,2)和(2,0)(0,2)
            // 3. (3,3), 那么交换(3,2)(2,3)和(3,1)(1,3)和(3,0)(0,3)
            // 4. 以此类推
            int count = 1;
            while (i - count >= 0) {
                swap(matrix, i, i - count, i - count, i);
                count++;
            }
        }
        // 沿着中轴线交换, 要考虑奇偶问题
        // 如果是奇数的话，可以将中轴线位置上的数两两交换，这样就可以当成偶数来处理
        for (int row = 0; row < n; row++) {
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                swap(matrix, row, left, row, right);
                left++;
                right--;
            }
        }
    }

    public void rotateImage2(int[][] matrix) {
        int n = matrix.length;
        // 沿对角线遍历
        // 顺时针
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                swap(matrix, i, j, j, i);
            }
        }

        // 沿着中轴线交换, 要考虑奇偶问题
        // 如果是奇数的话，可以将中轴线位置上的数两两交换，这样就可以当成偶数来处理
        for (int row = 0; row < n; row++) {
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                swap(matrix, row, left, row, right);
                left++;
                right--;
            }
        }
    }

    public void swap(int[][] matrix, int x, int y, int i, int j) {
        int tmp = matrix[x][y];
        matrix[x][y] = matrix[i][j];
        matrix[i][j] = tmp;
    }
}
