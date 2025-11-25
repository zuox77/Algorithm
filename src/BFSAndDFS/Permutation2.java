package BFSAndDFS;

/*
https://leetcode.cn/problems/permutations-ii/description/?orderBy=most_votes

思路: 递归/dfs
1. 跟Permutation这道题差不多, 但允许重复元素, 而结果中又不允许重复答案
2. 比如[1, 2, 2'], 按照Permutation的做法的话, 当第一层的for遍历到1时, 往下递归第二层遍历到2时, 再往下递归到第三层得到[1, 2, 2'],
    然后返回第二层, 第二层for循环继续, 遍历到2'时, 再往下递归得到[1, 2', 2], 但对于结果这两个算重复的, 只能计算一次
3. 一种省事的方案是, 按照上一题的思路算, 最后将答案转化成HashSet/SetList之类的不重复集合, 然后再转化回来, 但肯定过不了面试
4. 重复元素的题, 答案需要具体的方案, 可以考虑先给数组排个序 Arrays.sort()
5. 排序以后, 重复的元素一定都相互挨着, 此时考虑计算时候的条件:
    满足下面任意一个条件即表示需要计算:
    1. 当前后数不为重复数字, 说明当前数字跟前一个不重复, 又因为排序过, 所以跟之前也一定不重复,
        说明这是个新的数字, 一定不会与前面已经加入path的数造成重复计算
    2. 前一个数在visited里 = visited.contains(i - 1), 说明就算此时与前一个数重复, 但前一个数已经被使用, 那现在这个重复的数是在计算第一次,
        比如path为[1, 2, 3, 3']的第一次计算
        之后回溯回去再遍历到[1, 2, 3']时候, 在第三层, num[i] = 3', 此时它在原数组[1, 2, 3, 3']的前一个数为3, 因为在第三层,
        所以3还不在visited里, 所以虽然前后是重复数字, 但也不需要计算了, 因为这不是第一次计算了
    更通用的解释是: 如果要计算, 那一定是这个排列顺序的第一次出现, 所以有前后重复的数时, 在第一次计算中, 因为我们是从左向右遍历, 前面的重复的数
    已经先于后面的数加入visited或者说这个排列顺序path里了, 所以前边的数一定比后边先加入一种排列顺序并且此排列顺序已经加入了答案里面,
    所以之后重复计算时, 一定是后边的重复数先add进path里面, 所以前边出现的重复的数, 此时一定不在visited里面, 所以可以判断这是第二次或者更多次的计算,
    是重复的, 所以不需要计算了

 */

import java.util.*;

public class Permutation2 {
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        // 这道题需要把index作为key保存进去
        Set<Integer> visited = new HashSet<>();
        recursion(nums, ans, new ArrayList<>(), visited);
        return ans;
    }

    public void recursion(
            int[] nums, List<List<Integer>> ans, List<Integer> permutation, Set<Integer> visited) {
        // 退出条件（底部条件）
        if (visited.size() == nums.length) {
            ans.add(new ArrayList<>(permutation));
            return;
        }
        // 每次循环做什么
        for (int i = 0; i < nums.length; i++) {
            // 跳过已遍历过的
            if (visited.contains(i)) continue;
            // 跳过i=0，因为此时肯定不会重复
            // 后面两个条件是合在一起看的
            //
            if (i != 0 && nums[i - 1] == nums[i] && !visited.contains(i - 1)) {
                continue;
            }
            // 更新visited
            visited.add(i);
            // 更新permutation
            permutation.add(nums[i]);
            // 进入下一层
            recursion(nums, ans, permutation, visited);
            // 返回以后做什么
            // 更新visited
            visited.remove(i);
            // 更新permutation
            permutation.removeLast();
        }
    }
}
