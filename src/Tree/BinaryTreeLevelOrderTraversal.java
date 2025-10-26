package Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/*
刷题次数：2
第二次：记得
https://leetcode.cn/problems/binary-tree-level-order-traversal/description/

Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).

Input: root = [3,9,20,null,null,15,7]
Output: [[3],[9,20],[15,7]]

思路：BFS
1. 很纯正的BFS：一个queue,while这个queue不为空时,不断将新的node添加进queue,直到queue为空
2. 这道题很经典的考察层序遍历里面,判断每一层数量的问题
3. 两种解法：
   1. 通过一个变量count记录每一层有多少个数
   2. 通过在每一次while循环开始的时候,计算当前queue的长度,就能判断上一层有多少个数
 */

public class BinaryTreeLevelOrderTraversal {
    public List<List<Integer>> solution1(Node root) {
        // 定义变量
        Queue<Node> queue = new ArrayDeque<>();
        List<List<Integer>> result = new ArrayList<>();
        // corner case
        if (root == null) {
            return result;
        }
        // 将root添加进queue
        queue.offer(root);
        // 定义变量count去计算每一层有多少个节点
        int count = 1; // 初始化为1因为root就1个节点,root为null已经被排除掉了
        // 通过BFS遍历
        while (!queue.isEmpty()) {
            // 用另一个变量记录上次的count
            int prevCount = count;
            // 重置count
            count = 0;
            // 定义一个变量记录每一层的值
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < prevCount; i++) {
                // poll出第一个数
                Node node = queue.poll();
                // 将根节点添加进level
                level.add(node.val);
                // 左子节点
                if (node.left != null) {
                    queue.offer(node.left);
                    count++;
                }
                // 右子节点
                if (node.right != null) {
                    queue.offer(node.right);
                    count++;
                }
            }
            // 将level添加进result
            result.add(level);
        }
        // 返回
        return result;
    }

    public List<List<Integer>> levelOrder(Node root) {
        // 定义变量
        Queue<Node> queue = new ArrayDeque<>();
        List<List<Integer>> result = new ArrayList<>();
        // 定义一个变量记录每一层的值
        List<Integer> level = new ArrayList<>();
        // corner case
        if (root == null) {
            return result;
        }
        // 将root添加进queue
        queue.offer(root);
        // 通过BFS遍历
        while (!queue.isEmpty()) {
            // 用一个变量记录上层有多少个
            int count = queue.size();
            // 每一次去重置level,也是为什么添加进result的时候,不需要新建一个level
            level = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                // poll出第一个数
                Node node = queue.poll();
                // 将根节点添加进level
                level.add(node.val);
                // 左子节点
                if (node.left != null) {
                    queue.offer(node.left);
                }
                // 右子节点
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            // 将level添加进result
            result.add(level);
        }
        // 返回
        return result;
    }
}
