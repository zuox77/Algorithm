package BFSAndDFS;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/combination-sum/description/?favorite=2cktkvj

Given an array of distinct integers candidates and a target integer target,
return a list of all unique combinations of candidates where the chosen numbers sum to target.
You may return the combinations in any order.
The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the
frequency of at least one of the chosen numbers is different.
The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.

Input: candidates = [2,3,6,7], target = 7
Output: [[2,2,3],[7]]
Explanation:
2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
7 is a candidate, and 7 = 7.
These are the only two combinations.

思路: DFS
1. 利用DFS遍历整个数组, 将每一层的数添加进一个path的ArrayList里面, 找到等于target的数后再添加进最终的result的ArrayList里面
2. 记得使用result.add(new ArrayList<>(path));去对当前path里面的组合做一个深拷贝
3. 对于可能出现的重复的组合, 利用begin来剪枝, 具体思路是, 在DFS里面的for循环, 每一层只能从path里面的最后一个数所在的位置开始遍历,
   比如对于[2,3,6,7]来说, 当前如果处于path=[2,3,6]的时候, 当前最新的元素为6, 那么到下一层循环时, 只能从6开始, 不能从2或者3开始,
   因为当前如果正在遍历6, 那么说明前面的数的组合要么被遍历过了, 要么已经被添加进了path, 所以如果下一层还是从数组的第0个位置开始遍历, 就会出现重复,
   比如下一层如果从2开始遍历, 那么path=[2,3,6,2],这一定与已经遍历过的[2,2,3,6]重复
 */
public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        recursion(candidates, target, 0, 0, ans, new ArrayList<Integer>());
        return ans;
    }

    public void recursion(
            int[] candidates,
            int target,
            int depth,
            int curSum,
            List<List<Integer>> ans,
            List<Integer> comb) {
        // 退出条件
        if (curSum == target) {
            ans.add(new ArrayList<>(comb));
            return;
        }
        // 循环
        // 每次都只能找当前位置以及当前位置往后的，不能找之前的，比如[2,3,6,7]，假设现在已经把2与3都加入comb了，如果此时到6，那么这层的for循环应该直接跳过之前的2与3
        for (int i = depth; i < candidates.length; i++) {
            // 这道题因为只需要 = target的时候的答案，但其实还会遇到小于和大于两种情况
            // 小于那不用多说，直接继续加数就行
            // 大于的话如果不处理，就会一直加下去，因为退出条件是等于，而一直加反正都不等于，就会造成stackoverflow
            // 所以大于的情况我们直接跳过到下一个
            if (curSum + candidates[i] > target) continue;
            // 更新comb
            comb.add(candidates[i]);
            // 到下一层
            recursion(candidates, target, i, curSum + candidates[i], ans, comb);
            // 返回以后更新comb
            comb.removeLast();
        }
    }
}
