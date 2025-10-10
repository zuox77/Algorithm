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
   为什么不是<=数组长度-1？因为第一层的时候, 其实是添加了一个空数组进去, 此时path长度是0, 此时begin=0, 
   那么同理可推, 第二层的时候, 其实path长度是1, begin=1, 第三层的时候, path长度是2, begin=2, 
   第四层的时候path长度为3, begin=3, 此时是把 所有元素都包含的那个排列(即[1,2,3]) 添加进去了
   所以不能够用begin<数组长度
 */

public class Subsets {
    public List<List<Integer>> solution1(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        // dfs
        dfs(nums, new ArrayList<>(), result, 0);

        // result
        return result;
    }

    public void dfs(int[] nums, List<Integer> path, List<List<Integer>> result, int begin) {
        // exit
        if (begin <= nums.length) {
            result.add(new ArrayList<>(path));
        } else {
            return;
        }

        // traverse
        for (int i = begin; i < nums.length; i++) {
            path.add(nums[i]);
            dfs(nums, path, result, i + 1);
            path.remove(path.size() - 1);
        }
    }
}
