package Graph;

/*
刷题次数: 1

https://leetcode.cn/problems/network-delay-time/

You are given a network of n nodes, labeled from 1 to n.
You are also given times, a list of travel times as directed edges times[i] = (ui, vi, wi),
where ui is the source node, vi is the target node,
and wi is the time it takes for a signal to travel from source to target.

We will send a signal from a given node k.
Return the minimum time it takes for all the n nodes to receive the signal.
If it is impossible for all the n nodes to receive the signal, return -1.

Example 1:
Input: times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
Output: 2

Example 2:
Input: times = [[1,2,1]], n = 2, k = 1
Output: 1

Example 3:
Input: times = [[1,2,1]], n = 2, k = 2

解析：https://leetcode.cn/problems/network-delay-time/solutions/2659415/dijkstra-po-su-yu-dui-you-hua-dai-ma-sui-kp1q/

注意：
1. dijkstra只能针对于没有负值的情况！
2. 图有两种：
    1. 稠密图：当边数量接近点的数量的平方，即 m≈n^2 时，可定义为稠密图
    2. 稀疏图：当边数量接近点的数量，即 m≈n 时，可定义为稀疏图
3. 保存图的方法有两种：
    1. 邻接矩阵：适用于边数较多的稠密图使用（矩阵空间为n*n，边数多，浪费的空间少）
    2. 邻接表：适用于边数较少的稀疏图使用（矩阵空间为n*n，边数少，浪费的空间多，用邻接表不浪费空间）

思路: 邻接矩阵 + 朴素dijkstra
1. 构建邻接矩阵grid，grid[i][j]代表从i到j的距离，grid[j][i]的距离
    初始化grid全为Integer.MAX_VALUE，这样Integer.MAX_VALUE可以代表无法抵达的点（没有任何路径可以抵达）
2. 构建一个长度为n + 1的数组，保存minDist，其中minDist[i]代表源node到i的最短距离
3. 构建一个长度为n + 1的数组，保存visited，其中visited[i]代表i是否被访问过，或者与i相连的路径是否已经被计算过，避免重复计算
    第二步和第三步之所以是n + 1，是因为本题中node的编号是从1开始的，所以为了方便记录，这两个数组也是从1开始计算
    这两个数组的首位没有意义
4. 初始化这两个数组，一个是Integer.MAX_VALUE，一个是false
5. 从1开始遍历到n，在循环中需要：
    1. 每次重置minValue，下面有解释
    2. 找到当前未被访问的node中，哪个node距离源node1，的距离最小，即未被访问过的node中，minDist的当前最小值
    3. 将这个node标记为已访问
    4. 从1开始遍历到n，将grid[源node][第三步的node]的距离更新给minDist，需要用Math.min，以保证minDist只保留最短距离这个定义
6. 遍历minDist数组，找到其最大值，如果有Integer.MAX_VALUE，返回-1
时间复杂度: O(n^2)
空间复杂度: O(n^2)

思路：领接表 + 朴素dijkstra
1. 邻接表和邻接矩阵的区别在于，邻接表是一个二维的数组，数组的每一个位置用链表或者列表保存，
    列表里面保存一个自定义的类Pair，Pair记录node的值和距离类似于
    [
      0 -> null（可以不用初始化这个位置）
      1 -> Pair<3, 2> （代表node1到node3的距离为2）
      2 -> Pair<3, 1> （代表node2到node3的距离为1）-> Pair<4, 3> （代表node2到node4的距离为3）
      3 -> ...
      4 -> ...
      5 -> ...
        ]
    相比较邻接矩阵，这样空间占用更少，不需要用Integer.MAX_VALUE去代表无法抵达的点，邻接表中，不存在即是无法抵达
    比如2能到3，2能到4，不需要去保存：2到1的距离为Integer.MAX_VALUE，因为不存在就是无法抵达
2. 其余不变
时间复杂度: O(n^2)
空间复杂度: O(n^2)

思路：堆 + dijkstra
1. 将第一个思路中，5.2替换，5.2是利用for循环去找到当前minDist的最小值，选定这个最小值所代表的node为下一个计算的节点
    而这里用堆，所以我们不需要for循环去找，直接利用最小堆（默认就是最小堆）的特性，直接将堆顶推出，即可得到
2. 用堆的话，我们不需要visited数组了，因为堆本身也是一个数组，我们只需要通过条件，当某个新节点可以更新minDist的时候，再将其入堆即可
3. 需要跳过距离更远的堆：if (headDist > minDist[headNode]) continue;
4. 利用一个变量记录剩余未访问过的节点，如果最后这个变量不为0，那么说明有无法抵达的地方，返回-1
5. 利用一个变量记录maxDist，当跳过第三步里的距离更远的堆以后，将maxDist更新为符合条件的堆顶的dist
6. 为什么这里可以用maxDist记录，而不是非得像前两种解法一样，需要等计算完所有的minDist，然后再遍历一次找到最大值？
    因为堆的特性
    前两种解法中提到：（第x行）
             注意：我们不能在计算minDist的途中一起计算ans，因为其中可能出现非最短距离
             例如从1到2为1，从2到3为2，从1直接到3为4
             因为要计算到最远node的最短值，那么无可避免，我们一定得用Math.max去找到最远的node
             但如果当前的值还不是最短距离，我们无法判断
             刚刚的例子中：如果1到3距离为4，ans = Math.max(ans, 4) = 4
             但后面发现1到3的最短距离为2，那我们无法保存这个值
             ans = Math.max(ans, 4) = Math.max(4, 3) = 4
             我们永远只能得到4
             所以必须等minDist计算完成，保证minDist里面保存的都是最短距离以后，我们才能开始计算ans
    而堆会自动帮我们排序：如果1到3距离为4，假设当前堆顶为3，那么此时maxDist = headDist = 4
    但由于堆会排序，所以如果我们还有 从1到2为1，从2到3为2 这两个信息，那么这两个信息一定会比1到3距离为4更早出堆
    所以等到当前堆顶真的为3时，会用到第三步的判断if (headDist > minDist[headNode]) continue;
    headDist = 4，但是minDist[headNode]已经被更新成3了，所以直接跳过，maxDist不受影响


时间复杂度: O(n^2)
空间复杂度: O(n^2)
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class NetworkDelayTime {
    public int networkDelayTime(int[][] times, int n, int k) {
        // 创建一个邻接矩阵
        // n+1是因为本题中给于的node是从1开始计算的
        int[][] grid = new int[n + 1][n + 1];
        // 用最大值初始化邻接矩阵
        for (int i = 0; i < n + 1; i++) {
            Arrays.fill(grid[i], Integer.MAX_VALUE);
        }
        // 建立邻接矩阵
        // 邻接矩阵grid[i][j]代表从i到j的距离
        for (int[] time : times) {
            int x = time[0];
            int y = time[1];
            grid[x][y] = time[2];
        }

        // 创建一个minDist数组，下标代表node，数组的数字代表源node到下标node的最短距离
        // 例如此题，k=2，源node为2，minDist[5]=3代表从node2到node5的最短距离为3
        // 同理，minDist[2]=0，因为从node2到node2的最短距离为0
        // 仍旧定义为n+1位
        int[] minDist = new int[n + 1];
        // 初始化为最大值
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[k] = 0;

        // 创建一个visited数组，用来保存当前已经遍历过的node，以免重复遍历
        boolean[] visited = new boolean[n + 1];
        Arrays.fill(visited, false);

        // 更新minDist
        // 这里的i不重要，纯粹是为了能刚好把所有node都遍历一遍
        int curNode = 1;
        for (int i = 1; i < n + 1; i++) {
            /*
            如果不每次都重置minVal，在后几行的比较代码中，可能出现上一轮的minVal比这一轮的都小，导致无法继续的情况
            假设源node为1，假设上一轮获得的信息是：1 -> 3 = 4，1 -> 2 = 1
            按照算法，这一轮我们应该从node2开始，根据node2能抵达的点，计算源node1距离这些点的最短距离
            假设node2的信息有：2 -> 6 = 4，2 -> 3 = 2，2 -> 4 = 5
            那这一轮我们的目标是，已知1 -> 2 = 1，计算1 -> 6，1 -> 3，1 -> 4的最小距离
            如果不更新minValue，那么现在的minValue = 1（即1 -> 2 = 1），
            而1小于所有的node2能抵达的node的边权重（4、2、5），所以我们就选不出这一轮应该从哪里开始计算了
            */
            // 重置minVal
            int minValue = Integer.MAX_VALUE;

            /*
            例如上面的例子，假设node2的信息有：2 -> 6 = 4，2 -> 3 = 2，2 -> 4 = 5
            那么此时我们应该选node3，因为2 -> 3 = 2，2 < 4 < 5
             */
            // 找到当前未被访问的node中，哪个node距离源node1，的距离最小
            for (int j = 1; j < n + 1; j++) {
                if (!visited[j] && minDist[j] <= minValue) {
                    minValue = minDist[j];
                    curNode = j;
                }
            }
            // 将这个node标为已访问
            visited[curNode] = true;
            // 更新minDist
            for (int j = 1; j < n + 1; j++) {
                /*
                !visited[j]：该node需要未访问过，不然会覆盖之前的值
                grid[curNode][j] != Integer.MAX_VALUE：根据我们的定义，grid[i][j]代表从i到j的距离
                且我们将其初始化为最大值，所以
                如果grid[i][j] == Integer.MAX_VALUE，代表i无法抵达j，或者说i和j无法通行
                */
                if (!visited[j] && grid[curNode][j] != Integer.MAX_VALUE) {
                    /*
                    minDist[j]：node j距离源node最小距离
                    之所以用Math.min，是因为无法确定之前计算出的minDist[j]一定是node j距离源node最小距离
                    例如从1到2为1，从2到3为2，从1直接到3为4，
                    那么我们之前计算出的minDist[3] = 4，因为1和3直接相连，我们每次循环只会遍历j（即上面选出来的节点）所能连接的节点
                    而现在我们能看出，其实1到3的最短距离为1 + 2 = 3，所以要更新
                     */
                    minDist[j] = Math.min(minDist[j], minDist[curNode] + grid[curNode][j]);
                }
            }
        }
        // 找到从源node到所有node的最小距离，即源node到距离其最远的node的最小距离
        int ans = 0;
        /*
        在循环中，我们判断是否有Integer.MAX_VALUE，如果有说明：
        1. 这是minDist[0]，因为我们为了方便计算下标，而node的编号又是从1开始的，所以minDist[0]理论上是一个不存在的数
        2. 代表源node到该节点的距离为Integer.MAX_VALUE，即无法抵达
        而此时如果直接用for (int num: minDist)，我们无法分开这两个情况
        对于情况1，我们需要直接跳过
        对于情况2，我们需要直接返回，因为有源node无法抵达的点，所以按题目要求，返回源node到所有node的最短时间，那么就是-1
        所以我们只能用for (int i = 1; i < n + 1; i++)
        */
        for (int i = 1; i < n + 1; i++) {
            // 没有路径的
            if (minDist[i] == Integer.MAX_VALUE) return -1;
            /*
            计算最大值
            记住，minDist[i]代表从源node到i的最短距离，而题目要求是抵达所有node的最短距离
            所以为了抵达所有node，ans其实就是抵达最远的node的最短距离，即minDist的最大值

            注意：我们不能在计算minDist的途中一起计算ans，因为其中可能出现非最短距离
            例如从1到2为1，从2到3为2，从1直接到3为4
            因为要计算到最远node的最短值，那么无可避免，我们一定得用Math.max去找到最远的node
            但如果当前的值还不是最短距离，我们无法判断
            刚刚的例子中：如果1到3距离为4，ans = Math.max(ans, 4) = 4
            但后面发现1到3的最短距离为2，那我们无法保存这个值
            ans = Math.max(ans, 4) = Math.max(4, 3) = 4
            我们永远只能得到4
            所以必须等minDist计算完成，保证minDist里面保存的都是最短距离以后，我们才能开始计算ans
            */
            ans = Math.max(ans, minDist[i]);
        }
        return ans;
    }

    public int networkDelayTime2(int[][] times, int n, int k) {
        // 创建一个邻接矩阵
        // n+1是因为本题中给于的node是从1开始计算的
        List<Pair>[] grid = new List[n + 1];
        // 用最大值初始化邻接矩阵
        for (int i = 1; i < n + 1; i++) {
            grid[i] = new ArrayList<Pair>();
        }
        // 建立邻接表
        for (int[] time : times) {
            int x = time[0];
            int y = time[1];
            grid[x].add(new Pair(time[1], time[2]));
        }

        // 创建一个minDist数组，下标代表node，数组的数字代表源node到下标node的最短距离
        // 例如此题，k=2，源node为2，minDist[5]=3代表从node2到node5的最短距离为3
        // 同理，minDist[2]=0，因为从node2到node2的最短距离为0
        // 仍旧定义为n+1位
        int[] minDist = new int[n + 1];
        // 初始化为最大值
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[k] = 0;

        // 创建一个visited数组，用来保存当前已经遍历过的node，以免重复遍历
        boolean[] visited = new boolean[n + 1];
        Arrays.fill(visited, false);

        // 更新minDist
        // 这里的i不重要，纯粹是为了能刚好把所有node都遍历一遍
        int curNode = 1;
        for (int i = 1; i < n + 1; i++) {
            /*
            如果不每次都重置minVal，在后几行的比较代码中，可能出现上一轮的minVal比这一轮的都小，导致无法继续的情况
            假设源node为1，假设上一轮获得的信息是：1 -> 3 = 4，1 -> 2 = 1
            按照算法，这一轮我们应该从node2开始，根据node2能抵达的点，计算源node1距离这些点的最短距离
            假设node2的信息有：2 -> 6 = 4，2 -> 3 = 2，2 -> 4 = 5
            那这一轮我们的目标是，已知1 -> 2 = 1，计算1 -> 6，1 -> 3，1 -> 4的最小距离
            如果不更新minValue，那么现在的minValue = 1（即1 -> 2 = 1），
            而1小于所有的node2能抵达的node的边权重（4、2、5），所以我们就选不出这一轮应该从哪里开始计算了
            */
            // 重置minVal
            int minValue = Integer.MAX_VALUE;

            /*
            例如上面的例子，假设node2的信息有：2 -> 6 = 4，2 -> 3 = 2，2 -> 4 = 5
            那么此时我们应该选node3，因为2 -> 3 = 2，2 < 4 < 5
             */
            // 找到当前未被访问的node中，哪个node距离源node1，的距离最小
            for (int j = 1; j < n + 1; j++) {
                if (!visited[j] && minDist[j] <= minValue) {
                    minValue = minDist[j];
                    curNode = j;
                }
            }
            // 将这个node标为已访问
            visited[curNode] = true;
            // 更新minDist
            // 这里只需要遍历邻接表的第curNode个即可
            for (Pair pair : grid[curNode]) {
                if (!visited[pair.node]) {
                    minDist[pair.node] = Math.min(minDist[pair.node], minDist[curNode] + pair.dist);
                }
            }
        }
        // 找到从源node到所有node的最小距离，即源node到距离其最远的node的最小距离
        int ans = 0;
        /*
        在循环中，我们判断是否有Integer.MAX_VALUE，如果有说明：
        1. 这是minDist[0]，因为我们为了方便计算下标，而node的编号又是从1开始的，所以minDist[0]理论上是一个不存在的数
        2. 代表源node到该节点的距离为Integer.MAX_VALUE，即无法抵达
        而此时如果直接用for (int num: minDist)，我们无法分开这两个情况
        对于情况1，我们需要直接跳过
        对于情况2，我们需要直接返回，因为有源node无法抵达的点，所以按题目要求，返回源node到所有node的最短时间，那么就是-1
        所以我们只能用for (int i = 1; i < n + 1; i++)
        */
        for (int i = 1; i < n + 1; i++) {
            // 没有路径的
            if (minDist[i] == Integer.MAX_VALUE) return -1;
            /*
            计算最大值
            记住，minDist[i]代表从源node到i的最短距离，而题目要求是抵达所有node的最短距离
            所以为了抵达所有node，ans其实就是抵达最远的node的最短距离，即minDist的最大值

            注意：我们不能在计算minDist的途中一起计算ans，因为其中可能出现非最短距离
            例如从1到2为1，从2到3为2，从1直接到3为4
            因为要计算到最远node的最短值，那么无可避免，我们一定得用Math.max去找到最远的node
            但如果当前的值还不是最短距离，我们无法判断
            刚刚的例子中：如果1到3距离为4，ans = Math.max(ans, 4) = 4
            但后面发现1到3的最短距离为2，那我们无法保存这个值
            ans = Math.max(ans, 4) = Math.max(4, 3) = 4
            我们永远只能得到4
            所以必须等minDist计算完成，保证minDist里面保存的都是最短距离以后，我们才能开始计算ans
            */
            ans = Math.max(ans, minDist[i]);
        }
        return ans;
    }

    public int networkDelayTime3(int[][] times, int n, int k) {
        // 创建一个邻接矩阵
        // n+1是因为本题中给于的node是从1开始计算的
        List<Pair>[] grid = new List[n + 1];
        // 用最大值初始化邻接矩阵
        for (int i = 1; i < n + 1; i++) {
            grid[i] = new ArrayList<Pair>();
        }
        // 建立邻接表
        for (int[] time : times) {
            int x = time[0];
            int y = time[1];
            grid[x].add(new Pair(time[1], time[2]));
        }

        // 创建一个minDist数组，下标代表node，数组的数字代表源node到下标node的最短距离
        // 例如此题，k=2，源node为2，minDist[5]=3代表从node2到node5的最短距离为3
        // 同理，minDist[2]=0，因为从node2到node2的最短距离为0
        // 仍旧定义为n+1位
        int[] minDist = new int[n + 1];
        // 初始化为最大值，初始化源node到源node为0
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[k] = 0;

        // 创建一个堆，此时不需要visited了，因为只有特定条件的才会入堆
        PriorityQueue<Pair> heap = new PriorityQueue<>(n, (a, b) -> a.dist - b.dist);
        // 将源node入堆
        heap.offer(new Pair(k, 0));

        // 更新minDist
        // 这里不需要for循环了，直接遍历堆即可
        int maxDist = 0; // 最终答案
        int notVisited = n; // 记录仍未被访问到的节点有几个
        while (!heap.isEmpty()) {
            // 推出堆顶
            Pair headPair = heap.poll();
            int headDist = headPair.dist;
            int headNode = headPair.node;
            /*
            如果堆中的这个pair的dist大于minDist[node]，说明node之前已经出过堆
            即现在的这个pair所代表的dist，已经不是最短距离了，直接跳过
            例如1到2的距离为1，2到3的距离为2，1到3的距离为5
            那么可能在Pair<3, 5>出堆的时候，我们已经通过前两个条件，将minDist[3]更新成3了，
            此时我们不需要将1到3的距离为5这个信息，覆盖到现在的minDist[3]=3，所以直接跳过
            */
            if (headDist > minDist[headNode]) continue;
            // 更新maxDist
            maxDist = headDist;
            // 更新剩余未被访问的节点个数
            notVisited--;
            // 遍历堆顶节点所连接的节点
            for (Pair pair : grid[headNode]) {
                int pairDist = pair.dist;
                int pairNode = pair.node;
                /*
                headNode这个节点，就好比之前用for loop每次去找到的当前最小值
                假设node2的信息有：2 -> 6 = 4，2 -> 3 = 2，2 -> 4 = 5
                由于堆的特性，所以2 -> 3 = 2会是堆顶，headNode = 3，headDist = 2
                此时来遍历grid[headNode] = grid[3]
                假设node3的信息有：3 -> 4 = 4, 3 -> 6 = 3
                那么对于3 -> 4 = 4，newDist代表通过3 -> 4 = 4这个信息，去计算源node到4的距离
                headNode是3，所以headDist代表从2到3的距离为2
                pairNode是4，所以pairDist代表从3到4的距离为4
                所以newDist代表从2到4的距离为2 + 4 = 6
                然后检查minDist[4]，判断一下当前记录的从2到4的距离是多少，并将其更新为值小的那个
                 */
                int newDist = headDist + pairDist;
                if (newDist < minDist[pairNode]) {
                    minDist[pairNode] = newDist;
                    heap.offer(new Pair(pairNode, newDist));
                }
            }
        }

        // 当notVisited不为0时，说明有无法抵达的node，直接返回-1，否则返回maxDist
        return notVisited == 0 ? maxDist : -1;
    }

    static class Pair {
        public int node;
        public int dist;

        Pair(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }
}
