package DataStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/*
https://leetcode.cn/problems/top-k-frequent-elements/
https://www.jiuzhang.com/problem/top-k-frequent-elements/
https://www.lintcode.com/problem/471/

Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.

Example 1:
Input: nums = [1,1,1,2,2,3], k = 2
Output: [1,2]

Example 2:
Input: nums = [1], k = 1
Output: [1]

Example 3:
Input: nums = [1,2,1,2,1,2,3,1,3,2], k = 2

思路1: 最小堆
1. 建立最小堆, 最小堆的堆顶是最小的, 所以每个新的元素进来前都与堆顶做比较, 如果比堆顶大, 那么堆顶出, 新元素进, 这样可以保证最小堆里的数都是大的
2. 此题要求最高频率, 所以默认堆方法中比较数值就不适用了, 需要额外用一个哈希表记录频率, 并重写最小堆的比较函数(即compare()函数)
3. 遍历一遍数组, 算出频率
4. 遍历一遍哈希表, 一个个加进堆, 当堆中元素数量大于等于k时, 判断当前元素与堆顶的关系, 如果比堆顶大, 则堆顶出, 当前元素进
5. for循环一次, 次数为k, 将堆顶poll出, 加入result
时间复杂度: O(NlogK), N是数组长度, K是topK的K, 遍历一边数组花费O(N), 将哈希表放入长度为K的最小堆里, 每次加入里面排序需要logK的时间,
    注意就算已经有K个数了, 后面加入新的数, 只需要将堆顶弹出O(1)即可, 但因为新加入的数有可能是最大的数也有可能就放在堆顶, 所以也需要logK的时间排序
空间复杂度: O(N)
需要注意的点:
1. 要学会重写最小堆的比较函数:
        PriorityQueue<Pair> maxHeap = new PriorityQueue<>(k, new Comparator<Pair>() {
            @Override
            public int compare(Pair s1, Pair s2) {
                return s2.freq - s1.freq; // 最小排序的话就是第二个参数减去第一个参数, 反之则是最大排序, 即最大堆或者大顶堆
            }
        });  // 特别注意这里, 结尾是}), 整个大括号是new Comparator<Pair>()的对象, 即
             // PriorityQueue<Pair> maxHeap = new PriorityQueue<>(k, 新的Comparator);
             // 新的Comparator = new Comparator<Pair>() {重写函数在里面}
2. Leetcode上面要求返回的类型是int[], int[]属于Array, 而Array是固定长度的, 所以不存在add()方法, 只能用
    res[i] = value去赋值
3. Lintcode上面要求返回的类型是List<Integer>, List是属于ArrayList, ArrayList无固定长度, 所以可以用add()方法

思路2: 桶排序
 */

public class TopKFrequent {
    public int[] topKFrequent1(int[] nums, int k) {
        // define a max heap and override its compare method
        PriorityQueue<Pair> maxHeap =
                new PriorityQueue<>(
                        new Comparator<Pair>() {
                            @Override
                            public int compare(Pair p1, Pair p2) {
                                return p2.freq - p1.freq; // 第二个参数减去第一个参数 = 最大堆, 反之最小堆, 默认也是最小堆
                            }
                        });
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int num : nums) {
            if (!map.containsKey(num)) {
                map.put(num, 1);
            } else {
                map.put(num, map.get(num) + 1);
            }
        }

        for (int key : map.keySet()) {
            maxHeap.add(new Pair(key, map.get(key)));
        }

        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            Pair curElement = maxHeap.poll();
            res[i] = curElement.val;
        }

        return res;
    }

    /*
    如果是找top k frequent文字, 可以用下面的Comparator:
    private Comparator<Pair> pairComparator = new Comparator<Pair>() {
        public int compare(Pair p1, Pair p2) {
            if (p1.val != p2.val) {
                return p1.val - p2.val;
            }
            return p2.key.compareTo(p1.key); // compareTo函数比较的是两个string的词典顺序 lexicographical
        }
    };
    用的时候直接:
    pairComparator.compare(pair1, pair2) > 0: 表示pair1比pair2大
     */

    public int[] topKFrequent2(int[] nums, int k) {
        // 用map保存每个数字出现的次数
        Map<Integer, Integer> map = new HashMap<>();
        // 再用一个变量保存最大的次数的值
        int maxFreq = 0;
        for (int num : nums) {
            map.merge(num, 1, Integer::sum);
            maxFreq = Math.max(maxFreq, map.get(num));
        }
        // 创建一个数组，用数组下标作为次数，保存相应的数字
        List<Integer>[] freqList = new List[maxFreq + 1]; // 这里加1是因为下标为0代表频率为0的，所以多了一个数
        // 初始化
        Arrays.setAll(freqList, i -> new ArrayList<>());
        // 遍历map，填上对应的频率和数字
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            freqList[entry.getValue()].add(entry.getKey());
        }
        // 倒序遍历刚刚的数组，提取前k个数
        int[] ans = new int[k];
        int count = 0;
        for (int i = maxFreq; i >= 0 && count < k; i--) {
            for (int num : freqList[i]) {
                ans[count++] = num;
            }
        }
        return ans;
    }

    class Pair {
        int val;
        int freq;

        public Pair(int val, int freq) {
            this.val = val;
            this.freq = freq;
        }
    }
}
