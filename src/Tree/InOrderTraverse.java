package Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/*
刷题次数: 2
第二次: 基本记得

https://leetcode.cn/problems/binary-tree-inorder-traversal/description/

Given the root of a binary tree, return the inorder traversal of its nodes' values.

Input: root = [1,null,2,3]
Output: [1,3,2]

思路：
一共5种解法
具体思路看PreOrderTraverse
 */
public class InOrderTraverse {
    // 递归 + DFS
    public List<Integer> solution1(Node root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        // 递归
        dfs(root, result);
        // 返回结果
        return result;
    }

    public void dfs(Node root, List<Integer> result) {
        // 递归出口
        if (root == null) {
            return;
        }
        // 左 - 根 - 右
        dfs(root.left, result);
        result.add(root.val);
        dfs(root.right, result);
    }

    // 递归 + 分治
    public List<Integer> solution2(Node root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        // corner case, 也是递归的出口
        if (root == null) {
            return result;
        }
        // 分治的分
        List<Integer> left = solution2(root.left);
        List<Integer> right = solution2(root.right);
        // 分治的治
        result.addAll(left);
        result.add(root.val);
        result.addAll(right);
        // 返回结果
        return result;
    }

    // stack
    public List<Integer> solution3(Node root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        // 先检查root是否是null, 不然放入stack会报错
        if (root == null) {
            return result;
        }
        // 设置一个新指针, 指向root
        Node node = root;
        // root != null是因为最开始没有讲root添加进stack, 如果单写!stack.isEmpty()就会
        // 直接退出, 所以为了进入循环, 要么root不是null, 要么stack不是空
        while (node != null || !stack.isEmpty()) {
            // 先找到最最左侧的节点
            while (node != null) {
                // 一定要先加入栈, 再移动node
                // 因为第一次运行时, node = root
                // 且root并没有添加到result里面, 所以要先把root加进去
                stack.push(node);
                node = node.left;
            }
            // 将栈顶弹出
            node = stack.pop();
            // 左 - 根 - 右
            // 添加进result
            result.add(node.val);
            // 因为是顺着左侧一路遍历过来的, 所以左节点和根节点都已经添加进stack了, 
            // 所以最后只需要判断并移动到右节点, 注意: 这里不能直接添加进stack, 
            // 因为我们需要遍历每个右节点到其最左侧
            node = node.right;
        }
        // 返回结果
        return result;
    }

    public List<Integer> solution4(Node root) {
        // 定义变量
        List<Integer> result = new ArrayList<>();
        Deque<State> stack = new ArrayDeque<>();
        // 先检查是否是null, 不然加进stack会报错
        if (root == null) {
            return result;
        }
        // 将root放入stack
        stack.push(new State(root, 0));
        // 开始遍历
        while (!stack.isEmpty()) {
            // 将栈顶弹出
            State now = stack.pop();
            Node node = now.node;
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
            if (indicator == 2) {
                result.add(node.val);
            }
        }
        return result;
    }

    // morris
    public List<Integer> inorderTraversal(Node root) {
        // 定义变量
        List<Integer> result = new ArrayList<Integer>();
        // 创建一个指向root的新的指针, 这样移动新指针不会导致丢失对root的引用
        Node now = root;
        // 遍历
        while (now != null) {
            // 判断now.left是否存在
            if (now.left != null) {
                // 新设置一个指针指向now.left
                Node tmp = now.left;
                // 通过遍历, 将tmp遍历到当前节点的最右边节点
                while (tmp.right != null && tmp.right != now) {
                    tmp = tmp.right;
                }
                // 如果此时tmp.right == now, 说明我们已经将所有左子节点遍历过且添加进了result, 现在正处于一层层返回父节点的过程中
                // 所以此时只需要将新设置的连接断开, 然后一步步返回
                // 与PreOrder不一样的就是, 需要在一步步返回的过程中添加
                if (tmp.right == now) {
                    // 因为是前序遍历: 左 - 根 - 右
                    // 所以当第二次遍历到这个节点的时候就需要把它加入result, 因为此时的now即是左节点, 根要最开始被添加进去
                    result.add(now.val);
                    tmp.right = null;
                    now = now.right;
                    // 如果此时tmp.right != now, 说明我们还没有将右子节点和父节点连上
                    // 所以将tmp.right指向now, 然后now更新为now.left, 继续遍历
                } else {
                    tmp.right = now;
                    now = now.left;
                }
                // 这里代表最上层的左子树已经全部添加进了result, 现在换右子树
            } else {
                result.add(now.val);
                now = now.right;
            }
        }
        return result;
    }

    // stack2
    class State {
        Node node;
        int indicator;

        public State(Node node, int indicator) {
            this.node = node;
            this.indicator = indicator;
        }
    }
}
