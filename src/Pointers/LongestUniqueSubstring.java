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

思路: 不定长滑动窗口
1. 这个题有个隐藏信息需要首先清楚: 计算最长子字符串, 那么一定是一个区间, 想得到这个区间最长可以是多少, 而区间必然有左端和右端,
    最开始时, 从下标0开始, 左端即是下标0, 右端一直往右, 直到出现第一个重复字符
    当新的字符是重复字符时, 这个重复字符第一次出现的位置假设为i, 那么从这一刻开始, i之前的所有字符都要抛弃, 或者说计数要从头开始算
    比如"abcadef", 当第二个a出现时, 计数清零并且a第一次出现的位置的下一个位置, 成为新的左端
    比如"abccdef", 当第二个c出现时, 计数清零并且c第一次出现的位置的下一个位置, 成为新的左端
    因为如果还想继续往右计算, 那么必然会将这个重复字符的第二次包括在其中, 比如例子1里面的a, 后续的计算都要包括第二个a, 那只能抛弃第一个a
2. 用哈希表记录字符和下标的映射关系, 当重复出现时, 将左指针更新到max(left, map.get(重复字符) + 1）
    这里之所以用max是因为:
    比如"abba", 在第二个b出现时, 左指针其实已经更新到了下标2, 即第二个b的位置, 此时下标2之前的字符串都不能使用, 不然就会造成重复,
    如果没有这个max函数, 当第二个a出现时, 理论上应该将左指针更新到下标1, 即第一个b的位置, 但我们刚刚已经确定了, 下标2之前的所有字符都不能用,
    不然字符b就会重复, 所以为了避免, 有些字符第一次出现的早但第二次出现的晚的时候, 左指针会往回更新的情况, 要用max
3. 当没有重复时, 将新的字符put进哈希表, 并且更新最大的长度max(maxCount, i - left + 1)
    加1是因为, 比如i在下标1, left在下标0, 他们之间是2个字符

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
