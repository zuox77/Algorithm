package DynamicProgramming;

/*
https://leetcode.cn/problems/maximum-profit-in-job-scheduling/description/

We have n jobs, where every job is scheduled to be done from startTime[i] to endTime[i], obtaining a profit of profit[i].
You're given the startTime, endTime and profit arrays,
return the maximum profit you can take such that there are no two jobs in the subset with overlapping time range.
If you choose a job that ends at time X you will be able to start another job that starts at time X.

Example 1:
Input: startTime = [1,2,3,3], endTime = [3,4,5,6], profit = [50,10,40,70]
Output: 120
Explanation: The subset chosen is the first and fourth job.
Time range [1-3]+[3-6] , we get profit of 120 = 50 + 70.

Example 2:
Input: startTime = [1,2,3,4,6], endTime = [3,5,10,6,9], profit = [20,20,100,70,60]
Output: 150
Explanation: The subset chosen is the first, fourth and fifth job.
Profit obtained 150 = 20 + 70 + 60.

Example 3:
Input: startTime = [1,1,1], endTime = [2,3,4], profit = [5,6,4]
Output: 6

思路: DP + 二分
1. 将三个数字合并成一个三元组的数组,同时保存startTime、endTime和profit
2. 将合并后的数组按照endTime从小到大排序
3. DP核心：dp[i]代表前i个schedule（无论是否前i个schedule都选用）所能获得的最大profit
    1. 对于第i个schedule来说,我们有两种选择,选择第i个schedule与不选择第i个schedule
        如果不选择第i个schedule：那么profit不变,所以dp[i] = dp[i - 1]
        如果选择第i个schedule：那么其profit = dp[j] + profit[i]
            即,需要找到一个j,这个j代表前j个schedule所能获得的最大profit,且
            这个j的endTime要小与等于第i个schedule的startTime
            schedule[j][1] <= schedule[i][0]
        转化方程：dp[i] = Math.max(dp[i - 1], dp[j + 1] + schedule[i][2])
    2. 由于是排序过的,所以我们可以通过二分来查找j
        通过条件schedules[mid][0] <= target,left最终会停在最后一个<=target的数
4. 需要注意：
    1. 更新dp时,for循环的条件是int i = 0; i < n; i++
    2. dp的转化方程是dp[i + 1] = Math.max(dp[i], dp[j + 1] + schedule[i][2])
        因为dp的长度要比schedule多一位,dp[0]代表选前0个,是一个dummy位置
    3. j + 1是因为我们的二分的结果是停在最后一个<=target的数,target是第i个schedule的startTime
        而由于第二个原因,所有dp相关的下标都需要额外增加一位
        即,原本的转化方程：dp[i] = Math.max(dp[i - 1], dp[j] + schedule[i][2])
        由于dp的长度要比schedule多一位,所以实际的转化方程：
            dp[i + 1] = Math.max(dp[i], dp[j + 1] + schedule[i][2])
    4. 二分的条件是schedules[mid][0] <= target,最后left会停在最后一个<=target的数
 */

import java.util.Arrays;

public class MaximumProfitInJobScheduling {
    public int maximumProfitInJobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        // 将三个数组合并成一个多维数组,并以endTime排序
        int[][] schedules = new int[n][3];
        for (int i = 0; i < n; i++) {
            schedules[i] = new int[] {startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(schedules, (a, b) -> (a[1] - b[1]));
        // 创建一个dp数组,dp[i]表示
        int[] dp = new int[n + 1];
        // 遍历数组更新dp
        for (int i = 0; i < n; i++) {
            int j = search(schedules, i, schedules[i][0]);
            dp[i + 1] = Math.max(dp[i], dp[j + 1] + schedules[i][2]);
        }
        return 1;
    }

    private int search(int[][] schedules, int right, int target) {
        int left = -1;
        while (left + 1 < right) {
            int mid = (left + right) >>> 2;
            if (schedules[mid][0] <= target) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
