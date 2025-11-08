package Recursion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
https://leetcode.cn/problems/permutations/description/

思路: dfs
1. 一个HashSet保存当前已经遍历过的元素, 防止重复元素
2. 一个ArrayList保存当前路径
3. 一个ArrayList保存最后结果
4. 原方程里面不需要for循环, 直接在递归函数里面for就行, 不用多此一举
时间复杂度: O(N^2)
空间复杂度: O(N * N!), 递归深度O(logN), 忽略不计, 因为记录的是全排列, 全排列个数是N!, 每个全排列都是N的长度

 */

public class Permutation {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> permutation = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        recursion(nums, ans, visited, permutation);
        return ans;
    }

    public void recursion(
            int[] nums, List<List<Integer>> ans, Set<Integer> visited, List<Integer> permutation) {
        // 退出条件（边界条件）
        // 注意：在这个题里，depth理论上只能有三层，因为[1,2,3]是三个数
        // 所以退出条件也是第三层，所以其实可以直接用visited.size()来代替，毕竟visited里面有几个数也能代表同样的事情
        if (visited.size() == nums.length) {
            // 当到达边界的时候需要做的事情
            // 在这道题里是添加当前的排序进去
            ans.add(new ArrayList<>(permutation));
            // 返回上一层
            return;
        }

        // 每次循环里面做什么
        for (int num : nums) {
            // 先检查当前元素是否已经在visited里面，如果在，则跳过，避免重复访问
            // 注意：这里将nums[i]加入visited是因为题说了是distinct，所以每个元素都是唯一的
            // 否则的话，如果有重复的，就不能用nums[i]来作为visited的key
            if (visited.contains(num)) {
                continue;
            }
            // 先更新visited
            visited.add(num);
            // 更新permutation
            permutation.add(num);
            // 继续往下一层
            // 注意：这里不能写成depth++，因为depth++会真的改变depth的值，所以哪怕之后recursion开始返回，depth也回不去了
            // 除非额外做一个depth--
            recursion(nums, ans, visited, permutation);
            // 返回这一层以后需要做什么
            // 先更新visited
            visited.remove(num);
            // 再更新permuation
            permutation.removeLast();
        }
    }
}
