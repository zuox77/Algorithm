package Pointers;

/*
https://leetcode.cn/problems/next-permutation/description/
https://leetcode.cn/problems/next-permutation/solutions/80560/xia-yi-ge-pai-lie-suan-fa-xiang-jie-si-lu-tui-dao-/

A permutation of an array of integers is an arrangement of its members into a sequence or linear order.
For example, for arr = [1,2,3], the following are all the permutations of arr: [1,2,3], [1,3,2], [2, 1, 3], [2, 3, 1], [3,1,2], [3,2,1].
The next permutation of an array of integers is the next lexicographically greater permutation of its integer.
More formally, if all the permutations of the array are sorted in one container according to their lexicographical order,
then the next permutation of that array is the permutation that follows it in the sorted container.
If such arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).

For example, the next permutation of arr = [1,2,3] is [1,3,2].
Similarly, the next permutation of arr = [2,3,1] is [3,1,2].
While the next permutation of arr = [3,2,1] is [1,2,3] because [3,2,1] does not have a lexicographical larger rearrangement.
Given an array of integers nums, find the next permutation of nums.

The replacement must be in place and use only constant extra memory.

Example 1:
Input: nums = [1,2,3]
Output: [1,3,2]

Example 2:
Input: nums = [3,2,1]
Output: [1,2,3]

Example 3:
Input: nums = [1,1,5]
Output: [1,5,1]

思路:
1. 如果按照Permutation的思路来想这个题的话就会比较复杂, 此题要学会分析next permutation的准确定义是什么
2. 题中的定义是: 字典序中下一个更大的排列, 即数字大小, 比如123就比132小, 且只能大一位, 比如123的next permutation是132, 而不是同样比它自己大
   的321
3. 要找到比自己大的数: 可以将排在后面的大数值和排在前面的小数值换位置
   比如123, 3在2后面, 所以交换后132比之前的数大, 同理, 3和1交换321也可以
4. 不仅要找到比自己大的数, 还要找到比自己大的下一位数, 即比自己大但大的幅度尽可能小的数, 所以要满足:
    1. 尽可能靠右的位置进行交换, 所以需要从后往前找, 比如123456, 应该交换5和6, 而不是4和6
    2. 交换的大数值的数, 应该尽可能的小, 比如123465, 应该交换5和4, 而不是6和4
    3. 将大数值的数交换到前面后, 应该将大数值后面的数重新进行升序排序, 因为升序排序是最小的
5. 做法:
    1. 排除特殊值: n < 2
    2. 从后往前找到第一个出现的相邻的两个升序数, 将其标记为left和right,
       因为是第一个出现的升序, 所以right右边的数一定是降序, 即right是从left到n - 1最大的数
       且因为是第一个出现的升序, 所以满足了4.1中的尽可能靠右的条件
    3. 处理特殊情况: 如果left此时仍旧是初始值, 那么说明这个数组是一个降序数组, 就直接将其反转一遍
    4. 找到left和right以后, 从n - 1到right找到第一个比nums[left]大的数, 记为firstLarge
       因为是第一个, 且n - 1到right是降序, 所以这可以满足4.2中的大数值的数需要尽可能的小, 即第一个比nums[left]大的数
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
        // 找到第一个升序的pair
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                left = i;
                right = i + 1;
                break;
            }
        }
        // 这里需要检查一次，如果left还是-1，说明整个array都是升序，那么直接反转整个array即可
        if (left == -1) {
            reverse(nums, 0, n - 1);
            return;
        }
        /*
        找到从n-1到right（包括n-1和right）之间，第一个大于nums[left]的数
        注意：nums[right]一定是大于nums[left]的，因为我们找的就是一个升序的pair
        但是right不一定是第一个从右往左数大于nums[left]的数
        比如[1, 3, 2] 找到的pair是[1, 3]，right是3这个位置，即下标1
        我们需要从右往左，从n-1到right，即n - 1 >= j >= right中，找到第一个
        不难看出，第一个大于nums[left]的数是2，即下标2
        所以第一个大于nums[left]的数不一定就是right
         */
        for (int i = n - 1; i >= right; i--) {
            if (nums[i] > nums[left]) {
                firstLarge = i;
                break;
            }
        }
        swap(nums, left, firstLarge);
        reverse(nums, right, n - 1);
    }

    public void swap(int[] nums, int left, int right) {
        int tmp = nums[left];
        nums[left] = nums[right];
        nums[right] = tmp;
    }

    public void reverse(int[] nums, int left, int right) {
        while (left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }
}
