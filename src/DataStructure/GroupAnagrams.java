package DataStructure;
import java.util.*;
/*
刷题次数: 2

https://leetcode.cn/problems/group-anagrams/description/

Given an array of strings strs, group the anagrams together. You can return the answer in any order.
An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]

思路1: 排序 + 哈希表
核心思路: anagrams group表示, 被认定为同一个group里面的单词, 需要满足: 1. 组成单词所用的字母和这些字母的出现的次数都一样 2. 顺序不一样
        利用特性2, 通过字典序排序以后, 将它们统一整理成同一个顺序, 作为哈希表的key, 从而完成
1. 遍历strs里面的每个单词
2. 对单词的每个字母进行字典序排序, 存成哈希表的key
   注意: 在存入之前, 需要注意, 必须将其转换成String
        因为对于非基本类型(比如[], 即array)来说, 其本质都是对象, 而如果将非基本类型当作哈希表的key, 则本质是对该对象调用hashCode()函数, 并将hashCode()的返回值当作key
        而对对象来说, 是对其内存地址(而非实际的内容)进行哈希计算, 所以即使是相同内容的char[], 其是不同的对象, 会被哈希表认为是不同的key
        如果使用char[]作为key的话, 会发生下面的情况: 
        map = {["a", "e", "t"]: "eat", ["a", "e", "t"]: "tea", ["a", "e", "t"]: "ate"}
        看起来好像这个map里存了duplicate的key, 但其实因为每次都是创建的新的char[], 这三个内容一样的char[]其实是存储在不同的内存地址

   *** >>> 请一定看String.md, 有关String的详细解释 <<< ***

3. 判断这个key是否已经存在在哈希表中, 如果存在, 则将其value取出来, append新的单词进去; 如果不存在, 就创建一个新的List<String>
   注意: 可以用getOrDefault来简化代码, getOrDefault表示, 尝试用key去找, 如果找到value则返回value, 如果找不到value则返回默认值
4. 最后利用哈希表.values()来提取所有的值, 转化类型以后, 返回

时间复杂度: O(NKlogK), N表示strs的长度, 即strs单词的个数; K表示所有单词的最大长度
          需要遍历N个单词, 每个单词最多需要O(KlogK)的时间去排序, O(1)的时间更新哈希表
时间复杂度: O(NK), 需要存储所有的字母

思路2: 字母出现频率 + 哈希表
核心思路: 在思路1里面提到anagrams group的特点, 思路1利用了特点2, 思路2则利用特点1
        既然出现次数一样, 那么就记录下来出现的次数, 将其作为key
        所以可以通过计算出每个单词中, 所有字母出现的次数作为哈希表的key
1. 遍历strs里面的每个单词
2. (不推荐, 看第3步) 创建一个字母出现频率的哈希表, 但需要手动将所有的key(即所有a-z的字母), 添加进去
   HashMap<Character, List<String>> map = new HashMap<>();
   map.put('a', 0);
   map.put('b', 0);
   ...
3. 创建一个字母出现频率的数组
   步骤2中的方式很简单但很难编写, 所以更好的方式是利用自建哈希表完成
   我们想达到的效果是: {"a": 1, "b": 2, "d": 3}
   自建哈希表的方法, 即利用数组的下标作为哈希表的key, 数组的值作为哈希表的value
   规定对应关系: 0 -> a, 1 -> b, 2 -> c, ...
   则数组[1, 2, 0, 3, 0, ..., 0] 可以表示为: 
   下标0代表'a', 所以下标0位置上的数代表'a'出现的频率, 即{'a': 1}
   下标1代表'b', 所以下标1位置上的数代表'b'出现的频率, 即{'b': 2}
   下标2代表'c', 所以下标2位置上的数代表'c'出现的频率, 即{'c': 0}
   下标3代表'd', 所以下标3位置上的数代表'd'出现的频率, 即{'d': 3}
4. 将每个单词的字母出现频率记录下来(无论是用第2步的方式还是第3步的方式)
5. 遍历字母出现频率的集合, 用StringBuilder保存
   我们想要达到的效果: "a1b2d3"
   这里同时也体现出使用第3步的方法的好处: 因为如果是第2步的方法, 那么遍历一个哈希表是不保证顺序的, 所以还需要额外考虑如何排序的问题
   但如果使用第3步的方法, 只需要按照for循环依次序遍历, 就一定能得到字母序的结果(因为我们规定了0 -> a, 1 -> b, 2 -> c, ...)
6. 将StringBuilder转化成String, 这一步非常重要, 因为StringBuilder也是一个类, 创建这个类的实例, 无论其内容是否一样, 都是不同的实例, 
   具体可以看思路1第2步的解释
7. 思路1第3步
8. 思路1第4步

时间复杂度: O(N * (K + 字符集大小)), N表示strs的长度, 即strs单词的个数; K表示所有单词的最大长度; 字符集大小表示所有可能出现的字符的数量, 
          在本题中, 是所有小写字母, 所以是字符集大小 = 26, 即O(N(K+26))
          需要O(N)时间遍历N个单词, 每个单词最多需要O(K)的时间去计算字符出现频率, 需要O(字符集大小)的时间生成哈希表的键, O(1)的时间更新哈希表
时间复杂度: O(N * (K + 字符集大小))

 */
public class GroupAnagrams {
    // 思路1
    public List<List<String>> solution1(String[] strs) {
        // 创建哈希表
        HashMap<String, List<String>> map = new HashMap<>();
        // 遍历strs
        for (String str: strs) {
            // 因为Java只有toCharArray()函数, 所以只能通过这个方式把String变成一个可排序的集合
            char[] ch = str.toCharArray();
            // 将单词排序
            Arrays.sort(ch);
            // 创建新的String去存储排好序的值, 原因在上面解释过了
            String key = new String(ch);
            // 用key来查哈希表, 这里利用了getOrDefault()简化代码
            List<String> list = map.getOrDefault(key, new ArrayList<String>());
            // 将新的单词str加入list
            list.add(str);
            // 将更新后的list重新加入哈希表, 即更新哈希表
            map.put(key, list);
        }
        // 根据题目要求, 需要返回List<List<String>>, 而map.values()的类型是: Collection<List<String>>, 所以要转化一次
        return new ArrayList<List<String>>(map.values());
    }

    // 思路2
    public List<List<String>> solution2(String[] strs) {
        // 创建哈希表
        HashMap<String, List<String>> map = new HashMap<>();
        // 遍历
        for (String str: strs) {
            // 创建字母出现频率的数组, 
            int[] alphabet = new int[26];
            // 因为小写字母'a'对应的ASCII数字是97, 所以直接减去97, 即可将其变成0-26的数字, 然后对应数组的下标
            // 但更好的办法是, 既然我们想以 0 -> a,1 -> b,... 去一一对应, 那么直接用字母减去'a'即可
            for (char ch: str.toCharArray()) {
                alphabet[ch - 'a']++;
            }
            // 遍历字母出现频率的数组
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (alphabet[i] != 0) {
                    sb.append((char) ('a' + i));
                    sb.append(alphabet[i]);
                    // sb例子: "a1b2d3"
                }
            }
            // 加入哈希表
            String key = sb.toString(); // 同样要转化成String, 解释看上面的思路
            List<String> list = map.getOrDefault(key, new ArrayList<String>());
            list.add(str);
            map.put(key, list);
        }
        return new ArrayList<List<String>>(map.values());
    }
}