package Recursion;

import java.util.ArrayList;
import java.util.List;
/*
https://leetcode.cn/problems/generate-parentheses/description/

思路：DFS
1. 难点在于此题是一个隐式二叉树，怎么样去形成每一层的节点
2. 其实只需要通过左括号和右括号的数量来判断就可以
3. 并且左括号数量严格大于右括号时，要直接return
 */

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        recursion(temp, result, n, n);
        return result;
    }

    public void recursion(StringBuilder temp, List<String> result, int left, int right) {
        // recursion exit
        if (left == 0 && right == 0) {
            result.add(temp.toString());
            return;
        }

        // iterate
        if (left > right) {
            return;
        }

        if (left > 0) {
            temp.append("(");
            recursion(temp, result, left - 1, right);
            temp.deleteCharAt(temp.length() - 1);
        }
        if (right > 0) {
            temp.append(")");
            recursion(temp, result, left, right - 1);
            temp.deleteCharAt(temp.length() - 1);
        }
    }
}
