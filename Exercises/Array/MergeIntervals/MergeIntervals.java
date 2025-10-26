package Array.MergeIntervals;

/*
刷题次数: 1

https://leetcode.cn/problems/merge-intervals/description/?envType=study-plan-v2&envId=top-100-liked

Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals,
and return an array of the non-overlapping intervals that cover all the intervals in the input.

Example 1:
Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].

Example 2:
Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.

Example 3:
Input: intervals = [[4,7],[1,4]]
Output: [[1,7]]
Explanation: Intervals [1,4] and [4,7] are considered overlapping.

时间复杂度: O(N LogN), sort一次+遍历一次
空间复杂度: O(1)

思路：
1. 隐形条件：每个内嵌的区间，区间左端一定小于等于右端，比如[1,4]而不会出现[4,1]
2. 先sort，然后遍历区间，能合并的合并，不能合并的添加
3. 主要是判断条件该怎么写，其实不难，仔细列举每个条件然后融合同一个操作的条件们：
    1. 合并：n >= 0（n是ans.size()-1，即ans不为空就行） && 该区间右侧 >= 当前遍历的interval的左端
        1. 该区间右侧 <=  当前遍历的interval的右端：取当前遍历的interval的右端
        2. 该区间右侧 >   当前遍历的interval的右端：取该区间右侧
        3. 再合并一下，其实就是一个max方程：ans.get(n)[1] = Math.max(ans.get(n)[1], interval[1])
    2. 不合并：其余条件都不合并
4. 还需要特别注意数组（Array，即int[]这种）和列表（List，即List<Integer>这种）的相互转化
    1. 此题要求返回的是int[][]，但是如果直接把ans定义为int[][]会很麻烦，
        1. 定义时：int[][] ans = new int[][] 会得到 [null, null, null, ...]
            因为我们需要拿到ans中最新的区间，但是有可能我们遍历了intervals很多次，但ans只有一个区间（都被合并了）
            比如[[1,4],[1,3],[2,3],[5,7]],遍历到[5,7]的时候，ans为[[1,4],null,null, ...]
            此时只能通过遍历ans去找第一个非null的元素，很麻烦
        2. 同理，如果想要添加元素，也必须知道上一个元素的位置，也只能通过遍历去找
    2. 最好是定义为List<int[]>，这样的话，当需要添加的时候，只需要ans.add()，当需要找最新的非null元素时，只需要ans.size()-1就行
    3. 但一定记住如何相互转化：
        1. int[] test -> List<Integer>：Arrays.asList(test) / new ArrayList<Integer>(Arrays.asList(test))
        2. List<Integer> test -> int[]：test.toArray()
        3. List<int[]> test -> int[][]: test.toArray(new int[test.size()][])

 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        // 将原数组排序（注意这里的sort排序用的是dual pivot quick sort）
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        // 遍历有序数组
        List<int[]> ans = new ArrayList<>();
        for (int[] interval : intervals) {
            // ans中当前区间的位置
            int n = ans.size() - 1;
            // 如果n大于等于0（ans中已经曾加入过区间了）时，且：
            // 1. 该区间左侧 == 当前遍历的interval的左端：无法合并，直接添加当前遍历的interval
            // 2. 该区间左侧 >  当前遍历的interval的左端：不可能（因为我们已经按照左端排序过了，只可能小于等于）
            // 3. 该区间左侧 <  当前遍历的interval的左端：
            //   1. 该区间右侧 <  当前遍历的interval的左端：无法合并，直接添加当前遍历的interval
            //   2. 该区间右侧 >=  当前遍历的interval的左端 && 该区间右侧 <=  当前遍历的interval的右端：合并
            //   3. 该区间右侧 >=  当前遍历的interval的左端 && 该区间右侧 >=  当前遍历的interval的右端：合并
            // 总结：
            // 只有3.2和3.3是合并，其他都是不可能或者直接添加当前遍历的interval，按照这个来写判断条件
            // 合并的情况：n >= 0（ans中必须要存在区间，不然没法比较，直接添加）&& 该区间右侧 >=  当前遍历的interval的左端
            // 其余情况全都不合并
            if (n >= 0 && ans.get(n)[1] >= interval[0]) {
                // 该区间左侧不需要变，因为左侧一定小于等于当前遍历的interval的左端，
                // 只更新该区间右侧：该区间右侧和当前遍历的interval的右端，谁大就是谁
                ans.get(n)[1] = Math.max(ans.get(n)[1], interval[1]);
            } else {
                ans.add(interval);
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }
}
