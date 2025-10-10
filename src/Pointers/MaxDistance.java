package Pointers;

import java.util.List;
/*
刷题次数: 2
二刷: 忘记更新历史最大最小值

https://leetcode.cn/problems/maximum-distance-in-arrays/description/

You are given m arrays, where each array is sorted in ascending order.
You can pick up two integers from two different arrays (each array picks one) and calculate the distance.
We define the distance between two integers a and b to be their absolute difference |a - b|.
Return the maximum distance.

Input: arrays = [[1,2,3],[4,5],[1,2,3]]
Output: 4
Explanation: One way to reach the maximum distance 4 is to pick 1 in the first or third array and pick 5 in the second array.

思路: 类似于动态规划或者双指针的线性遍历
别忘了更新历史最小值和最大值
1. 首次做的时候, 第一反应就是遍历每个array的第一个数, 找到最小, 同样方法遍历所有数的最后一个数, 找到最大, 然后相减
   问题在于, 有的时候可能全局的最小值和全局的最大值在同一个array里面, 而题目要求一定要从2个不同array找到最小和最大值, 所以此方法不可行
2. 第二次做的时候, 想用两个堆记录最大值和最小值和它们分别属于第几个array, 然后一个个poll出来, 比较值, 并且需要最大最小值不在同一个array
   但此方法过于复杂, 并且时间复杂度为O(NlogN), 所以也不通过
3. 换种思路, 只需要动态的去存储最大最小值就可以, 类似于动态规划的方法
4. 先用2个值记录至今为止的最大最小值, 初始化则使用第0个array的第0个数做为最小值, 最后一个数做为最大值
5. 然后在for循环中, 用2个值记录当前array的最大最小值, for循环从1开始
6. 比较 当前最大值-历史最小值 与 当前最小值-历史最大值 的绝对值, 将结果记录下来maxDiff, 然后与result去比较, 同样取最大值
7. 更新历史最大值和最小值
8. 不需要担心最大值和最小值是处于同一个array的情况, 因为在上面第6步计算时, 我们是交叉相减的, 没有用历史最大值 - 历史最小值
 */

public class MaxDistance {
    private List<List<Integer>> arrays;

    public int maxDistance(List<List<Integer>> arrays) {
        this.arrays = arrays; // 懒得一直把arrays做为参数传入各种函数
        int m = arrays.size();
        // 初始化历史最小值和历史最大值
        int minNum = getValue(0, 0);
        int maxNum = getValue(0, colSize(0));
        int result = 0;

        for (int i = 1; i < m; i++) {
            // 获得当前array的最小值和最大值, 即第0个数和最后一个数
            int newMin = getValue(i, 0);
            int newMax = getValue(i, colSize(i));
            // 交叉相减, 具体看第6步
            int maxDiff = Math.max(Math.abs(newMax - minNum), Math.abs(newMin - maxNum));
            result = Math.max(result, maxDiff);
            // 更新历史最小值和历史最大值
            minNum = Math.min(minNum, newMin);
            maxNum = Math.max(maxNum, newMax);
        }

        return result;
    }

    public int colSize(int i) {
        return arrays.get(i).size() - 1;
    }

    public int getValue(int i, int j) {
        return arrays.get(i).get(j);
    }
}
