package Array.SlidingWindow;

/*
刷题次数: 1

https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/?envType=study-plan-v2&envId=top-100-liked

Given two strings s and p, return an array of all the start indices of p's anagrams in s. You may return the answer in any order.

Example 1:
Input: s = "cbaebabacd", p = "abc"
Output: [0,6]
Explanation:
The substring with start index = 0 is "cba", which is an anagram of "abc".
The substring with start index = 6 is "bac", which is an anagram of "abc".

Example 2:
Input: s = "abab", p = "ab"
Output: [0,1,2]
Explanation:
The substring with start index = 0 is "ab", which is an anagram of "ab".
The substring with start index = 1 is "ba", which is an anagram of "ab".
The substring with start index = 2 is "ab", which is an anagram of "ab".

时间复杂度: O(N), 遍历一次
空间复杂度: O(1)

思路1：定长滑动窗口
1. 利用数组targetFreq的下标来代表字母所代表的数字，这样去记录每个字母出现的次数：freq[当前字母 - 'a']++;
2. 将p中出现过的字母遍历一遍，记录进targetFreq数组
3. 创建一个新的数组windowFreq，用来在下面的遍历中记录当前滑动窗口里面每个字母出现的次数
4. 遍历s，用for loop，且for loop里的指针最好是右侧端点，这样更简单去处理边界问题
5. for loop中：
    1. 将右侧端点加入滑动窗口
    2. 计算出左侧端点的位置
    3. 判断左侧端点是否小于0，如果小于，那么说明此时滑动窗口的还没达到p.length个，要继续从右侧加入字母
    4. 判断滑动窗口中的字母是否等于p，用Arrays.equals，如果是，将左侧端点位置加入ans
    5. 将左侧端点移出windowFreq

思路2：不定长滑动窗口
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindAllAnagramsInAString {
    public List<Integer> findAnagrams(String s, String p) {
        // 创建一个数组记录每个字母在p中出现的频率
        int[] targetFreq = new int[26];
        for (char c : p.toCharArray()) {
            targetFreq[c - 'a']++;
        }

        // 以右侧端点作为指针，遍历s数组，建立滑动窗口，每次右端进入一个新的字母，然后判断当前滑动窗口是否匹配p
        // 如果匹配，则把左侧端点位置加入ans，如果不匹配则跳过。最后把左侧端点移出滑动窗口
        List<Integer> ans = new ArrayList<>();
        int[] windowFreq = new int[26];
        for (int right = 0; right < s.length(); right++) {
            // 将右侧端点的字母加入滑动窗口
            windowFreq[s.charAt(right) - 'a']++;
            // 按照滑动窗口长度，计算当右侧端点位置为right时，左侧端点应该为多少
            int left = right - p.length() + 1;
            // 如果小于0，表示滑动窗口中的字母数量还没有达到p的数量，则右侧端点继续移动，直到左侧端点为0
            if (left < 0) continue;
            // 判断
            if (Arrays.equals(windowFreq, targetFreq)) {
                // 将左侧端点位置加入ans
                ans.add(left);
            }
            // 从滑动窗口中（windowFreq）移出左侧端点
            windowFreq[s.charAt(left) - 'a']--;
        }

        return ans;
    }

    public List<Integer> findAnagrams2(String s, String p) {
        // 创建一个数组来记录p中的字母数量
        int[] targetFreq = new int[26];
        // 更新数组
        for (char c : p.toCharArray()) {
            targetFreq[c - 'a']++;
        }
        // 遍历s
        List<Integer> ans = new ArrayList<>();
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            // 将右端点加入滑动窗口
            int rightCharIndex = s.charAt(right);
            targetFreq[rightCharIndex - 'a']--;
            // 当遇到不存在的字母，将左指针移动到右指针位置，即使滑动窗口重置
            // 从不存在的字母的下一个位置开始重新算
            while (targetFreq[rightCharIndex - 'a'] < 0) {
                // 将左指针往右移动，即将左指针所在位置移出滑动窗口
                targetFreq[s.charAt(left) - 'a']++;
                left++;
            }
            // 判断长度是否等于p，如果等于，那么一定是符合答案的
            if (right - left + 1 == p.length()) {
                ans.add(left);
            }
        }
        return ans;
    }
}
