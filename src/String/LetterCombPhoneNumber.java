package String;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/letter-combinations-of-a-phone-number/description/

思路: 递归dfs
1. 普通递归
2. 需要注意各种格式的使用和转换: 
    1. 数字与子母的转化不一定非要用哈希表, 可以用一个数组, 下标表示数字
    2. 用StringBuilder来记录每一个结果, 用toString()最后存入result
    3. StringBuilder的添加是append, 删除是deleteCharAt
    4. char相减可以直接变成int
 */
public class LetterCombPhoneNumber {
    class solution {
        private String[] map = {" ", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        public List<String> letterCombinations(String digits) {
            int n = digits.length();
            List<String> result = new ArrayList<>();
            if (n < 1) {
                return result;
            }
            StringBuilder tmpResult = new StringBuilder();
            recursion(digits, 0, n, result, tmpResult);
            return result;
        }

        public void recursion(String digits, int curIndex, int n, List<String> result, StringBuilder tmpResult) {
            // recursion exit
            if (curIndex >= n) {
                result.add(tmpResult.toString());
                return;
            }

            // iterate
            // char/string都可以直接相减变成int
            for (Character ch : map[digits.charAt(curIndex) - '0'].toCharArray()) {
                tmpResult.append(ch);
                recursion(digits, curIndex + 1, n, result, tmpResult);
                tmpResult.deleteCharAt(tmpResult.length() - 1);
            }
        }
    }
}
