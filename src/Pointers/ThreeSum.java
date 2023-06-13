package Pointers;

import java.util.ArrayList;
import java.util.Arrays;
/*
https://leetcode.cn/problems/3sum/
https://www.lintcode.com/problem/57/
https://www.jiuzhang.com/problem/3sum/

思路：双指针，或者说三指针
1. 利用双指针的做法，先确定一个值，然后其余两个值用双指针来算
2. 在最开始的for循环要记得去重，遇到重复就移动指针
3. 后面twoPointer里面的循环里，也要去重，用一个while来去重，遇到重复就在while里面移动指针
 */

public class ThreeSum {
    public ArrayList<ArrayList<Integer>> Solution1(int[] nums) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        int n = nums.length;
        if (n < 3) {
            return res;
        }

        // sort
        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {
            // if duplicate
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int left = i + 1, right = n - 1, target = nums[i];
            twoPointer(nums, left, right, target, res);

        }

        return res;
    }

    public void twoPointer(int[] nums, int left, int right, int target, ArrayList<ArrayList<Integer>> results) {
        while (left < right) {
            if (nums[left] + nums[right] + target == 0) {
                ArrayList<Integer> triple = new ArrayList<Integer>();
                triple.add(target);
                triple.add(nums[left]);
                triple.add(nums[right]);
                results.add(triple);
                left++;
                right--;
                // remove duplicate
                while (left < right && nums[left] == nums[left - 1]) {
                    left++;
                }
                while (left < right && nums[right] == nums[right + 1]) {
                    right--;
                }
            } else if (nums[left] + nums[right] + target < 0) {
                left++;
            } else {
                right--;
            }
        }
    }
}
