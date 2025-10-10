package DataStructure;

import java.util.HashSet;
import java.util.PriorityQueue;
/*
https://leetcode.cn/problems/ugly-number-ii/description/
https://www.lintcode.com/problem/4/
https://www.jiuzhang.com/problem/ugly-number-ii/

思路: 堆+哈希集合
注意: java的堆都是最小堆, 即堆顶是最小的, 想要用最大堆就将数字变成负数, 或者override其comparator
PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(new Comparator<Integer>(){
    @Override
    public int compare(Integer i1, Integer i2){
        return i2 - i1;
    }
}

1. 需要按顺序求第n个任意factor的倍数, 所以用PriorityQueue(即heap)来排序
2. 初始化将1加进heap里, 每次从heap弹出堆顶, 分别乘以所有factor得到新的数
3. 用HashSet记录某一个数是否已经计算过了, 比如factor是2和3, 那么他们的公共倍数6就会被算两次
4. 将新的数加进heap和HashSet
5. for循环从0开始, 循环到i < n
 */

public class UglyNumber2 {
    public int nthUglyNumber(int n) {
        PriorityQueue<Long> heap = new PriorityQueue<>();
        HashSet<Long> visited = new HashSet<>();
        final int[] factors = new int[]{2, 3, 5};

        heap.add(1L);
        long curUgly = 1;
        long newUgly;

        for (int i = 0; i < n; i++) {
            curUgly = heap.poll();
            for (int factor : factors) {
                newUgly = curUgly * factor;
                if (!visited.contains(newUgly)) {
                    visited.add(newUgly);
                    heap.add(newUgly);
                }
            }
        }
        return (int) curUgly;
    }
}
