package BFSAndDFS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
https://leetcode.cn/problems/n-queens/description/?envType=study-plan-v2&envId=top-100-liked

The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.
Given an integer n, return all distinct solutions to the n-queens puzzle. You may return the answer in any order.
Each solution contains a distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate a queen and an empty space, respectively.

Example 1:
Input: n = 4
Output: [
    [" .Q.. ",
     " ...Q ",
     " Q... ",
     " ..Q. "
    ],
    [" ..Q. ",
     " Q... ",
     " ...Q ",
     " .Q.. "
    ]
]
Explanation: There exist two distinct solutions to the 4-queens puzzle as shown above
Example 2:

Input: n = 1
Output: [["Q"]]

思路: DFS
1. 题目要求每个皇后都不能打架，而对打架的定义是同一行同一列同一个斜线（两条对角线）都不行
2. 主要是如何判断对角线
    对于一个n x n的矩阵来说，每个点都有两条对角线，一个从右上到左下，一个从左上到右下
    右上到左下的对角线：其中的点，他们的x+y都相等
    左上到右下的对角线：其中的点，他们的x-y的都相等
3. 还要考虑到，对于同一种排列方法，如何去重
 */
public class NQueens {
    /*
    注意：本解法会超时，原因是：
    1. 用了双重循环，虽然每次检查set平均需要O(1)的时间，但是查询次数太多，导致整体时间花销很大
        数组访问比HashSet快很多，改用数组
    2. 双重循环会导致重复的答案，因为它会从matrix的每一个点都计算一次，但是如果改为按行（毕竟每一行每一列都只有一个皇后）或者按列遍历
        就不需要双重循环，也不会重复，能节省时间，也不需要在最后得到一个解法的时候考虑重复答案的问题
     */
    public List<List<String>> solveNQueens(int n) {
        // 创建一个matrix代表记录结果
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], '.');
        }
        List<List<String>> ansList = new ArrayList<>();

        dfs(
                n,
                0,
                matrix,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                ansList);

        return ansList;
    }

    public void dfs(
            int n,
            int count,
            char[][] matrix,
            Set<String> unique,
            Set<Integer> visitedRow,
            Set<Integer> visitedCol,
            Set<Integer> visitedRightUp,
            Set<Integer> visitedLeftUp,
            List<List<String>> ansList) {
        // 退出条件
        if (count == n) {
            List<String> ans = new ArrayList<>(n);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                ans.add(new String(matrix[i]));
                sb.append(matrix[i]);
            }
            if (unique.add(sb.toString())) ansList.add(ans);
            return;
        }

        // 遍历且修改matrix
        for (int i = 0; i < n; i++) {
            if (visitedRow.contains(i)) continue;
            visitedRow.add(i);
            for (int j = 0; j < n; j++) {
                /*
                1. 每次放置一个Q以后，该行和列都不能再被使用
                2. 检查对角线
                3. 检查是否为'Q'

                对于一个n x n的矩阵来说，每个点都有两条对角线，一个从右上到左下，一个从左上到右下
                右上到左下的对角线：其中的点，他们的x+y都相等
                左上到右下的对角线：其中的点，他们的x-y都相等
                 */
                if (visitedCol.contains(j)) continue;
                if (visitedRightUp.contains(i + j)) continue;
                if (visitedLeftUp.contains(i - j)) continue;
                if (matrix[i][j] == 1) continue;
                // 修改matrix
                matrix[i][j] = 'Q';
                // 加入visited
                visitedCol.add(j);
                visitedRightUp.add(i + j);
                visitedLeftUp.add(i - j);
                // 进入下一层
                dfs(
                        n,
                        count + 1,
                        matrix,
                        unique,
                        visitedRow,
                        visitedCol,
                        visitedRightUp,
                        visitedLeftUp,
                        ansList);
                // 改回来
                matrix[i][j] = '.';
                // 去除visited
                visitedCol.remove(j);
                visitedRightUp.remove(i + j);
                visitedLeftUp.remove(i - j);
            }
            visitedRow.remove(i);
        }
    }

    /*
    这是优化以后的
     */
    public List<List<String>> solveNQueens2(int n) {
        List<List<String>> ansList = new ArrayList<>();
        char[][] matrix = new char[n][n];

        // 初始化棋盘
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], '.');
        }

        // 列占用标记
        boolean[] colVisited = new boolean[n];
        /*
        对于一个n x n的矩阵来说，每个点都有两条对角线，一个从右上到左下，一个从左上到右下
        右上到左下的对角线：其中的点，他们的row + col都相等
        左上到右下的对角线：其中的点，他们的row - col都相等
         */
        // 主对角线 (row + colVisited)
        boolean[] diagonal1 = new boolean[2 * n - 1];
        /*
        副对角线 (row - colVisited + n - 1)
        加上n - 1是为了避免负数，因为row - col有可能为负数，但我们想用数组下标做索引就不能有负数，所以统一加上n - 1，避免负数
         */
        boolean[] diagonal2 = new boolean[2 * n - 1];

        /*
        按行递归，每进入一层，在那一层里，对每一列再进行操作
        这样可以避免双重循环
         */
        dfs(0, n, matrix, colVisited, diagonal1, diagonal2, ansList);
        return ansList;
    }

    private void dfs(
            int row,
            int n,
            char[][] matrix,
            boolean[] colVisited,
            boolean[] diagonal1,
            boolean[] diagonal2,
            List<List<String>> ansList) {
        // 所有行都处理完毕，找到一个解
        if (row == n) {
            List<String> solution = new ArrayList<>(n);
            for (char[] chars : matrix) {
                solution.add(new String(chars));
            }
            ansList.add(solution);
            return;
        }

        // 在当前行的每一列尝试放置皇后
        for (int col = 0; col < n; col++) {
            int d1 = row + col; // 主对角线索引
            int d2 = row - col + n - 1; // 副对角线索引

            // 检查冲突：列、两条对角线
            if (!colVisited[col] && !diagonal1[d1] && !diagonal2[d2]) {
                // 放置皇后
                matrix[row][col] = 'Q';
                colVisited[col] = diagonal1[d1] = diagonal2[d2] = true;

                // 递归处理下一行
                dfs(row + 1, n, matrix, colVisited, diagonal1, diagonal2, ansList);

                // 回溯：撤销选择
                matrix[row][col] = '.';
                colVisited[col] = diagonal1[d1] = diagonal2[d2] = false;
            }
        }
    }
}
