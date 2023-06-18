package DynamicProgramming;
/*
https://leetcode.cn/problems/unique-paths/description/
https://www.lintcode.com/problem/114/
https://www.jiuzhang.com/problem/unique-paths/

思路: DP
1. state: dp[i][j]表示: 从(0, 0)开始, 走到(i, j)总共有多少种路径
2. function: 根据规定, 只能往右和往下走, 那么走到(i, j)的路径总数只能是左边一格的路径数+上面一格的路径数
    dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
3. initialize: 二维数组的初始化都需要将边界初始化, 而边界则为全为一, 因为只有一种走法
4. answer: dp[n - 1][m - 1]
 */

public class UniquePath {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[n][m];

        // 二刷: 初始化不能跳过(0, 0)
        for (int i = 0; i < n; i++) {
            dp[i][0] = 1;
        }
        for (int i = 0; i < m; i++) {
            dp[0][i] = 1;
        }

        for (int i = 1; i < n; i++){
            for (int j = 1; j < m; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[n - 1][m - 1];
    }
}
