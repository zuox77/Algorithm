package BinarySearch;

/*
https://leetcode.cn/problems/search-a-2d-matrix-ii/description/

Write an efficient algorithm that searches for a value target in an m x n integer matrix.
This matrix has the following properties:
Integers in each row are sorted in ascending from left to right.
Integers in each column are sorted in ascending from top to bottom.

Input: matrix = [
[ 1, 4, 7,11,15],
[ 2, 5, 8,12,19],
[ 3, 6, 9,16,22],
[10,13,14,17,24],
[18,21,23,26,30]], target = 5
Output: true

思路: 递归
1. 这道题如果用指针来做的话, 可能比较麻烦, 因为最优解的做法是, 每次排除大约四分之一的范围, 所以很难单用指针来判断
2. 这道题需要用一个隐含条件, 即任意坐标的数, 左上角的数都比它小, 右下角都比他大, 比如:
   假设matrix如上, 则9这个数, 即坐标(2, 2), 的左上角都比它小:
  [1, 4, 7
   2, 5, 8
   3, 6, 9]
   右下角都比它大:
  [ 9, 16, 22
   14, 17, 24
   23, 26, 30]
3. 通过这点, 当计算出mid, 可以通过判断mid和target的关系, 排除要么左上角要么右下角
4. 特别注意, 这道题还需要一个额外的设定, 即边界要故意多加一行, 在第*行可以发现我们是直接将len和width传入, 而不是len-1和width-1作为边界传入
   其原因是: 当rowLeft/rowRight或者colLeft/colRight运行到最后两行/最后两列时, 由于整除的精确性的问题, 会永远等于小的那个数
   比如: rowLeft = 3, rowRight = 4, 则rowMid永远等于3, 那么永远无法计算最后一行的数
   所以我们不如直接假设原来的matrix为:
    matrix = [
    [ 1, 4, 7,11,15, *],
    [ 2, 5, 8,12,19, *],
    [ 3, 6, 9,16,22, *],
    [10,13,14,17,24, *],
    [18,21,23,26,30, *],
    [ *, *, *, *, *, *]
    ]
   这样的话, 就算看起来好像会超出边界, 但因为我们只会把rowMid和colMid带入去找数(matrix[rowMid][colMid]), 所以其实永远不会出界
 */

public class SearchMatrixII {
  public boolean solution1(int[][] matrix, int target) {
    // 找到长宽
    int len = matrix[0].length, width = matrix.length;
    // 用递归来做
    return recursion(matrix, 0, width, 0, len, target);
  }

  public boolean recursion(
      int[][] matrix, int rowLeft, int rowRight, int colLeft, int colRight, int target) {
    // 递归出口
    if (rowLeft == rowRight || colLeft == colRight) {
      return false;
    }
    // 找到中点
    int rowMid = rowLeft + (rowRight - rowLeft) / 2;
    int colMid = colLeft + (colRight - colLeft) / 2;
    int num = matrix[rowMid][colMid];
    // 根据情况判断
    // 找到答案, 则直接返回
    if (num == target) {
      return true;
      // 如果比target大, 那么说明右下角的所有数都比target大, 所以排除左下角
    } else if (num > target) {
      // if (recursion(matrix, rowLeft, rowMid, colLeft, colRight, target)) {return true;}
      // if (recursion(matrix, rowMid, rowRight, colLeft, colMid, target)) {return true;}
      if (recursion(matrix, rowLeft, rowMid, colMid, colRight, target)) {
        return true;
      }
      if (recursion(matrix, rowLeft, rowRight, colLeft, colMid, target)) {
        return true;
      }
      // 如果比target小, 那么说明左上角的所有数都比target小, 所以排除左上角
    } else {
      // if (recursion(matrix, rowLeft, rowMid + 1, colMid + 1, colRight, target)) {return true;}
      // if (recursion(matrix, rowMid + 1, rowRight, colLeft, colRight, target)) {return true;}
      if (recursion(matrix, rowLeft, rowMid + 1, colMid + 1, colRight, target)) {
        return true;
      }
      if (recursion(matrix, rowMid + 1, rowRight, colLeft, colRight, target)) {
        return true;
      }
    }
    return false;
  }
}
