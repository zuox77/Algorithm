package Pointers;
import java.util.HashMap;
/*
输入: abcabcbb
3, abc
输入: aaaa
输出a

abcdec

start = 0
end = 5
 */
public class LongestUniqueSubstring {
    public int solution1(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int n = s.length();
        // corner case
        if (n < 2) {
            return 1;
        }

        int start = 0;
        int end = 1;
        map.put(s.charAt(0), 0);
        int maxLength = 0;

        while (end < n) {
            if (map.containsKey(s.charAt(end))) {
                start = map.get(s.charAt(end)) + 1;
                map.put(s.charAt(end), end);
            } else {
                map.put(s.charAt(end), end);
            }
            maxLength = Math.max(maxLength, end - start + 1);
            end++;
        }
        return maxLength;
    }
}
