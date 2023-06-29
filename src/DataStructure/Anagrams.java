package DataStructure;
import java.util.*;
/*
刷题次数: 2

https://www.lintcode.com/problem/171/description

Given an array of strings, return all groups of strings that are anagrams.
If a string is Anagram,there must be another string with the same letter set but different order in S.

Input:["ab", "ba", "cd", "dc", "e"]
Output: ["ab", "ba", "cd", "dc"]

思路: 自建哈希表
1. 整体与GroupAnagrams一样, 区别: 
   1. 不需要跟GroupAnagrams一样, 将不同的分别存放到一个list, 即result不是List<List<String>>, 而是<List<String>
   2. 只需要多次出现的, 单独只出现了一次的不需要, 比如上面的input中的'e'就不需要
2. 具体可以看GroupAnagrams的题解, 这里只用了自建哈希表来解决, 也可以用排序
 */

public class Anagrams {
    public List<String> solution(String[] strs) {
        // 声明变量
        List<String> result = new ArrayList<>();
        HashMap<String, List<String>> map = new HashMap<>();
        // 遍历每个单词
        for (String str: strs) {
            // 遍历m单词的每个字母, 放入字母表数组中
            int[] alphabet = new int[26];
            for (int i = 0; i < str.length(); i++) {
                alphabet[str.charAt(i) - 'a'] = alphabet[str.charAt(i) - 'a'] + 1;
            }
            // 遍历字母表数组, 将出现过的字母以及其频率组成字符串
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                // 只把出现过的字母加入字符串
                if (alphabet[i] > 0) {
                    sb.append((char) (i + 'a'));
                    sb.append(alphabet[i]);
                }
            }
            // 将字符串转成String类型
            String key = sb.toString();
            // 将字符串和对应的原字符串放入HashMap中
            List<String> value = map.getOrDefault(key, new ArrayList<String>());
            value.add(str);
            map.put(key, value);
        }
        // 遍历map, 找到键值对中数量大于1的, 加入result
        for (List<String> value: map.values()) {
            if (value.size() > 1) {
                result.addAll(value);
            }
        }
        return result;
    }
}
