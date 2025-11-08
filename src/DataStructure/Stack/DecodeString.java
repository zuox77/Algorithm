package DataStructure.Stack;

import java.util.ArrayDeque;
import java.util.Deque;

/*
https://leetcode.cn/problems/decode-string/description/?envType=study-plan-v2&envId=top-100-liked

Given an encoded string, return its decoded string.
The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times.
Note that k is guaranteed to be a positive integer.
You may assume that the input string is always valid;
there are no extra white spaces, square brackets are well-formed, etc.
Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k.
For example, there will not be input like 3a or 2[4].
The test cases are generated so that the length of the output will never exceed 105.

Example 1:
Input: s = "3[a]2[bc]"
Output: "aaabcbc"

Example 2:
Input: s = "3[a2[c]]"
Output: "accaccacc"

Example 3:
Input: s = "2[abc]3[cd]ef"
Output: "abcabccdcdcdef"

思路: 栈
1. 利用栈的原理, 将出现的char所对应的char放入栈
2. 需要注意的是, 最好用一个dummy char去代替不存在于哈希表中的char, 比如"[]]]"这种情况, 前两个char因为是刚好对应, 所以可以消除,
   但如果直接把']'放入栈中, 因为后一个']'刚好与前一个相等, 所以会造成好像能消除的假象
   如果能加入一个dummy来代替所有这些元素, 比如'?', 那么就不会发生这种事情
3. 一定要检查stack.peek是否为null，或者用getOrDefault
 */

public class DecodeString {

    public String decodeString(String s) {
        // 创建一个栈
        Deque<Pair> stack = new ArrayDeque<>();
        // 创建一个StringBuilder
        StringBuilder sb = new StringBuilder();
        // 创建一个变量保存当前的k
        int k = 0;

        // 遍历字符串
        for (char ch : s.toCharArray()) {
            // 如果是数字，那么更新k
            if (Character.isDigit(ch)) {
                k = k * 10 + ch - '0';
                // 如果是字母，直接放入sb
            } else if (Character.isLetter(ch)) {
                sb.append(ch);
                // 如果是[，那么代表接下来又有新的需要计算res和k的值了，将当前的res和k保存到栈里
            } else if (ch == '[') {
                // 保存到栈
                stack.addFirst(new Pair(sb.toString(), k));
                // 重置res和k
                k = 0;
                sb.setLength(0);
                // 如果是]，那么代表我们已经完整的获得了一个k[encoded_string]格式的字符，我们需要将其从栈中推出，然后计算，最后放入sb
            } else {
                Pair pair = stack.pollFirst();
                // 计算sb
                /*
                注意，pair里面的string和k不是匹配的，毕竟我们是在[以后才将sb计算好的，
                也就是说pair里面的数字，是与当前的sb匹配的，而pair里面字符，需要和计算过后的当前sb，合在一起，以待下次计算
                例如 a2[c]
                假设我们现在遍历到]，此时的stack的栈首为 pair = {str = "a", times = 2}
                而]结束时，我们需要将其化为：acc，所以可以得出：
                我们要的答案 = pair里面的str + 当前的sb * pair里面的times
                */
                sb = new StringBuilder(pair.str).repeat(sb, pair.times);
            }
        }
        return sb.toString();
    }
    ;

    private record Pair(String str, int times) {}
}
