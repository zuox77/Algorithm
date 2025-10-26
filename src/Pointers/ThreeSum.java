package Pointers;

import java.util.ArrayList;
import java.util.Arrays;

/*
https://leetcode.cn/problems/3sum/
https://www.lintcode.com/problem/57/
https://www.jiuzhang.com/problem/3sum/

思路: 双指针, 或者说三指针
1. 利用双指针的做法, 先确定一个值, 然后其余两个值用双指针来算
2. 在最开始的for循环要记得去重, 遇到重复就移动指针
3. 后面twoPointer里面的循环里, 也要去重, 用一个while来去重, 遇到重复就在while里面移动指针
 */

public class ThreeSum {
    public ArrayList<ArrayList<Integer>> threeSum1(int[] nums) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        int n = nums.length;

        // sort
        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {
            // 去重
            if (i > 0 && nums[i - 1] == nums[i]) {
                continue;
            }
            // 优化1：当前nums[i]与后面最小的两个数相加都大于0
            // 因为sort过，所以nums[i]会逐渐增大，而当前nums[i]与当前后面最小的两个数相加都大于0，
            // 则说明随着nums[i]越来越大，后续不可能出现 =0 的组合，所以直接break for循环
            if (nums[i] + nums[i + 1] + nums[i + 2] > 0) {
                break;
            }
            // 优化2：当前nums[i]与后面最大的两个数相加都小于0
            // 因为sort过，所以nums[i]会逐渐增大，而当前nums[i]与当前后面最小的两个数相加小于0，
            // 则说明当前nums[i]不会有 =0 的组合，但随着nums[i]越来越大，后续可能有，所以跳过当前循环
            if (nums[i] + nums[n - 1] + nums[n - 2] < 0) {
                continue;
            }

            int left = i + 1, right = n - 1, target = nums[i];
            twoPointer(nums, left, right, target, res);
        }

        return res;
    }

    public void twoPointer(
            int[] nums, int left, int right, int target, ArrayList<ArrayList<Integer>> results) {
        while (left < right) {
            if (nums[left] + nums[right] + target == 0) {
                results.add(new ArrayList<Integer>(Arrays.asList(target, nums[left], nums[right])));
                // 去重
                left++;
                right--;
                while (left < right && nums[left] == nums[left - 1]) {
                    left++;
                }
                while (left < right && nums[right] == nums[right + 1]) {
                    right--;
                }
                /*
                注意：这里去重，不能写成 while (--left < right && nums[left] == nums[left - 1])
                因为这样的话，每次循环，left会减2
                不同于 for (left++; left < right && nums[left] == nums[left - 1]; left++);
                for loop里可以这样写是因为left++作为一个初始点，一旦循环开始以后，不会再次运行left++
                 */
            } else if (nums[left] + nums[right] + target < 0) {
                left++;
            } else {
                right--;
            }
        }
    }
}
