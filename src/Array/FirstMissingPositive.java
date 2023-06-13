package Array;
import java.util.*;
/*
https://leetcode.cn/problems/first-missing-positive/description/
Given an unsorted integer array nums, return the smallest missing positive integer.
You must implement an algorithm that runs in O(n) time and uses constant extra space.

Input: nums = [3,4,-1,1]
Output: 2
Explanation: 1 is in the array but 2 is missing.

Input: nums = [7,8,9,11,12]
Output: 1
Explanation: The smallest positive integer 1 is missing.

思路1：最佳解法：原地哈希表
1. 要求O(N)的时间复杂度和O(1)的空间复杂度，那基本上就是遍历常数次数，且不通过任何额外的集合变量解决问题
2. 常规的解法是
   1. for循环一次，通过HashSet哈希集合记录所有的数，顺便去重
   2. for循环第二次，这次以i=0开始，直到n结束，看i是否在HashSet中，第一个不存在的数就是答案
3. 为了满足O(1)的空间复杂度，最好的做法就是通过数组的下标和数组本身的值来建立key-value关系，从而原地构建哈希表
4. 规定数值为i的数，其下标应该是i-1，即数值为2的数应该在下标1的位置
   例如：[2, 3, 1] -> [1, 2, 3]
5. 这样的话通过两次for循环，第一次排序，第二次找数，第一个下标和数值不匹配的数就是答案，返回当前下标+1即可
6. 对于数值超过当前数组长度的数，例如[2, 7, 3]，则不去管他，因为有个隐藏条件就是，题目所要求的第一个缺失的数，并不是某个连续的subarray中
   缺失的数(比如[1, 5, 8, 9]，缺失的数是2，而不是7)而是从1开始计算，第一个缺失的数
   所以如果有超过数组长度的数，那么其前面一定存在第一个缺失的数，比如[1, 2, 7, 3]，缺4
   可以想象一个初始且排序正确的满数组，比如[1, 2, 3, 4, 5]
   任何比当前数组长度大的数，替换这个满数组的时候，都会造成满数组的缺失，比如假设6去替换3，则第一个缺失的数是3，8去替换5，第一个缺失的数是5
   所以无论如何，任何比当前数组长度大的数，前面都一定有缺失的数，所以只要该数的数值比数组长度大，就可以不用考虑
   当所有数的数值都比数组长度大时，则答案为1
7. 对于数值为负数的数，也不需要管，即负数可以被其他数交换，但不主动去交换别的数，即负数最后会在缺失的位置上
   因为题目规定了从positive integer开始，即1
8. 在第一次排序for循环时，除了考虑第6步和第7步的排除范围之外的数，还需要考虑交换的问题
   比如[3, 2, 4, 6, 1]，当for的i=0时，数为3，3应该在下标2的位置上，所以3与7交换 -> [4, 2, 3, 6, 1]
   当如果此时结束，会发现此时4在下标0，这是不正确的，就算后面遍历到第五个数1时，1会再与4交换，1会到下标0，即4当前的位置
   但4又会被交换到下标4，但4应该是在下标3的位置，所以如果此时停止，4可能永远无法处于正确的位置
   所以我们要用一个while，不仅是将当前数交换到正确的位置，且要继续判断交换过来的数，并将其交换到正确的位置，类似于一个小的DFS
   因为for循环一旦走过这个位置，就不会再回来
9. 除了while循环，还要考虑到一个问题，就是无限循环，假设[3, 2, 3, 6, 1]，那么第一个3因为应该在下标2的位置，所以第一个3会与第二个3交换，
   但交换过来，数值是一样的，所以第二个3又会与第一个3交换，导致无限循环
   所以在判断是否交换时：
   1. 条件nums[i] - 1 != i表示：当前位置i的数的数值不等于其下标时，交换，即当前位置i的数不在正确的位置
   2. 条件nums[i] != nums[nums[i] - 1]表示：nums[i] != nums[(nums[i]这个数应该在的位置)]，
      即nums[i]这个数应该在的位置上的数，已经是正确的了
   判断即将交换的位置上的数是否已经是正确的数 和 判断当前数是否在正确的位置上 都可以满足第4步的规定
   但如果用条件1，则无法排除无限循环，所以要用条件2
10. 排好序后，第二次for循环，此时可以用第9步的条件1来判断了，第一个不是的，就是需要的数

思路2: 用哈希集合
1. for循环一次，通过HashSet哈希集合记录所有的数，顺便去重
2. for循环第二次，这次以i=0开始，直到n结束，看i是否在HashSet中，第一个不存在的数就是答案

 */

public class FirstMissingPositive {
    public int Solution1(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] >= 1 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) { //用第9步的条件2而不是条件1
                swap(nums, i, nums[i] - 1);
            }
        }

        for (int j = 0; j < n; j++) {
            if (nums[j] - 1 != j) return j + 1;
        }
        return n + 1;
    }

    public void swap(int[] nums, int start, int end) {
        int temp = nums[start];
        nums[start] = nums[end];
        nums[end] = temp;
    }

    public int Solution2(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num: nums) {
            set.add(num);
        }

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        return 0;
    }
}
