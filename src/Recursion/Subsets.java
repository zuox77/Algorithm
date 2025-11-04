package Recursion;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/subsets/description/

Given an integer array nums of unique elements, return all possible
subsets (the power set).
The solution set must not contain duplicate subsets. Return the solution in any order.

Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]

思路: DFS
1. 这道题和CombinationSum很相似, 都是求所有的组合, 且不能重复
2. 与CombinationSum不同的是, 此题中的数字在每个组合里仅能使用1次, 所以为达到这个目的, 每一层的for循环开始都要是上一层的i的值再多加1
3. 因为是求所有的排列, 所以此题中, 添加进result的条件是begin<=数组长度
   为什么不是<=数组长度-1？因为第一层的时候, 其实是添加了一个空数组进去, 此时subset长度是0, 此时begin=0,
   那么同理可推, 第二层的时候, 其实subset长度是1, begin=1, 第三层的时候, subset长度是2, begin=2,
   第四层的时候subset长度为3, begin=3, 此时是把 所有元素都包含的那个排列(即[1,2,3]) 添加进去了
   所以不能够用begin<数组长度
   
解法上有两个小区别：
1. 由于[]也算一个元素，所以可以按照以前的做法，将[]单独视为特殊情况，在recursion前就加入ans，
    这样的话，在每一层，都会将现在的subset加入ans，例如第一层加入[1]，第二层加入[1,2]，第三层[1,2,3]
2. 或者将[]也包含在写法内，即退出的时候加入
 */

public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // recursion
        recursion(nums, new ArrayList<>(), result, 0);
        // result
        return result;
    }

    public void recursion(int[] nums, List<Integer> subset, List<List<Integer>> result, int begin) {
        // exit
        if (begin <= nums.length) {
            result.add(new ArrayList<>(subset));
        } else {
            return;
        }

        // traverse
        for (int i = begin; i < nums.length; i++) {
            subset.add(nums[i]);
            recursion(nums, subset, result, i + 1);
            subset.remove(subset.size() - 1);
        }
    }

    public List<List<Integer>> subsets2(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> subset = new ArrayList<>();
        ans.add(subset);
        recursion(0, nums, subset, ans);
        return ans;
    }

    public void recursion(int start, int[] nums, List<Integer> subset, List<List<Integer>> ans) {
        // 退出条件（设置recursion的底部，防止无限往下）
        if (start == nums.length) {
            return;
        }

        // 每个循环需要做什么
        // 由于这道题需要将所有的不重复subset加入，所以当depth<=nums.length的时候就要加入
        // 且由于需要不重复，那么for循环就需要从上一层的i额外加1开始
        for (int i = start; i < nums.length; i++) {
            // 加入subset
            subset.add(nums[i]);
            // 加入ans
            ans.add(new ArrayList<>(subset));
            // 进入下一层
            recursion(i + 1, nums, subset, ans);
            // 返回以后需要做什么
            // 移除subset
            subset.removeLast();
        }
    }
}
