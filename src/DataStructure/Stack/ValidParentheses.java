package DataStructure.Stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/*
https://leetcode.cn/problems/valid-parentheses/

思路: 栈
1. 利用栈的原理, 将出现的char所对应的char放入栈
2. 需要注意的是, 最好用一个dummy char去代替不存在于哈希表中的char, 比如"[]]]"这种情况, 前两个char因为是刚好对应, 所以可以消除,
   但如果直接把']'放入栈中, 因为后一个']'刚好与前一个相等, 所以会造成好像能消除的假象
   如果能加入一个dummy来代替所有这些元素, 比如'?', 那么就不会发生这种事情
3. 一定要检查stack.peek是否为null，或者用getOrDefault
 */

public class ValidParentheses {

    private static final Map<Character, Character> MAP = Map.of('(', ')', '{', '}', '[', ']');

    public boolean isValid(String s) {
        // 创建一个栈
        Deque<Character> stack = new ArrayDeque<>();
        // 遍历s，将其放入栈
        for(char c: s.toCharArray()) {
            // 如果stack是空的，或者是任意的左括号，放入栈
            if (MAP.containsKey(c)) {
                stack.offerFirst(c);
                // 如果当前遍历的char与栈首，刚好能匹配，那么出栈，否则返回false
            } else if (MAP.getOrDefault(stack.peekFirst(), ' ') == c) {
                stack.pollFirst();
            } else {
                return false;
            }
        }

        return stack.isEmpty();
    }
}
