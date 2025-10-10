package BFS;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/*
刷题次数: 2

https://leetcode.cn/problems/max-area-of-island/description/

You are given an m x n binary matrix grid.
An island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.)
You may assume all four edges of the grid are surrounded by water.
The area of an island is the number of cells with a value 1 on the island.
Return the maximum area of an island in grid. If there is no island, return 0.

Input: grid = [
[0,0,1,0,0,0,0,1,0,0,0,0,0],
[0,0,0,0,0,0,0,1,1,1,0,0,0],
[0,1,1,0,1,0,0,0,0,0,0,0,0],
[0,1,0,0,1,1,0,0,1,0,1,0,0],
[0,1,0,0,1,1,0,0,1,1,1,0,0],
[0,0,0,0,0,0,0,0,0,0,1,0,0],
[0,0,0,0,0,0,0,1,1,1,0,0,0],
[0,0,0,0,0,0,0,1,1,0,0,0,0]]
Output: 6

思路1: BFS
1. 经典BFS题
2. 思路看BFS.md
3. 需要注意: 记得每次将点加入队列的时候, 就一定要标记为已遍历, 无论是改变值或者放入一个哈希集合

思路2: DFS
1. 无论是BFS还是DFS, 只要是多点式的, 那么都一定需要一个for循环去判断所有的入口
2. 判断是否进入dfs需要判断:
   1. 该坐标是否为1
   2. 是否已经访问过
3. 每次从一个新的1开始的时候, 需要更新重制当前面积

 */
public class MaxAreaOfIsland {
    public int solution1(int[][] grid) {
        // 声明变量
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int result = 0;
        int[] xDirections = new int[]{-1, 1, 0, 0};
        int[] yDirections = new int[]{0, 0, 1, -1};
        // 遍历整个矩阵
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 是0的直接跳过
                if (grid[i][j] == 0) {
                    continue;
                }
                // 找到1, 开始BFS
                queue.offer(new int[]{i, j});
                // 改变值为-1, 标记为已遍历过
                grid[i][j] = -1;
                // 声明变量记录当前岛屿面积
                int currentArea = 1;
                while (!queue.isEmpty()) {
                    int[] location = queue.poll();
                    // 遍历4个方向
                    for (int k = 0; k < 4; k++) {
                        int newX = location[0] + xDirections[k];
                        int newY = location[1] + yDirections[k];
                        if (inRange(m, n, newX, newY) && grid[newX][newY] == 1) {
                            // 加入队列
                            queue.offer(new int[]{newX, newY});
                            // 标记为已遍历过
                            grid[newX][newY] = -1;
                            // 更新岛屿面积
                            currentArea++;
                        }
                    }
                }
                // 结束当前岛屿, 更新一下结果
                result = Math.max(result, currentArea);
            }
        }
        return result;
    }

    public boolean inRange(int m, int n, int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    private int area;
    private int result = 0;
    private int[] xDire = new int[]{0, 0, 1, -1};
    private int[] yDire = new int[]{1, -1, 0, 0};
    private Set<Integer> visited = new HashSet<>();

    public int solution2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        // 遍历
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 如果是0直接跳过
                if (grid[i][j] == 0 || visited.contains(i * n + j)) {
                    continue;
                }
                // 重制面积area
                area = 0;
                // dfs
                dfs(grid, m, n, i, j);
                // 更新最大面积
                result = Math.max(result, area);
            }
        }
        return result;
    }

    public void dfs(int[][] grid, int m, int n, int x, int y) {
        // 递归出口, 即出界或者为0
        if (x < 0 || y < 0 || x >= m || y >= n || grid[x][y] == 0 || visited.contains(x * n + y)) {
            return;
        }
        // 标记为已访问
        visited.add(x * n + y);
        // 面积+1
        area++;
        // 遍历4个方向
        for (int i = 0; i < 4; i++) {
            dfs(grid, m, n, x + xDire[i], y + yDire[i]);
        }
    }
}
