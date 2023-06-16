package DataStructure;
import java.util.*;
/*
刷题次数：1

https://leetcode.cn/problems/group-anagrams/description/

Given an array of strings strs, group the anagrams together. You can return the answer in any order.
An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]

思路1：排序 + HashMap
核心思路：anagrams group表示，被认定为同一个group里面的单词，需要满足：1. 组成单词所用的字母和这些字母的出现的次数都一样 2. 顺序不一样
        利用特性2，通过字典序排序以后，将它们统一整理成同一个顺序，作为HashMap的key，从而完成
1. 挨个遍历strs里面的每个单词
2. 对单词的每个字母进行字典序排序，存成HashMap的key
   注意：在存入之前，需要注意，必须将其转换成String，因为对于非基本类型(比如char[])来说，其存入其他集合(比如HashMap)里面是，存入的其实是其
        内存地址，而非实际的内容，所以会导致明明是相同内容的char[]，会被HashMap认为是不同的key。如果使用char[]作为key的话，会发生下面的情况：
        map = {["a", "e", "t"]: "eat", ["a", "e", "t"]: "tea", ["a", "e", "t"]: "ate"}
        看起来好像这个map里存了duplicate的key，但其实因为每次都是创建的新的char[]，这三个内容一样的char[]其实是存储在不同的内存地址
        而如果用String的话，因为String类型在Java中被声明为final, 且Java有字符串常量池，所以就算每次是创建新的String，
        只要内容相同，JVM不会创建一个新的String，会直接调用常量池中的值。
3. 判断这个key是否已经存在在HashMap中，如果存在，则将其value取出来，append新的单词进去；如果不存在，就创建一个新的List<String>
   注意：可以用getOrDefault来简化代码，getOrDefault表示，尝试用key去找，如果找到value则返回value，如果找不到value则返回默认值
4. 最后利用HashMap.values()来提取所有的值，转化类型以后，返回

思路1：字母出现频率 + HashMap
核心思路：在思路1里面提到anagrams group的特点，思路1利用了出现的字母都一样不过顺序不一样的特点，思路2则利用出现的字母的次数一样这个特点
        所以可以通过计算出每个单词中，所有字母出现的次数作为HashMap的key
 */
public class GroupAnagrams {
    // 思路1
    public List<List<String>> solution1(String[] strs) {
        // 创建哈希表
        HashMap<String, List<String>> map = new HashMap<>();
        // 遍历strs
        for (String str: strs) {
            // 因为Java只有toCharArray()函数，所以只能通过这个方式把String变成一个可排序的集合
            char[] ch = str.toCharArray();
            // 排序
            Arrays.sort(ch);
            // 创建新的String去存储排好序的值，原因在上面解释过了
            String key = new String(ch);
            // 用key来查HashMap，这里利用了getOrDefault()简化代码
            List<String> list = map.getOrDefault(key, new ArrayList<String>());
            // 将新的单词str加入list
            list.add(str);
            // 将更新后的list重新加入HashMap，即更新HashMap
            map.put(key, list);
        }
        // 根据题目要求，需要返回List<List<String>>，而map.values()的类型是：Collection<List<String>>，所以要转化一次
        return new ArrayList<List<String>>(map.values());
    }

    // 思路2
    public List<List<String>> solution2(String[] strs) {
        // 创建哈希表
        return null;
    }
}