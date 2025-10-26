package Pointers;

/*
刷题次数: 1

https://leetcode.cn/problems/move-zeroes/description/?envType=study-plan-v2&envId=top-100-liked

Given an integer array nums, move all 0's to the end of it while maintaining the relative order of the non-zero elements.

Note that you must do this in-place without making a copy of the array.

Example 1:
Input: nums = [0,1,0,3,12]
Output: [1,3,12,0,0]
Example 2:
Input: nums = [0]
Output: [0]

思路: 遍历数组，然后用一个指针记录当前最左边的0的位置，每次只需要交换当前位置的数与最左边的0的数
注意：
1. 交换的时候，还是必须存一个tmp，而不能直接给0，因为如果出现nums=[1]的时候，还是会经过循环，而此时就是i = firstZero，所以相当于没动，
    但如果直接assign一个0，就变了。
    不能写成：
    nums[i] = nums[firstZero];
    nums[firstZero] = nums[i];
2. firstZero只能一次加1，因为相当于是有几个数移动到了前面
 */

public class MoveZeroes {
    public void moveZeroes(int[] nums) {
        int firstZero = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                // 这里不能因为知道是0，
                int tmp = nums[i];
                nums[i] = nums[firstZero];
                nums[firstZero] = tmp;
                firstZero++;
            }
        }
    }
}
