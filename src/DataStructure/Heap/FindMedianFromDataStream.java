package DataStructure.Heap;

import java.util.PriorityQueue;

/*
https://leetcode.cn/problems/find-median-from-data-stream/description/?envType=study-plan-v2&envId=top-100-liked

The median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value, and the median is the mean of the two middle values.

For example, for arr = [2,3,4], the median is 3.
For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.

Implement the MedianFinder class:
MedianFinder() initializes the MedianFinder object.
void addNum(int num) adds the integer num from the data stream to the data structure.
double findMedian() returns the median of all elements so far. Answers within 10-5 of the actual answer will be accepted.

Example 1:
Input
["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
[[], [1], [2], [], [3], []]
Output
[null, null, null, 1.5, null, 2.0]

Explanation
MedianFinder medianFinder = new MedianFinder();
medianFinder.addNum(1);    // arr = [1]
medianFinder.addNum(2);    // arr = [1, 2]
medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
medianFinder.addNum(3);    // arr[1, 2, 3]
medianFinder.findMedian(); // return 2.0

思路: 双堆
1. 题目规定输入的数字一定是有序的，而我们只需要中位数，即中间的两个数，那么我们可以考虑利用两个堆来完成
    最小堆代表中间往后的数字们，最大堆代表中间往前的数字们
    [1, 3, 4, 7, 8, 10]
    最大堆：（尾）[1, 3, 4]（顶）
    最小堆：              （顶）[7, 8, 10]（尾）
    这样最大堆的堆顶就是4，最小堆的堆顶就是7，我们就能算出其中位数
2. 特别注意：例如上面的例子
    假设我需要加入20进去，那理论上，20应该在最小堆里面
    但假设我需要加入-100，那-100应该在最大堆
    且如果我不断加入一些非常大或者非常小的数，我该如何平衡这两个堆各自保存哪些数？这两个堆各自的size如何平衡？
    答案是：每次都需要加入两个堆，只不过根据定义，加入的顺序不同
    因为每次都会通过两个堆，所以会做好排序，
    1. 我们定义：最大堆比最小堆多一个数，即当两个堆的数量一致时（偶数时），新加入的数会被存入最大堆
    2. 如果两个堆数量一致时，先将新的数加入最小堆，然后将最小堆的堆顶的数加入最大堆
    3. 如果最大堆数量已经大于最小堆时，先将新的数加入最大堆，然后将最大堆的堆顶加入最小堆
    即
    数量一致：minHeap.offer(num);
            maxHeap.offer(minHeap.poll());
    否则：   maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());

 */

public class FindMedianFromDataStream {

    private PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    ;
    private PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
    ;

    public FindMedianFromDataStream() {}

    public void addNum(int num) {
        if (minHeap.size() == maxHeap.size()) {
            minHeap.offer(num);
            maxHeap.offer(minHeap.poll());
        } else {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (minHeap.peek() + maxHeap.peek()) / 2.0;
    }
}
