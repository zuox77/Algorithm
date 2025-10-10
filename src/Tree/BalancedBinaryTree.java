package Tree;
/*
刷题次数：2
第二次：忘了

https://leetcode.cn/problems/balanced-binary-tree/description/

Given a binary tree, determine if it is height-balanced

Input: root = [1,2,2,3,3,null,null,4,4]
Output: false

思路：分治
1. 递归方程：
   1. 先写递归出口,也是一个corner case的判断,如果是null,那么返回0
   2. 分治的分,分别计算左右两侧的高度
   3. 分治的治,通过判断左子树/右子树/左右子树的高度差,如果任意一个不符合,则返回-1
   4. 返回左右子树的最大值 + 1
2. 返回递归方程的结果是否为-1
 */

public class BalancedBinaryTree {
    public boolean isBalanced(Node root) {
        return maxHeight(root) != -1;
    }

    public int maxHeight(Node node) {
        // 递归出口,同时也是corner case
        if (node == null) {
            return 0;
        }
        // 分治的分
        int left = maxHeight(node.left);
        int right = maxHeight(node.right);
        // 判断
        // 如果左子树或者右子树是-1,则代表左子树或者右子树不平衡,那么返回-1
        // 如果一个根节点的左右子节点之差大于1,那么表示左右子节点不平衡,返回-1
        if (left == -1 || right == -1 || Math.abs(left - right) > 1) { return -1; }
        // 返回
        // 当遍历到最下面一层的节点的时候,left和right都是0,但该节点本身是有高度的,所以要+1
        return Math.max(left, right) + 1;
    }
}
