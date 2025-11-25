package DynamicProgramming;

/*
https://leetcode.cn/problems/unique-paths/description/
https://www.lintcode.com/problem/114/
https://www.jiuzhang.com/problem/unique-paths/

There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]).
The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.
Given the two integers m and n, return the number of possible unique paths that the robot can take to reach the bottom-right corner.

The test cases are generated so that the answer will be less than or equal to 2 * 109.

Example 1:
Input: m = 3, n = 7
Output: 28

Example 2:
Input: m = 3, n = 2
Output: 3
Explanation: From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
1. Right -> Down -> Down
2. Down -> Down -> Right
3. Down -> Right -> Down


思路: DP
1. state: dp[i][j]表示: 从(0, 0)开始, 走到(i, j)总共有多少种路径
2. function: 根据规定, 只能往右和往下走, 那么走到(i, j)的路径总数只能是左边一格的路径数+上面一格的路径数
    dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
3. initialize: 二维数组的初始化都需要将边界初始化, 而边界则为全为一, 因为只有一种走法
4. answer: dp[n - 1][m - 1]
 */

public class UniquePath {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];

        // 二刷: 初始化不能跳过(0, 0)
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }
}
