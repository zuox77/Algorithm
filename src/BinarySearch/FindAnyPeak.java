package BinarySearch;

/*
刷题次数: 2

https://www.lintcode.com/problem/75/
https://www.jiuzhang.com/solutions/find-peak-element/

题目:
There is an integer array which has the following features:
The numbers in adjacent positions are different.
A[0] < A[1] && A[A.length - 2] > A[A.length - 1].
We define a position P is a peak if: A[P] > A[P-1] && A[P] > A[P+1]
Find a peak element in this array. Return the index of the peak.
It's guaranteed the array has at least one peak.
The array may contain multiple peeks, find any of them.
The array has at least 3 numbers in it.

Input: A = [1, 2, 1, 3, 4, 5, 7, 6]
Output: 1

思路:
1. 已知第一个数a[0] < a[1], a[-2] > a[-1], 所以这个数列的肯定是先升后降
2. 二分切一刀以后, 查找mid所处的位置是上升还是下降:
    1. 如果上升, 则说明峰值在left后面, 所以移动left
    2. 如果下降, 则说明峰值在left前面, 所以移动right

时间复杂度: 其实是O(N)
空间复杂度: O(1)

 */
public class FindAnyPeak {
  public static int solution1(int[] a) {
    // corner case
    if (a == null) {
      return 0;
    }
    // 定义指针
    int left = 0, right = a.length - 1, result = -1;
    // 二分法标准公式, while循环
    while (left + 1 < right) {
      int mid = left + (right - left) / 2;
      // 如果mid处于递增区间, 因为已知数组末尾一定是递减
      // 所以移动left指针到mid, 排除mid左侧的区间, 能保证一定能找到峰顶
      if (a[mid - 1] < a[mid] && a[mid] < a[mid + 1]) {
        left = mid;
        // 如果mid处于递减区间, 因为已知数组开头一定是递增
        // 所以移动right指针到mid, 排除mid右侧的区间, 能保证一定能找到峰顶
      } else if (a[mid - 1] > a[mid] && a[mid] > a[mid + 1]) {
        right = mid;
        // 如果是峰顶, 直接返回
      } else if (a[mid - 1] < a[mid] && a[mid + 1] < a[mid]) {
        return mid;
        // 如果是峰谷, 则随便移动
      } else {
        left = mid;
      }
    }
    // 二分法的本质其实是将范围缩小到相邻两个数, 所以这个时候要返回最大值
    return Math.max(left, right);
  }
}
