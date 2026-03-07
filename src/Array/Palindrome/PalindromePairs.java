package Array.Palindrome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

https://leetcode.cn/problems/palindrome-pairs/description/

You are given a 0-indexed array of unique strings words.
A palindrome pair is a pair of integers (i, j) such that:
0 <= i, j < words.length,
i != j, and
words[i] + words[j] (the concatenation of the two strings) is a palindrome.
Return an array of all the palindrome pairs of words.
You must write an algorithm with O(sum of words[i].length) runtime complexity.

Example 1:
Input: words = ["abcd","dcba","lls","s","sssll"]
Output: [[0,1],[1,0],[3,2],[2,4]]
Explanation: The palindromes are ["abcddcba","dcbaabcd","slls","llssssll"]

Example 2:
Input: words = ["bat","tab","cat"]
Output: [[0,1],[1,0]]
Explanation: The palindromes are ["battab","tabbat"]

Example 3:
Input: words = ["a",""]
Output: [[0,1],[1,0]]
Explanation: The palindromes are ["a","a"]

思路: 哈希表+拆分
1. 两个word是否能组成回文串可以这么判断:
    1. 将word1分成两部分,一部分自己即是回文串,另一部分刚好能与word2互为反转
        例子: word1 = "llba",word2 = "ab"
        将word1拆分为"ll"与"ba","ll"自己就是回文串,"ba"刚好与"ab"互为反转,所以"abllba"就是回文串
    2. 空字符串也是回文
        例子: word1 = "abcd",word2 = "dcba",
        将word1拆分为""与"abcd",""自己就是回文串,"abcd"刚好与"dcba"互为反转,所以"abcddcba"就是回文串
2. 所以我们需要做的就是:
    1. 将所有的word的反转都保存在哈希表中
    2. 遍历每一个word
    3. 对每一个word,从0到其长度(闭区间,取0时即为""与word本身,取长度时即为word本身与"")拆分
    4. 对拆分后的左右两部分,分别判断
        如果left是回文串,那么我们需要在哈希表中找到right,且right的下标不是自己
        例子:"aba",拆分为""与"aba",""是回文串,"aba"的反转还是"aba",所以能在哈希表中找到,但其实这不算两个字符串,这代表该word本身就是回文,所以要排除这个情况
        同理,如果right是回文串,那么我们需要在哈希表中找到left,且left的下标不是自己
    5. 在判断right的时候,记得跳过j=长度,j=长度时,即拆分为""与word本身,这个跟j=0时(拆分为word本身与"")是重复的
        例子:
        word1 = "ab",word2 = "ba",j = 0时,将word1拆分为""与"a",所以word1能与word2组成回文串,记为(2,1)
                                  j = 2时,将word1拆分为"ab"与"",所以word1能与word2组成回文串,记为(1,2)
                                  j = 0时,将word2拆分为""与"ba",所以word2能与word1组成回文串,记为(1,2)
                                  j = 2时,将word2拆分为"ba"与"",所以word2能与word1组成回文串,记为(2,1)
        可以发现,我们记录了两次
        拆分word1且j=0 拆分word2且j=2 重复
        拆分word2且j=0 拆分word1且j=2 重复
        所以我们排除所有j=长度的情况,避免重复
3. 记录答案时的顺序很重要:
    1. 我们需要对left和right都判断一次
    当left是回文串时,它能与另一个word组成回文串的格式是: 另一个word + left + right, 所以记录为(另一个word的下标, 当前word)
    当right是回文串时,它能与另一个word组成回文串的格式是: left + right + 另一个word, 所以记录为(当前word, 另一个word的下标)
 */
public class PalindromePairs {
    public List<List<Integer>> palindromePairs(String[] words) {
        // 将每一个word的翻转都保存在一个map中
        // 用StringBuilder是因为它做翻转的性能最好
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> revMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            sb.setLength(0);
            revMap.put(sb.append(words[i]).reverse().toString(), i);
        }
        // 遍历每个word,并将其拆分成左右两部分,然后判断
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            /*
            这里需要i <= word.length(),因为我们是从每一个字母的间隙去拆分
            例子: s = "adg", 长度为3
            i = 0 -> ""和"adg"
            i = 1 -> "a"和"dg"
            i = 2 -> "ad"和"g"
            i = 3 -> "adg"和""
            */
            String word = words[i];
            for (int j = 0; j <= word.length(); j++) {
                String left = word.substring(0, j);
                String right = word.substring(j);
                if (isPalindrome(left) && revMap.containsKey(right) && revMap.get(right) != i) {
                    result.add(Arrays.asList(revMap.get(right), i));
                }
                if (j != word.length()
                        && isPalindrome(right)
                        && revMap.containsKey(left)
                        && revMap.get(left) != i) {
                    result.add(Arrays.asList(i, revMap.get(left)));
                }
            }
        }
        return result;
    }

    private boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left <= right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }
}
