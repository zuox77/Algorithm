package DataStructure;
import java.util.PriorityQueue;
/*
https://leetcode.cn/problems/find-median-from-data-stream/description/?orderBy=most_votes
https://www.lintcode.com/problem/81/
https://www.jiuzhang.com/solutions/data-stream-median/

思路: 两个堆
1. 此题的关键在于median的定义不是任意数组的下标在中间的数, 而是排序数组的下标在中间的数
2. 因为是数据流, 所以不知道最大长度, 无法用一般的排序方法解决, 又因为每个数都不一定是有序的, 所以还涉及到如何排序的问题
3. 理论上可以用普通的ArrayList之类的数据结构, 每次getMedian就排序一次, 那每次getMedian的时间复杂度为O(NlogN), N表示当前有多少数
    但如果要优化的话, 就用两个堆, 一个最大堆和一个最小堆
4. 最大堆和最小堆分别维护一半的数据, 最大堆负责[0, 1, 2, ... , 19], 或者说左半边, 最小堆负责[20, 21, 22, ... , 40], 即右半边
    每次有新的数加入进来的时候, 比较最大堆的堆顶和最小堆的堆顶, 选择将其加入一边, 求median的时候就看当前数据的多少的奇偶性, return要么两个
    堆顶的平均数, 要么某一个堆顶
5. 设定当奇数时, 一定是最大堆比最小堆多一个数, 即median就是最大堆的堆顶
    当偶数时, median为两个堆的堆顶的平均值
6. 很重要的一点是: 遵循5的规定后, 还要判断新加入的数, 究竟属于哪一边
    比maxHeap的堆顶大, 那么应该属于minHeap, 比maxHeap的堆顶小, 那么应该属于maxHeap
    所以此题最重要的两个条件/关系: 1. 两个堆之间的数量关系 2. 新数和堆顶(无论哪个堆都是一样的)的大小关系
    将这两个条件加在一起判断, 如下: 
7. 当奇数时加入一个新数num: 奇数时跟maxHeap比大小(或者上面规定数量多的那个堆)
    1. num < maxHeap.peek(): 将maxHeap的堆顶pop出来加入minHeap, 将num加入maxHeap, 这样同时保证了数量关系和大小关系
        maxHeap = [0, 1, 3, 5], minHeap = [8, 9, 11], num = 4, 4应该属于maxHeap, 但我们规定maxHeap只能多一个, 所以
        将maxHeap的堆顶pop出来加入minHeap: maxHeap = [0, 1, 3], minHeap = [5, 8, 9, 11]
        再将num加入maxHeap: maxHeap = [0, 1, 3, 4], minHeap = [5, 8, 9, 11]
    2. num >= maxHeap.peek(): 将num加入minHeap
    3. 按照数量关系, 我们想将num放进minHeap, 所以此时要比较跟maxHeap的堆顶大小, 看它在不在maxHeap里
        此时不能跟minHeap的堆顶比大小, 因为我们无非两种操作: 1. 直接加进minHeap 2. 让minHeap推出堆顶加入maxHeap, num自己再加入minHeap
        如果跟minHeap堆顶比大小的话: 
        1. 第一种情况: maxHeap = [0, 1, 2, 3, 5], minHeap = [6, 8, 9, 11], num = 4, 4 < 6, 如果直接加进minHeap破坏整体顺序
        2. 第二种情况: maxHeap = [0, 1, 2, 3, 5], minHeap = [6, 8, 9, 11], num = 4, 4 > 6, 如果让minHeap推出堆顶加入maxHeap, 
            num自己加入minHeap也破坏了顺序
        3. 所以无论哪种情况都需要判断与maxHeap的大小
8. 当偶数时加入一个新数num: 偶数时跟minHeap比大
    1. num <= minHeap.peek(): 将num加入maxHeap, 因为num属于maxHeap, 并且规定如果奇数时, maxHeap多一个数
    2. num > minHeap.peek(): 将minHeap的堆顶pop出来加入maxHeap, 将num加入minHeap, 这样同时保证了数量关系和大小关系
        maxHeap = [0, 1, 3, 5], minHeap = [6, 8, 9, 11], num = 7, 7应该属于minHeap, 但我们规定maxHeap要多一个, 所以
        将minHeap的堆顶pop出来加入maxHeap: maxHeap = [0, 1, 3, 5, 6], minHeap = [8, 9, 11]
        再将num加入minHeap: maxHeap = [0, 1, 3, 5, 6], minHeap = [7, 8, 9, 11]
    3. 按照数量关系, 我们想将num放进maxHeap, 所以此时要比较跟minHeap的堆顶大小, 看它在不在minHeap里
        此时不能跟maxHeap的堆顶比大小, 因为我们无非两种操作: 1. 直接加进maxHeap 2. 让minHeap推出堆顶加入maxHeap, num自己再加入minHeap
        1. 第一种情况: maxHeap = [0, 1, 3, 5], minHeap = [6, 8, 9, 11], num = 7, 7 > 5, 如果直接加进maxHeap破坏顺序了
        2. 第二种情况: maxHeap = [0, 1, 3, 5], minHeap = [8, 8, 9, 11], num = 7, 7 > 5, 如果让minHeap推出堆顶加入maxHeap, 
            num自己加入minHeap也破坏了顺序
        3. 所以无论哪种情况都需要判断与minHeap的大小
addNum时间复杂度: O(logN)
getMedian时间复杂度: O(1)
空间复杂度: O(N)
 */
public class DataStreamMedian {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
    public DataStreamMedian() {}

    public void addNum(int num) {
        int minSize = minHeap.size();
        int maxSize = maxHeap.size();
        // first element add to maxHeap
        if (maxSize == 0) {
            maxHeap.add(num);
        } else if (maxSize - minSize == 1){ // total length is odd
            if (num < maxHeap.peek()) { // means num should belong to maxHeap, but maxHeap already has 1 element more
                minHeap.add(maxHeap.poll());
                maxHeap.add(num);
            } else {
                minHeap.add(num);
            }
        } else {
            if (num > minHeap.peek()) { // total length is even
                maxHeap.add(minHeap.poll()); // means num should belong to minHeap, but maxHeap needs to have 1 element more
                minHeap.add(num);
            } else {
                maxHeap.add(num);
            }
        }
    }

    public double getMedian() {
        int minSize = minHeap.size();
        int maxSize = maxHeap.size();
        if (maxSize - minSize == 1) {
            return (double)maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }
}
