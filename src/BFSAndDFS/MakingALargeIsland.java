package BFSAndDFS;

/*
https://leetcode.cn/problems/making-a-large-island/description/

You are given an n x n binary matrix grid. You are allowed to change at most one 0 to be 1.
Return the size of the largest island in grid after applying this operation.
An island is a 4-directionally connected group of 1s.

Example 1:
Input: grid = [[1,0],[0,1]]
Output: 3
Explanation: Change one 0 to 1 and connect two 1s, then we get an island with area = 3.

Example 2:
Input: grid = [[1,1],[1,0]]
Output: 4
Explanation: Change the 0 to 1 and make the island bigger, only one island with area = 4.

Example 3:
Input: grid = [[1,1],[1,1]]
Output: 4
Explanation: Can't change any 0 to 1, only one island with area = 4.

思路: DFS
1. 先遍历所有的岛，并将其重新编号，与0和1区分开，然后用一个map记录编号和面积
2. 再遍历所有的0，对每一个0，上下左右四个方向，找grid上面是否有编号，如果有，通过第一步的map找到面积并加上
3. 记住判断没有岛屿的情况
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MakingALargeIsland {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public int largestIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        // 创建一个Map记录每个岛屿的面积
        Map<Integer, Integer> areaMap = new HashMap<>();
        /*
        注意：

         创建一个Map，记录每个岛屿的边界坐标（并非矩阵边界，而是水的边界）
         Map<Integer, Set<Integer>> edgeMap = new HashMap<>();

         曾经想这么做，但不行，理由是：
         如果是想记录每个岛的水边界的坐标，以此来优化时间，但实际情况是，一个岛的水边界可能和不同的岛连接在一起
         例如
            {2, 0, 3}
            {0, 0, 0}
            {0, 4, 4}
         如果是3这个岛屿，3的水边界有3个
         3左侧的0和2岛屿连接，计算面积的时候就是1+1+1=3（中间连接处也要算进去）
         但再往后遍历的时候，3的下面的0和4岛屿连接，计算面积的时候就是3+2=5
         相当于连接了两次
         所以这里只能通过计算出来areaMap以后，遍历所有0，这样才能确保每次都只连接一次
         */
        // 创建一个变量记录每个岛屿的新编号，从2开始，避开0和1
        // 如果用1的话，dfs里面就不能用来记录visited了
        int symbol = 2;
        // 找到每个岛的面积，以及其边界，并且赋予其新的编号
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    Set<Integer> edges = new HashSet<>();
                    int area = dfs(grid, m, n, i, j, symbol, edges);
                    // 记录当前面积
                    areaMap.put(symbol, area);
                    // 更新symbol
                    symbol++;
                }
            }
        }

        // 特判没有岛的情况
        if (areaMap.isEmpty()) return 1;

        // 通过edgeList，找到每个edgeList上下左右是否有其他岛屿，如果有，计算新的面积
        int maxArea = 0;
        // 遍历所有0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 跳过所有非0的点
                if (grid[i][j] != 0) continue;
                // 1 -> 根据题目里面的，change at most one 0 to 1，所以面积加一，当前岛屿的面积会在后面加上
                // 因为有visited，可以保证不重复，所以不额外写一次
                int area = 1;
                // 用一个visited记录已经添加过的岛屿编号，防止重复添加
                Set<Integer> visited = new HashSet<>();
                // 可以先添加一个0进去，相当与下面检查时候的排除其余的0
                visited.add(0);
                // 找到当前坐标的四个方向是否有其他岛屿，如果有，加上面积
                for (int[] dire : DIRECTIONS) {
                    int newX = i + dire[0];
                    int newY = j + dire[1];
                    // 检查边界 + 添加visited
                    // 本来应该再检查一下grid[newX][newY] == 0，但因为visited已经添加了0，所以不需要检查了
                    if (!isValid(m, n, newX, newY) || !visited.add(grid[newX][newY])) continue;
                    // 计算面积
                    area += areaMap.get(grid[newX][newY]);
                }
                // 更新最大面积
                maxArea = Math.max(maxArea, area);
            }
        }
        // 如果maxArea为0，说明所有格子都是1，那么返回n * n
        return maxArea == 0 ? n * n : maxArea;
    }

    private int dfs(int[][] grid, int m, int n, int x, int y, int symbol, Set<Integer> edges) {
        // 超过边界或者已经遍历过
        if (!isValid(m, n, x, y) || grid[x][y] == symbol || grid[x][y] == 0) return 0;
        // 赋予新的标号
        grid[x][y] = symbol;
        // 更新面积
        int area = 1;
        // 按照方向遍历四周计算面积
        for (int[] dire : DIRECTIONS) {
            area += dfs(grid, m, n, x + dire[0], y + dire[1], symbol, edges);
        }
        return area;
    }

    private boolean isValid(int m, int n, int x, int y) {
        return 0 <= x && 0 <= y && x < m && y < n;
    }
}
