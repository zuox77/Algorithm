package BFSAndDFS;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
https://leetcode.cn/problems/shortest-path-to-get-all-keys/description/

You are given an m x n grid where:
'.' is an empty cell.
'#' is a wall.
'@' is the starting point.
Lowercase letters represent keys.
Uppercase letters represent locks.

You start at the starting point and one move consists of walking one space in one of the four cardinal directions.
You cannot walk outside the grid, or walk into a wall.
If you walk over a key, you can pick it up, and you cannot walk over a lock unless you have its corresponding key.
For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English alphabet in the grid.
This means that there is exactly one key for each lock, and one lock for each key; and also that the letters used to represent the keys and locks were chosen in the same order as the English alphabet.
Return the lowest number of moves to acquire all keys. If it is impossible, return -1.

Example 1:
Input: grid = ["@.a..","###.#","b.A.B"]
Output: 8
Explanation: Note that the goal is to obtain all the keys not to open all the locks.

Example 2:
Input: grid = ["@..aA","..B#.","....b"]
Output: 6

Example 3:
Input: grid = ["@Aa"]
Output: -1

思路: BFS + 位掩码
1. 要找到最短的步数,最简单的就是用BFS,因为先到的一定最短,DFS的话需要用一个变量记录步长
2. 这道题的核心是如何用相应的钥匙去开锁
    普通的做法可以用一个set去保存当前已经获得的key,然后去查找
    但问题是,比如上方有钥匙,左右下都没有钥匙
    从上方继续进行的下一层遍历就会有这个钥匙,但左右下的下一层遍历会没有这个钥匙,这是两种不同的状态
    所以用set的话,我们就需要对每一个格子都创建一个set,不然的话,无法记录,什么时候去到这一步有没有钥匙
3. 解法:用bitMask去做,通过二进制上面的1的位置,代表拥有第几个钥匙,同样用相应的方法判断是第几个锁,然后匹配
4. 创建一个int[][][]数组record,其中record[i][j][k]=x代表
    第i行,第j列,当bitMask为k时,所需要的步数是多少
5. 用一个queue去做BFS遍历,queue需要装一个三元组,分别是行,列,bitMask的信息
 */
public class ShortestPathToGetAllKeys {

    private static final int[][] DIRECTIONS =
            new int[][] {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1, 0}
            };

    public int shortestPathAllKeys(String[] grid) {
        // 遍历grid记录起始点,添加起点进queue,并计算有多少个钥匙
        // 之所以要记录起始点是因为我们需要对起点初始化
        int startX = 0, startY = 0, totalKeys = 0;
        int m = grid.length, n = grid[0].length();
        Deque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char cell = grid[i].charAt(j);
                if (cell == '@') {
                    startX = i;
                    startY = j;
                    queue.addFirst(new int[] {i, j, 0}); // 0是因为此时一定没有任何key,所以状态为0
                } else if (cell >= 'a' && cell <= 'z') {
                    totalKeys++;
                }
            }
        }
        // 记录搜集所有的key的状态
        int fullKeys = (1 << totalKeys) - 1;
        // 创建record数组
        int[][][] records = new int[m][n][1 << totalKeys]; // 1 << totalKeys代表一共有这么多种状态
        // 初始化
        for (int[][] record : records) {
            for (int[] row : record) {
                Arrays.fill(row, -1); // -1 代表步数的默认值,因为如果最终抵达不了,我们可以直接返回-1
            }
        }
        records[startX][startY][0] = 0; // 对起始位置初始化,其步数为0

        // BFS
        while (!queue.isEmpty()) {
            int[] current = queue.removeLast();
            int x = current[0], y = current[1], bitMask = current[2];
            int step = records[x][y][bitMask];
            // 遍历4个方向
            for (int[] dire : DIRECTIONS) {
                int newX = x + dire[0];
                int newY = y + dire[1];
                // 检查边界
                if (0 > newX || 0 > newY || newX >= m || newY >= n) continue;
                // 对每一个格子里面的情况做判断
                char cell = grid[newX].charAt(newY);
                // 如果是墙,直接跳过
                if (cell == '#') continue;
                // 如果是锁,检查是否有key
                if (cell >= 'A' && cell <= 'Z') {
                    int lock = cell - 'A';
                    boolean hasKey = (bitMask >> lock & 1) == 1;
                    // 如果没有key,直接跳过,有key的话,就等于一个空房间,放在后面处理
                    if (!hasKey) continue;
                }
                // 如果是key,更新bitMask
                int nextBitMask = bitMask;
                if (cell >= 'a' && cell <= 'z') {
                    int key = cell - 'a';
                    nextBitMask |= 1 << key;
                }
                // 因为题目要求搜集完所有的key而不是开完所有的锁,所以此时可以判断下是否搜集完,搜集完可以直接返回
                if (nextBitMask == fullKeys) return step + 1;
                // 此时需要考虑是否遍历过,只有record[x][y][bitMask] == -1时,才代表未遍历过
                if (records[newX][newY][nextBitMask] != -1) continue;
                // 此时,剩下的情况为:以nextBitMask这个状态走到newX和newY时,是没有visited的,所以需要更新步数以及加入queue
                records[newX][newY][nextBitMask] = step + 1;
                queue.addFirst(new int[] {newX, newY, nextBitMask});
            }
        }
        return -1;
    }
}
