package Pointers;
/*
https://www.lintcode.com/problem/148/
https://leetcode.cn/problems/sort-colors/
https://www.jiuzhang.com/problem/sort-colors/

思路1：快速排序
1. 具体可以搜quickSort
时间复杂度：O(NlogN)
空间复杂度：O(1)

思路2：计数排序
1. 具体可以搜countingSort
时间复杂度：O(N)
空间复杂度：O(1)

思路3：双指针，或者说三指针
1. 用left，mid，right三个指针将数组划分排序为需要的三部分
2. left指针的左侧，不包括left自己，全是0，left是数字为1的区间的起始点
3. right指针的右侧，不包括right自己，全是2，right是数字为1的区间的终点
4. 初始化left = mid = 0，right = n - 1
5. while条件是mid <= right
6. 特别注意：
    1. 等于0时，放在left左边，交换完以后，因为是从左向右遍历，并且left和mid的初始值都是0，所以我们能保证left左边一定全部是0，
        所以交换完以后，left和mid都可以向右移动一位
    2. 等于2时，放在right右边，交换完以后，只有right可以移动，因为right的初始值是最后一位数，所以同理，我们能保证right右边全是2，
        但是不能移动mid，因为这次的交换是由于mid现在位置上的数是2所以交换的，但我们没有检查right现在位置上的数是什么，有可能是0或者1或者2，
        所以mid不能动，下一轮循环的时候就可以将right交换过来的数检查一遍了
        比如[2, 0, 1, 2]，left和right初始都在下标0，right在下标3，此时发现nums[mid] == 2，需要跟right交换，但此时right位置上的数也是2，
        所以如果交换完了以后mid往右移动的话，mid到了下标1，即数字0的位置，那么就错过了这个2
时间复杂度：O(N)
空间复杂度：O(1)
 */

public class SortColors {
    // 双指针
    public int[] Solution1(int[] nums) {
        int n = nums.length;
        int left = 0, mid = 0, right = n - 1;

        while (mid <= right) {
            if (nums[mid] == 0) { // 等于0，放在left左边
                swap(nums, left, mid);
                mid++;
                left++;
            } else if (nums[mid] == 2) { // 等于2，放在right右边
                swap(nums, right, mid);
                right--;
            } else {
                mid++;
            }
        }

        return nums;
    }

    public void swap(int[] nums, int start, int end) {
        int temp = nums[start];
        nums[start] = nums[end];
        nums[end] = temp;
    }
}
