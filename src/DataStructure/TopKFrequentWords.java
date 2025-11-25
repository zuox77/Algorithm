package DataStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/*

https://leetcode.cn/problems/top-k-frequent-words/description/

Given an array of strings words and an integer k, return the k most frequent strings.
Return the answer sorted by the frequency from highest to lowest.
Sort the words with the same frequency by their lexicographical order.

Example 1:
Input: words = ["i","love","leetcode","i","love","coding"], k = 2
Output: ["i","love"]
Explanation: "i" and "love" are the two most frequent words.
Note that "i" comes before "love" due to a lower alphabetical order.

Example 2:
Input: words = ["the","day","is","sunny","the","the","the","sunny","is","is"], k = 4
Output: ["the","is","sunny","day"]
Explanation: "the", "is", "sunny" and "day" are the four most frequent words, with the number of occurrence being 4, 3, 2 and 1 respectively.

思路：堆

思路：桶排序
1. 用一个map计算所有的单词出现的频率
2. 用一个List<Integer>[]保存不同频率的所有单词
3. 用k找到前k个频率的所有单词
4. 排序然后返回
 */
public class TopKFrequentWords {
    public String[][] topKFrequentWords1(String[] words, int k) {
        HashMap<String, Pair> map = new HashMap<>();
        PriorityQueue<Pair> heap =
                new PriorityQueue<>(
                        k,
                        new Comparator<Pair>() {
                            @Override
                            public int compare(Pair o1, Pair o2) {
                                if (o1.occur > o2.occur || o1.occur < o2.occur) {
                                    return o2.occur - o1.occur;
                                } else {
                                    return o2.str.compareTo(o1.str);
                                }
                            }
                        });
        for (String word : words) {
            if (map.containsKey(word)) {
                map.get(word).occur++;
            } else {
                Pair pair = new Pair(word, 1);
                map.put(word, pair);
            }
        }

        for (Pair pair : map.values()) {
            heap.add(pair);
        }

        int count = 0;
        String[][] result = new String[k][2];
        while (count < k) {
            Pair pair = heap.poll();
            result[count][0] = pair.str;
            result[count][1] = pair.occur + "";
            count++;
        }
        return result;
    }

    public List<String> topKFrequentWords2(String[] words, int k) {
        // 用map保存所有的频率
        Map<String, Integer> freqMap = new HashMap<>();
        for (String s : words) {
            freqMap.merge(s, 1, Integer::sum);
        }
        // 创建一个数组保存不同频率的所有单词
        int n = words.length;
        List<String>[] freqArray = new List[n];
        Arrays.setAll(freqArray, i -> new ArrayList<>());
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            freqArray[entry.getValue()].add(entry.getKey());
        }

        // 找到前k个频率
        List<String> ans = new ArrayList<>();
        int count = 0;
        /*
        注意：
        1. 题目要求前k个，但是同一个频率可能有多个，所以要考虑同一个频率里的单词数量大于k的情况
        2. 因为用的桶排序，所以如果从最大的频率开始遍历到最小的频率，可能有些频率里面是空的，要排除这些空情况
         */
        for (int i = n - 1; i >= 0 && count < k; i--) {
            List<String> wordList = freqArray[i];
            if (wordList.isEmpty()) continue;
            Collections.sort(wordList);
            for (String word : wordList) {
                ans.add(word);
                if (++count == k) break;
            }
        }
        return ans;
    }

    /*
    写一个自定义的排序来解题
    将所有元素都放进List<String>，然后通过查询map中的频率来自定义排序方法
     */
    public List<String> topKFrequentWords3(String[] words, int k) {
        // 不同：这里创建一个List<String>，将所有元素添加进去
        List<String> ans = new ArrayList<>();
        // 用map保存所有的频率
        Map<String, Integer> freqMap = new HashMap<>();
        for (String s : words) {
            // 如果是第一次出现的单词，需要添加进ans
            if (!freqMap.containsKey(s)) ans.add(s);
            freqMap.merge(s, 1, Integer::sum);
        }
        // 找到前k个频率
        Collections.sort(
                ans,
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // 先比较频率，注意频率高的在前面，所以是o2 - o1
                        if (freqMap.get(o1) != freqMap.get(o2))
                            return freqMap.get(o2) - freqMap.get(o1);
                        // 再比较字符串顺序
                        return o1.compareTo(o2);
                    }
                });
        return ans.subList(0, k);
    }

    class Pair {
        public String str;
        public int occur;

        public Pair(String str, int occur) {
            this.str = str;
            this.occur = occur;
        }
    }
}
