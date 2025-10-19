package BinarySearch;

/*
刷题次数: 2
第二次: 忘了应该用什么来二分

https://www.lintcode.com/problem/183/description

Given n pieces of wood with length L[i] (integer array).
Cut them into small pieces to guarantee you could have equal or more than k pieces with the same length.
What is the longest length you can get from the n pieces of wood?
Given L & k, return the maximum length of the small pieces.

Input:
L = [232, 124, 456]
k = 7
Output: 114

思路: 二分 + 变形
1. 这个题的关键是, 它不是用一个给定的数组来做二分, 而是要进行一些转化
2. 需要用给定数组里面最长的数作为二分的右边界, 1作为二分的左边界, 来做二分
3. 切记左边界不能为0, 因为没有长度为0的切法
4. 右边界不能是给定数组里面最短的数, 因为最短的数包含的范围小, 会错过特殊情况, 比如L = [1,2,3], k = 1, 即当只需要切一块时, 求最大的长度是多少
   如果是最短的数, 那么会发现切不出来, 因为如果1是则至少可以切6块, 但我们只需要1块, 所以找不到答案
   但如果是最长的数, 就可以找到答案, 且最长的数包含了最短的数的所有结果, 所以选最长的数最多就是在二分里面多循环几次
5. 判断条件为, 如果以mid为长度, 计算能切几块, 如果比k大, 则说明长度太短了, 需要增加长度, 所以左指针移动
   如果比k小, 则说明长度太长了, 需要减少长度, 所以右指针移动
6. 额外检查时, 一定要先检查right, 因为题目要求最大长度, 有可能left和right都可以, 但right更大

时间复杂度: O(NlogMaxLength), 其中N是给定数组的长度, MaxLength是给定数组的最长的数
空间复杂度: O(1)
 */

public class WoodCut {
  public int solution1(int[] woods, int k) {
    // 定义指针, 且找到右侧最大值, 即右边界
    int left = 1, right = -1; // 注意: 左指针left一定是从1开始
    for (int wood : woods) {
      right = Math.max(wood, right);
    }
    // corner case
    if (right < 1 || right < k) {
      return 0;
    }
    // 二分遍历
    while (left + 1 < right) {
      // 找到中点
      int mid = left + (right - left) / 2;
      // 判断情况
      // 当此时切割的份数大于等于k, 即每一份的长度不够, 答案在右侧, 所以需要增加长度, 移动左指针
      if (getCutNum(woods, mid) >= k) {
        left = mid;
        // 当此时切割的份数小于k, 即每一份的长度太长, 答案在左侧, 所以需要减少长度, 移动右指针
      } else {
        right = mid;
      }
    }
    // 额外检查一次left和right
    // 一定要先检查right, 因为题目要求最大长度, 有可能left和right都可以, 但right更大
    if (getCutNum(woods, right) >= k) {
      return right;
    }
    if (getCutNum(woods, left) >= k) {
      return left;
    }
    return 0;
  }

  public int getCutNum(int[] woods, int len) {
    int num = 0;
    // 遍历找到一共能切割成多少份
    for (int wood : woods) {
      num += wood / len;
    }
    return num;
  }
}
