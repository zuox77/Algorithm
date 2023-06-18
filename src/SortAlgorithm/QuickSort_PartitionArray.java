package SortAlgorithm;
/*
https://www.lintcode.com/problem/31/
https://www.jiuzhang.com/problem/partition-array/
快排思路: 
1. 双指针+递归
2. 递归出口: start >= end
3. 找到一个pivot基点
4. 左右指针分别指向数组两端, 通过判断与pivot的的大小, 移动指针
5. 用while将左指针移动到第一个比pivot大的下标, 同理, 右指针需要移动到第一个比pivot小的下标
6. 交换左右指针所指向的数组里的数
7. 所有指针移动或者交换的操作, 都要检查left <= right, 注意一定是小于等于
8. 交换以后指针也要移动
9. 遍历结束后, 下一层递归的参数, 记得此时left一定大于right, 所以是(start, right)和(left, end)

整个while (left <= right)循环结束以后, left和right的位置: 
1. 如果按照一个下标来划分, 比如mid, 那么, 
    1. 如果在后面判断大小时, 只交换比nums[mid]大和nums[mid]小的位置, 不管等于nums[mid]的情况, 那么nums[mid]这个值的位置不会变, 且
        right会处于mid下标的位置, left会在right右侧一位, 即mid+1下标的位置, 
        [3, 2, 1, 4, 5 ,9, 8], mid = 3, 那么right在4的位置, left在5的位置
    2. 如果在后面判断大小时, 将等于nums[mid]的情况划分到比nums[mid]大或者比nums[mid]小, 即判断时候用>=或者<=, 那么
        nums[mid]这个值的位置会变, left和right则是在分割线的左右, left在右, right在左, 比如
        [6 ,9, 3, 2, 8, 1, 4], mid = 3, nums[mid]=2, 划分结束后为
        [1, 9, 3, 2, 8, 6, 4], 此时right在0, left在1, 0与1之间是分割线
2. 如果没有pivot, 只是需要按照某种方式划分出两部分, 比如正负划分, 或者pivot不存在在数组里面, 那么left会在第二个部分的第一个位置, 
    right会在第一个部分的最后一个位置, 比如
    [1, 2, 3, -1, -2, -3], 那么right会在3的位置, left会在-1的位置
    [3, 2, 1, 4, 6 ,9, 8], pivot = 5, 那么right在4的位置, left在6的位置

思路: 
1. 这道题不需要把数组完全排好序, 只需要找到数组划分的位置, 所以其实是阉割版的quickSort, 不需要递归, 只需要遍历一次即可
2. 因为要求返回大于K的第一个数, 所以返回left(每次交换完left都大于right), 如果是返回小于K的最后一个数, 就返回right-1就行
 */

public class QuickSort_PartitionArray {

    // quick sort template
    public void quickSortTemplate(int[] nums, int start, int end) {
        // recursion exit
        if (start >= end) {
            return;
        }

        // iterate
        int left = start, right = end;
        int pivotIndex = (start + end) / 2;
        int pivot = nums[pivotIndex];
        while (left <= right) { // 记住left和right的比较都是小于等于, 因为指针有可能重叠
            // find the first element which is > pivot and on the left side of pivot
            while (left <= right && nums[left] < pivot) {
                left++;
            }
            // find the first element which is < pivot and on the right side of pivot
            while (left <= right && nums[right] > pivot) {
                right++;
            }

            if (left <= right) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
                right--;
            }

        // recursion
        quickSortTemplate(nums, start, right);
        quickSortTemplate(nums, left, end);
        }
    }

    // partition array
    public int[] solution(int[] nums, int k) {
        int left = 0, right = nums.length - 1;
        int pivotIndex = (left + right) / 2;
        k = nums[pivotIndex];
        while (left <= right) {
            while (left <= right && nums[left] < k) {
                left++;
            }
            while (left <= right && nums[right] >= k) {
                right--;
            }
            if (left <= right) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
                right--;
            }
        }
        return nums;
    }
}
