package DynamicProgramming;

/*
刷题次数: 1

https://leetcode.cn/problems/jump-game/description/?envType=study-plan-v2&envId=top-100-liked

You are given an integer array nums. You are initially positioned at the array's first index,
and each element in the array represents your maximum jump length at that position.
Return true if you can reach the last index, or false otherwise.

Example 1:
Input: nums = [2,3,1,1,4]
Output: true
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.

Example 2:
Input: nums = [3,2,1,0,4]
Output: false
Explanation: You will always arrive at index 3 no matter what. Its maximum jump length is 0, which makes it impossible to reach the last index.

思路：DP数组
1. 假设有个位置i，i能否抵达，取决于前面的位置是否能抵达i，那么我们可以用一个数组来记录每个位置是否可以抵达
    boolean[] reachable
    对于i来说，reachable[i] = (reachable[i - 1] && nums[i - 1] >= 1) || (reachable[i - 2] && nums[i - 2] >= 2) || ...
    说人话就是：reachable[i]是true还是false，取决于
        i - 1是否能抵达（reachable[i - 1] = true），且nums[i - 1]是否大于等于1，或者
        i - 2是否能抵达（reachable[i - 2] = true），且nums[i - 2]是否大于等于2，。。。
        直到reachable[0]
2. 设置这个boolean[]，特殊处理i = 0的情况，然后对每一个位置，都用for循环去更新reachable
3. 最后判断reachable[最后一位]是否为true

思路：DP单变量
1. 根据上面的思路，我们发现，其实我们只需要维护一个变量，去表示能抵达的最右边的位置是什么
    在循环中，不断去更新能抵达的最右边的位置，并且判断，如果i大于这个位置，那么直接返回false
    因为我们维护的是能抵达的最右边的位置，这是包含了所有前面的位置的，所以一旦有一个位置，是能抵达的最右边的位置无法抵达的，说明当前这个位置
    没有任何方法可以抵达，无论这是不是最后一个位置，都说明无法继续下去了


时间复杂度: O(N)
空间复杂度: O(1)
 */

import java.util.Arrays;

public class CanJump {
    public boolean canJump(int[] nums) {
        int n = nums.length;
        boolean[] reachable = new boolean[n];
        Arrays.fill(reachable, false);

        for (int i = 0; i < n; i++) {
            // 特殊处理第一个位置
            if (i == 0) {
                fillTrue(reachable, nums, i, n);
                continue;
            }
            // 如果当前位置本身就是true，即前面的某个位置可以抵达当前位置1
            if (reachable[i]) {
                fillTrue(reachable, nums, i, n);
            }
            // 检查一次，这样更快
            if (reachable[n - 1]) return true;
        }
        return reachable[n - 1];
    }

    private void fillTrue(boolean[] reachable, int[] nums, int start, int n) {
        int k = nums[start];
        for (int i = 0; i <= k && start + i < n; i++) {
            reachable[start + i] = true;
        }
    }

    public boolean canJump2(int[] nums) {
        int n = nums.length;
        int maxRight = 0;

        for (int i = 0; i < n; i++) {
            if (i > maxRight) return false;
            maxRight = Math.max(maxRight, i + nums[i]);
        }
        return true;
    }
}
