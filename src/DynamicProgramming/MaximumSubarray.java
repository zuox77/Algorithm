package DynamicProgramming;
/*
刷题次数: 2
二刷: 忘记了dp的定义和迭代公式, 注意此题需要用变形的前缀和, 而非普通前缀和
dp定义: 以下标i的数作为结尾的连续子数组的最大和
一般前缀和: dp[i] = dp[i - 1] + nums[i]
求最大值的前缀和: dp[i] = Math.max(dp[i - 1] + nums[i], nums[i])

https://leetcode.cn/problems/maximum-subarray/
https://www.lintcode.com/problem/41/
https://www.jiuzhang.com/problem/maximum-subarray/

Given an integer array nums, find the subarray with the largest sum, and return its sum.

Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: The subarray [4,-1,2,1] has the largest sum 6.

思路1: DP, 或者说变形前缀和
1. state: dp[i]表示, 以下标为i的数作为结尾的连续子数组的最大和
2. function: dp[i] =
    1. 如果dp[i-1] < 0, 那么此时加上任何数, 最大和都会减少, 为了求得最大和, 直接抛弃前面的结果, 从0开始
        dp[i] = nums[i]
    2. 如果dp[i - 1] >= 0, 那么此时加上任何数, 至少最大和不会减少, 所以
        dp[i] = dp[i - 1] + nums[i]
3. initialize: dp[0] = nums[0], 按照dp的定义应该很好理解
4. answer: 注意答案并不是dp[-1], 因为定义中, dp只是表示某个数为结尾的连续子数组的最大和, 但是
    题目并不限制这个连续子数组从哪开始从哪结束, 所以答案是max(dp[:])

注意: 这里的dp并非是前缀和, 而是变形后的
前缀和: 以i为结尾的前面所有数的和
变形后的前缀和: 以i为结尾的前面所有数的和, 但如果某个位置的和<0, 则从头开始
          以: [-2, 1,-3, 4,-1, 2, 1,-5, 4]为例子: 
      前缀和:  [-2,-1,-4, 0,-1, 1, 2,-3, 1]
变形后的前缀和  [-2, 1,-2, 4, 3, 5, 6, 1, 5]

时间复杂度: O(2N), 即O(N), 先遍历一次获得前缀和, 再遍历一次前缀和得最大值
空间复杂度: O(N)

思路2: 优化DP, 思路1的写法优化, 逻辑一样
1. 看上面可以知道, dp[i]只和dp[i-1]有关, 所以其实不需要保存一整个数组, 用一个常量来存储dp[i-1]并每次更新就好
2. 由于答案需要知道max(dp[:]), 所以还需要一个变量res来记录整体最大值
3. 初始化的时候dp和res都要等于nums[0], 以防nums只有一个数的情况
4. 初始化了以后, 后面for循环从1开始

时间复杂度: O(N)
空间复杂度: O(1)
 */

public class MaximumSubarray {
    // DP
    public int solution1(int[] nums) {
        // corner case
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // define state function
        int len = nums.length;
        int[] dp = new int[len];
        dp[0] = nums[0];

        // iterate to get dp
        for (int i = 1; i < len; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
        }

        // iterate dp to get maximum
        int res = dp[0];
        for (int i = 1; i < len; i++) {
            res = Math.max(res, dp[i]);
        }

        return res;
    }

    // 写法优化版DP
    public int solution1Improved(int[] nums) {
        // corner case
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // define state function
        int len = nums.length;
        int dp = nums[0];
        int res = nums[0]; // res的初始化也必须是第0个数, 因为下方是从下标1开始, 等于是已经遍历了i = 0
        /*
        如果for循环直接从0开始的话, 那么dp和res的初始化就必须是一个理论最小值
        因为如果从0开始, 那么dp和res的初始化就是下标0之前的数, 假设是下标-1, 这个下标-1要保证遍历到下标0时, 
        在max函数中, 一定是nums[0]大, 才能保证整个解法的正确性
        但如果设为Integer.MIN_VALUE的话, 假设第一个数是负数, 虽然确实是理论最小了, 但由于max里其中一个变量是dp + nums[0], 
        此时一个负数+理论最小值, 就会超出内存, 反而变成Integer.MAX_VALUE, 所以最好初始化, 然后从1开始
         */
        // iterate
        for (int i = 1; i < len; i++) {
            dp = Math.max(nums[i], dp + nums[i]);
            res = Math.max(res, dp);
        }

        return res;
    }

    // Prefix Sum
    public int solution2(int[] nums) {
        // corner case
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // define state function
        int res = Integer.MIN_VALUE;
        int sum = 0; // nums的累加的和, 等于sum(nums[:i])
        /*
          minSum是sum的全局最小值
          minSum的含义其实也和sum一样, 隐含前i个数的和的意思, 不过是多了一个前i个数的和的最小值
          理论来讲, 暴力解法需要双循环遍历N^2次, 不然sum永远只能计算从0开始到i的和, 怎么知道比如从3到5的连续子数组的和呢？
          这里的minSum其实通过取巧的方式节省了一个循环, sum - minSum其实就是
          for i < n:
            sum_1 += i
            for j < n:
              sum_2 += j
              max(sum_1 - sum_2)
         */
        int minSum = 0;

        for (int num: nums) {
            sum += num;
            res = Math.max(res, sum - minSum);
            minSum = Math.min(minSum, sum);
        }

        return res;
    }
}
