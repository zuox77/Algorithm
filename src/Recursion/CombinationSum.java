package Recursion;

import java.util.ArrayList;
import java.util.Arrays;
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
  public List<List<Integer>> solution1(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(candidates);
    dfs(candidates, target, 0, new ArrayList<Integer>(), 0, result);
    return result;
  }

  public void dfs(
      int[] candidates,
      int target,
      int begin,
      List<Integer> path,
      int curSum,
      List<List<Integer>> result) {
    // exit
    if (curSum == target) {
      result.add(new ArrayList<>(path));
      return;
    }

    for (int i = begin; i < candidates.length; i++) {
      int num = candidates[i];
      if (curSum + num > target) {
        continue;
      }
      path.add(num);
      curSum += num;
      dfs(candidates, target, i, path, curSum, result);
      curSum -= num;
      path.remove(path.size() - 1);
    }
  }
}
