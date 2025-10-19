package DataStructure;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/*
https://leetcode.cn/problems/valid-parentheses/

思路: 栈
1. 利用栈的原理, 将出现的char所对应的char放入栈
2. 需要注意的是, 最好用一个dummy char去代替不存在于哈希表中的char, 比如"[]]]"这种情况, 前两个char因为是刚好对应, 所以可以消除,
   但如果直接把']'放入栈中, 因为后一个']'刚好与前一个相等, 所以会造成好像能消除的假象
   如果能加入一个dummy来代替所有这些元素, 比如'?', 那么就不会发生这种事情
3. 一定要检查stack.peek是否为null
 */

public class ValidParentheses {
  public boolean solution1(String s) {
    HashMap<Character, Character> charMap = new HashMap<>();
    charMap.put('(', ')');
    charMap.put('{', '}');
    charMap.put('[', ']');
    // 加入一个dummy
    charMap.put('?', '?');
    Deque<Character> stack = new LinkedList<>();
    for (char ch : s.toCharArray()) {
      if (charMap.containsKey(ch)) {
        stack.addFirst(charMap.get(ch));
      } else if (stack.peek() != null && stack.peek() == ch) {
        stack.removeFirst();
      } else {
        stack.addFirst(charMap.get('?'));
      }
    }
    return stack.isEmpty();
  }
}
