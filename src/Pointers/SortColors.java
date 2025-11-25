package Pointers;

/*
https://www.lintcode.com/problem/148/
https://leetcode.cn/problems/sort-colors/
https://www.jiuzhang.com/problem/sort-colors/

Given an array nums with n objects colored red, white, or blue,
sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white, and blue.
We will use the integers 0, 1, and 2 to represent the color red, white, and blue, respectively.
You must solve this problem without using the library's sort function.

Example 1:
Input: nums = [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]

Example 2:
Input: nums = [2,0,1]
Output: [0,1,2]

思路1: 快速排序
1. 具体可以搜quickSort
时间复杂度: O(NlogN)
空间复杂度: O(1)

思路2: 计数排序
1. 具体可以搜countingSort
时间复杂度: O(N)
空间复杂度: O(1)

思路3: 双指针, 或者说三指针
1. 用left, mid, right三个指针将数组划分排序为需要的三部分
2. left指针的左侧, 不包括left自己, 全是0, left是数字为1的区间的起始点
3. right指针的右侧, 不包括right自己, 全是2, right是数字为1的区间的终点
4. 初始化left = mid = 0, right = n - 1
5. while条件是mid <= right
6. 特别注意:
    1. 等于0时, 放在left左边, 交换完以后, 因为是从左向右遍历, 并且left和mid的初始值都是0, 所以我们能保证left左边一定全部是0,
        所以交换完以后, left和mid都可以向右移动一位
    2. 等于2时, 放在right右边, 交换完以后, 只有right可以移动, 因为right的初始值是最后一位数, 所以同理, 我们能保证right右边全是2,
        但是不能移动mid, 因为这次的交换是由于mid现在位置上的数是2所以交换的, 但我们没有检查right现在位置上的数是什么, 有可能是0或者1或者2,
        所以mid不能动, 下一轮循环的时候就可以将right交换过来的数检查一遍了
        比如[2, 0, 1, 2], left和right初始都在下标0, right在下标3, 此时发现nums[mid] == 2, 需要跟right交换, 但此时right位置上的数也是2,
        所以如果交换完了以后mid往右移动的话, mid到了下标1, 即数字0的位置, 那么就错过了这个2
时间复杂度: O(N)
空间复杂度: O(1)
 */

public class SortColors {
    // 双指针
    public int[] sortColors1(int[] nums) {
        int n = nums.length;
        int left = 0, mid = 0, right = n - 1;

        while (mid <= right) {
            if (nums[mid] == 0) { // 等于0, 放在left左边
                swap(nums, left, mid);
                mid++;
                left++;
            } else if (nums[mid] == 2) { // 等于2, 放在right右边
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

    /*
    整体思路为：将排序的问题转化为插入和修改的问题，即将数组按照0-1-2排序，变成通过顺序，插入0/1/2，然后再修改其他位置上的数，从而达到排序的目的
    1. 用两个指针记录当前0应该插入的位置和当前1应该插入的位置，都从0开始
    2. 遍历数组，记录nums[i]  -> curNum = nums[i]
    3. 将nums[i]直接变成2
    4. 判断curNum <= 1，如果是，那么代表我们将nums[i]上面一个为0或者1的数，变成了2，所以我们需要
        将 当前1应该插入的位置 的数变成1，并且将其往右移动一位 -> nums[posOne++] = 1
    5. 判断curNum == 0，如果是，那么代表我们将nums[i]上面一个为0的数，变成了1，所以我们需要
        将 当前0应该插入的位置 的数变成0，并且将其往右移动一位 -> nums[posZero++] = 0
    6. 为什么要用curNum <= 1？而不是像第五步的判断一样用curNum == 1？
        因为本质是做覆盖，且是一步一步扩大的去做覆盖：
        我们在第三步，不管大小，直接全部变成2，这是最初的覆盖，也是范围最大的，因为无论大小我们都做覆盖
        然后第四步，通过curNum <= 1，将0和1的情况都覆盖了
        最后第五步，只覆盖1的情况
        说白了，如果能正确结束，那么最后的结果一定是：[0, 0, ..., 0, 1, 1, 1, ..., 1, 2, 2, ..., 2]
        而此时的posZero一定在0和1的交界处，posOne一定在1和2的交界处
        如果不这么做，假设我们用curNum == 1来判断，那么nums[i] = 0时，因为curNum != 1，所以posOne不会移动，但是posZero会移动
        所以此时可能posZero的位置其实是在posOne右侧，之后就会报错

        比如[2, 0, 1]
        i = 0, curNum = 2, curNum == 1 -> false, curNum == 0 -> false, 所以什么都不做
            posZero = 0, posOne = 0, [2, 0, 1]
        i = 1, curNum = 0, curNum == 1 -> false, curNum == 0 -> true, 此时要将posZero的数改为0, 且posZero向右移动，停在下标1
            即nums[posZero++] = 0, 即nums[0] = 0 与 posZero += 1
            posZero = 1, posOne = 0, [0, 0, 1]
        i = 2, curNum = 1, curNum == 1 -> true, curNum == 0 -> false, 此时要将posOne的数改为1，且posOne向右移动，停在下标1
            即nums[posOne++] = 1, 即nums[0] = 1 与 posOne += 1
            posZero = 1, posOne = 1, [1, 0, 2]
            此时，1把0覆盖了，我们在i = 1时，将nums[0] = 0设置好了，但现在i = 3时，我们又设置了nums[0] = 1，这样做就覆盖了原本该是0的信息

        我们只允许2覆盖1和0，1覆盖0，0覆盖自己，这样才能做到[0, 0, ..., 0, 1, 1, 1, ..., 1, 2, 2, ..., 2]
     */
    public void sortColors2(int[] nums) {
        int posZero = 0;
        int posOne = 0;
        for (int i = 0; i < nums.length; i++) {
            int curNum = nums[i];
            nums[i] = 2;
            if (curNum <= 1) {
                nums[posOne++] = 1;
            }
            if (curNum == 0) {
                nums[posZero++] = 0;
            }
        }
    }
}
