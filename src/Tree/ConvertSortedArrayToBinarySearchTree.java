package Tree;

/*
https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/description/?envType=study-plan-v2&envId=top-100-liked

Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.

Example 1:
Input: nums = [-10,-3,0,5,9]
Output: [0,-3,9,-10,null,5]
Explanation: [0,-10,5,null,-3,null,9] is also accepted:

Example 2:
Input: nums = [1,3]
Output: [3,1]
Explanation: [1,null,3] and [3,1] are both height-balanced BSTs.
 */

public class ConvertSortedArrayToBinarySearchTree {
    public TreeNode sortedArrayToBST(int[] nums) {
        // 调用递归函数，初始范围是整个数组 [0, nums.length)
        return dfs(nums, 0, nums.length);
    }

    // 递归函数：将 nums[left] 到 nums[right-1] 转换为平衡BST
    private TreeNode dfs(int[] nums, int left, int right) {
        // 基础情况：当左边界等于右边界时，表示没有元素了
        if (left == right) {
            return null;
        }

        // 计算中间位置，使用无符号右移防止溢出并提高效率
        int m = (left + right) >>> 1;

        // 递归构建树：中间元素作为根，左边构建左子树，右边构建右子树
        return new TreeNode(
                nums[m],
                dfs(nums, left, m), // 左子树：左半部分
                dfs(nums, m + 1, right)); // 右子树：右半部分
    }
}
