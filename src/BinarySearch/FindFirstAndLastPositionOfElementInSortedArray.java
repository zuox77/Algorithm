package BinarySearch;

/*
刷题次数: 2
第二次: 基本记得

https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/description/?envType=study-plan-v2&envId=top-100-liked

Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
If target is not found in the array, return [-1, -1].
You must write an algorithm with O(log n) runtime complexity.

Example 1:
Input: nums = [5,7,7,8,8,10], target = 8
Output: [3,4]

Example 2:
Input: nums = [5,7,7,8,8,10], target = 6
Output: [-1,-1]

Example 3:
Input: nums = [], target = 0
Output: [-1,-1]


思路: 二分
1. 先用正常的方式找到left和right的点
2. 通过循环让left不断往左，right不断往右，找到第一个和最后一个
 */

public class FindFirstAndLastPositionOfElementInSortedArray {
    public int[] searchRange(int[] nums, int target) {
        // Corner case
        if (nums == null || nums.length == 0) return new int[] {-1, -1};
        // 创建双指针
        int left = 0, right = nums.length - 1;
        // 二分
        while (left + 1 < right) {
            // 计算中点
            int mid = (right - left) / 2 + left;
            if (nums[mid] == target) {
                return findFirstAndLast(nums, target, mid);
            } else if (nums[mid] < target) {
                left = mid;
            } else {
                right = mid;
            }
        }
        // 最后检查一次left和right
        if (nums[left] == target) {
            return findFirstAndLast(nums, target, left);
        }
        if (nums[right] == target) {
            return findFirstAndLast(nums, target, right);
        }
        return new int[] {-1, -1};
    }

    public int[] findFirstAndLast(int[] nums, int target, int i) {
        // 找最左侧
        int left = i;
        while (left >= 0 && nums[left] == target) {
            left--;
        }
        // 找最右测
        int right = i;
        while (right < nums.length && nums[right] == target) {
            right++;
        }
        return new int[] {left + 1, right - 1};
    }
}
