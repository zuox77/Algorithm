package Tree;

/*
刷题次数：2
第二次：基本忘了

https://leetcode.cn/problems/symmetric-tree/description/?envType=study-plan-v2&envId=top-100-liked

Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).

Example 1:
Input: root = [1,2,2,3,4,4,3]
Output: true

Example 2:
Input: root = [1,2,2,null,3,null,3]
Output: false

 */

public class SymmetricTree {
    public boolean symmetricTree(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return true;
        return isSymmetricTree(root.left, root.right);
    }

    private boolean isSymmetricTree(TreeNode left, TreeNode right) {
        // 如果左节点或者右节点有任意一个为空，那么就判断他们是否都为空
        if (left == null || right == null) return left == right;
        /*
        因为是镜像，所以要满足
        1. left的val是否等于right的val
        2. left.left和right.right
        3. left.right和right.left
        */
        return left.val == right.val
                && isSymmetricTree(left.left, right.right)
                && isSymmetricTree(left.right, right.left);
    }
}
