package Tree;

/*
刷题次数：2
第二次：基本忘了

https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/description/

Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.

According to the definition of LCA on Wikipedia:
“The lowest common ancestor is defined between two nodes p and q as the lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”

Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
Output: 3
Explanation: The LCA of nodes 5 and 1 is 3.

思路：DFS + 分治
1. 这道题最主要要想清楚怎么找,其本质是找p或者q
 */

public class LowestCommonAncestor {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 递归出口，也是corner case
        if (root == null) {
            return root;
        }
        // 判断是不是遍历到p或者q了
        // 如果遍历到任意一个，那么此时root就一定是ancestor
        if (root == p || root == q) {
            return root;
        }
        // 分治的分
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        // 分治的治
        // 如果左子树存能找到节点，右子树也能找到节点，那么说明root是他们的ancestor
        if (left != null && right != null) {
            return root;
            // 如果只有左子树能找到节点，那么左子树就是ancestor
        } else if (left != null) {
            return left;
            // 如果只有右子树能找到节点，那么右子树就是ancestor
        } else if (right != null) {
            return right;
        } else {
            return null;
        }
    }
}
