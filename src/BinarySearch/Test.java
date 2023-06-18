package BinarySearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
给定两个数组, 编写一个函数来计算它们的交集
示例 1:
输入: nums1 = [1,2,2,1], nums2 = [2,2]
输出: [2,2]

示例 2:
输入: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
输出: [4,9]

说明: 
输出结果中每个元素出现的次数, 应与元素在两个数组中出现的次数一致
我们可以不考虑输出结果的顺序
 */
public class Test {
    public List<Integer> solution(int[] nums1, int[] nums2) {
        // check length
        int n = nums1.length;
        int m = nums2.length;

        // corner case
        List<Integer> result = new ArrayList<>();
        if (n == 0 || m == 0) {
            return result;
        }

        // traverse nums1
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (map.containsKey(nums1[i])) {
                map.put(nums1[i], map.get(nums1[i]) + 1);
            } else {
                map.put(nums1[i], 1);
            }
        }

        // traverse nums2
        for (int j = 0; j < m; j++) {
            if (map.containsKey(nums2[j]) && map.get(nums2[j]) > 0) {
                result.add(nums2[j]);
                map.put(nums2[j], map.get(nums2[j]) - 1);
            }
        }
        return result;
    }
}
