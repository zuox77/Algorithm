package Interview.Airbnb;

/*
A ski resort has mapped out all its routes between various checkpoints on a mountain.
They now want to find the most rewarding ski path from the lodge to any of the designated finish lines.

You are given a Directed Acyclic Graph (DAG) representing this network. The graph consists of:

A set of one-way routes, each with associated travel cost.
A set of checkpoints, each with a reward value for visiting it.

You are given two input arrays:
routes: An array where each element [from, cost, to] describes a route from checkpoint from to checkpoint to with travel cost.
checkpoints: An array where each element [checkpoint, reward] specifies the reward gained upon arriving at that checkpoint.
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class MaximumSkiScore {
    public int maxReward(int[][] routes, int[][] checkpoints) {
        // 找到最大的checkpoint的id
        int maxId = 0;
        for (int[] route : routes) {
            maxId = Math.max(maxId, route[2]);
        }
        for (int[] checkpoint : checkpoints) {
            maxId = Math.max(maxId, checkpoint[0]);
        }
        // 重构checkpoints,变成一个一维数组
        int[] rewards = new int[maxId + 1];
        for (int[] checkpoint : checkpoints) {
            rewards[checkpoint[0]] = checkpoint[1];
        }
        // 建立一个数组,dp[i]代表从出发点到i的最大收益（reward - cost）
        int[] dp = new int[maxId + 1];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = 0;
        // 建立一个数组,indegree[i]代表i的入度,即有几个点可以抵达i
        int[] indegree = new int[maxId + 1];
        // 建立一个数组,edges[i]代表从i可以抵达哪些点,以及它们的cost是多少
        List<int[]>[] edges = new ArrayList[maxId + 1];
        Arrays.setAll(edges, i -> new ArrayList<>());
        // 初始化
        for (int[] route : routes) {
            int from = route[0];
            int to = route[2];
            int cost = route[1];
            indegree[to]++;
            edges[from].add(new int[] {to, cost});
        }
        // 用一个queue来顺序遍历入度为0到最大的入度
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < maxId + 1; i++) {
            if (indegree[i] == 0 && (rewards[i] > 0 || !edges[i].isEmpty())) {
                queue.addFirst(i);
                dp[i] = rewards[i];
            }
        }
        // 一层一层遍历并更新dp
        if (queue.isEmpty()) return -1;
        while (!queue.isEmpty()) {
            int from = queue.pollLast();
            // 遍历该checkpoint的所有edge
            for (int[] edge : edges[from]) {
                int to = edge[0];
                int cost = edge[1];
                // 更新dp
                if (dp[from] != Integer.MIN_VALUE)
                    dp[to] = Math.max(dp[to], dp[from] - cost + rewards[to]);
                // 更新indegree并看情况加入queue
                if (--indegree[to] == 0) queue.addFirst(to);
            }
        }

        // 找到最大收益
        int maxReward = 0;
        for (int i = 1; i < maxId + 1; i++) {
            if (dp[i] != Integer.MIN_VALUE) maxReward = Math.max(maxReward, dp[i]);
        }
        return Math.max(0, maxReward);
    }
}
