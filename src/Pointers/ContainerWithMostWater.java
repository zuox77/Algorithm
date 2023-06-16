package Pointers;
/*
https://leetcode.cn/problems/container-with-most-water/description/

思路：双指针
1. 双指针分别指向数组的左右两端
2. 装水的大小 = (right - left) * height
3. right - left是宽，因为无论如何，指针只能向中心移动，所以宽一定是越来越小
4. 要想装水尽可能的多，只能关注height，height要尽可能的高，所以判断哪个指针该移动时，要移动二者中短的那个，这样装水才有可能增多
    移动短的：1. 下一个更长：面积可能增大
            2. 下一个更短：面积减小
    移动长的：1. 下一个更长：面积不会增大，因为短的并没有变，height是由最短的那个决定的
            2. 下一个更短：面积减小，面积可能减少(比当前最短的还要短时)，可能不变
 */
public class ContainerWithMostWater {
    public int solution(int[] height) {
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
