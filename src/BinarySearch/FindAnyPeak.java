package BinarySearch;
/*
https://www.lintcode.com/problem/75/
https://www.jiuzhang.com/solutions/find-peak-element/
题目：
There is an integer array which has the following features:
The numbers in adjacent positions are different.
A[0] < A[1] && A[A.length - 2] > A[A.length - 1].
We define a position P is a peak if: A[P] > A[P-1] && A[P] > A[P+1]
Find a peak element in this array. Return the index of the peak.
It's guaranteed the array has at least one peak.
The array may contain multiple peeks, find any of them.
The array has at least 3 numbers in it.
Example:
Input:
A = [1, 2, 1, 3, 4, 5, 7, 6]
Output:
1

思路：
1. 已知第一个数a[0] < a[1]，a[-2] > a[-1]，所以这个数列的肯定是先升后降
2. 二分切一刀以后，查找mid所处的位置是上升还是下降：
    1. 如果上升，则说明峰值在left后面，所以移动left
    2. 如果下降，则说明峰值在left前面，所以移动right
时间复杂度：其实是O(N)
空间复杂度：O(1)
 */
public class FindAnyPeak {
    public static int solution(int[] a) {
        // corner case
        if (a == null) {
            return 0;
        }

        // define pointers
        int left = 0;
        int right = a.length - 1;

        // iterate
        int answer = 0;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (a[mid] > a[mid + 1] && a[mid] > a[mid - 1]) { // 刚好找到峰值
                return mid;
            } else if (a[mid] < a[mid + 1]) { // 上升
                left = mid;
            } else if (a[mid] > a[mid + 1]) { // 下降
                right = mid;
            }
        }

        // 二分法的本质其实是将范围缩小到相邻两个数，所以这个时候还要判断一次
        if (a[left] < a[right]) {
            return right;
        } else {
            return left;
        }
    }
}
