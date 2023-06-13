package Recursion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/*
https://leetcode.cn/problems/permutations/description/

思路：dfs
1. 一个HashSet保存当前已经遍历过的元素，防止重复元素
2. 一个ArrayList保存当前路径
3. 一个ArrayList保存最后结果
4. 原方程里面不需要for循环，直接在递归函数里面for就行，不用多此一举
时间复杂度：O(N^2)
空间复杂度：O(N * N!)，递归深度O(logN)，忽略不计，因为记录的是全排列，全排列个数是N!，每个全排列都是N的长度

 */

public class Permutation {
    public List<List<Integer>> Solution1(int[] nums) {
        int n = nums.length;
        List<List<Integer>> result = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        List<Integer> path = new ArrayList<>();

        recursion(nums, 0, n, visited, path, result);
        return result;
    }

    public void recursion(int[] nums, int depth, int n, HashSet<Integer> visited, List<Integer> path, List<List<Integer>> result) {
        // recursion exit
        if (depth >= n) {
            result.add(new ArrayList<>(path)); // 要深度拷贝
            return;
        }

        for (int i = 0; i < n; i++) {
            if (visited.contains(nums[i])) {
                continue;
            }
            visited.add(nums[i]);
            path.add(nums[i]);
            recursion(nums, depth + 1, n, visited, path, result);
            visited.remove(nums[i]);
            path.remove(path.size() - 1); // 记住List/ArrayList里的remove是根据index来的
        }
    }
}
