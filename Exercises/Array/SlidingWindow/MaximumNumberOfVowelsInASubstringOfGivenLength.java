package Array.SlidingWindow;

/*
刷题次数: 1

https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/?envType=study-plan-v2&envId=top-100-liked

Given a string s and an integer k, return the maximum number of vowel letters in any substring of s with length k.
Vowel letters in English are 'a', 'e', 'i', 'o', and 'u'.

Example 1:
Input: s = "abciiidef", k = 3
Output: 3
Explanation: The substring "iii" contains 3 vowel letters.

Example 2:
Input: s = "aeiou", k = 2
Output: 2
Explanation: Any substring of length 2 contains 2 vowels.

Example 3:
Input: s = "leetcode", k = 3
Output: 2
Explanation: "lee", "eet" and "ode" contain 2 vowels.

思路：滑动窗口
1. 利用一个set保存所有的元音，这样方便查询
2. 在遍历时，用for loop，且for loop里的指针最好是右侧端点，这样更简单去处理边界问题
 */

import java.util.Set;

public class MaximumNumberOfVowelsInASubstringOfGivenLength {
    public int maxVowels(String s, int k) {
        // 创建一个包含所有元音的set，方便之后进行判断
        Set<Character> set = Set.of('a', 'e', 'i', 'o', 'u');

        // 通过右指针进行for loop遍历s
        int ans = 0;
        int maxAns = 0;
        for (int right = 0; right < s.length(); right++) {
            // 判断当前右指针所指向的字母是否算元音
            if (set.contains(s.charAt(right))) {
                ans++;
            }
            // 通过右指针和k的长度，算出滑动窗口的左指针
            int left = right - k + 1;
            if (left < 0) continue;
            // 更新maxAns
            maxAns = Math.max(maxAns, ans);
            // 将左侧端点位置移动（从滑动窗口中移出）
            if (set.contains(s.charAt(left))) {
                ans--;
            }
            if (maxAns == k) {
                return maxAns;
            }
        }
        return maxAns;
    }
}
