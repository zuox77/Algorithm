package Array.PrefixSum;

/*
刷题次数: 1

https://leetcode.cn/problems/subarray-sum-equals-k/description/?envType=study-plan-v2&envId=top-100-liked

Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:
Input: nums = [1,1,1], k = 2
Output: 2

Example 2:
Input: nums = [1,2,3], k = 3
Output: 2

时间复杂度: O(N), 遍历两次
空间复杂度: O(1)

思路：前缀和
1. 计算前缀和，记住前缀和数组一般要多一位，多了第一位为0，即prefixSum[0]=0
2. 将问题转化为：设定j > i，有多少个prefixSum[j]和prefixSum[i]的组合，可以使得：prefixSum[j] - prefixSum[i] = k
3. 用map去保存以前出现过的前缀和的值，用ans去计算总共有多少种方案：
    1. ans = 以prefixSum[i]（即prefixSum[j] - k）为key，在map里面找，并将其value加入ans
    2. 更新freq，以prefixSum[j]作为key，加入map中
 */

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        // 计算前缀和数组
        int[] prefixSum = new int[nums.length + 1]; // 加1是因为让前缀和数组第一位为0，从而更简单处理边界情况
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        // 假设j>i，那么现在问题也可以看作：有多少个prefixSum[j] - prefixSum[i] = k
        // 所以我们要用一个map去保存曾经出现过的前缀和
        // 现在我们遍历前缀和数组，且在每次循环中我们需要做两件事：
        // 1. 更新ans：我们应该将当前遍历到的前缀和的值，当成是prefixSum[j]，然后看字典里，以前出现过多少个符合条件的数，即去找map中key = prefixSum[j]
        // - k有多少个，即prefixSum[i]
        // 2. 更新freq：为了未来能将当前遍历到的前缀和的值也用上，我们需要将其加入freq里面
        // 3.
        // 注意：更新ans再更新freq的顺序不能变，很简单，假设k=2，而当前prefixSum[j]=1，那么如果先加入freq，再更新ans的话，假设freq中已经有key=1，那么此时ans就额外计算了，因为
        // 此时的prefixSum[j]=1，应该在之后的遍历中，被后来的prefixSum[j]当成prefixSum[i]使用。说白了，先找之前的数，再把自己加入。
        Map<Integer, Integer> freq = new HashMap<>(nums.length + 1, 1);
        int ans = 0;
        for (int prefix : prefixSum) { // 这个就是prefixSum[j]
            ans += freq.getOrDefault(prefix - k, 0); // prefix - k = prefixSum[i]
            freq.merge(prefix, 1, Integer::sum);
        }
        return ans;
    }
}
