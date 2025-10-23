package Array.SlidingWindow;

/*
刷题次数: 1

https://leetcode.cn/problems/minimum-window-substring/description/

Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that
every character in t (including duplicates) is included in the window. If there is no such substring, return the empty string "".
The testcases will be generated such that the answer is unique.

Example 1:
Input: s = "ADOBECODEBANC", t = "ABC"
Output: "BANC"
Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.

Example 2:
Input: s = "a", t = "a"
Output: "a"
Explanation: The entire string s is the minimum window.

Example 3:
Input: s = "a", t = "aa"
Output: ""
Explanation: Both 'a's from t must be included in the window.
Since the largest window of s only has one 'a', return empty string.

时间复杂度: O(M+N), 遍历一次
空间复杂度: O(1)

思路：不定长滑动窗口
1. 整体思路与定长滑动窗口类似，不同的是：
    1. 只需要targetFreq来记录p的字母的出现次数，当遍历s时，只需在targetFreq中，将当前出现的字母减1
        意义为：targetFreq里面的字母出现的次数都为0时，表明当前滑动窗口里面的字母刚好等于p，反之则不等于
    2. 如果遍历s时，出现了p中不存在的字母，只需要不断的移动左指针left，直到该不存在的字母出现的频率不为负数
        意义为：如果右端点是一个p中不存在的字母，那么要达到不存在的字母出现的频率不为负数条件，只有左端点向右移动，于右端点重合，
        即为抛弃右端点前面所有的字母，因为出现了一个不存在的字母，前面无论有多少符合的字母，就因为这一个不存在的字母，左端点和右端点都需要
        挪到该不存在的字母的下一个位置，然后继续遍历
2. for loop：
    1. 右端点加入滑动窗口，即targetFreq[右端点字母所处位置]-1
    2. 计算左端点位置
    3. 判断targetFreq[右端点字母所处位置]是否小于0，如果小于0，那么说明出现了不存在p中的字母，则
        左端点一路往右，即targetFreq[left]+1; left++;
    4. 当左端点和右端点刚好相距p的长度时候，由于前面的条件排除了所有遇到不存在的字幕的情况，所以此时，只剩一种可能，即当前滑动窗口就是p
        所以无需其他判断，只需要长度相等，就可以把left加入ans

 */

import java.util.*;

public class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        // 创建一个map来记录t中每个字母的出现次数
        Map<Character, Integer> targetFreq = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetFreq.merge(c, 1, Integer::sum);
        }

        // 创建一个map在记录当前滑动窗口中字母出现的次数
        Map<Character, Integer> windowFreq = new HashMap<>();

        // 从左往右遍历s
        int left = 0; // 左指针
        int ansLeft = -1;
        int ansRight = s.length();
        char[] source = s.toCharArray();
        for (int right = 0; right < source.length; right++) {
            // 将右指针位置上的字母放入滑动窗口
            windowFreq.merge(source[right], 1, Integer::sum);
            // 判断是否涵盖，如果涵盖且长度更短，那么更新答案
            while (ifIncluded(windowFreq, targetFreq)) {
                if (right - left < ansRight - ansLeft) {
                    ansRight = right;
                    ansLeft = left;
                }
                // 将左侧端点位置移出滑动窗口
                windowFreq.merge(source[left], -1, Integer::sum);
                left++;
            }
        }
        return ansLeft < 0 ? "" : s.substring(ansLeft, ansRight + 1);
    }

    public boolean ifIncluded(
            Map<Character, Integer> windowFreq, Map<Character, Integer> targetFreq) {
        // 检查targetFreq中每个字母的次数是否都大于windowFreq中对应字母的次数
        // 如果大于，则说明当前滑动窗口并没有覆盖t中所有的字母
        for (Map.Entry<Character, Integer> entry : targetFreq.entrySet()) {
            if (entry.getValue() > windowFreq.getOrDefault(entry.getKey(), 0)) {
                return false;
            }
        }
        return true;
    }
}
