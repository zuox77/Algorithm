package DynamicProgramming;
/*
https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
https://www.lintcode.com/problem/151/
https://www.jiuzhang.com/problem/best-time-to-buy-and-sell-stock-iii/

You are given an array prices where prices[i] is the price of a given stock on the ith day.
Find the maximum profit you can achieve. You may complete at most two transactions.
Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).

Input: prices = [3,3,5,0,0,3,1,4]
Output: 6
Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.

思路1: 巧妙的DP
1. 这个题的需要考虑因素很多, 总结下来有四个: 
    1. 什么时候买
    2. 什么时候卖
    3. 当前买卖了几次
    4. 当前收益多少
2. 与其去考虑所有的这些因素, 建一个四维的dp方程, 不如直接考虑自己的收益
3. 当一天结束的时候, 无论如何只会处于以下五个状态: 
    1. 未进行任何操作
    2. 买了一次 => buy1
    3. 买了一次卖了一次(完成一笔交易) => sell1
    4. 买了两次卖了一次 => buy2
    5. 买了两次卖了两次(完成二笔交易) => sell2
    由于第一个状态无论怎么样, 对自己的收益没有任何影响所以可以不考虑, 其他四个状态所对应的值是累计到那天的总收益
4. 假设buy1'是第i-1天结束时的收益, buy1是第i天结束的收益, 那么相比较第i-1天, 第i天可以做两件事: 
    1. 不进行任何操作 buy1 = buy1'
    2. 以price[i]买股票 buy1 = -price[i]
    所以转移方程: buy1 = max(buy1', -price[i])
    注意这里虽然看起来像是buy1'和buy1连续两天都在买, 违反了必须卖了才能买的原则, 但实际上这是在寻找最好的买入日期
    可以理解为buy1其实是一个list, buy1[i]表示第i天买入的话, 会让自己的收益发生什么变化, 但因为这个变化只和前一天有关, 
    所以其实没必要用list保存所有的变化, 直接一个变量就可以
    并且通过max, 如果buy1买入比buy1'买入更好, 那么就会被max更新, 也就是说我们在遍历的过程中寻找一个最佳值而不代表i-1天买, i天也买就是违反规则
5. 假设自己初始没有钱, 那么第一次buy1就是借钱买, 初始化的时候buy1应该初始化为-price[i], buy2也是一样, 因为buy2代表在当天卖了又买
6. sell1与buy1同理
    1. 选择不卖, 或者说不进行任何操作, 即sell1 = sell1'
    1. 选择卖, 即sell1 = buy1 + price[i]
    所以转移方程: sell1 = max(sell1', buy1 + price[i]) 记住buy1是借钱买的, 所以卖出应该加号, 因为钱变多了
7. 记住我们的视角永远是自己的收益是多少, buy是借钱, 所以是负的, 我们希望负的越少越好, 所以用max, 代表我们希望价格越低越好, 如果比前一天价格高, 那么不买入
    sell是卖出, 所以是buy1 + price[i], 并且我们希望价格越高的时候卖出越好, 如果比不上前一天的价格, 那么我不卖出
8. buy1和buy2类似, sell1和sell2类似
9. 初始化的时候, 考虑第0天, buy1就是-price[i], 同一天卖出没有收益, 所以sell1是0, buy2也是-price[i]因为卖了又买, 价格不变, 还是-price[i], 
    sell2也是0, 因为同一天买卖没有收益
时间复杂度: O(N), 只遍历了一次
空间复杂度: O(1), 只建了4个变量

思路2: 普通DP
1. 按照上面说的四大因素, 其实其中有些因素可以优化, 比如什么时候买, 其实不需要定下来什么时候买, 因为我们不关心具体的时间, 只关心最大的利润, 所以可以
    用一个变量记录价格最低的点就可以, 或者记录利润最高的点
2. 优化以后就可以建立dp方程
3. state: dp[i][j]代表, 最多交易完成i次(买卖都完成)时, 在第j天卖出能获得的最大利润
4. function: 思路跟上面一样, 因为只考虑卖出, 所以每天只有两个操作: 什么都不做和卖出股票, 所以dp[i][j] =
    1. 如果不做操作, 那么利润不会有任何改变, 所以dp[i][j] = dp[i][j - 1]
    2. 如果卖出, 假设之前在第m天买入, 那么最大利润等于当天股票价格price[j]减去买入股票价格price[m]加上上次交易时的获利, 即
        dp[i][j] = price[j] - min(price[m]) + dp[i - 1][m], where m = 0, 1, ... , j - 1
        即dp[i][j] = price[j] - min(price[j - 1]) + dp[i - 1][j - 1]
        j需要从0遍历到j-1
        前部分好理解 price[j] - min(price[j - 1])表示今天股价减去之前最低的价格, 以此获得最大利润
        后部分dp[i-1][j-1]不好理解, 因为dp[i-1][j-1]按理来说代表的是完成i-1次交易时, 在第j-1天卖出的最大利润, 但明明是前一天, 我们怎么确定
        前一天就能获得最大利润呢？其实还是要根据式子来看, 切记每一个dp都是由前面的dp的max来的, 所以一路遍历过来, 如果利润没有之间高, 那么
        dp[i-1][j-1] = dp[i-1][j-2] = dp[i-1][j-3] = ... = dp[i-1][能让利润最大的那个j]
5. 此时如果结束, 那么时间复杂度和空间复杂度都是O(N^2), 因为每次计算最小的m都需要内循环遍历一次, 但我们上面说过, 什么时候买入这个因素可以通过一个变量去存储最小值
    就避免每次都计算一次, 所以转移方程的第二个选择(卖出)可以优化为: 
    dp[i][j] = price[j] + maxDiff
    maxDiff = max(maxDiff, dp[i - 1][j - 1] - price[j - 1])
    dp[i - 1][j - 1]代表上次完整的交易后的利润, price[j - 1]代表: 上次买入时的价格
    这里之所以只计算前一天(即j-1)是因为, 每次都计算前一天并且保留最大, 那么就是到j为止的最大了
6. initialize: 
7. 此时, 时间复杂度被优化到了O(N * k), 空间复杂度被优化到了O(N), 其中k为最多交易的次数, 即是dp中的i的最大值
8. 当k = 2时, 是一个常量, 就不考虑
时间复杂度: O(N), 只遍历了一次
空间复杂度: O(N)
 */

public class BestTimeStock3 {
    // DP巧妙解法
    public int solution1(int[] prices) {
        int n = prices.length;
        // corner case
        if (n <= 1) {
            return 0;
        }

        int buy1 = -prices[0], sell1 = 0, buy2 = -prices[0], sell2 = 0;
        for (int price: prices) {
            buy1 = Math.max(buy1, -price);
            sell1 = Math.max(sell1, buy1 + price);
            buy2 = Math.max(buy2, sell1 - price);
            sell2 = Math.max(sell2, buy2 + price);
        }

        return sell2;
    }

    // DP正常解法
    public int solution2(int[] prices) {
        int n = prices.length;
        int k = 2;
        // corner case
        if (n <= 1 || k == 0) {
            return 0;
        }

        // params
        int[][] dp = new int[k + 1][n];

        // iterate
        // i必须从1开始, 因为0的话代表没有发生过任何一次完整的买卖交易, 那么肯定是0
        // 也可以理解为, 转移方程是跟i-1有关, 所以必须从1开始
        for (int i = 1; i < dp.length; i++) {
            int maxDiff = -prices[0];
            // j也必须从1开始, 因为0的话代表在第0天卖出, 第0天卖出也只能在第0天买入, 那么利润无论如何都是0
            // 也可以理解为, 转移方程是跟j-1有关, 所以必须从1开始
            for (int j = 1; j < dp[0].length; j++) {
                maxDiff = Math.max(maxDiff, dp[i - 1][j - 1] - prices[j - 1]);
                dp[i][j] = Math.max(dp[i][j - 1], prices[j] + maxDiff);
            }
        }

        return dp[k][n - 1];
    }
}
