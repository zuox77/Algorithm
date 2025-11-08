package BFSAndDFS;

/*
https://leetcode.cn/problems/number-of-distinct-islands/description/

You are given an m x n binary matrix grid. An island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.)
You may assume all four edges of the grid are surrounded by water.
An island is considered to be the same as another if and only if one island can be translated (and not rotated or reflected) to equal the other.
Return the number of distinct islands.

Example 1:
Input: grid = [[1,1,0,0,0],[1,1,0,0,0],[0,0,0,1,1],[0,0,0,1,1]]
Output: 1

Example 2:
Input: grid = [[1,1,0,1,1],[1,0,0,0,0],[0,0,0,0,1],[1,1,0,1,1]]
Output: 3

思路: DFS
1. 主要是要想到一种办法可以判断一个岛的形状是独一无二的，可以用dfs中的方向
    给上下左右都设置一个symbol，可以是数字也可以是字母，然后每次在上下左右移动时，利用一个StringBuilder去记录走过的方向
    比如1代表上，2代表下，3代表左，4代表右
    然后用其负数代表回撤
    因为都是从左往右、从上往下遍历，所以如果两个岛的形状相同，那么它们一定会有完全一致的路径
    [1,1,0,0,0]
    [1,1,0,0,0]
    [0,0,0,1,1]
    [0,0,0,1,1]
    左上角四个点形成的岛屿，遍历的方向一定是：
    上 上回撤 下 上 上回撤 下 下回撤 左 左回撤 右 上 上 上回撤 ....
2. 然后用一个set去记录所有的路径，有相同的就会被去重，最后set的大小就是答案
3. 详细讲解看这个：https://leetcode.cn/problems/number-of-distinct-islands/solutions/1252714/dfs-suan-fa-miao-sha-dao-yu-xi-lie-ti-mu-m7k1/
    全系列DFS/BFS
 */

import java.util.HashSet;
import java.util.Set;

public class NumberOfDistinctIslands {
    // 创建上下左右方向并放入数组中
    // 用symbol来表示当前的方向，这样可以利用序列化每个岛屿，
    // 通过找到这个岛屿的第一个位置到最后一个位置，最后返回第一个位置的顺序，去分辨不同形状的
    private static final Direction[] directions = {
        new Direction(-1, 0, 1), // 上
        new Direction(1, 0, 2), // 下
        new Direction(0, -1, 3), // 左
        new Direction(0, 1, 4) // 右
    };

    public int numDistinctIslands(int[][] grid) {
        // 用set保存symbol的最终顺序，用来去重
        Set<String> unique = new HashSet<>();

        // 遍历整个矩阵
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 如果扎到岛屿
                if (grid[i][j] == 1) {
                    // 建立一个StringBuilder保存symbol
                    StringBuilder sb = new StringBuilder();
                    dfs(grid, m, n, i, j, sb);
                    unique.add(sb.toString());
                    int cnt = unique.size();
                }
            }
        }
        return unique.size();
    }

    private void dfs(int[][] grid, int m, int n, int row, int col, StringBuilder sb) {
        // 退出条件
        if (0 > row || 0 > col || row >= m || col >= n || grid[row][col] == 0) return;
        // 标记岛屿
        grid[row][col] = 0;
        // 遍历四个方向
        for (Direction dire : directions) {
            // 记录symbol
            sb.append(dire.symbol);
            // 进入下一层
            dfs(grid, m, n, row + dire.row, col + dire.col, sb);
            // 返回以后加入负symbol，表示回撤
            sb.append((-dire.symbol));
        }
    }

    static class Direction {
        int row;
        int col;
        int symbol;

        public Direction(int row, int col, int symbol) {
            this.row = row;
            this.col = col;
            this.symbol = symbol;
        }
    }
}
