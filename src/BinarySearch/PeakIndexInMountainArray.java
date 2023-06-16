package BinarySearch;
/*
https://leetcode.cn/problems/peak-index-in-a-mountain-array/description/
题目：
An array arr a mountain if the following properties hold:

arr.length >= 3
There exists some i with 0 < i < arr.length - 1 such that:
arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
Given a mountain array arr, return the index i such that arr[0] < arr[1] < ... < arr[i - 1] < arr[i] > arr[i + 1] > ... > arr[arr.length - 1].

You must solve it in O(log(arr.length)) time complexity

思路：二分
1.
1.
1.
1.
1.
1.
 */

public class PeakIndexInMountainArray {
    public int solution(int[] arr) {
        int n = arr.length;
        int left = 0;
        int right = n - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            boolean isLeftLarge = mid - 1 >= left && arr[mid - 1] > arr[mid];
            boolean isRightLarge = mid + 1 <= right && arr[mid + 1] > arr[mid];
            // 找到峰值
            if (!isLeftLarge && !isRightLarge) {
                return mid;
                // 递增
            } else if (!isLeftLarge && isRightLarge) {
                left = mid + 1;
                // 递减和谷底
            } else {// 递减：isLeftLarge && !isRightLarge，谷底：isLeftLarge && isRightLarge
                right = mid;
            }
        }
        return -1;
    }
}
