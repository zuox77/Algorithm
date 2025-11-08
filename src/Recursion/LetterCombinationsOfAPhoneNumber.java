package Recursion;

/*
https://leetcode.cn/problems/letter-combinations-of-a-phone-number/description/?envType=study-plan-v2&envId=top-100-liked

Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.
A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.

Example 1:
Input: digits = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]

Example 2:
Input: digits = "2"
Output: ["a","b","c"]

思路: 递归/dfs
1. 跟Permutation这道题差不多, 但允许重复元素, 而结果中又不允许重复答案
2. 比如[1, 2, 2'], 按照Permutation的做法的话, 当第一层的for遍历到1时, 往下递归第二层遍历到2时, 再往下递归到第三层得到[1, 2, 2'],
    然后返回第二层, 第二层for循环继续, 遍历到2'时, 再往下递归得到[1, 2', 2], 但对于结果这两个算重复的, 只能计算一次
3. 一种省事的方案是, 按照上一题的思路算, 最后将答案转化成HashSet/SetList之类的不重复集合, 然后再转化回来, 但肯定过不了面试
4. 重复元素的题, 答案需要具体的方案, 可以考虑先给数组排个序 Arrays.sort()
5. 排序以后, 重复的元素一定都相互挨着, 此时考虑计算时候的条件:
    满足下面任意一个条件即表示需要计算:
    1. 当前后数不为重复数字, 说明当前数字跟前一个不重复, 又因为排序过, 所以跟之前也一定不重复,
        说明这是个新的数字, 一定不会与前面已经加入path的数造成重复计算
    2. 前一个数在visited里 = visited.contains(i - 1), 说明就算此时与前一个数重复, 但前一个数已经被使用, 那现在这个重复的数是在计算第一次,
        比如path为[1, 2, 3, 3']的第一次计算
        之后回溯回去再遍历到[1, 2, 3']时候, 在第三层, num[i] = 3', 此时它在原数组[1, 2, 3, 3']的前一个数为3, 因为在第三层,
        所以3还不在visited里, 所以虽然前后是重复数字, 但也不需要计算了, 因为这不是第一次计算了
    更通用的解释是: 如果要计算, 那一定是这个排列顺序的第一次出现, 所以有前后重复的数时, 在第一次计算中, 因为我们是从左向右遍历, 前面的重复的数
    已经先于后面的数加入visited或者说这个排列顺序path里了, 所以前边的数一定比后边先加入一种排列顺序并且此排列顺序已经加入了答案里面,
    所以之后重复计算时, 一定是后边的重复数先add进path里面, 所以前边出现的重复的数, 此时一定不在visited里面, 所以可以判断这是第二次或者更多次的计算,
    是重复的, 所以不需要计算了

 */

import java.util.*;

public class LetterCombinationsOfAPhoneNumber {

    private static final String[] MAP = {
        "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<String>();
        recursion(digits, 0, new StringBuilder(), ans);
        return ans;
    }

    public void recursion(String digits, int depth, StringBuilder sb, List<String> ans) {
        // 退出条件
        if (depth == digits.length()) {
            ans.add(new String(sb));
            return;
        }
        // 每次循环
        for (char c : MAP[digits.charAt(depth) - '0'].toCharArray()) {
            // 更新sb
            sb.append(c);
            // 进入下一层
            recursion(digits, depth + 1, sb, ans);
            // 返回以后，更新sb
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
