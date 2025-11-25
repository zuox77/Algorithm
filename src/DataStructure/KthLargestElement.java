package DataStructure;

import java.util.PriorityQueue;

/*
https://leetcode.cn/problems/kth-largest-element-in-an-array/?favorite=2cktkvj

Given an integer array nums and an integer k, return the kth largest element in the array.
Note that it is the kth largest element in the sorted order, not the kth distinct element.
You must solve it in O(n) time complexity.

Input: nums = [3,2,1,5,6,4], k = 2
Output: 5
 */

public class KthLargestElement {
    public int solution1(int[] nums, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>(k);
        int result = -1;
        // corner case
        if (k < 1) {
            return result;
        }

        for (int num : nums) {
            if (heap.size() < k) {
                heap.add(num);
            } else if (heap.peek() < num) {
                heap.poll();
                heap.add(num);
            }
        }

        return heap.poll();
    }
}
