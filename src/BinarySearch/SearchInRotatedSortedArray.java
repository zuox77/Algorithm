package BinarySearch;

/*
刷题次数: 2
第二次: 大概记得, 但具体的如何实现, 有多少种情况忘了

https://leetcode.cn/problems/search-in-rotated-sorted-array/description/

There is an integer array nums sorted in ascending order (with distinct values).
Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that
the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed).
For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.
You must write an algorithm with O(log n) runtime complexity.

Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4

思路: 二分
1. 这题算是变形二分, 多了一个条件, 数组是旋转的, 与FindMinimumInRotatedSortedArray类似, 但不同之处在于, 这道题要找的是target而不是最小值, 算是进阶题
2. 依旧是用标准的二分公式, 由于题目里面提到了没有重复, 所以依旧推荐用搭配2来做
3. 这道题的难点在于, 由于是旋转后的, 所以不能再单纯的判断mid和target的值, 决定指针的移动方向, 举个例子:
   现在有数组如下:
       23|
      11 |
     9   |
    4    |
    ----------
         |  3
         | 1
   假设此时mid = 2, 即nums[mid] = 11, 假设target是3
   按照普通的二分, 我们此时判断大小, 可得nums[mid] > target, 所以target应该在mid右侧, 所以应该移动left, left = mid
   但如果target是9呢？此时判断大小, 依旧是nums[mid] > target, 但此时移动left会直接把9给漏掉, 因为left = mid后, 剩下的是[11,23,1,3]
   所以要分情况讨论
4. 依旧是用nums[n - 1]代表横轴的标准来比较, 具体原因有在FindMinimumInRotatedSortedArray里讲过, 可以看看
5. 这个题的情况与三个因素有关: 1. target在竖轴的左侧还是右侧 2. nums[mid]在竖轴的左侧还是右侧 3. nums[mid]与target的大小
6. 通过横轴的标准来比较, 假设两个变量分别代表前两个因素:
   1. targetInLeft:
       1. target > nums[n - 1]: target在竖轴的左侧, 例子: target = 9, nums[n - 1] = 3 -> targetInLeft = true
       2. target < nums[n - 1]: target在竖轴的右侧, 例子: target = 1, nums[n - 1] = 3 -> targetInLeft = false
       3. target = nums[n - 1]: 找到target, 直接返回  -> return n - 1;
   2. midInLeft:
       1. nums[mid] > nums[n - 1]: nums[mid]在竖轴的左侧, 例子: nums[mid] = 9, nums[n - 1] = 3 -> midInLeft = true
       2. nums[mid] < nums[n - 1]: nums[mid]在竖轴的右侧, 例子: nums[mid] = 1, nums[n - 1] = 3 -> midInLeft = false
       3. nums[mid] = nums[n - 1]: 因为nums[n - 1]自己就在竖轴的右侧, 所以此时nums[mid]也在竖轴的右侧, -> midInLeft = false
5. 现在列举一下所有的情况:
   1. 当target在竖轴的左侧时: (targetInLeft = true)
       1. 当nums[mid] > target:
           1. 因为target在竖轴的左侧, 且nums[mid] > target, 所以无论如何, nums[mid]都一定在左侧, 因为右侧一定比左侧小
           2. 所以此时midInLeft只能是true, 不然nums[mid] > target就不成立
           3. 所以此时移动右指针 -> right = mid
       2. 当nums[mid] < target:
           1. 当nums[mid]在竖轴左侧, 即midInLeft = true: 例子: target = 9, nums[mid] = 4 -> 移动左指针 -> left = mid
           2. 当nums[mid]在竖轴右侧, 即midInLeft = false: 例子: target = 1, nums[mid] = 4 -> 移动右指针 -> right = mid
       3. 当nums[mid] = target: 直接返回 -> return mid
   2. 当target在竖轴的右侧时: (targetInLeft = false)
       1. 当nums[mid] < target:
           1. 因为target在竖轴的右侧, 且nums[mid] < target, 所以无论如何, nums[mid]都一定在右侧, 因为左侧一定比右侧大
           2. 所以此时midInLeft只能是false, 不然nums[mid] < target就不成立
           3. 所以此时移动左指针 -> left = mid
       2. 当nums[mid] > target:
           1. 当nums[mid]在竖轴左侧, 即midInLeft = true: 例子: target = 1, nums[mid] = 9 -> 移动左指针 -> left = mid
           2. 当nums[mid]在竖轴右侧, 即midInLeft = false: 例子: target = 1, nums[mid] = 3 -> 移动右指针 -> right = mid
       3. 当nums[mid] = target: 直接返回 -> return mid
6. 因为条件和情况都比较多, 不太好写, 所以写最简单的条件, 用else代替复杂的, 规整一下上面的条件, 现在有:
   targetInLeft:
       nums[mid] > target                -> right = mid;
       nums[mid] < target && midInLeft   ->  left = mid;
       nums[mid] < target && !midInLeft  -> right = mid;
   !targetInLeft:
       nums[mid] < target                ->  left = mid;
       nums[mid] > target && midInLeft   ->  left = mid;
       nums[mid] > target && !midInLeft  -> right = mid;
   所以一种好写的方式是: targetInLeft时, 就写nums[mid] < target && midInLeft, 剩下的用else
                    !targetInLeft时, 就写nums[mid] > target && !midInLeft, 剩下的用else
8. 如果要记忆的话, 就记住三点:
   1. 先以targetInLeft作为条件写一个if
   2. target和mid都在左侧, 且nums[mid] < target, 左指针动, 其余写else且右指针动 -> 都在左 mid小 左指针动
   3. target和mid都在右侧, 且nums[mid] > target, 右指针动, 其余写else且左指针动 -> 都在右 mid大 右指针动
7. 由于是搭配2, 所以最后需要检查一下left和right, 如果找到就返回, 否则就返回-1
 */

public class SearchInRotatedSortedArray {
    public int search(int[] nums, int target) {
        // 定义指针
        int n = nums.length;
        int left = 0, right = n - 1;
        // 定义横轴, 以nums[n - 1]作为横轴
        if (target == nums[n - 1]) {
            return n - 1;
        }
        // 判断target在竖轴左侧还是右侧
        boolean targetInLeft = target < nums[n - 1] ? false : true;
        while (left + 1 < right) {
            // 找到中点
            int mid = left + (right - left) / 2;
            // 找到直接返回
            if (nums[mid] == target) {
                return mid;
            }
            // 判断中点在竖轴左侧还是右侧
            boolean midInLeft = nums[mid] < nums[n - 1] ? false : true;
            // 如果target在左侧
            if (targetInLeft) {
                // 看上面 -> 5.1.1.2.1
                if (nums[mid] < target && midInLeft) {
                    left = mid;
                    // 其余的所有可能
                } else {
                    right = mid;
                }
                // 如果target在右侧
            } else {
                // 看上面 -> 5.2.1.2.2
                if (nums[mid] > target && !midInLeft) {
                    right = mid;
                } else {
                    left = mid;
                }
            }
        }
        // 最后额外检查一次
        return nums[left] == target ? left : nums[right] == target ? right : -1;
    }
}
