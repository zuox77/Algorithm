package Array.Rotate;

/*
刷题次数: 1

https://leetcode.cn/problems/rotate-array/?envType=study-plan-v2&envId=top-100-liked

Given an integer array nums, rotate the array to the right by k steps, where k is non-negative.

Example 1:
Input: nums = [1,2,3,4,5,6,7], k = 3
Output: [5,6,7,1,2,3,4]
Explanation:
rotate 1 steps to the right: [7,1,2,3,4,5,6]
rotate 2 steps to the right: [6,7,1,2,3,4,5]
rotate 3 steps to the right: [5,6,7,1,2,3,4]

Example 2:
Input: nums = [-1,-100,3,99], k = 2
Output: [3,99,-1,-100]
Explanation:
rotate 1 steps to the right: [99,-1,-100,3]
rotate 2 steps to the right: [3,99,-1,-100]

时间复杂度: O(N), 遍历3次
空间复杂度: O(1)

思路：翻转
1. 在这道题中，隐形条件是：
    1. k等于所有数往右移动多少位，比如[1,2,3]，k = 1，则为[3,1,2]
    2. 写交换方程时，直接把左端点和右端点当成参数传进来，好写一些
    3. k是可以大于数组长度的，比如[1,2,3]，k = 5，则为[2,3,1]，但此时交换方程会报错，所以需要余一下：k = k % n，这样可以保证只翻转最小的次数
2. 先翻转全部数组           ->  flip(nums, 0, n - 1)
3. 再翻转前k个数组（包括k）   -> flip(nums, 0, k - 1)
    之所以翻转前k个数组的时候要把k-1传进去，是因为k代表总共几个数，但我们传进去的其实是下标，比如k=3，那么是翻转下标为0，1，2的这三个数
4. 再翻转后k个数组（不包括k） -> flip(nums, k, n - 1)
 */

public class RotateArray {
    public void rotateArray(int[] nums, int k) {
        // 需要先把k % n，一个是避免麻烦，当k大于nums.length时，会报错，比如[1,2,3]，k=5，其实是往右移动2位。所以即k = k % n
        int n = nums.length;
        k %= n;
        // 翻转整个数组
        swapArray(nums, 0, n - 1);
        // 翻转前k个
        swapArray(nums, 0, k - 1);
        // 翻转后k个
        swapArray(nums, k, n - 1);
    }

    public void swapArray(int[] nums, int left, int right) {
        while (left < right) {
            int tmp = nums[left];
            nums[left] = nums[right];
            nums[right] = tmp;
            left++;
            right--;
        }
    }
}
