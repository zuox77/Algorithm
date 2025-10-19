package DynamicProgramming;

import java.util.ArrayList;
import java.util.List;

/*
刷题次数: 1

https://www.lintcode.com/problem/42/
https://www.jiuzhang.com/problem/maximum-subarray-ii/

Given an array of integers, find two non-overlapping subarrays which have the largest sum.
The number in each subarray should be contiguous.
Return the largest sum.

input: nums = [1, 3, -1, 2, -1, 2]
output: 7, the two subarrays are [1, 3] and [2, -1, 2] or [1, 3, -1, 2] and [2]

详细可以看DynamicProgramming.md
 */
public class MaximumSubarray2 {
  public class solution1 {
    public int maxTwoSubArrays(List<Integer> nums) {
      int len = nums.size();

      // leftMax的定义: 变形前缀和, 即以i为结尾的任意连续子数组的和的最大值
      List<Integer> leftMax = new ArrayList<>(nums);
      for (int i = 1; i < len; i++) {
        leftMax.set(i, Math.max(leftMax.get(i - 1) + nums.get(i), nums.get(i)));
      }
      System.out.println(leftMax);

      // rightMax的定义: 变形后缀和, 即以i为起始的任意连续子数组的和的最大值
      List<Integer> rightMax = new ArrayList<>(nums);
      for (int i = len - 2; i >= 0; i--) {
        rightMax.set(i, Math.max(rightMax.get(i + 1) + nums.get(i), nums.get(i)));
      }
      System.out.println(rightMax);

      // prefixMax的定义: 变形前缀和的历史最大值, 即以i为结尾的任意连续子数组的和的历史最大值
      List<Integer> prefixMax = new ArrayList<>(leftMax);
      for (int i = 1; i < len; i++) {
        prefixMax.set(i, Math.max(prefixMax.get(i - 1), prefixMax.get(i)));
      }
      System.out.println(prefixMax);

      // postfixMax的定义: 变形后缀和的历史最大值, 即以i为起始的任意连续子数组的和的历史最大值
      List<Integer> postfixMax = new ArrayList<>(rightMax);
      for (int i = len - 2; i >= 0; i--) {
        postfixMax.set(i, Math.max(rightMax.get(i + 1), rightMax.get(i)));
      }
      System.out.println(postfixMax);

      // 因为要求必须是两个不重复子数组的和, 所以在遍历一次, 相当于找分界线, 从而算出不同子数组的和的最大值
      int result = Integer.MIN_VALUE;
      for (int i = 0; i < len - 1; i++) {
        result = Math.max(prefixMax.get(i) + postfixMax.get(i + 1), result);
        // System.out.println(result);
      }

      return result;
    }
  }
}
