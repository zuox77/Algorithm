package DataStructure.Stack;

import java.util.ArrayDeque;
import java.util.Deque;

/*
https://leetcode.cn/problems/daily-temperatures/description/?envType=study-plan-v2&envId=top-100-liked

Given an array of integers temperatures represents the daily temperatures,
return an array answer such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature.
If there is no future day for which this is possible, keep answer[i] == 0 instead.

Example 1:
Input: temperatures = [73,74,75,71,69,72,76,73]
Output: [1,1,4,2,1,1,0,0]

Example 2:
Input: temperatures = [30,40,50,60]
Output: [1,1,1,0]

Example 3:
Input: temperatures = [30,60,90]
Output: [1,1,0]

思路: 栈

 */

public class DailyTemperatures {
    public int[] dailyTemperatures(int[] temperatures) {
        // 创建一个数组保存答案
        int[] ans = new int[temperatures.length];
        // 创建一个stack
        Deque<Integer> stack = new ArrayDeque<>();
        // 遍历数组
        for (int i = 0; i < temperatures.length; i++) {
            // 如果大于栈首且stack不为空，记录答案
            while (!stack.isEmpty() && temperatures[stack.peekFirst()] < temperatures[i]) {
                int preDate = stack.pollFirst();
                ans[preDate] = i - preDate;
            }
            // 栈首进栈
            stack.offerFirst(i);
        }
        // 如果stack还有数字
        while (!stack.isEmpty()) {
            ans[stack.pollFirst()] = 0;
        }
        return ans;
    }
}
