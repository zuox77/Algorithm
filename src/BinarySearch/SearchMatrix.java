package BinarySearch;
/*
刷题次数: 2

https://leetcode.cn/problems/search-a-2d-matrix/description/

You are given an m x n integer matrix with the following two properties:
Each row is sorted in non-decreasing order.
The first integer of each row is greater than the last integer of the previous row.
Given an integer target, return true if target is in matrix or false otherwise.
You must write a solution in O(log(M * N)) time complexity.

Input: matrix = [
[ 1, 3, 5, 7],
[10,11,16,20],
[23,30,34,60]
]
target = 3
Output: true

思路: 转化成一维 + 二分法
1. 经典的二维的二分法, 与FindAnyPeakII类似, 不过此题更简单, 因为根据题意, 整个矩阵其实都是单调递增的, 可以想像成一个一维的数组[1, 3, 5, 7, 10, 11]
2. 计算出矩阵总共有多少个数(totalLen), 以及矩阵的长和宽(len = matrix[0].length, width = matrix.length)
3. 定义left和right指针
4. 用while循环遍历
5. 在循环中, 找到中点
6. 通过除以长得到坐标的行数(row = mid / len), 通过余长得到坐标的列数(col = mid % len)
7. 判断该点与target的大小, 来移动指针
8. 根据while循环的条件, 决定最后是否要额外判断一次
9. solution1会展示
   while (left + 1 < right) {
       ...
       left = mid;
       ...
       right = mid;
   }
   的搭配
   solution2会展示
   while (left <= right) {
       ...
       left = mid + 1;
       ...
       right = mid - 1;
   }
   的搭配
10. 第9步中, 两种搭配的区别可以看BinarySearch.md, 简单来说就是
    搭配一: 退出循环时, left和right指针刚好相邻, 且没有经历过第5步到第7步的判断, 所以需要额外判断一次, 但这次不需要移动指针了, 因为
           其本质就是, 只剩left和right指针推出循环时的那两个数, 没有被判断过, 所以只需要直接返回结果就行
    搭配二: 退出循环时, left和right指针已经相互交错, 即right会在left的左边, left在right右边, 所以此时所有的数都已经判断过了, 如果没找到, 
           那就表示没有这个数, 所以不需要额外判断一次了
 */
public class SearchMatrix {
    public boolean solution1(int[][] matrix, int target) {
        // 找到长宽
        int len = matrix[0].length, width = matrix.length;
        // 因为每一行都是递增, 又因为每一行的第一个数一定大于上一行的最后一个数, 所以整个矩阵都是递增的
        // 所以可以将整个矩阵看成一个一维的数组
        int totalLen = len * width;
        // 定义指针
        int left = 0, right = totalLen - 1;
        // 循环遍历
        while (left + 1 < right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 找到中点所代表的数的数值
            int num = findNum(matrix, mid, len);
            // 根据情况判断
            if (num == target) {
                return true;
            } else if (num > target) {
                right = mid;
            } else {
                left = mid;
            }
        }
        // 对left和right指针再找一次
        if (findNum(matrix, left, len) == target) {
            return true;
        }
        if (findNum(matrix, right, len) == target) {
            return true;
        }
        return false;
    }

    public boolean solution2(int[][] matrix, int target) {
        // 找到长宽
        int len = matrix[0].length, width = matrix.length;
        // 因为每一行都是递增, 又因为每一行的第一个数一定大于上一行的最后一个数, 所以整个矩阵都是递增的
        // 所以可以将整个矩阵看成一个一维的数组
        int totalLen = len * width;
        // 定义指针
        int left = 0, right = totalLen - 1;
        // 循环遍历
        while (left <= right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 找到中点的所代表的数的数值
            int num = findNum(matrix, mid, len);
            // 根据情况判断
            if (num == target) {
                return true;
            } else if (num > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        // 不需要再找一次了, 如果运行到这里还没找到, 直接返回false就行
        return false;
    }

    public int findNum(int[][] matrix, int mid, int len) {
        int row = mid / len, col = mid % len;
        return matrix[row][col];
    }
}
