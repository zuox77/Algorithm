package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
刷题次数: 1

https://leetcode.cn/problems/top-k-frequent-elements/description/?envType=study-plan-v2&envId=top-100-liked

Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.

Example 1:
Input: nums = [1,1,1,2,2,3], k = 2
Output: [1,2]

Example 2:
Input: nums = [1], k = 1
Output: [1]

Example 3:
Input: nums = [1,2,1,2,1,2,3,1,3,2], k = 2
Output: [1,2]

思路:
1. 先遍历一遍，用map记录每个数字出现的次数
2. 创建一个数组，利用数组的下标代表次数，数组里面装一个List<Integer>，用来装所有次数为下标的数字
3. 遍历数组，取前k个

注意：
1. 创建一个数组，数组里面是列表：List<Integer>[] freqList = new List[maxFreq + 1]
2. 创建完以后记得初始化，否则每个都是null：Arrays.setAll(freqList, i -> new ArrayList<>())

 */

public class TopKFrequentElements {
    public int[] topKFrequent(int[] nums, int k) {
        // 用map保存每个数字出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        // 再用一个变量保存最大的次数的值
        int maxFreq = 0;
        for (int num: nums) {
            map.merge(num, 1, Integer::sum);
            maxFreq = Math.max(maxFreq, map.get(num));
        }
        // 创建一个数组，用数组下标作为次数，保存相应的数字
        List<Integer>[] freqList = new List[maxFreq + 1]; // 这里加1是因为下标为0代表频率为0的，所以多了一个数
        // 初始化
        Arrays.setAll(freqList, i -> new ArrayList<>());
        // 遍历map，填上对应的频率和数字
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            freqList[entry.getValue()].add(entry.getKey());
        }
        // 倒序遍历刚刚的数组，提取前k个数
        int[] ans = new int[k];
        int count = 0;
        for (int i = maxFreq; i >= 0 && count < k; i--) {
            for (int num: freqList[i]) {
                ans[count++] = num;
            }
        }
        return ans;
    }
}
