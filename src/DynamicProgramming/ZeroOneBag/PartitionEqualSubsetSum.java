package DynamicProgramming.ZeroOneBag;

/*
https://leetcode.cn/problems/partition-equal-subset-sum/description/

Given an integer array nums,
return true if you can partition the array into two subsets such that the sum of the elements in both subsets is equal or false otherwise.

Example 1:
Input: nums = [1,5,11,5]
Output: true
Explanation: The array can be partitioned as [1, 5, 5] and [11].

Example 2:
Input: nums = [1,2,3,5]
Output: false
Explanation: The array cannot be partitioned into equal sum subsets.

思路: DP的0-1背包
1. 题目要求将数组分成两个子集,且这两个子集的和相等,很显而易见,默认条件是,每个数字只能使用一次,那就是0-1背包问题
2. dp[i]表示,当总和为i时,是否存在一个子集的总和为i,dp = boolean[]
    我们需要遍历所有的数字来更新dp,而更新时有两种情况:
        1. dp[i]为true,即之前的某个子集的总和为i已经将其更新为true了,所以此时dp[i] = dp[i]
        2. dp[i]为false,即还没有发现某个子集的和为i,但是我们此时还有数字num,我们还可以检查是否dp[i - num]为true
            举个例子,当前遍历所有的数字,我们遍历到了数字3,然后内层循环遍历dp的时候,此时i = 5
            dp[5] = false,所以还没有子集的和是5,但是dp[2] = true,说明有个子集的和为2,而2+3刚好等于5,所以我们可以将dp[5]更新为true
3. 转化方程: dp[i] = dp[i] || dp[i - num]
注意:
1. 我们需要去计算总体的和为多少,target = sum / 2
2. 可以判断一下奇偶,如果是奇数,那么直接排除
3. dp的长度为target + 1
4. 更新dp时要从target往前遍历,这样不会重复计算,并且上限是target,下限是num,因为比num还小那肯定是false
    就比如现在遍历nums,遍历到了num = 5,那么i如果为4,那么dp的转化方程此时只能为dp[i] = dp[i],dp[i] = dp[i - num]是不存在的
 */

public class PartitionEqualSubsetSum {
    public boolean canPartition(int[] nums) {
        // 计算和有多大
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // 如果是奇数,直接排除
        if (sum % 2 == 1) return false;
        int target = sum / 2;
        // 创建dp数组初始化,dp[i]表示,当总和为i时,是否存在一个子集的总和为i
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        // 遍历数组
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                // 转化方程
                dp[i] = dp[i] || dp[i - num];
            }
        }
        return dp[target];
    }
}
