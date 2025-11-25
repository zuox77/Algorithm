package Pointers;

import java.util.HashMap;

/*

https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/

Given a string s, find the length of the longest substring without duplicate characters.

Example 1:
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3. Note that "bca" and "cab" are also correct answers.

Example 2:
Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.

Example 3:
Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.

 */
public class LongestUniqueSubstring {
    public int longestUniqueSubstring(String s) {
        // 哈希表保存字母和下标的关系
        HashMap<Character, Integer> map = new HashMap<>();

        /*
        滑动窗口的思路
        left代表左端，通过for循环，遍历右端
        1. 新出现的字母 -> 直接加入map，更新长度
        2. 已经出现过的字母 -> 找到哈希表中该字母上次出现时的下标，
            将从left到上次出现时的下标（包括left和上次出现时的下标）中的字母，一一从字典中删除
            每删除一次，left往右挪动一次
            例如："abccbdf" 当遍历到第二个c的时候，哪怕left现在在0，但是ab都不能用了，后面的计算中，left要从第二个c开始
            所以将a b c全部删除，left指向第二个c
         */
        int left = 0;
        int maxLength = 0;
        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            if (map.containsKey(ch)) {
                int lastCh = map.get(ch);
                for (int i = left; i <= lastCh; i++) {
                    map.remove(s.charAt(i));
                    left++;
                }
            }
            map.put(ch, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }
}
