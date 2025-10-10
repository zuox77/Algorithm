package BinarySearch;

/*
刷题次数: 2

https://leetcode.cn/problems/search-insert-position/description/

Given a sorted array of distinct integers and a target value, return the index if the target is found.
If not, return the index where it would be if it were inserted in order.
You must write an algorithm with O(log n) runtime complexity.

Input: nums = [1,3,5,6], target = 5
Output: 2

思路: 二分
此题不难, 主要是为了展示不同搭配的用法
1. 定义left和right指针
2. 建立循环
3. 在循环中, 先找到mid
4. 判断mid位置上的数与target的大小
   1. 如果是= target, 那么直接返回答案
   2. 如果是> target, 那么说明target在左侧, 所以right指针往左移动
   3. 如果是< target, 那么说明target在右侧, 所以left指针往右移动

 */
public class SearchInsertPosition {
    public int searchInsert(int[] nums, int target) {
        // 定义指针
        int len = nums.length;
        int left = 0, right = len - 1;
        // while循环遍历
        while (left <= right) {
            // 找到mid
            int mid = left + (right - left) / 2;
            // 如果找到了直接返回
            if (nums[mid] == target) {
                return mid;
                // 如果比target大, 说明target在左侧, 所以right指针往左移动
            } else if (nums[mid] > target) {
                right = mid;
                // 如果比target小, 说明target在右侧, 所以left指针往右移动
            } else {
                left = mid + 1;
            }
        }
        // 根据题意, 如果[1, 3, 5, 6], target = 2, 答案是1, 说明插入的位置在比target大的第一个数所在的位置
        // 而用left <= right的话, 退出循环时, left应该和right是同一个数, 所以比较哪个都可以
        if (nums[left] >= target) {
            return left;
        }
        return len;
    }
}
