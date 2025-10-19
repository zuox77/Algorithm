package String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class GroupAnagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String word: strs) {
            char[] wordList = word.toCharArray();
            Arrays.sort(wordList);
            String key = new String(wordList);
            map.computeIfAbsent(key, x -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(map.values());
    }
}
