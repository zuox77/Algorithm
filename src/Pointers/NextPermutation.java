package Pointers;
/*
https://leetcode.cn/problems/next-permutation/description/
https://leetcode.cn/problems/next-permutation/solutions/80560/xia-yi-ge-pai-lie-suan-fa-xiang-jie-si-lu-tui-dao-/

思路：
1. 如果按照Permutation的思路来想这个题的话就会比较复杂，此题要学会分析next permutation的准确定义是什么
2. 题中的定义是：字典序中下一个更大的排列，即数字大小，比如123就比132小，且只能大一位，比如123的next permutation是132，而不是同样比它自己大
   的321
3. 要找到比自己大的数：可以将排在后面的大数值和排在前面的小数值换位置
   比如123，3在2后面，所以交换后132比之前的数大，同理，3和1交换321也可以
4. 不仅要找到比自己大的数，还要找到比自己大的下一位数，即比自己大但大的幅度尽可能小的数，所以要满足：
    1. 尽可能靠右的位置进行交换，所以需要从后往前找，比如123456，应该交换5和6，而不是4和6
    2. 交换的大数值的数，应该尽可能的小，比如123465，应该交换5和4，而不是6和4
    3. 将大数值的数交换到前面后，应该将大数值后面的数重新进行升序排序，因为升序排序是最小的
5. 做法：
    1. 排除特殊值：n < 2
    2. 从后往前找到第一个出现的相邻的两个升序数，将其标记为left和right，
       因为是第一个出现的升序，所以right右边的数一定是降序，即right是从left到n - 1最大的数
       且因为是第一个出现的升序，所以满足了4.1中的尽可能靠右的条件
    3. 处理特殊情况：如果left此时仍旧是初始值，那么说明这个数组是一个降序数组，就直接将其反转一遍
    4. 找到left和right以后，从n - 1到right找到第一个比nums[left]大的数，记为firstLarge
       因为是第一个，且n - 1到right是降序，所以这可以满足4.2中的大数值的数需要尽可能的小，即第一个比nums[left]大的数
    5. 交换left和firstLarge
    6. 将right到n - 1反转排序
 */

public class NextPermutation {
    public void nextPermutation(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return;
        }
        int left = -1, right = -1, firstLarge = -1;
        // find first ascending pair
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                left = i;
                right = i + 1;
                break;
            }
        }
        // the whole array is already a descending array
        if (left == -1) {
            reverse(nums, 0, n - 1);
            return;
        }
        // find the first element within range right and n - 1 and larger than left
        for (int i = n - 1; i >= right; i--) {
            if (nums[i] > nums[left]) {
                firstLarge = i;
                break;
            }
        }
        swap(nums, left, firstLarge);
        reverse(nums, right, n - 1);
    }

    public void swap (int[]nums, int left, int right) {
        int tmp = nums[left];
        nums[left] = nums[right];
        nums[right] = tmp;
    }

    public void reverse (int[]nums, int left, int right) {
        while (left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }
}
