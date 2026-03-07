package DynamicProgramming.CompleteBag;

import java.util.Arrays;

/*
https://leetcode.cn/problems/coin-change/

You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.
You may assume that you have an infinite number of each kind of coin.

Example 1:
Input: coins = [1,2,5], amount = 11
Output: 3
Explanation: 11 = 5 + 5 + 1

Example 2:
Input: coins = [2], amount = 3
Output: -1

Example 3:
Input: coins = [1], amount = 0
Output: 0

思路: DP + 完全背包问题
1. 创建一个dp数组,该数组的含义是,dp[i]代表,当总金额amount为i时,所需要的最少的硬币数量
2. dp[i]的转化方程的思路是
    1. 对于amount = i时,我们遍历整个coins数组,因为需要凑成exactly等于i,所以coin必须小于i,不然就直接大于了
    2. 当我们选择某个coin的时候,如果要凑成i,那么数量相当于dp[i - coin]的数量加一
        因为选择coin以后,剩余的金额为i - coin,所以只需要查dp[i - coin]就行
        而如果dp[i - coin]是默认值,那么我们需要选最小的那个
        即:dp[i] = Math.min(dp[i], dp[i - coin] + 1)
    3. 初始化的时候,不要用Integer.MAX_VALUE,因为dp[i - coin] + 1可能导致超过int的最大值,只需要用amount+1即可
        因为如果coin只有1,也最多需要amount个coin就行
        记住:dp代表的是当amount为i时,所需要的最少的硬币数量,所以amount+1是一个不可能达到的值
    例子: coins=[1, 2, 5], amount = 11, dp = [0, 12, 12, 12, 12, ... , 12](一共13个数)
    i = 1
    遍历coin,coin = 1,dp[1] = Math.min(dp[1], dp[1 - 1] + 1),dp[0] + 1 = 0 + 1 = 1,dp[1] = 12,所以dp[1] = 1
    i = 2
    遍历coin,coin = 1,dp[2] = Math.min(dp[2], dp[2 - 1] + 1),dp[1] + 1 = 2, dp[2] = 12, 所以dp[2] = 2
    coin = 2,dp[2] = Math.min(dp[2], dp[2 - 1] + 1), dp[0] + 1 = 1, dp[2] = 2, 所以dp[2] = 1
注意:
1. amount = 0是特殊情况,需要单独处理
2. for循环,要从i = 1开始,因为i = 0时,代表凑齐总金额0元需要多少金币,没有意义
3. 结果记得判断一下,如果是默认值(即amount + 1),那么代表无法抵达,返回-1

一般来说,0-1背包问题和完全背包问题的两个循环是有要求的
0-1背包:
    外层遍历arrs,内层遍历target,且内层循环倒序
完全背包:
    1. 如果不考虑元素顺序(即只求最大/最小的数量/金额,而不是具体的解决方案):
        外层遍历arrs,内层遍历target,且内层循环正序
        内外层可以互换,因为不考虑元素顺序
    2. 如果考虑元素顺序:
        外层遍历target,内层遍历arras,且内层循环正序

 */
public class CoinChange {
    public int coinChange(int[] coins, int amount) {
        // 处理特殊情况
        if (amount == 0) return 0;
        // 假设coins都为1,那么也就最多amount个值需要找寻结果,所以amount+1就可以
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        /*
        这样也可以:
        for (int coin: coins) {
            for (int i = 1; i <= amount; i++) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
         */
        for (int i = 1; i < amount + 1; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
