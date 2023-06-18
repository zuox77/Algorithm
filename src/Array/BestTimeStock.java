package Array;
/*
刷题次数: 2

https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
https://www.lintcode.com/problem/149/
https://www.jiuzhang.com/problem/best-time-to-buy-and-sell-stock/

You are given an array prices where prices[i] is the price of a given stock on the ith day.
You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.

Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.

思路: 贪心算法
1. 一个变量记录遍历到当前位置时的最低价格, 并不断更新 minPrice = Math.min(minPrice, price)
2. 一个变量记录遍历到当前位置时的最大获益, 即当前位置的价格-最低价格, 并不断更新 maxProfit = Math.max(maxProfit, price - minPrice)

时间复杂度: O(N), 遍历一次
空间复杂度: O(1)
 */

public class BestTimeStock {
    public int solution(int[] prices) {
        // corner case
        if (prices == null || prices.length == 0) {
            return 0;
        }

        // define params
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;

        // iterate
        for (int price: prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(price - minPrice, maxProfit);
        }

        return maxProfit;
    }
}
