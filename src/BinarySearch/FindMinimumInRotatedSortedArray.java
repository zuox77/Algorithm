package BinarySearch;

/*
刷题次数: 2
第二次: 基本记得

https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/description/

Suppose an array of length n sorted in ascrighting order is rotated between 1 and n times. For example, the array nums = [0,1,2,4,5,6,7] might become:
[4,5,6,7,0,1,2] if it was rotated 4 times.
[0,1,2,4,5,6,7] if it was rotated 7 times.
Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
Given the sorted rotated array nums of unique elements, return the minimum element of this array.
You must write an algorithm that runs in O(log n) time.

Input: nums = [4,5,6,7,0,1,2]
Output: 0

思路: 二分
1. 思路还是正常的二分
2. 因为是找的整个数组的最小值, 所以最后只需要判断一下left和right所在位置的值哪个小, 就返回那个
3. 再一次强调, 用二分的本质是将范围缩小到两个数, 即退出循环后的left和right, 所以需要额外判断一次
4. 在此题中, 其实可以理解为, 找到旋转的那个竖轴, 具体看下面的解释
5. 首先理解旋转其实就是把一条斜线, 变成了两条, 如下: 
         23
        11
       9
      4
     3
    1
    变为: 
       23|
      11 |
     9   |
    4    |
    ----------
         |  3
         | 1
5. 上面的竖轴的左(23)和右(1)分别就是最大和最小, 也就是我们想找到的答案
   而我们用二分和双指针来做的话, 其实就是考虑究竟什么时候能让left和right指针不断向竖轴接近
   即left与right相邻时, left在横轴的左侧(指着23), right在横轴的右侧(指着1)
6. 其实向判断什么时候能移动指针以及怎么移动, 我们只需要简单的判断此时的mid点究竟是在竖轴左侧还是右侧
   而判断左侧右侧就需要用到横轴, 即比横轴大的就在左侧, 小的就在右侧
   即比横轴大的就向右移动: nums[mid] > nums[-1] => right = mid
   比横轴小的就向左移动: nums[mid] < nums[-1] => left = mid
7. 横轴有两个选择, 要么第一个数要么最后一个数 (推荐使用最后一个数作为横轴)
8. 有个特殊情况就是这个数列没有旋转的时候
   1. 如果选第一个数作为横轴, 此时left指针其实已经指向了竖轴的右边, 所以此时left还会向右移动, 所以得到的答案其实是一个中位数, 即常规二分法
        可以写一个if把未旋转的情况排除, 即if nums[0] < nums[-1] => return nums[0]
   2. 如果选第最后一个数作为横轴, 因为所有的数都比最后一个数小, 所以right一直在移动, 不能直接返回nums[right], 比如[2,1]就是right最小, 但
        [1,2]就是left最小, 所以要用min找left与right的最小值, 即return min(nums[left], nums[right])

思路1: 二分 + 搭配2
1. 用搭配2的话, 就不需要考虑特别多, 按照公式来就行

思路2: 二分 + 搭配1
1. 用搭配1的话, 需要考虑right到底怎么移动, 然后需要想明白循环退出时left和right的位置
2. right = mid就行
3. 退出时left和right在同一个位置, 直接返回就行

时间复杂度: O(logN), 其中N是数组的长度
空间复杂度: O(1)
 */

public class FindMinimumInRotatedSortedArray {
    public int solution1(int[] nums) {
        // 定义指针
        int n = nums.length;
        int left = 0, right = n - 1;
        // 以最后一位数作为横轴
        int xAxis = nums[n - 1];
        // 二分法遍历
        while (left + 1 < right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 根据情况做判断
            // 如果mid大于横轴, 说明mid在竖轴左侧, 那么移动左指针去接近竖轴
            if (nums[mid] > xAxis) {
                left = mid;
            // 如果mid小于横轴, 说明mid在竖轴右侧, 那么移动右指针去接近竖轴
            } else {
                right = mid;
            }
        }
        // 最后判断一下left和right
        return nums[left] < nums[right] ? nums[left] : nums[right];
    }

    public int solution2(int[] nums) {
        // 定义指针
        int n = nums.length;
        int left = 0, right = n - 1;
        // 以最后一位数作为横轴
        int xAxie = nums[n - 1];
        // 二分法遍历
        while (left < right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 根据情况做判断
            // 如果mid大于横轴, 说明mid在竖轴左侧, 那么移动左指针去接近竖轴
            if (nums[mid] > xAxie) {
                left = mid + 1;
            // 如果mid小于横轴, 说明mid在竖轴右侧, 那么移动右指针去接近竖轴
            } else {
                right = mid;
            }
        }
        // 由于循环条件是left < right, 所以退出循环时, left和right肯定是在同一个点, 即最小的点, 所以直接返回任意一个就行
        return nums[left]; // return nums[right]也可以
    }
}
