package Array.ScanLines;

/*
刷题次数: 1

https://leetcode.cn/problems/meeting-rooms-ii/description/
Given an array of meeting time intervals intervals where intervals[i] = [starti, endi], return the minimum number of conference rooms required.

Example 1:
Input: intervals = [[0,30],[5,10],[15,20]]
Output: 2

Example 2:
Input: intervals = [[7,10],[2,4]]
Output: 1

时间复杂度: O(NlogN), 排序2次
空间复杂度: O(1)

思路：扫线法
1. 用两个新数组，将start和end分别从小到大排序，形成一条时间线：begins[i]和ends[j]
2. 用两个指针分别指着这两个数组
    1. 当i<n和j<n时
    2. 比较begins[i]和ends[j]
        如果begins[i] < ends[j]，说明此时需要一个会议室 -> count++ i++
        反之，此时有个会议结束了，减少一个会议室 -> count-- j++
        然后随时更新最大的count

思路2：堆
1. 排序数组
2. 用一个堆保存结束时间
3. for循环遍历，当 当前的区间的起始时间小于等于堆顶（堆顶是最早的结束时间）时，说明当前区间和堆顶的区间不会重叠，所以直接出堆
    反之，则直接将当前区间的结束时间压入堆
3. 循环结束时，堆里还剩多少元素，就代表最多需要多少房间
3. 整体思路和扫线法类似，不过这里用堆来表示还有多少个会议正在进行中

 */

import java.util.Arrays;
import java.util.PriorityQueue;

public class MeetingRooms2 {
    public int minMeetingRooms(int[][] intervals) {
        int n = intervals.length;
        int[] begins = new int[n];
        int[] ends = new int[n];

        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        for (int i = 0; i < n; i++) {
            begins[i] = intervals[i][0];
        }

        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        for (int i = 0; i < n; i++) {
            ends[i] = intervals[i][1];
        }

        int i = 0, j = 0, count = 0, maxCount = 0;

        while (i < n && j < n) {
            if (begins[i] < ends[j]) {
                count++;
                i++;
            } else {
                count--;
                j++;
            }
            maxCount = Math.max(count, maxCount);
        }

        return maxCount;
    }

    public int minMeetingRooms2(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        //
        PriorityQueue<Integer> q = new PriorityQueue<>();
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        q.add(intervals[0][1]);
        for (int i = 1; i < intervals.length; i++) {
            if (q.peek() <= intervals[i][0]) {
                q.poll();
            }
            q.add(intervals[i][1]);
        }
        return q.size();
    }
}
