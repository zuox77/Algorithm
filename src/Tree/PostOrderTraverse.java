package Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/*
刷题次数: 2
第二次: 基本记得

https://leetcode.cn/problems/binary-tree-postorder-traversal/description/

Given the root of a binary tree, return the postorder traversal of its nodes' values.

Input: root = [1,null,2,3]
Output: [3,2,1]

思路：
一共5种解法
具体思路看PreOrderTraverse
*/

public class PostOrderTraverse {
    // 递归 + DFS
    public List<Integer> solution1(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        // 递归
        dfs(root, result);
        // 返回
        return result;
    }

    public void dfs(TreeNode node, List<Integer> result) {
        // 递归出口
        if (node == null) {
            return;
        }
        // 左 - 右 - 根
        if (node.left != null) {
            dfs(node.left, result);
        }
        if (node.right != null) {
            dfs(node.right, result);
        }
        result.add(node.val);
    }

    // 分治 + DFS
    public List<Integer> solution2(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        // corner case,也是递归的出口
        if (root == null) {
            return result;
        }
        // 分治的分
        List<Integer> left = solution2(root.left);
        List<Integer> right = solution2(root.right);
        // 分治的治
        result.addAll(left);
        result.addAll(right);
        result.add(root.val);
        // 返回
        return result;
    }

    // stack

    public List<Integer> solution4(TreeNode root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        Deque<PostOrderTraverse.State> stack = new ArrayDeque<>();
        // 先检查是否是null, 不然加进stack会报错
        if (root == null) {
            return result;
        }
        // 将root放入stack
        stack.push(new State(root, 0));
        // 开始遍历
        while (!stack.isEmpty()) {
            // 将栈顶弹出
            PostOrderTraverse.State now = stack.pop();
            TreeNode node = now.node;
            int indicator = now.indicator;
            // 先检查null, 避免后续node.left/node.right出错
            if (node == null) {
                continue;
            }
            // 这是一个通用template, 其本质就是逆向添加进stack, 再一一弹出
            // 对于前序遍历, 其实下面的代码就够了:
            // stack.push(new State(node.right, 0));
            // stack.push(new State(node.left, 0));
            // stack.push(new State(node, 1));
            // 之所以添加其他的, 是因为写成了一个通用公式, 只需要改变添加进result的时候的数值, 就可以做中序/后序遍历
            if (indicator == 0) {
                stack.push(new State(node, 3));
                stack.push(new State(node.right, 0));
                stack.push(new State(node, 2));
                stack.push(new State(node.left, 0));
                stack.push(new State(node, 1));
            }
            // 1代表前序遍历, 2代表中序遍历, 3代表后续遍历
            if (indicator == 1) {
                result.add(node.val);
            }
        }
        return result;
    }

    // stack2
    class State {
        TreeNode node;
        int indicator;

        public State(TreeNode node, int indicator) {
            this.node = node;
            this.indicator = indicator;
        }
    }

    // morris
}
