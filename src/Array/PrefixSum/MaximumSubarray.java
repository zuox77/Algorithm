package Array.PrefixSum;

/*
刷题次数: 1

https://leetcode.cn/problems/maximum-subarray/description/?envType=study-plan-v2&envId=top-100-liked

Given an integer array nums, find the subarray with the largest sum, and return its sum.

Example 1:
Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: The subarray [4,-1,2,1] has the largest sum 6.

Example 2:
Input: nums = [1]
Output: 1
Explanation: The subarray [1] has the largest sum 1.

Example 3:
Input: nums = [5,4,-1,7,8]
Output: 23
Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.

时间复杂度: O(N), 遍历一次
空间复杂度: O(1)

思路：前缀和/DP
1. 计算前缀和。但这道题因为求所有subarray中最大的值，所以虽说是前缀和，但其实只需要维护前缀和中出现过的最大值和最大值即可
2. 注意：因为可能有负数，所以初始化ans的时候，需要用Integer.MIN_VALUE
3. 注意：一定要先更新ans，因为如果先更新minSum，相当于把下一次循环该做的事情做了，因为下一个循环会根据当前的prefixSum来更新minSum，所以
    相当于i = 0的时候的值没有计算，所有循环都在计算下一次的
 */

public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        // 前缀和
        int ans = Integer.MIN_VALUE;
        int minSum = 0;
        int prefixSum = 0;

        for (int num : nums) {
            // 更新prefixSum
            prefixSum += num;
            // 先更新ans，用当前前缀和减去曾出现过的前缀和的最小值
            ans = Math.max(ans, prefixSum - minSum);
            // 更新曾出现过的前缀和中的最小值
            minSum = Math.min(prefixSum, minSum);
        }

        return ans;
    }
}
