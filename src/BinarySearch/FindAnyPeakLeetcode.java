package BinarySearch;

/*
https://leetcode.cn/problems/find-peak-element/
与FindAnyPeak的区别在于: 
FindAnyPeak规定:      1. 一定有peak
                     2. peak的定义: a[i-1] < a[i] > a[i+1]
                     3. a[0] < a[1]
                     4. a[-2] > a[-1]
此题规定:  1. peak的定义: a[i-1] < a[i] or a[i+1] < a[i]
         2. 上面规定中的第1、3、4条均不存在

A peak element is an element that is strictly greater than its neighbors.
Given a 0-indexed integer array nums, find a peak element, and return its index.
If the array contains multiple peaks, return the index to any of the peaks.
You may imagine that nums[-1] = nums[n] = -∞.
In other words, an element is always considered to be strictly greater than a neighbor that is outside the array.
You must write an algorithm that runs in O(log n) time.
nums[i] != nums[i + 1] for all valid i

Example:
Input: nums = [1,2,3,1]
Output: 2
Explanation: 3 is a peak element and your function should return the index number 2.

思路: 
1. 因为给定条件中, 数组的前后均为负无穷小, 所以可以理解为: 
    1. 如果二分的点(mid), 正处于上升的位置, 那么虽然这个上升趋势的前面也有峰值, 但我们知道最后一个数是负无穷小, 所以后面一定有峰值, 所以
        取可能性最大的一边, 即left = mid
    2. 如果二分的点(mid), 正处于下降的位置, 那么虽然这个下降的趋势后面可能峰回路转也有峰值, 但因为第一个数是负无穷小, 所以前面一定有个峰值, 所以
        取可能性最大的一边, 即right = mid
时间复杂度: 其实是O(N)
空间复杂度: O(1)
 */
public class FindAnyPeakLeetcode {
    public static int solution(int[] nums) {
        // corner case
        if (nums == null) {
            return -1;
        }

        // define pointers
        int left = 0;
        int right = nums.length - 1;

        // iterate
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid - 1] < nums[mid]){ // 处于上升区间, 所以继续往后找
                left = mid;
            } else if (nums[mid + 1] < nums[mid]) { // 处于下降区间, 所以往前面找
                right = mid;
            } else {
                if (nums[mid - 1] >= nums[mid + 1]) {  // 处于峰谷, 即左右两边都比mid大, 判断左右两边的大小
                    right = mid;
                } else {
                    left = mid;
                }
            }
        }
        if (nums[left] > nums[right]) {
            return left;
        } else {
            return right;
        }
    }
}
