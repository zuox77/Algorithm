package DataStructure;

import java.util.PriorityQueue;
/*
https://leetcode.cn/problems/k-closest-points-to-origin/description/

思路：堆
1. 基本一看见topK的题，都可以想到用堆来做
2. 遍历一遍数组，计算出每个坐标与原点的欧式距离，放进最小堆heap里面
3. for循环k次，每次poll出来一个数放入存放答案的数组里面
时间复杂度：O(NlogK)
空间复杂度：O(N)

需要注意的点：
1. 题目有两个关键信息要在遍历的时候保存，1. 距离 2. 当前数组的下标，因为要求返回的是具体的坐标，所以要知道它在原数组里的index
2. 开始我用HashMap去反应距离和下标的关系，然后发现如果有距离相等的坐标(比如[1, 0]和[0, 1])，那么第二个坐标就会被覆盖
3. 所以如果要用HashMap，记得是HashMap<Integer, List>
4. 更好的办法是直接建一个新的Pair类，用类似于链表/树节点的方式，直接.dist/.index来保存值，写起来很简洁

思路2：快速排序
时间复杂度：平均为O(N)，最坏为O(N^2)
空间复杂度：O(logN)，即递归深度
 */

public class KClosestPointsToOrigin {
    class Pair {
        int dist;
        int index;
        public Pair(int dist, int index) {
            this.dist = dist;
            this.index = index;
        }
    }

    public int[][] Solution(int[][] points, int k) {
        PriorityQueue<Pair> heap = new PriorityQueue<>((a, b) -> a.dist - b.dist);
        int[][] res = new int[k][2];

        for (int i = 0; i < points.length; i++) {
            int dis = distance(points[i][0], points[i][1]);
            Pair pair = new Pair(dis, i);
            heap.add(pair);
        }

        for (int i = 0; i < k; i++) {
            res[i] = points[(heap.poll()).index];
        }

        return res;
    }

    public int distance(int x, int y) {
        return x * x + y * y;
    }
}
