package DataStructure;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/*
给定一个字符串数组, 再给定整数 k , 请返回出现次数前k名的字符串和对应的次数
返回的答案应该按字符串出现频率由高到低排序, 如果不同的字符串有相同出现频率, 按字典序排序
对于两个字符串, 大小关系取决于两个字符串从左到右第一个不同字符的 ASCII 值的大小关系
比如"ah1x"小于"ahb", "231"<”32"

字符仅包含数字和字母



输入:

["123","123","231","32"],2
返回值:

[["123","2"],["231","1"]]
说明:

 "123"出现了2次, 记["123","2"], "231"与"32"各出现1次, 但是"231"字典序在"32"前面, 记["231","1"], 最后返回[["123","2"],["231","1"]]
 */
public class TopKWords {
    public String[][] solution1(String[] words, int k) {
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

    class Pair {
        public String str;
        public int occur;

        public Pair(String str, int occur) {
            this.str = str;
            this.occur = occur;
        }
    }
}
