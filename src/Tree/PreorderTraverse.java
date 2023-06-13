package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/*
https://www.lintcode.cn/problem/66/
https://leetcode.com/problems/binary-tree-preorder-traversal/
https://www.jiuzhang.com/problem/binary-tree-preorder-traversal/

思路1：递归/DFS
1. 用一个helper函数来做递归，主函数只需要新建出存储结果的变量即可
2. helper函数：
    1. 先写出口
    2. 因为前序遍历是 根 - 左 - 右，所以先加入结果，再递归左，再递归右
    3. 递归之前检查左右子节点是否存在
时间复杂度：O(N)
空间复杂度：O(k)，递归会用到stack，k代表树高

思路2：递归/分治
1. 跟思路1整体思路差不多，不过由于是分治，所以不需要额外helper函数来帮忙
时间复杂度：O(N)
空间复杂度：O(k)，递归会用到stack，k代表树高

思路3：无递归/栈
1. 如果不用递归，由于无法从子节点回到父节点，那么一定需要一个stack将其经过的父节点都存起来
2. stack是一个FILO的队列，所以要先将右节点压进去
3. 初始化stack将root压进去
4. 用while循环条件stack是否为空，类似于BFS里面的queue是否为空
5. 先pop出来一个node，将此node加入res，每pop出来一个就将值存进res里面
6. 再检查右节点是否存在，存在则压进stack，再检查左子节点是否存在，存在则压进stack
时间复杂度：O(N)
空间复杂度：O(k)，k代表树高

思路4：无递归/栈
1. 如果不用递归，由于无法从子节点回到父节点，那么一定需要一个stack将其经过的父节点都存起来
2. 将一个元组(node, i)压进stack
3. 当i为0，代表到到下一层，需要将与i成对的node的左右子节点压进stack
4. 当i为1/2/3时，将node的值存入res，1/2/3分别对应前/中/后序遍历
时间复杂度：O(N)
空间复杂度：O(k)，k代表树高

思路5：无递归无栈/Morris算法
1. 无论前中后序遍历，最大的难点就是到了叶子节点怎么返回父子节点
2. Morris算法利用额外的指针将所有的尾节点都加一个指针返回父子节点，以达到不用额外空间又能返回父子节点的目的
时间复杂度：O(N)
空间复杂度：O(1)
 */

public class PreorderTraverse {
    // DFS
    public List<Integer> Solution1(Node root) {
        List<Integer> res = new ArrayList<Integer>();
        recursion(res, root);
        return res;
    }

    public void recursion(List<Integer> res, Node root) {
        // recursion exit
        if (root == null) {
            return;
        }

        // recursion
        res.add(root.val);
        if (root.left != null) {
            recursion(res, root.left);
        }
        if (root.right != null) {
            recursion(res, root.right);
        }
    }

    // 分治
    public List<Integer> Solution2(Node root) {
        List<Integer> res = new ArrayList<Integer>();
        if (root == null) {
            return res;
        }

        // divide
        List<Integer> left = Solution2(root.left);
        List<Integer> right = Solution2(root.right);

        // conquer
        res.add(root.val);
        res.addAll(left);
        res.addAll(right);

        return res;
    }

    // stack
    public List<Integer> Solution3(Node root) {
        List<Integer> res = new ArrayList<Integer>();
        Stack<Node> stack = new Stack<Node>();

        // push root to stack
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            res.add(node.val);
            // add right side to stack first, because stack is FILO
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        return res;
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
    public List<Integer> Solution4(Node root) {
        List<Integer> res = new ArrayList<Integer>();
        Stack<State> stack = new Stack<>();
        stack.push(new State(root, 0));

        while (!stack.isEmpty()) {
            State now = stack.pop();
            Node node = now.node;
            int indicator = now.indicator;

            if (node == null) {
                continue;
            }
            if (indicator == 0) {
                stack.push(new State(node, 3));
                stack.push(new State(node.right, 0));
                stack.push(new State(node, 2));
                stack.push(new State(node.left, 0));
                stack.push(new State(node, 1));
            }

            if (indicator == 1) {
                res.add(node.val);
            }
        }
        return res;
    }

    // morris
    public List<Integer> Solution5(Node root) {
        List<Integer> res = new ArrayList<Integer>();
        // create dummy node in case root is lost
        Node now = root;
        while (now != null) {
            if (now.left != null) {
                Node tmp = now.left;
                // iterate to the last right node of tmp (now.left)
                while (tmp.right != null && tmp.right != now) {
                    tmp = tmp.right;
                }
                // if it's the second time, it means we've already iterated all nodes, just remove the right pointer
                // and go to next node
                if (tmp.right == now) {
                    tmp.right = null;
                    now = now.right;
                    // if it's the first time, then add a new pointer to now so we can get back to upper level
                } else {
                    res.add(now.val);
                    tmp.right = now;
                    now = now.left;
                }
            } else {
                res.add(now.val);
                now = now.right;
            }
        }
        return res;
    }
}
