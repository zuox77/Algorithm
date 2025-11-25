package BFSAndDFS;

import java.util.ArrayList;
import java.util.List;

/*
https://leetcode.cn/problems/generate-parentheses/description/

Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.

Example 1:
Input: n = 3
Output: ["((()))","(()())","(())()","()(())","()()()"]

Example 2:
Input: n = 1
Output: ["()"]

思路: DFS
1. 难点在于此题是一个隐式二叉树, 怎么样去形成每一层的节点
2. 其实只需要通过左括号和右括号的数量来判断就可以
3. 并且左括号数量严格大于右括号时, 要直接return
 */

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();
        recursion(n, n, ans, new StringBuilder());
        return ans;
    }

    public void recursion(int left, int right, List<String> ans, StringBuilder sb) {
        // 退出条件
        if (left == 0 && right == 0) {
            ans.add(new String(sb));
            return;
        }
        // 如果左括号数量比右括号多，那么直接返回，去掉多的
        if (left > right) return;
        // 增加左括号括号
        if (left > 0) {
            sb.append("(");
            recursion(left - 1, right, ans, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
        // 增加右括号括号
        if (right > 0) {
            sb.append(")");
            recursion(left, right - 1, ans, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
