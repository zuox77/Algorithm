package Pointers;

/*
https://leetcode.cn/problems/container-with-most-water/description/

You are given an integer array height of length n.
There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
Find two lines that together with the x-axis form a container, such that the container contains the most water.
Return the maximum amount of water a container can store.

Notice that you may not slant the container.

Example 1:
Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7].
In this case, the max area of water (blue section) the container can contain is 49.

Example 2:
Input: height = [1,1]
Output: 1

思路: 双指针
1. 双指针分别指向数组的左右两端
2. 装水的大小 = (right - left) * height
3. right - left是宽, 因为无论如何, 指针只能向中心移动, 所以宽一定是越来越小
4. 要想装水尽可能的多, 只能关注height, height要尽可能的高, 所以判断哪个指针该移动时, 要移动二者中短的那个, 这样装水才有可能增多
    移动短的: 1. 下一个更长: 面积可能增大
            2. 下一个更短: 面积减小
    移动长的: 1. 下一个更长: 面积不会增大, 因为短的并没有变, height是由最短的那个决定的
            2. 下一个更短: 面积减小, 面积可能减少(比当前最短的还要短时), 可能不变
    所以：height[left]和height[right] 谁小 谁往中间移动
 */
public class ContainerWithMostWater {
    public int containerWithMostWaterArea(int[] height) {
        int n = height.length;
        int maxWater = 0, left = 0, right = n - 1;
        while (left < right) {
            maxWater = Math.max((right - left) * Math.min(height[left], height[right]), maxWater);
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;
    }
}
